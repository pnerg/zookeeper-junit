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

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Utility for finding a free port. <br>
 * Separated into own class for testing purposes.
 * @author Peter Nerg
 * @since 1.0
 */
class FreePortUtil {

	/**
	 * Attempts to find a free port to use. <br>
	 * This is done by starting a {@link ServerSocket} on port <tt>0</tt> thus letting the O/S decide on which port to use. <br>
	 * We then return the local port from the server socket and then close the server socket without using it.
	 * 
	 * @return The found free port number
	 * @throws IOException
	 * @since 1.0
	 */
	static int getFreePort() throws IOException {
		try (ServerSocket server = new ServerSocket(0)) {
			return server.getLocalPort();
		}
	}

}
