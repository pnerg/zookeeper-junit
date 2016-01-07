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

import javascalautils.Option;
import javascalautils.Unit;
import javascalautils.concurrent.Future;

/**
 * @author Peter Nerg
 * @since 1.0
 */
public interface ZKInstance {

	/**
	 * Starts the instance. <br>
	 * This is a non-blocking operation and returns a Future that will be completed once the instance is started.
	 * @return The future that will be completed once the instance is started.
	 * @since 1.0
	 */
	Future<Unit> start();

	/**
	 * Stops this instance.
	 * This is a non-blocking operation and returns a Future that will be completed once the instance is stopped. <br>
	 * Data on disc is not destroyed meaning that the instances can be {@link #start() started} again.
	 * @return The future that will be completed once the instance is stopped.
	 * @since 1.0
	 */
	Future<Unit> stop();

	/**
	 * Stops and destroys this instance.
	 * This is a non-blocking operation and returns a Future that will be completed once the instance is stopped. <br>
	 * Data on disc is destroyed meaning that invoking {@link #start() start} again will yield an empty database.
	 * @return The future that will be completed once the instance is stopped.
	 * @since 1.0
	 */
	Future<Unit> destroy();
	
	/**
	 * Get the connect string [host:port] to the started instance. 
	 * @return Some containing the string if started, None if not started
	 * @since 1.0
	 */
	Option<String> connectString();

	/**
	 * Get the port to the started instance. 
	 * @return Some containing the port if started, None if not started
	 * @since 1.0
	 */
	Option<Integer> port();
	
}
