package cn.zhouyafeng.catalina.startup;

import java.io.File;

import org.apache.commons.digester.Digester;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.Server;
import cn.zhouyafeng.catalina.core.StandardServer;
import cn.zhouyafeng.catalina.core.StandardService;
import cn.zhouyafeng.catalina.deploy.NamingResourcesImpl;

public class Catalina {

	private static Logger log = LoggerFactory.getLogger(Catalina.class);

	protected Server server = null;
	protected boolean await = false;
	protected boolean useShutdownHook = true;

	protected String configFile = "conf/server.xml";

	public Catalina() {

	}

	public void load() {

		long t1 = System.nanoTime();

		// this.server = new StandardServer();
		createStartDigester();
		getServer().setCatalina(this);
		getServer().setCatalinaHome(Bootstrap.getCatalinaHomeFile());
		getServer().setCatalinaBase(Bootstrap.getCatalinaBaseFile());

		initStreams();

		try {
			getServer().init();
		} catch (LifecycleException e) {
			// TODO
			log.error("Catalina load() error");
		}

		long t2 = System.nanoTime();

		if (log.isInfoEnabled()) {
			log.info("Initialization processed in " + ((t2 - t1) / 1000000) + " ms");
		}

	}

	public void load(String[] args) {

	}

	public void start() {
		if (getServer() == null) {
			load();
		}

		if (getServer() == null) {
			log.error("Cannot start server. Server instance is not configured.");
			return;
		}

		long t1 = System.nanoTime();
		try {
			getServer().start();
		} catch (LifecycleException e) {
			log.error("catalina.serverStartFail", e);
			try {
				getServer().destroy();
			} catch (LifecycleException e1) {
				log.debug("destroy() failed for failed Server ", e1);
			}
			return;

		}

		long t2 = System.nanoTime();
		if (log.isInfoEnabled()) {
			log.info("Server startup in " + ((t2 - t1) / 1000000) + " ms");
		}

		if (useShutdownHook) { // TODO

		}

		if (await) {
			await();
			stop();
		}

	}

	public void stop() {

	}

	public void setAwait(boolean await) {
		this.await = await;
	}

	public void stopServer(String[] args) {

	}

	public Server getServer() {
		return server;
	}

	protected void initDirs() {
		String temp = System.getProperty("java.io.tmpdir");
		if (temp == null || (!(new File(temp)).isDirectory())) {
			log.info("embedded.notmp", temp);
		}
	}

	protected void initNaming() {

	}

	protected Digester createStartDigester() {
		long t1 = System.currentTimeMillis();
		this.server = new StandardServer();
		server.setGlobalNamingResources(new NamingResourcesImpl());
		// TODO server.addListener(new LifecycleListener());
		server.addService(new StandardService());
		return null;
	}

	protected File configFile() {

		File file = new File(configFile);
		if (!file.isAbsolute()) {
			file = new File(Bootstrap.getCatalinaBase(), configFile);
		}
		return (file);

	}

	public String getConfigFile() {
		return configFile;
	}

	protected void initStreams() {
		// Replace System.out and System.err with a custom PrintStream
		System.setOut(new SystemLogHandler(System.out));
		System.setErr(new SystemLogHandler(System.err));
	}

	public void await() {
		log.info("Catalina await");
		getServer().await();

	}

}
