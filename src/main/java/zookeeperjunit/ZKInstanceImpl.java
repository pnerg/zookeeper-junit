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

import static javascalautils.OptionCompanion.None;
import static javascalautils.TryCompanion.Try;
import static javascalautils.OptionCompanion.Some;
import static javascalautils.concurrent.FutureCompanion.Future;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

import javascalautils.Option;
import javascalautils.Unit;
import javascalautils.concurrent.Future;

/**
 * @author Peter Nerg
 */
final class ZKInstanceImpl implements ZKInstance {

	private final int cfgPort;
	private final File rootDir;

	private final AtomicBoolean started = new AtomicBoolean(false);

	/** This is the root dir where all ZK data is stored for this ZK instance. */
	private File rootZooDir;

	private Option<FileTxnSnapLog> fileTxnSnapLog = None();
	private Option<ServerCnxnFactory> serverCnxnFactory = None();

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
		return Future(() -> {
			// create a unique path using the port number for identification
			rootZooDir = new File(rootDir, "zk-" + System.currentTimeMillis() + File.separator);
			FileUtil.delete(rootZooDir); // clear out any old data

			ZooKeeperServer zkServer = new ZooKeeperServer();
			FileTxnSnapLog log = new FileTxnSnapLog(new File(rootZooDir, "dataDir"), new File(rootZooDir, "dataLogDir"));
			zkServer.setTxnLogFactory(log);
			zkServer.setTickTime(2000);
			zkServer.setMinSessionTimeout(10000);
			zkServer.setMaxSessionTimeout(10000);
			ServerCnxnFactory cnxnFactory = ServerCnxnFactory.createFactory();
			cnxnFactory.configure(new InetSocketAddress(cfgPort), 50);
			cnxnFactory.startup(zkServer);
			fileTxnSnapLog = Some(log);
			serverCnxnFactory = Some(cnxnFactory);

			return Unit.Instance;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zookeeperjunit.ZKInstance#stop()
	 */
	@Override
	public Future<Unit> stop() {
		return Future(() -> {
			serverCnxnFactory.forEach(s -> s.shutdown());
			fileTxnSnapLog.forEach(f -> Try(() -> {
				f.close();
				return Unit.Instance;
			}));
			return Unit.Instance;
		});
	}
	
	/* (non-Javadoc)
	 * @see zookeeperjunit.ZKInstance#connectString()
	 */
	@Override
	public Option<String> connectString() {		
		return port().map(port -> "127.0.0.1:"+port);
	}
	
	/* (non-Javadoc)
	 * @see zookeeperjunit.ZKInstance#port()
	 */
	@Override
	public Option<Integer> port() {
		return serverCnxnFactory.map(sf -> sf.getLocalPort());
	}
}
