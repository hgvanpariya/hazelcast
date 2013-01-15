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

package com.hazelcast.collection.multimap;

import com.hazelcast.collection.processor.BaseEntryProcessor;
import com.hazelcast.collection.processor.Entry;

import java.util.Collection;

/**
 * @ali 1/9/13
 */
public class CountEntryProcessor extends BaseEntryProcessor<Integer> {

    public CountEntryProcessor() {
    }

    public Integer execute(Entry entry) {
        Collection coll = entry.getValue();
        return coll == null ? 0 : coll.size();
    }
}
