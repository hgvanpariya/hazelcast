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

package com.hazelcast.query;

import com.hazelcast.core.MapEntry;
import com.hazelcast.query.impl.Index;
import com.hazelcast.query.impl.QueryContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IndexAwarePredicate extends Predicate {
    boolean collectIndexAwarePredicates(List<IndexAwarePredicate> lsIndexAwarePredicates, Map<Expression, Index> mapIndexes);

    void collectAppliedIndexes(Set<Index> setAppliedIndexes, Map<Expression, Index> mapIndexes);

    Set<MapEntry> filter(QueryContext queryContext);

    boolean isIndexed(QueryContext queryContext);
}
