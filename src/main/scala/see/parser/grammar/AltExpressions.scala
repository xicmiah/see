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
import see.tree.Untyped._

class AltExpressions(val numberFactory: NumberFactory,
                     val functions: FunctionResolver) extends Parser {

  val argumentSeparator = if (numberFactory.getDecimalSeparator != ',') "," else ";"
  val literals = AltLiterals(numberFactory.getDecimalSeparator)

  import literals._

  def fNode(name: String, args: Node*):Node = FNode(name, args.toIndexedSeq)
  def op(body: Rule0) = (body ~> identity).terminal
  def repeatWithOperator(body: Rule1[Node], operator: Rule0) = body ~ zeroOrMore(op(operator) ~ body ~~> ((a:Node, op, b) => fNode(op, a, b)))
  def binOp(op: String) = (a: Node, b: Node) => fNode(op, a, b)
  def seqNode(terms: Node*) = if (terms.size == 1) terms.head else fNode(";", terms:_*)


  def ReturnExpression = rule { ExpressionList ~ T("return") ~ RightExpression ~ optional(T(";")) ~~> (seqNode(_, _)) }


  def Block = rule { T("{") ~ ExpressionList ~ T("}") | Term }
  def ExpressionList = rule { zeroOrMore(Term) ~~> (seqNode(_:_*)) }
  def Term:Rule1[Node] = rule { Conditional | ForLoop | WhileLoop | TerminatedExpression }

  def Conditional = rule {
    T("if") ~ T("(") ~ RightExpression ~ T(")") ~ Block ~ optional(T("else") ~ Block) ~~>
    ((cond, then, elseOpt) => fNode("if", cond::then::elseOpt.toList:_*))
  }
  def ForLoop = rule {
    T("for") ~ T("(") ~ VarName ~ (T(":" | "in")) ~ RightExpression ~ T(")") ~
      Block ~~>
      ((varName, target, body) => fNode("for", varName, target, ConstNode(body)))
  }
  def WhileLoop = rule { T("while") ~ T("(") ~ Expression ~ T(")") ~ Block ~~> binOp("while") }
  def TerminatedExpression = rule { Expression ~ T(";") }


  def Expression: Rule1[Node] = rule { Assignment | Binding | RightExpression }

  def Assignment = rule { Settable ~ T("=") ~ Expression ~~> binOp("=") }
  def Settable = rule { SettableProperty | SettableVariable }
  def SettableProperty = rule { Atom ~ oneOrMore(optional(FunctionApplication) ~ PropertyChain) }.memoMismatches
  def SettableVariable = rule { VarName ~~> (fNode("v=", _)) }.memoMismatches

  def Binding = rule { Settable ~ (
    T("<<") ~ SignalExpression ~~> binOp("<<")
  | T("<<=") ~ SignalExpression ~~> binOp("=")
    )
  }
  def SignalExpression = rule { Expression ~~> (expr => fNode("signal", ConstNode(expr))) }

  def RightExpression:Rule1[Node] = rule { OrExpression }

  def OrExpression = rule  { repeatWithOperator(AndExpression, "||") }
  def AndExpression = rule  { repeatWithOperator(EqualExpression, "&&") }
  def EqualExpression = rule { repeatWithOperator(RelationalExpression, "!=" | "==") }
  def RelationalExpression = rule { repeatWithOperator(AdditiveExpression, "<=" | ">=" | "<" | ">") }
  def AdditiveExpression = rule { repeatWithOperator(MultiplicativeExpression, "+" | "-") }
  def MultiplicativeExpression = rule { repeatWithOperator(UnaryExpression, "*" | "/") }

  def UnaryExpression:Rule1[Node] = rule {
    op(anyOf("+-!")) ~ UnaryExpression ~~> (fNode(_, _)) | PowerExpression
  }

  def PowerExpression = rule { PropertyExpression ~ optional(T("^") ~ UnaryExpression ~~> binOp("^")) }

  def PropertyExpression = rule { Atom ~ zeroOrMore(FunctionApplication | PropertyChain ~ GetProperty ) }

  def FunctionApplication = rule {
    T("(") ~ zeroOrMore(Expression, separator = ArgSeparator) ~ T(")") ~~>
      ((target:Node, args) => fNode("apply", target::args:_*))
  }
  def PropertyChain:ReductionRule1[Node, Node] = rule {
    oneOrMore(SimpleProperty | IndexedProperty) ~~> ((target:Node, props) => PropertyNode(target, props))
  }
  def GetProperty = rule { EMPTY ~~> (fNode(".", _:Node))  }
  def SimpleProperty = rule { "." ~ (Identifier ~> PropertyDescriptor.simple _) }.terminal
  def IndexedProperty = rule { T("[") ~ RightExpression ~ T("]") ~~> PropertyDescriptor.indexed _ }


  def Atom = rule {
    Constant |
      SpecialForm |
      FunctionDefinition |
      ShortFunctionDef |
      Variable |
      JsonLiteral |
      T("(") ~ Expression ~ T(")")
  }


  def JsonLiteral = rule { ListLiteral | MapLiteral }
  def ListLiteral = rule {
    T("[") ~ zeroOrMore(Expression, separator = ArgSeparator) ~ T("]") ~~> (fNode("[]", _:_*))
  }
  def MapLiteral = rule {
    T("{") ~ zeroOrMore(KeyValue, separator = ArgSeparator) ~ T("}") ~~> (pairs => fNode("{}", pairs.flatten:_*))
  }
  def KeyValue = rule { (JsonKey | String) ~ T(":") ~ Expression ~~> (Seq(_, _)) }
  def JsonKey = rule { oneOrMore(Letter | Digit) ~> ConstNode }.terminal


  def FunctionDefinition = rule {
    T("function") ~ T("(") ~ ArgumentDeclaration ~ T(")") ~ T("{") ~ ExpressionList ~ T("}") ~~>
      ((args, body) => fNode("def", constList(args), ConstNode(body)))
  }

  def ShortFunctionDef: Rule1[Node] = rule {
    ShortArgList ~ T("=>") ~ RightExpression ~~> ((args, body) => fNode("def", constList(args), ConstNode(body)))
  }

  def ShortArgList = rule { T("(") ~ ArgumentDeclaration ~ T(")") | op(Identifier) ~~> (id => List(id)) }

  def SpecialForm = rule { MakeSignal | Tree }
  def MakeSignal = rule { T("signal") ~ T("(") ~ SignalExpression ~ T(")") }
  def Tree = rule { T("@tree") ~ Expression ~~> ConstNode.apply _ }


  def ArgSeparator = T(argumentSeparator)
  def ArgumentDeclaration = rule { zeroOrMore(op(Identifier), separator = ArgSeparator) }


  def Variable = rule { optional(T("var" ~ !LetterOrDigit)) ~ (Identifier ~> VarNode) }.terminal
  def VarName = rule { Variable ~~> (varNode => ConstNode(varNode.name)) }
  def Constant = rule { String | Number | Boolean | Null }.suppressSubnodes

  def String = rule { StringLiteral ~> const(stripQuotes(_)) }.terminal
  def Number = rule { (FloatLiteral | IntLiteral) ~> const(numberFactory.getNumber(_)) }.terminal
  def Boolean = rule { BooleanLiteral ~> toBoolean }.terminal
  def Null  = rule { NullLiteral ~ push(ConstNode(null)) }.terminal

  def toBoolean(text: String) = ConstNode(text.toBoolean: java.lang.Boolean)
}
