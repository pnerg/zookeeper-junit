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

import static javascalautils.TryCompanion.Try;
import static javascalautils.concurrent.FutureCompanion.Future;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import javascalautils.Try;
import javascalautils.Unit;
import javascalautils.concurrent.Future;

/**
 * Extends the standard {@link ZooKeeper} connection class with the {@link Closeable} interface. <br>
 * This is to be able to use it in try-with-resources clauses.
 * 
 * @author Peter Nerg
 * @since 1.1
 */
public final class CloseableZooKeeper extends ZooKeeper implements Closeable {
	CloseableZooKeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
		super(connectString, sessionTimeout, watcher);
	}

	/**
	 * Creates a ZooKeeper connection.
	 * 
	 * @param connectString
	 *            The connect string
	 * @return A Future which will be completed once the connections is properly made and we're connected.
	 */
	static Future<CloseableZooKeeper> connect(String connectString) {
		return Future(() -> {
			CountDownLatch latch = new CountDownLatch(1);
			CloseableZooKeeper zk = new CloseableZooKeeper(connectString, 10000, event -> {
				if (event.getState() == KeeperState.SyncConnected) {
					latch.countDown();
				}
			});
			latch.await(); // this blocks indefinitely
			return zk;
		});
	}

	/**
	 * Creates a ZooKeeper connection.
	 * 
	 * @param connectString
	 *            The connect string
	 * @param duration
	 *            The duration to wait for a connection to be established
	 * @return The connection
	 * @throws TimeoutException
	 *             If exceed the provided duration
	 * @throws Throwable
	 *             Any other issue
	 */
	static CloseableZooKeeper blockingConnect(String connectString, Duration duration) throws TimeoutException, Throwable {
		return connect(connectString).result(duration);
	}

	/**
	 * Recursively deletes the specified node and all of it's children. <br>
	 * Will fail if the node/path does not exist or ZK is down.
	 * 
	 * @param path
	 *            The path
	 * @return The result of the operation
	 * @since 1.1
	 */
	public Try<Unit> deleteRecursively(String path) {
		Try<Stream<String>> children = getChildren(path);
		getChildren(path).forEach(stream -> {
			stream.forEach(child -> {
				String joinedZNode = path.equals("/") ? path + child : path + "/" + child;
				deleteRecursively(joinedZNode);
			});
		});
		// the flatMap/delete only executes if the getChildren is a Success
		// i.e. no point in deleting a path that doesn't exist or ZK is down
		return children.flatMap(s -> delete(path));
	}

	/**
	 * Attempts to delete the provided path. <br>
	 * Will fail if the node/path does not exist or ZK is down.
	 * 
	 * @param path
	 *            The path
	 * @return The result of the operation
	 * @since 1.1
	 */
	public Try<Unit> delete(String path) {
		return Try(() -> delete(path, -1)); // -1 for ANY version
	}

	/**
	 * Attempts to get all the children of the path. <br>
	 * Will fail if the node/path does not exist or ZK is down.
	 * 
	 * @param path
	 *            The path
	 * @return The result of the operation
	 * @since 1.1
	 */
	public Try<Stream<String>> getChildren(String path) {
		return Try(() -> getChildren(path, null).stream());
	}

	/**
	 * Attempts to get the byte data from the specified path. <br>
	 * Will fail if the node/path does not exist or ZK is down.
	 * 
	 * @param path
	 *            The path
	 * @return The result of the operation
	 * @since 1.1
	 */
	public Try<byte[]> getData(String path) {
		return Try(() -> getData(path, null, null));
	}

	/**
	 * Just invokes {@link ZooKeeper#close()} to shutdown the ZK instance. <br>
	 * Any errors during shutdown are ignored.
	 * 
	 * @since 1.1
	 */
	@Override
	public void close() {
		Try(() -> super.close());
	}
}
