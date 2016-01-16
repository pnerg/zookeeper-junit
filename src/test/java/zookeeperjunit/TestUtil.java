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

import org.junit.Test;
import static zookeeperjunit.Util.assertPositive;
/**
 * Test the class {@link Util}.
 * @author Peter Nerg
 */
public class TestUtil extends BaseAssert {

	@Test
	public void assertPositive_success() {
		assertPositive(666);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void assertPositive_failure() {
		assertPositive(-666);
	}
}