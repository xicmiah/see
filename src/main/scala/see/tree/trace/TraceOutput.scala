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

package see.tree.trace

import see.tree.Untyped._
import org.parboiled.errors.ErrorUtils
import see.parser.grammar.PropertyDescriptor

object TraceOutput {
  def getNodes(parent: Node): Seq[Node] = parent +: getChildren(parent)

  def getChildren(parent: Node): Seq[Node] = parent match {
    case ConstNode(_, _) => Seq()
    case VarNode(_, _) => Seq()
    case FNode(_, args, _) => args flatMap getNodes
    case PropertyNode(target, props, _) => getNodes(target) ++ getNodes(props)
  }

  def getNodes(props: Seq[PropertyDescriptor]): Seq[Node] = props.collect {
    case desc: PropertyDescriptor.Indexed => desc.getIndex.asInstanceOf[Node]
  }

  def formatTrace(node: Node) = for (pos <- node.position) yield
    ErrorUtils.printErrorMessage("%s line %s col %s", node.toString, pos.index, pos.input)

  def dump(parent: Node): Seq[String] = getNodes(parent) flatMap formatTrace

  def dump(parent: see.tree.Node[_]): Seq[String] = parent match {
    case node: Node => dump(node)
    case _ => Seq()
  }
}
