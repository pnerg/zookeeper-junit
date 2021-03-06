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

import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

/**
 * Utility methods to poke around with ZooKeeper.
 * @author Peter Nerg
 */
abstract class ZKConnectionUtil {

	static boolean createIfNotExist(ZooKeeper zooKeeper, String path, byte[] data) throws KeeperException, InterruptedException {
		return createIfNotExist(zooKeeper, path, data, OPEN_ACL_UNSAFE, PERSISTENT);
	}

	static boolean createIfNotExist(ZooKeeper zooKeeper, String path, byte[] data, List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException {
		boolean result = false;
		try {
			// check if the path exists before trying to create
			if (!exists(zooKeeper, path)) {
				zooKeeper.create(path, data, acl, createMode);
				result = true;
			}
		}
		// this may still happen in a concurrent world some other process/thread may end up creating the path
		// after we did the exists(...) operation, so just in case we need to manage the exception
		catch (KeeperException.NodeExistsException ex) {
		}

		return result;
	}

	static boolean createRecursive(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
		return createRecursive(zooKeeper, path, new byte[0], OPEN_ACL_UNSAFE, PERSISTENT);
	}

	static boolean createRecursive(ZooKeeper zooKeeper, String path, byte[] data) throws KeeperException, InterruptedException {
		return createRecursive(zooKeeper, path, data, OPEN_ACL_UNSAFE, PERSISTENT);
	}

	static boolean createRecursive(ZooKeeper zooKeeper, String path, byte[] data, List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException {
		try {
			return createIfNotExist(zooKeeper, path, data, acl, createMode);
		} catch (KeeperException.NoNodeException ex) {
			int pos = path.lastIndexOf("/");
			String parentPath = path.substring(0, pos);
			createRecursive(zooKeeper, parentPath, new byte[0], acl, createMode);
			return createIfNotExist(zooKeeper, path, data, acl, createMode);
		}
	}

	static boolean exists(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
		return zooKeeper.exists(path, null) != null;
	}

}
