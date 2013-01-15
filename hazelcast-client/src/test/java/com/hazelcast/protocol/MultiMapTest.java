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

package com.hazelcast.protocol;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MultiMapTest extends ProtocolTest {

    @Test
    @Ignore
    public void put() throws IOException {
        put(socket, "1".getBytes(), "a".getBytes(), 0);
        assertTrue(read(socket).contains("true"));
    }

    private List<String> put(Socket socket, byte[] key, byte[] value, long ttl) throws IOException {
        OutputStream out = doOp("MMPUT default " + ttl + " #2", "" + key.length + " " + value.length, socket);
        out.write(key);
        out.write(value);
        out.write("\r\n".getBytes());
        out.flush();
        return read(socket);
    }
}
