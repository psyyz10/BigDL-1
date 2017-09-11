/*
 * Copyright 2016 The BigDL Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intel.analytics.bigdl.nn.ops

import com.intel.analytics.bigdl.tensor.Tensor
import com.intel.analytics.bigdl.tensor.TensorNumericMath.TensorNumeric

import scala.reflect.ClassTag

class TruncatedNormal[T: ClassTag](
  mean: Double = 0.0,
  stddev: Double = 1.0,
  seed: Int = 0
)
  (implicit ev: TensorNumeric[T]) extends Operation[Tensor[T], T] {

  def updateOutput(input: Tensor[T]): Tensor[T] = {
    require(input.nDimension() == 1, "the shape should be a one-dimensional tensor.")

    val shape = input.asInstanceOf[Tensor[Int]].storage().toArray
    output.resize(shape).randn(
      mean.asInstanceOf[Double],
      stddev.asInstanceOf[Double])

    output
  }
}

object TruncatedNormal {
  def apply[T: ClassTag](
    mean: Double = 0.0,
    stddev: Double = 1.0,
    seed: Int = 0)
    (implicit ev: TensorNumeric[T]): Operation[Tensor[T], T]
  = ModuleToOperation[Tensor[T], T](
    new TruncatedNormal(mean, stddev, seed))
}
