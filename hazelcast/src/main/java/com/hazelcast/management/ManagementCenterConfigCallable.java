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

package com.hazelcast.management;

import com.hazelcast.instance.HazelcastInstanceImpl;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.concurrent.Callable;

public class ManagementCenterConfigCallable extends ClusterServiceCallable implements Callable<Void>, DataSerializable {

    private String newUrl;
    private int redoCount = 10;

    public ManagementCenterConfigCallable() {
    }

    public ManagementCenterConfigCallable(String newUrl) {
        super();
        this.newUrl = newUrl;
    }

    public Void call() throws Exception {
        HazelcastInstanceImpl factory = (HazelcastInstanceImpl) hazelcastInstance;
        ManagementCenterService service = factory.node.getManagementCenterService();
        int count = 0;
        while (service == null && count < redoCount) {
            Thread.sleep(1000);
            count++;
            service = factory.node.getManagementCenterService();
        }
        if(service != null)
            service.changeWebServerUrl(newUrl);
        return null;
    }

    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(newUrl);
    }

    public void readData(ObjectDataInput in) throws IOException {
        newUrl = in.readUTF();
    }
}
