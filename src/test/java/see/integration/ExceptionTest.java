/*
 * Copyright 2012 Vasily Shiyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package see.integration;

import org.junit.Test;
import see.See;
import see.exceptions.SeeRuntimeException;
import see.tree.trace.TraceElement;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExceptionTest {
    See see = new See();

    @Test
    public void testExceptions() throws Exception {
        try {
            see.eval("1+2*(1/0)");
        } catch (SeeRuntimeException e) {
            List<TraceElement> trace = e.getTrace();
            assertEquals(3, trace.size());
        }
    }
}
