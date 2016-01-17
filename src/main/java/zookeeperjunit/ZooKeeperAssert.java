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

import javascalautils.Try;
import junitextensions.TryAssert;

/**
 * Provides additional assert operations related to ZooKeeper data.
 * @author Peter Nerg
 * @since 1.1
 */
public interface ZooKeeperAssert extends TryAssert {

	/**
	 * Assert that the provided path exists in ZooKeeper. <br>
	 * This method will automatically create/close a {@link #connection()} to get access to the ZooKeeper data. 
	 * 
	 * @param path
	 *            The path to assert
	 * @since 1.1
	 */
	default void assertExists(String path) {
		try (CloseableZooKeeper zookeeper = connection()) {
			assertSuccess(true, zookeeper.exists(path));
		}
	}

	/**
	 * Assert that the provided path not exists in ZooKeeper. <br>
	 * This method will automatically create/close a {@link #connection()} to get access to the ZooKeeper data. 
	 * 
	 * @param path
	 *            The path to assert
	 * @since 1.1
	 */
	default void assertNotExists(String path) {
		try (CloseableZooKeeper zookeeper = connection()) {
			assertSuccess(false, zookeeper.exists(path));
		}
	}

	/**
	 * Gets a connection to the ZooKeeper instance. <br>
	 * Should the instance not be running this will fail with an AssertionError.
	 * 
	 * @return The connection
	 * @since 1.1
	 */
	default CloseableZooKeeper connection() {
		Try<CloseableZooKeeper> connect = instance().connect();
		assertSuccess(connect);
		return connect.orNull(); // orNull will never happen as we've asserted it's a Success
	}

	/**
	 * Provides the ZKInstance for the test framework to use. <br>
	 * This is needed to be able to create connections towards the ZooKeeper instance.
	 * 
	 * @return The ZKInstance used by the test case
	 * @since 1.1
	 */
	ZKInstance instance();

}
