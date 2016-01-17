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

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javascalautils.Option;
import junitextensions.OptionAssert;
import zookeeperjunit.ZKFactory;
import zookeeperjunit.ZKInstance;
import zookeeperjunit.ZooKeeperAssert;

import static javascalautils.OptionCompanion.Option;

/**
 * Illustrates how to use the test framework to test an application using ZooKeeper.
 * 
 * @author Peter Nerg
 */
public class TestPropertyDatabase implements ZooKeeperAssert, OptionAssert {

	private static final Duration duration = Duration.ofSeconds(5);
	private final ZKInstance zkInstance = ZKFactory.apply().create();
	private PropertyDatabase database;

	/* (non-Javadoc)
	 * @see zookeeperjunit.ZooKeeperAssert#instance()
	 */
	@Override
	public ZKInstance instance() {
		return zkInstance;
	}

	/**
	 * Starts a ZooKeeper server for each test case.
	 * @throws TimeoutException 
	 * @throws Throwable 
	 */
	@Before
	public void start() throws TimeoutException, Throwable {
		// starting server is an asynchronous non-blocking operation
		// for the purpose of this example we stop and block here
		zkInstance.start().result(duration);
		
		//forcing a get on the Option as we should be connected at this stage
		database = new PropertyDatabase(zkInstance.connectString().get());
		database.connect();
	}
	
	/**
	 * Stops the ZooKeeper instance and also deletes any data files. <br>
	 * This makes sure no state is kept between test cases.
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	@After
	public void stop() throws TimeoutException, InterruptedException {
		// stopping server is an asynchronous non-blocking operation
		// for the purpose of this example we stop and block here
		zkInstance.destroy().ready(duration);
		
		//don't like null checks...:)
		Option(database).forEach(d -> d.disconnect());
	}

	@Test
	public void setProperty() {
		database.setProperty("port", "6969");
		assertExists("/properties/port");
	}

	@Test
	public void getProperty() {
		setProperty();

		Option<String> property = database.getProperty("port");
		assertSome("6969", property);
	}
}
