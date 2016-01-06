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

import java.io.IOException;

import org.junit.Test;

/**
 * Test the class FreePortUtil.
 * @author Peter Nerg
 */
public class TestFreePortUtil extends BaseAssert {

	@Test
	public void getFreePort_withZero() throws IOException {
		assertTrue(FreePortUtil.getFreePort(0) > 0);
	}
	
	@Test
	public void getFreePort_withNonZero() throws IOException {
		assertEquals(6969, FreePortUtil.getFreePort(6969));
	}
}
