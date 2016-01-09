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

import java.io.Closeable;
import java.io.IOException;
import java.util.stream.Stream;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javascalautils.Try;
import javascalautils.Unit;

/**
 * Extends the standard {@link ZooKeeper} connection class with the {@link Closeable} interface. <br>
 * This is to be able to use it in try-with-resources clauses.
 * 
 * @author Peter Nerg
 * @since 1.0
 */
final class CloseableZooKeeper extends ZooKeeper implements Closeable {
	CloseableZooKeeper(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
		super(connectString, sessionTimeout, watcher);
	}

	/**
	 * Delete the specified node and all of it's children.
	 * @param path
	 *            The path
	 */
	Try<Unit> deleteRecursively(String path) {
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
	 * @return The result
	 */
	private Try<Unit> delete(String path) {
		return Try(() -> {
			delete(path, -1); // -1 for ANY version
			return Unit.Instance;
		});
	}

	/**
	 * Attempts to get all the children of the path. <br>
	 * Will fail if the node/path does not exist or ZK is down.
	 * 
	 * @param path
	 *            The path
	 * @return The result
	 */
	private Try<Stream<String>> getChildren(String path) {
		return Try(() -> getChildren(path, null).stream());
	}

	/**
	 * Just invokes {@link ZooKeeper#close()} to shutdown the ZK instance.
	 */
	@Override
	public void close() {
		Try(() -> {
			super.close();
			return Unit.Instance;
		});
	}
}
