package cn.zhouyafeng.catalina.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.Server;
import cn.zhouyafeng.catalina.core.StandardServer;

public class Catalina {

	private static Logger log = LoggerFactory.getLogger(Catalina.class);

	protected Server server = null;
	protected boolean await = false;
	protected boolean useShutdownHook = true;

	protected String configFile = "conf/server.xml";

	public Catalina() {

	}

	public void load() {
		this.server = new StandardServer();

	}

	public void load(String[] args) {
		long t1 = System.nanoTime();

		initDirs();

		initNaming();

		Digester digester = createStartDigester();

		InputSource inputSource = null;
		InputStream inputStream = null;
		File file = null;

		try {
			try {
				file = configFile();
				inputStream = new FileInputStream(file);
				inputSource = new InputSource(file.toURI().toURL().toString());
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("catalina.configFail", e);
				}
			}
			if (inputStream == null) {
				try {
					inputStream = getClass().getClassLoader().getResourceAsStream(getConfigFile());
					inputSource = new InputSource(getClass().getClassLoader().getResource(getConfigFile()).toString());
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("catalina.configFail", e);
					}
				}
			}

			if (inputStream == null) {
				try {
					inputStream = getClass().getClassLoader().getResourceAsStream("server-embed.xml");
					inputSource = new InputSource(
							getClass().getClassLoader().getResource("server-embed.xml").toString());
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("catalina.configFail", "server-embed.xml", e);
					}
				}
			}

			if (inputStream == null) {
				try {
					inputStream = getClass().getClassLoader().getResourceAsStream("server-embed.xml");
					inputSource = new InputSource(
							getClass().getClassLoader().getResource("server-embed.xml").toString());
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("catalina.configFail", "server-embed.xml", e);
					}
				}
			}

			if (inputStream == null || inputSource == null) {
				if (file == null) {
					log.warn("catalina.configFail", getConfigFile() + "] or [server-embed.xml]");
				} else {
					log.warn("catalina.configFail", file.getAbsolutePath());
					if (file.exists() && !file.canRead()) {
						log.warn("Permissions incorrect, read permission is not allowed on the file.");
					}
				}
				return;
			}

			try {
				inputSource.setByteStream(inputStream);
				digester.push(this);
				digester.parse(inputSource);
			} catch (SAXParseException spe) {
				log.warn("Catalina.start using " + getConfigFile() + ": " + spe.getMessage());
				return;
			} catch (Exception e) {
				log.warn("Catalina.start using " + getConfigFile() + ": ", e);
				return;
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}

		getServer().setCatalina(this);
		getServer().setCatalinaHome(Bootstrap.getCatalinaHomeFile());
		getServer().setCatalinaBase(Bootstrap.getCatalinaBaseFile());

		// Stream redirection
		initStreams();

		// Start the new server
		try {
			getServer().init();
		} catch (LifecycleException e) {
			if (Boolean.getBoolean("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE")) {
				throw new java.lang.Error(e);
			} else {
				log.error("Catalina.start", e);
			}
		}

		long t2 = System.nanoTime();
		if (log.isInfoEnabled()) {
			log.info("Initialization processed in " + ((t2 - t1) / 1000000) + " ms");
		}
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
		// Initialize the digester
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
