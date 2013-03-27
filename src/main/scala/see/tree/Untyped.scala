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

package see.tree

import see.functions.ContextCurriedFunction
import scala.collection.JavaConversions._
import see.parser.grammar.PropertyDescriptor
import com.google.common.collect.ImmutableList
import java.util
import trace.{TraceElement, Tracing}
import scala.None
import org.parboiled.Context

object Untyped {
  type SeeNode = see.tree.Node[AnyRef]
  sealed abstract class Node extends SeeNode with Tracing {
    def withPos(trace: TraceElement):Node
    def withPos(context: Context[Any]):Node = withPos(TraceElement(context.getInputBuffer, context.getStartIndex))
  }

  def const[T <: AnyRef](f: String => T): String => ConstNode = { s:String => ConstNode(f(s)) }
  def constList[T](items: Seq[T]) = ConstNode(seqAsJavaList(items))

  case class ConstNode(value: AnyRef, position:Option[TraceElement] = None) extends Node with see.tree.ConstNode[AnyRef] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getValue = value

    def withPos(trace: TraceElement) = copy(position = Some(trace))

    override def toString = String.valueOf(value)
  }

  case class VarNode(name: String, position: Option[TraceElement] = None) extends Node with see.tree.VarNode[AnyRef] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getName = name

    def withPos(trace: TraceElement) = copy(position = Some(trace))

    override def toString = "Var(%s)".format(name)
  }

  case class FNode(f: String, args: IndexedSeq[Node], position: Option[TraceElement] = None)
    extends Node
    with see.tree.FunctionNode[AnyRef, AnyRef] {

    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getFunctionName = f
    val getArguments = ImmutableList.copyOf(args.iterator).asInstanceOf[util.List[SeeNode]]

    def withPos(trace: TraceElement) = copy(position = Some(trace))

    override def toString = "%s(%s)".format(f, args.mkString(","))
  }

  case class PropertyNode(target: Node, props: Seq[PropertyDescriptor], position: Option[TraceElement] = None) extends Node with see.tree.PropertyNode[AnyRef] {
    def accept(visitor: Visitor) = visitor.visit(this)
    def accept[V](visitor: ValueVisitor[V]) = visitor.visit(this)

    def getTarget = target
    val getProperties = ImmutableList.copyOf(props.iterator)

    def withPos(trace: TraceElement) = copy(position = Some(trace))

    override def toString = "%s%s".format(target, props.mkString)
  }
}
