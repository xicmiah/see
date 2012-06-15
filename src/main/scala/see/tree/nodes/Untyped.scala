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

package see.tree.nodes

import see.tree.Node.ValueVisitor
import see.tree.Visitor
import see.functions.ContextCurriedFunction
import scala.collection.JavaConversions._
import see.parser.grammar.PropertyDescriptor

object Untyped {
  sealed abstract class Node extends see.tree.Node[Any]

  def const[T](f: String => T): String => ConstNode = { s:String => ConstNode(f(s)) }


  case class ConstNode(value: Any) extends Node with see.tree.ConstNode[Any] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getValue = value
  }

  case class VarNode(name: String) extends Node with see.tree.VarNode[Any] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getName = name
  }

  case class FNode(f: ContextCurriedFunction[Any, Any], args: IndexedSeq[Node])
    extends Node
    with see.tree.FunctionNode[Any, Any] {

    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getFunction = f
    def getArguments = args
  }

  case class PropertyNode(target: Node, props: Seq[PropertyDescriptor]) extends Node with see.tree.PropertyNode[Any] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getTarget = target
    def getProperties = props
  }
}
