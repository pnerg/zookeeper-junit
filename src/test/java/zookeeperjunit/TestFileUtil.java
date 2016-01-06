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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.junit.After;
import org.junit.Test;

/**
 * Test the class {@link FileUtil}
 * @author Peter Nerg
 */
public class TestFileUtil extends BaseAssert {

	private final File rootDir = new File("target/TestFileUtil-"+System.currentTimeMillis());
	
	@After
	public void after() {
		assertTrue(FileUtil.delete(rootDir));
	}
	
	@Test
	public void mkdir() throws IOException {
		File newDir = FileUtil.mkdir(rootDir, "mkdir");
		assertTrue(newDir.isDirectory());
	}
	
	@Test(expected = FileAlreadyExistsException.class)
	public void mkdir_alreadyExists() throws IOException {
		mkdir();
		FileUtil.mkdir(rootDir, "mkdir"); //same dir again
	}

	@Test(expected = IOException.class)
	public void mkdir_illegalPathExists() throws IOException {
		FileUtil.mkdir(new File("/illegal-path"), "mkdir");
	}

}
