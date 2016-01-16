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

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import junitextensions.TryAssert;
import static zookeeperjunit.ZKConnectionUtil.createRecursive;
import static zookeeperjunit.ZKConnectionUtil.exists;

/**
 * Test the class {@link CloseableZooKeeper}
 * @author Peter Nerg
 */
public class TestCloseableZooKeeper extends BaseAssert implements TryAssert {

	private static Duration duration = Duration.ofSeconds(5);
	private static ZKInstance instance = ZKFactory.apply().create();
	
	private static String connectString;
	
	@BeforeClass
	public static void startZooKeeper() throws TimeoutException, Throwable {
		instance.start().result(duration);
		connectString = instance.connectString().get();
	}
	
	@AfterClass
	public static void stopZooKeeper() throws TimeoutException, Throwable {
		instance.destroy().result(duration);
	}
	
	@Test
	public void deleteRecursively_existingPath() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = CloseableZooKeeper.blockingConnect(connectString, duration)) {
			//create the path to delete
			String path = "/tmp/delete_existingPath";
			createRecursive(zookeeper,path);
			//assert the path is gone
			assertTrue(exists(zookeeper, path));
			
			//delete the path
			zookeeper.deleteRecursively("/tmp");

			//assert the path is gone
			assertFalse(exists(zookeeper, path));
		}
	}
	
	@Test
	public void getData_success() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = CloseableZooKeeper.blockingConnect(connectString, duration)) {
			String path = "/tmp/getData_success";
			String data = "Peter was here!";
			ZKConnectionUtil.createRecursive(zookeeper, path, data.getBytes());
			assertTrue(exists(zookeeper, path));
			
			assertEquals(data, new String(zookeeper.getData(path).get()));
		}		
	}
	
	@Test
	public void getData_failure() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = CloseableZooKeeper.blockingConnect(connectString, duration)) {
			assertFailure(zookeeper.getData("/no-such-path"));
		}		
	}
	
}
