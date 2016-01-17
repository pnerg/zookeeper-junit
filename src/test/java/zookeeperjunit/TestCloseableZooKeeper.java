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

import static zookeeperjunit.ZKConnectionUtil.createRecursive;
import static zookeeperjunit.ZKConnectionUtil.exists;

import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junitextensions.TryAssert;

/**
 * Test the class {@link CloseableZooKeeper}
 * @author Peter Nerg
 */
public class TestCloseableZooKeeper extends BaseAssert implements TryAssert {

	private static Duration duration = Duration.ofSeconds(5);
	private static ZKInstance instance = ZKFactory.apply().create();
	
	private static String connectString;
	
	private final AtomicLong counter = new AtomicLong(1);
	private final String rootPath = "/TestCloseableZooKeeper-"+counter.getAndIncrement();
	
	@BeforeClass
	public static void startZooKeeper() throws TimeoutException, Throwable {
		instance.start().result(duration);
		connectString = instance.connectString().get();
	}
	
	@AfterClass
	public static void stopZooKeeper() throws TimeoutException, Throwable {
		instance.destroy().result(duration);
	}
	
	@Before
	public void before() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
			createRecursive(zookeeper, rootPath);
		}		
	}
	
	@After
	public void after() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
			zookeeper.deleteRecursively(rootPath);
		}		
	}
	
	@Test
	public void deleteRecursively_existingPath() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
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
		try(CloseableZooKeeper zookeeper = connection()) {
			String path = "/tmp/getData_success";
			String data = "Peter was here!";
			ZKConnectionUtil.createRecursive(zookeeper, path, data.getBytes());
			assertTrue(exists(zookeeper, path));
			
			assertEquals(data, new String(zookeeper.getData(path).get()));
		}		
	}

	@Test
	public void exists_existingPath() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
			assertSuccess(true, zookeeper.exists(rootPath));
		}		
	}

	@Test
	public void exists_nonExistingPath() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
			assertSuccess(false, zookeeper.exists("/no-such-path"));
		}		
	}
	
	@Test
	public void getData_failure() throws TimeoutException, Throwable {
		try(CloseableZooKeeper zookeeper = connection()) {
			assertFailure(zookeeper.getData("/no-such-path"));
		}		
	}
	
	private CloseableZooKeeper connection() throws TimeoutException, Throwable {
		return CloseableZooKeeper.blockingConnect(connectString, duration);
	}
}
