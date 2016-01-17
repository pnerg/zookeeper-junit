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

import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Peter Nerg
 *
 */
public class TestZooKeeperAssert extends BaseAssert implements ZooKeeperAssert {

	private static ZKInstance zkInstance = ZKFactory.apply().create();

	@BeforeClass
	public static void startZK() throws TimeoutException, Throwable {
		zkInstance.start().result(duration);
	}
	
	@AfterClass
	public static void stopZK() throws TimeoutException, Throwable {
		zkInstance.destroy().result(duration);
	}
	
	/* (non-Javadoc)
	 * @see zookeeperjunit.ZooKeeperAssert#instance()
	 */
	@Override
	public ZKInstance instance() {
		return zkInstance;
	}
	
	@Test(expected = AssertionError.class)
	public void exists() {
		exists("/no-such-path");
	}

}
