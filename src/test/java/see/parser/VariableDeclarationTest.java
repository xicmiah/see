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

package see.parser;

import org.junit.Test;
import see.See;
import see.tree.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static see.parser.ExpressionMatcher.expressionList;
import static see.parser.ExpressionMatcher.singleExpression;

public class VariableDeclarationTest {
    See see = new See();

    @Test
    public void testVarDeclarations() throws Exception {
        assertThat("var c = 9", singleExpression());
    }

    @Test
    public void testForLoopDeclarations() throws Exception {
        assertThat("for(var a in items) a.omg;", expressionList());
    }

    @Test
    public void testIdentifiersWithVar() throws Exception {
        Node<?> tree = see.parseExpressionList("varC = 'bka'; varC;");
        assertEquals("bka", see.evaluate(tree));
    }
}
