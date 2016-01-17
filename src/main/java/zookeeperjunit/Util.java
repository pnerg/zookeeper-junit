/**
 *  Copyright 2016 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package zookeeperjunit;

/**
 * Utility with helper methods.
 * 
 * @author Peter Nerg
 * @since 1.1
 */
final class Util {
	/**
	 * Inhibitive constructor.
	 */
	private Util() {
	}

	/**
	 * Asserts that the provided integer is >= 0. <br>
	 * Throws a IllegalArgumentException if the assert fails.
	 * 
	 * @param i
	 *            The integer to assert
	 */
	static void assertPositive(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Only positive numbers are allowed");
		}
	}
}
