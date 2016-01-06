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

import static javascalautils.concurrent.FutureCompanion.Future;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import javascalautils.Unit;
import javascalautils.concurrent.Future;

/**
 * @author Peter Nerg
 */
final class ZKInstanceImpl implements ZKInstance {

	private final int cfgPort;
	private final File rootDir;

	private final AtomicBoolean started = new AtomicBoolean(false);

	/** This is the actual port ZK listens to. */
	private int zookeeperPort = -1;

	/** This is the root dir where all ZK data is stored for this ZK instance. */
	private File rootZooDir;

	ZKInstanceImpl(int cfgPort, File rootDir) {
		this.cfgPort = cfgPort;
		this.rootDir = rootDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zookeeperjunit.ZKInstance#start()
	 */
	@Override
	public Future<Unit> start() {

		// File tmpDir = new File(System.getProperty("zookeepermanager.io.tmpdir", "target"));
		// // create a unique path using the current time and append the port number for identification
		// rootZooDir = mkdir(tmpDir, "zookeeper-" + port + "-" + System.currentTimeMillis() + "/");
		// File dataDir = mkdir(rootZooDir, "dataDir/");
		// File dataLogDir = mkdir(rootZooDir, "dataLogDir/");
		//
		// // create the zookeeper.cfg with the necessary parameters
		// File configFile = new File(rootZooDir, "zookeeper.cfg");
		// Properties properties = new Properties();
		// properties.put("tickTime", "2000");
		// properties.put("dataDir", dataDir.getAbsolutePath());
		// properties.put("dataLogDir", dataLogDir.getAbsolutePath());
		// properties.put("clientPort", String.valueOf(port));
		//
		// try (OutputStream ostream = new FileOutputStream(configFile)) {
		// properties.store(ostream, "");
		// }
		// start(configFile);
		return Future(() -> {
			zookeeperPort = FreePortUtil.getFreePort(cfgPort);

			// create a unique path using the port number for identification
			rootZooDir = FileUtil.createZooKeeperConfig(rootDir, zookeeperPort);
			
			return Unit.Instance;
		});
				
//	    /**
//	     * Starts the instance with the provided configuration file
//	     * 
//	     * @param cfgFile
//	     * @throws IOException
//	     * @throws InterruptedException
//	     */
//	    private void start(File cfgFile) throws IOException, InterruptedException {
//	        // starts ZK using a separate thread as it the call to start ZK locks the running thread
//	        starter = new ZooKeeperStarter(cfgFile.getAbsolutePath());
//	        new Thread(starter, "ZooKeeper-Starter").start();
//
//
//	        started = true;
//	    }		
	}

}
