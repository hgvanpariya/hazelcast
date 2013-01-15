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

package com.hazelcast.map;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.BackupAwareOperation;
import com.hazelcast.spi.BackupOperation;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.impl.AbstractNamedKeyBasedOperation;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class EntryBackupOperation extends AbstractNamedKeyBasedOperation implements BackupOperation {

    EntryProcessor entryProcessor;

    Map.Entry entry;
    MapService mapService;

    public EntryBackupOperation(String name, Data dataKey, EntryProcessor entryProcessor) {
        super(name, dataKey);
        this.entryProcessor = entryProcessor;
    }

    public EntryBackupOperation() {
    }

    public void run() {
        mapService = (MapService) getService();
        RecordStore recordStore = mapService.getRecordStore(getPartitionId(), name);
        Map.Entry<Data, Data> dataEntry = recordStore.getMapEntry(dataKey);
        NodeEngine nodeEngine = mapService.getNodeEngine();
        Object key = nodeEngine.toObject(dataKey);
        Object value = nodeEngine.toObject(dataEntry.getValue());
        entry = new AbstractMap.SimpleEntry(key, value);
        entryProcessor.processBackup(entry);
        recordStore.put(new AbstractMap.SimpleImmutableEntry<Data, Data>(dataKey, nodeEngine.toData(entry.getValue())));
    }

    @Override
    public void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        entryProcessor = in.readObject();
    }

    @Override
    public void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeObject(entryProcessor);
    }

    @Override
    public Object getResponse() {
        return true;
    }

    @Override
    public String toString() {
        return "EntryOperation{}";
    }

}
