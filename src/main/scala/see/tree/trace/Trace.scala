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

case class TraceElement(input: InputBuffer, index: Int) {
  override def toString = ErrorUtils.printErrorMessage("%s line %s col %s", "at", index, input)

  private val buffersEqual: Equiv[InputBuffer] = Equiv.by(bufferToString)

  private def bufferToString(buffer: InputBuffer) = buffer.extract(0, Int.MaxValue)

  override def equals(obj: Any) = obj match {
    case TraceElement(otherInput, otherIndex) => buffersEqual.equiv(input, otherInput) && index == otherIndex
    case _ => false
  }

  override def hashCode() = (bufferToString(input), index).hashCode()
}


trait Tracing {
  def position: Option[TraceElement]
}
