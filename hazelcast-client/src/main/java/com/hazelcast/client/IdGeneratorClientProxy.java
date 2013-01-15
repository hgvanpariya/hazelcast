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

package com.hazelcast.client;

import com.hazelcast.core.IdGenerator;

public class IdGeneratorClientProxy implements IdGenerator {
    private final String name;
    private final PacketProxyHelper proxyHelper;

    public IdGeneratorClientProxy(HazelcastClient hazelcastClient, String name) {
        this.name = name;
        proxyHelper = new PacketProxyHelper(name, hazelcastClient);
    }

    public String getName() {
        return name;
    }

    public long newId() {
        return 0;
//        return (Long) proxyHelper.doOp(ClusterOperation.NEW_ID, null, null);
    }

    public void destroy() {
        proxyHelper.destroy();
    }

    public Object getId() {
        return name;
    }
}
