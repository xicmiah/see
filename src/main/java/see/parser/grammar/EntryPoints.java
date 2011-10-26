/*
 * Copyright 2011 Vasily Shiyan
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

package see.parser.grammar;

import org.parboiled.Parboiled;
import org.parboiled.Rule;
import see.parser.config.GrammarConfiguration;

/**
 * Grammar entry points.
 */
public class EntryPoints extends AbstractGrammar {
    final Expressions expressions;

    public EntryPoints(GrammarConfiguration configuration) {
        expressions = Parboiled.createParser(Expressions.class, configuration);
    }

    /**
     * Semicolon-separated list of expressions, ending with return keyword and a single expression.
     * @return cosutructed rule
     */
    public Rule CalcExpression() {
        return Sequence(Whitespace(), expressions.ReturnExpression(), EOI);
    }

    /**
     * A single expression. Assignment and "if" expressions not supported.
     * @return constructed rule
     */
    public Rule Condition() {
        return Sequence(Whitespace(), expressions.Expression(), EOI);
    }

    /**
     * Semicolon-separated list of expressions.
     * @return constructed rule
     */
    public Rule Statements() {
        return Sequence(Whitespace(), expressions.ExpressionList(), EOI);
    }

}
