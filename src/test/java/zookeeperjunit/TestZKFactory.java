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

import java.io.File;

import org.junit.Test;

/**
 * Test the {@link ZKFactory}.
 * @author Peter Nerg
 */
public class TestZKFactory extends BaseAssert {

	private final ZKFactory factory = ZKFactory.apply();
	
	@Test
	public void create() {
		ZKInstance instance = factory.create();
		assertNotNull(instance);
	}
	
	@Test
	public void withPort() {
		ZKInstance instance = factory.withPort(6969).create();
		assertNotNull(instance);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void withRootDir_illegalDir() {
		factory.withRootDir(new File("no-such-dir"));
	}
	
	@Test
	public void withRootDir() {
		ZKInstance instance = factory.withRootDir(new File("target")).create();
		assertNotNull(instance);
	}
}
