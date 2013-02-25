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

object AltLiterals {
  def apply(decimalSeparator: Char) = new AltLiterals(decimalSeparator.toString)
}

class SimpleLiterals extends Parser {
  def Letter = "a" - "z" | "A" - "Z" | "_" | "$"
  def LetterOrDigit = Letter | Digit
  def Digit = "0" - "9"
  def BooleanLiteral = rule { ("true" | "false") ~ endOfWord }
  def NullLiteral: Rule0 = rule { "null" ~ endOfWord }

  def endOfWord = !LetterOrDigit

  def Identifier = rule { Letter ~ zeroOrMore(Letter | Digit) }

  implicit def toTerminal[R <: rules.Rule](rule: R) = new { def terminal = Terminal(rule) }
  def Terminal[R <: rules.Rule](body: R):R = body ~ Whitespace
  def T(body: Rule0) = body ~ Whitespace
  def Whitespace = optional(WhitespaceElement).suppressNode
  def WhitespaceElement = oneOrMore(
    anyOf(" \t\f\r\n") |
      "/*" ~ zeroOrMore(!"*/" ~ ANY) ~ "*/" |
      "//" ~ zeroOrMore(!anyOf("\r\n") ~ ANY) ~ ("\r\n" | "\r" | "\n" | EOI)
    ).suppressNode

}

class AltLiterals(val decimalSeparator: String) extends SimpleLiterals {

  def StringLiteral = DelimitedString("\'") | DelimitedString("\"")
  def DelimitedString(delimiter: String) = delimiter ~ zeroOrMore(!delimiter ~ ANY) ~ delimiter
  def stripQuotes(s: String) = s.substring(1, s.length - 1)

  def IntLiteral = oneOrMore(Digit)

  def FloatLiteral = zeroOrMore(Digit) ~ decimalSeparator ~ oneOrMore(Digit) ~ optional(Exponent) |
      oneOrMore(Digit) ~ Exponent

  def Exponent = anyOf("eE") ~ optional(anyOf("+-")) ~ oneOrMore(Digit)

}
