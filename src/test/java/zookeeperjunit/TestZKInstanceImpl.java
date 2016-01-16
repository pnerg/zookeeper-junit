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

import static zookeeperjunit.CloseableZooKeeper.blockingConnect;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Test;

import javascalautils.Option;
import javascalautils.Try;
import javascalautils.Unit;
import javascalautils.concurrent.Future;
import junitextensions.FutureAssert;
import junitextensions.OptionAssert;

/**
 * Test the class {@link ZKInstanceImpl}
 * 
 * @author Peter Nerg
 */
public class TestZKInstanceImpl extends BaseAssert implements OptionAssert, FutureAssert {
	private static final long Timeout = 5000;
	private static final Duration duration = Duration.ofMillis(Timeout);
	private final ZKInstanceImpl instance = new ZKInstanceImpl(0, new File("target/"));

	@After
	public void after() throws TimeoutException, Throwable {
		value(instance.destroy());
	}

	@Test(timeout = Timeout)
	public void start() throws TimeoutException, Throwable {
		Future<Unit> start = instance.start();
		assertSuccess(start, duration);
	}

	@Test(timeout = Timeout)
	public void connectString() throws TimeoutException, Throwable {
		start();
		assertSome(instance.connectString());
	}

	@Test(timeout = Timeout)
	public void port() throws TimeoutException, Throwable {
		start();
		assertSome(instance.port());
	}

	/**
	 * Test the scenario: <br>
	 * - Start ZK <br>
	 * - Connect to ZK <br>
	 * - Create data in ZK <br>
	 * - Stop ZK <br>
	 * - Start ZK (again) <br>
	 * - Verify connection is valid <br>
	 * - Verify data is still there <br>
	 * 
	 * @throws TimeoutException
	 * @throws Throwable
	 */
	@Test(timeout = Timeout)
	public void restart() throws TimeoutException, Throwable {
		start();
		final Integer port = value(instance.port());
		final String data = "Peter was here!";
		final String path = "/tmp/restart-" + System.currentTimeMillis();

		// connect and create data in ZK
		try (CloseableZooKeeper zookeeper = blockingConnect(instance.connectString().get(),duration)) {
			ZKConnectionUtil.createRecursive(zookeeper, path, data.getBytes());
		}
		
		//stop the instance
		instance.stop().result(duration);
		assertNone(instance.port());

		//restart the instance again
		start();

		assertEquals(port, value(instance.port()));

		try (CloseableZooKeeper zookeeper = blockingConnect(instance.connectString().get(),duration)) {
			assertEquals(data, new String(zookeeper.getData(path).get()));
		}
	}

	@Test
	public void connect_success() throws TimeoutException, Throwable {
		start();
		Try<CloseableZooKeeper> connect = instance.connect();
		connect.forEach(CloseableZooKeeper::close);
		assertSuccess(connect);
	}

	@Test
	public void connect_failure() throws TimeoutException, Throwable {
		Try<CloseableZooKeeper> connect = instance.connect(); // no server is running, this should fail
		connect.forEach(CloseableZooKeeper::close); //close just in case
		assertFailure(connect);
	}
	
	private <T> T value(Option<T> option) {
		assertSome(option);
		return option.get();
	}

	private <T> T value(Future<T> future) throws Throwable {
		assertSuccess(future, duration);
		return future.value().get().get();
	}

}
