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

package see.parser.grammar

import org.parboiled.scala._
import see.parser.numbers.NumberFactory
import see.parser.config.FunctionResolver

class AltEntryPoints(val numberFactory: NumberFactory,
                     val functions: FunctionResolver) extends Parser {

  val expressions = new AltExpressions(numberFactory, functions)
  val literals = new SimpleLiterals

  import literals.Whitespace
  import expressions.{ReturnExpression, Expression, ExpressionList}


  def CalcExpression = rule { Whitespace ~ ReturnExpression ~ EOI }
  def Condition = rule { Whitespace ~ Expression ~ EOI }
  def Script = rule { Whitespace ~ ExpressionList ~ EOI }
}
