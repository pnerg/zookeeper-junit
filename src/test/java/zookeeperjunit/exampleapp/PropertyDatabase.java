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
package zookeeperjunit.exampleapp;

import static javascalautils.OptionCompanion.None;
import static javascalautils.TryCompanion.Try;
import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import javascalautils.Option;

/**
 * This is a simple example on a property database in ZooKeeper. <br>
 * The main purpose is to provide an example on how to use this test framework. <br>
 * The data is stored in a root path called <tt>properties</tt> the properties are set as name/value pairs. <br>
 * An example of the data model <br>
 * 
 * <pre>
 * /properties
 *    /host[localhost]
 *    /port[6969]
 * </pre>
 * 
 * @author Peter Nerg
 */
public final class PropertyDatabase {

	private final String connectString;

	private Option<ZooKeeper> zooKeeper = None();

	public PropertyDatabase(String connectString) {
		this.connectString = connectString;
	}

	public void connect() {
		zooKeeper = Try(() -> {
			CountDownLatch latch = new CountDownLatch(1);
			ZooKeeper zk = new ZooKeeper(connectString, 10000, event -> {
				if (event.getState() == KeeperState.SyncConnected) {
					latch.countDown();
				}
			});
			latch.await(); // this blocks indefinitely
			return zk;
		}).asOption();
		create("/properties", "");
	}

	public void disconnect() {
		zooKeeper.forEach(zk -> {
			Try(() -> zk.close());
		});
		zooKeeper = None();
	}
	
	public void setProperty(String name, String value) {
		create(getPath(name), value);
	}
	
	public Option<String> getProperty(String name) {
		return zooKeeper.flatMap(zk -> {
			return Try(() -> zk.getData(getPath(name), null, null)).map(bytes -> new String(bytes)).asOption();
		});
	}
	
	private void create(String path, String data) {
		zooKeeper.forEach(zk -> Try(() -> zk.create(path, data.getBytes(), OPEN_ACL_UNSAFE, PERSISTENT)));
	}
	
	private static String getPath(String propertyName) {
		return "/properties/"+propertyName;
	}
	
}
