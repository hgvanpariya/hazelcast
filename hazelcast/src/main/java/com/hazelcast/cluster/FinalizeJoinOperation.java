/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.cluster;

import com.hazelcast.core.HazelcastException;
import com.hazelcast.instance.MemberImpl;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.impl.NodeEngineImpl;
import com.hazelcast.spi.impl.ResponseHandlerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FinalizeJoinOperation extends MemberInfoUpdateOperation implements JoinOperation {

    private PostJoinOperation postJoinOp;

    public FinalizeJoinOperation() {
    }

    public FinalizeJoinOperation(Collection<MemberInfo> members, long masterTime) {
        super(members, masterTime, true);
    }

    public FinalizeJoinOperation(Collection<MemberInfo> members, PostJoinOperation postJoinOp, long masterTime) {
        super(members, masterTime, true);
        this.postJoinOp = postJoinOp;
    }

    @Override
    public void run() {
        if (isValid()) {
            super.run();

            // Post join operations must be lock free; means no locks at all;
            // no partition locks, no key-based locks, no service level locks!

            final ClusterService clusterService = getService();
            final NodeEngineImpl nodeEngine = clusterService.getNodeEngine();
            final Operation[] postJoinOperations = nodeEngine.getPostJoinOperations();
            Collection<Future> calls = null;
            if (postJoinOperations != null && postJoinOperations.length > 0) {
                final PostJoinOperation localPJOp = new PostJoinOperation(postJoinOperations);
                final Collection<MemberImpl> members = clusterService.getMemberList();
                calls = new ArrayList<Future>(members.size());
                for (MemberImpl member : members) {
                    if (!member.localMember()) {
                        Future f = nodeEngine.getOperationService().createInvocationBuilder(ClusterService.SERVICE_NAME,
                                localPJOp, member.getAddress())
                                .setTryCount(10).setTryPauseMillis(100).build().invoke();
                        calls.add(f);
                    }
                }
            }

            if (postJoinOp != null) {
                postJoinOp.setNodeEngine(nodeEngine).setCaller(getCaller()).setConnection(getConnection());
                postJoinOp.setResponseHandler(ResponseHandlerFactory.createEmptyResponseHandler());
                nodeEngine.getOperationService().runOperation(postJoinOp);
            }

            if (calls != null) {
                for (Future f : calls) {
                    try {
                        f.get(1, TimeUnit.SECONDS);
                    } catch (InterruptedException ignored) {
                    } catch (TimeoutException ignored) {
                    } catch (ExecutionException e) {
                        throw new HazelcastException(e);
                    }
                }
            }
        }
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        boolean hasPJOp = postJoinOp != null;
        out.writeBoolean(hasPJOp);
        if (hasPJOp) {
            postJoinOp.writeData(out);
        }
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        boolean hasPJOp = in.readBoolean();
        if (hasPJOp) {
            postJoinOp = new PostJoinOperation();
            postJoinOp.readData(in);
        }
    }
}

