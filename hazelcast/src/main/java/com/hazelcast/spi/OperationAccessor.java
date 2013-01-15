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

package com.hazelcast.spi;

import com.hazelcast.spi.annotation.PrivateApi;

/**
 * @mdogan 12/21/12
 */

@PrivateApi
public class OperationAccessor {

    public static void setCallId(Operation op, long callId) {
        op.setCallId(callId);
    }

    public static void setStartTime(Operation op, long startTime) {
        op.setStartTime(startTime);
    }

    public static void setInvocationTime(Operation op, long invocationTime) {
        op.setInvocationTime(invocationTime);
    }

    public static void setCallTimeout(Operation op, long callTimeout) {
        op.setCallTimeout(callTimeout);
    }
}
