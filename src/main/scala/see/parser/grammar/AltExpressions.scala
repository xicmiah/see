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
import see.tree.nodes.Untyped._

class AltExpressions(val numberFactory: NumberFactory,
                     val functions: FunctionResolver) extends Parser {

  val argumentSeparator = if (numberFactory.getDecimalSeparator != ',') "," else ";"
  val literals = AltLiterals(numberFactory.getDecimalSeparator)

  import literals._

  def fNode(name: String, args: Node*) = FNode(functions.get(name).asInstanceOf, args.toIndexedSeq)
  def fNode(name: String, args: Seq[Node]):Node = fNode(name, args:_*)

  def ExpressionList = Expression
  def Expression: Rule1[Node] = RightExpression
  def RightExpression = Atom


  def PropertyExpression = rule { Atom ~ zeroOrMore(
    FunctionApplication ~~> ((target:Node, args) => fNode("apply", target::args))
  | PropertyChain ~~> ((target:Node, args) => fNode(".", PropertyNode(target, args)))
  )}

  def FunctionApplication = rule { T("(") ~ zeroOrMore(Expression, separator = argumentSeparator) ~ T(")") }
  def PropertyChain = oneOrMore(SimpleProperty | IndexedProperty)
  def SimpleProperty = rule { "." ~ (Identifier ~> PropertyDescriptor.simple _) }.terminal
  def IndexedProperty = rule { T("[") ~ RightExpression ~ T("]") ~~> PropertyDescriptor.indexed _ }


  def Atom = rule {
    Constant |
      SpecialForm |
      FunctionDefinition |
      Variable |
      JsonLiteral |
      T("(") ~ Expression ~ T(")")
  }


  def JsonLiteral = rule { ListLiteral | MapLiteral }
  def ListLiteral = rule {
    T("[") ~ zeroOrMore(Expression, separator = T(",")) ~ T("]") ~~> (fNode("[]", _))
  }
  def MapLiteral = rule {
    T("{") ~ zeroOrMore(KeyValue, separator = T(",")) ~ T("}") ~~> (pairs => fNode("{}", pairs.flatten))
  }
  def KeyValue = rule { (JsonKey | String) ~ T(":") ~ Expression ~~> (Seq(_, _)) }
  def JsonKey = rule { zeroOrMore(Letter | Digit) ~> ConstNode }.terminal


  def FunctionDefinition = rule {
    T("function") ~ T("(") ~ ArgumentDeclaration ~ T(")") ~ T("{") ~ ExpressionList ~ T("}") ~~>
      ((args, body) => fNode("functions", ConstNode(args), body))
  }


  def SpecialForm = rule { IsDefined | MakeSignal | Tree }
  def IsDefined = rule { T("isDefined") ~ T("(") ~ VarName ~ T(")") ~~> (fNode("isDefined", _)) }
  def MakeSignal = rule { T("signal") ~ T("(") ~ Expression ~ T(")") ~~> (expr => fNode("signal", ConstNode(expr))) }
  def Tree = rule { T("@tree") ~ Expression ~~> ConstNode.apply _ }


  def ArgSeparator = T(argumentSeparator)
  def ArgumentDeclaration = rule { zeroOrMore(Identifier ~> identity, separator = ArgSeparator) }


  def Variable = rule { Identifier ~> VarNode }.terminal
  def VarName = rule { Identifier ~> ConstNode }.terminal
  def Constant = rule { String | Number | Boolean | Null }.suppressSubnodes.terminal

  def String = rule { StringLiteral ~> const(stripQuotes(_)) }
  def Number = rule { (FloatLiteral | IntLiteral) ~> const(numberFactory.getNumber(_)) }
  def Boolean = rule { BooleanLiteral ~> const(java.lang.Boolean.valueOf(_)) }
  def Null  = rule { NullLiteral ~> const(null) }
}
