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
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Test;

import javascalautils.Unit;
import javascalautils.concurrent.Future;

/**
 * @author Peter Nerg
 *
 */
public class TestZKInstanceImpl extends BaseAssert {
	private static final Duration duration = Duration.ofSeconds(5);
	private final ZKInstanceImpl instance = new ZKInstanceImpl(0, new File("target/"));
	
	@After
	public void after() throws TimeoutException, Throwable {
		instance.stop().result(duration);
	}
	
	@Test(timeout=5000)
	public void start() throws TimeoutException, Throwable {
		Future<Unit> start = instance.start();
		start.result(duration);
	}
}
