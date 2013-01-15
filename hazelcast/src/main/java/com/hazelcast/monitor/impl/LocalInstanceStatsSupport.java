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

package com.hazelcast.monitor.impl;

import com.hazelcast.monitor.LocalInstanceOperationStats;
import com.hazelcast.monitor.LocalInstanceStats;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

abstract class LocalInstanceStatsSupport<T extends LocalInstanceOperationStats>
        implements LocalInstanceStats<T>, DataSerializable {

    T operationStats;

    public final T getOperationStats() {
        return operationStats;
    }

    public final void setOperationStats(T operationStats) {
        this.operationStats = operationStats;
    }

    public final void writeData(ObjectDataOutput out) throws IOException {
        operationStats.writeData(out);
        writeDataInternal(out);
    }

    void writeDataInternal(ObjectDataOutput out) throws IOException {
    }

    public final void readData(ObjectDataInput in) throws IOException {
        operationStats = newOperationStatsInstance();
        operationStats.readData(in);
        readDataInternal(in);
    }

    void readDataInternal(ObjectDataInput in) throws IOException {
    }

    abstract T newOperationStatsInstance();
}
