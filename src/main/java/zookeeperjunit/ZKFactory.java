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
import static zookeeperjunit.Util.assertPositive;

/**
 * Factory for creating ZooKeeper instances.
 * 
 * @author Peter Nerg
 * @since 1.0
 */
public class ZKFactory {
	private int port = 0;
	private File rootDir = new File("target");
	private int maxClientConnections = 50;
	/**
	 * Inhibitive constructor.
	 */
	private ZKFactory() {
	}

	/**
	 * Creates an instance of the factory.
	 * 
	 * @return The factory instance
	 * @since 1.0
	 */
	public static ZKFactory apply() {
		return new ZKFactory();
	}

	/**
	 * Sets the port to use for the ZooKeeper instance. <br>
	 * This is optional, if not provided <tt>0</tt> is used and the O/S decided for a free port. <br>
	 * Unless special needs for ports are required it's recommended to not set any specific port and let the O/S decide.
	 * @param port The port ZooKeeper shall listen to
	 * @return The factory instance
	 * @since 1.0
	 */
	public ZKFactory withPort(int port) {
		assertPositive(port);
		this.port = port;
		return this;
	}
	
	/**
	 * Sets the root directory for where the ZooKeeper data is to be stored. <br>
	 * The data is persisted in order to be able to start/stop the ZooKeeper instance and still retain data. <br>
	 * The argument must denote an existing directory. <br>
	 * If not provided the default directory is <i>target</i> i.e. the build directory Maven uses.
	 * @param dir The directory
	 * @return The factory instance
	 * @since 1.1
	 */
	public ZKFactory withRootDir(File dir) {
		if(!dir.isDirectory()) {
			throw new IllegalArgumentException("The path ["+dir.getAbsolutePath()+"] does not denote an existing directory");
		}
		this.rootDir = dir;
		return this;
	}
	
	/**
	 * Sets the maximum amount of connections that can be made towards the ZooKeeper instance. <br>
	 * If not provided the default value of <tt>50</tt> is used.
	 * @param connections The max number of connections.
	 * @return The factory instance
	 * @since 1.1
	 */
	public ZKFactory withMaxClientConnections(int connections) {
		assertPositive(connections);
		return this;
	}
	
	/**
	 * Creates the ZooKeeper instance.
	 * @return The placeholder for the ZooKeeper instance.
	 * @since 1.0
	 */
	public ZKInstance create() {
		return new ZKInstanceImpl(port, rootDir, maxClientConnections);
	}
}
