/*
 * Licensed to Intel Corporation under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Intel Corporation licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intel.analytics.bigdl.nn

import com.intel.analytics.bigdl.nn.abstractnn.AbstractModule
import com.intel.analytics.bigdl.tensor.Tensor
import com.intel.analytics.bigdl.tensor.TensorNumericMath.TensorNumeric
import com.intel.analytics.bigdl.utils.{T, Table}

import scala.reflect.ClassTag

@SerialVersionUID(- 4318640284973082779L)
class SplitTable[T: ClassTag](
  var dimension: Int,
  var nInputDims: Int = -1)
  (implicit ev: TensorNumeric[T]) extends AbstractModule[Tensor[T], Table, T]{

  private def getPositiveDimension(input: Tensor[T]): Int = {
    if (dimension < 0) {
      input.dim() + dimension + 1
    } else if (dimension != Int.MinValue && input.dim() == nInputDims + 1) {
      dimension + 1
    } else {
      dimension
    }
  }

  override def updateOutput(input: Tensor[T]): Table = {
    val dim = getPositiveDimension(input)
    val slices = input.size(dim)

    val currentOutput = T()
    var i = 1
    while (i <= slices) {
      currentOutput.insert(input.select(dim, i))
      i += 1
    }
    output = currentOutput

    output
  }

  override def updateGradInput(input: Tensor[T], gradOutput: Table): Tensor[T] = {
    val dim = getPositiveDimension(input)
    val slices = input.size(dim)

    gradInput.resizeAs(input)

    var i = 1
    while (i <= slices) {
      val currentGradInput: Tensor[T] = gradOutput(i)
      gradInput.select(dim, i).copy(currentGradInput)
      i += 1
    }

    gradInput
  }


  override def canEqual(other: Any): Boolean = other.isInstanceOf[SplitTable[T]]

  override def equals(other: Any): Boolean = other match {
    case that: SplitTable[_] =>
      super.equals(that) &&
        (that canEqual this) &&
        dimension == that.dimension &&
        nInputDims == that.nInputDims
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(super.hashCode(), dimension, nInputDims)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }


  override def toString: String = s"SplitTable($dimension, $nInputDims)"
}

object SplitTable {
  def apply[@specialized(Float, Double) T: ClassTag](
    dimension: Int,
    nInputDims: Int = -1)(implicit ev: TensorNumeric[T]) : SplitTable[T] = {
    new SplitTable[T](dimension, nInputDims)
  }
}