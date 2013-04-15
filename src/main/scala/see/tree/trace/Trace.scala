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

import org.parboiled.buffers.InputBuffer
import org.parboiled.errors.ErrorUtils
import org.parboiled.support.Position

object TraceElement {
  def apply(input: InputBuffer, index: Int): TraceElement = {
    val msg = ErrorUtils.printErrorMessage("%s line %s col %s", "at", index, input)
    val position = input.getPosition(index)
    val line = input.extractLine(position.line)
    TraceElement(msg, line, position.line, position.column)
  }
}

case class TraceElement(msg: String, affectedLine: String, line: Int, col: Int) {
  override def toString = msg

  def position = new Position(line, col)
}


trait Tracing {
  def position: Option[TraceElement]
}
