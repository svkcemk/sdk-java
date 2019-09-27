/**
 * Copyright 2019 The CloudEvents Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudevents.fun;

import java.util.Map;

import io.cloudevents.format.Wire;

/**
 * 
 * @author fabiojose
 *
 */
@FunctionalInterface
public interface WireBuilder<P, K, V> {

	/**
	 * Builds a wire format
	 * @param payload
	 * @param headers
	 * @return
	 */
	Wire<P, K, V> build(P payload, Map<K, V> headers);
	
}