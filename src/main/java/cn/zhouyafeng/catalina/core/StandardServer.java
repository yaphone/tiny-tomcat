package cn.zhouyafeng.catalina.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.AccessControlException;
import java.util.Random;

import javax.naming.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.Server;
import cn.zhouyafeng.catalina.Service;
import cn.zhouyafeng.catalina.deploy.NamingResourcesImpl;
import cn.zhouyafeng.catalina.startup.Catalina;
import cn.zhouyafeng.catalina.util.LifecycleMBeanBase;

public class StandardServer extends LifecycleMBeanBase implements Server {

	private static Logger log = LoggerFactory.getLogger(StandardServer.class);

	private int port = 8005;
	private volatile Thread awaitThread = null;
	private volatile boolean stopAwait = false;
	private volatile ServerSocket awaitSocket = null;
	private String address = "localhost";
	private String shutdown = "SHUTDOWN";
	private Random random = null;

	@Override
	public void init() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public NamingResourcesImpl getGlobalNamingResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlobalNamingResources(NamingResourcesImpl globalNamingResources) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getGlobalNamingContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPort(int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getShutdown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShutdown(String shutdown) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassLoader getParentClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentClassLoader(ClassLoader parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Catalina getCatalina() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCatalina(Catalina catalina) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getCatalinaBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCatalinaBase(File catalinaBase) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getCatalinaHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCatalinaHome(File catalinaHome) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void await() {
		log.info("StandardServer await");
		if (port == -2) {
			return;
		}
		if (port == -1) {
			try {
				awaitThread = Thread.currentThread();
				while (!stopAwait) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException ex) {

					}
				}
			} finally {
				awaitThread = null;
			}
			return;
		}

		try {
			awaitSocket = new ServerSocket(port, 1, InetAddress.getByName(address));
		} catch (Exception e) {
			log.error("StandardServer.await: create[" + address + ":" + port + "]: ", e);
			return;
		}

		try {
			awaitThread = Thread.currentThread();
			while (!stopAwait) {
				ServerSocket serverSocket = awaitSocket;
				if (serverSocket == null) {
					break;
				}

				Socket socket = null;

				StringBuilder command = new StringBuilder();
				try {
					InputStream stream;
					long acceptStartTime = System.currentTimeMillis();
					try {
						socket = serverSocket.accept();
						socket.setSoTimeout(10 * 1000);
						stream = socket.getInputStream();
					} catch (SocketTimeoutException ste) {
						log.warn("standardServer.accept.timeout",
								Long.valueOf(System.currentTimeMillis() - acceptStartTime), ste);
						continue;
					} catch (AccessControlException ace) {
						log.warn("StandardServer.accept security exception: " + ace.getMessage(), ace);
						continue;
					} catch (IOException e) {
						if (stopAwait) {
							// Wait was aborted with socket.close()
							break;
						}
						log.error("StandardServer.await: accept: ", e);
						break;
					}

					int expected = 1024;// Cut off to avoid DoS attack
					while (expected < shutdown.length()) {
						if (random == null)
							random = new Random();
						expected += (random.nextInt() % 1024);
					}
					while (expected > 0) {
						int ch = -1;
						try {
							ch = stream.read();
						} catch (IOException e) {
							log.warn("StandardServer.await: read: ", e);
							ch = -1;
						}
						// Control character or EOF (-1) terminates loop
						if (ch < 32 || ch == 127) {
							break;
						}
						command.append((char) ch);
						expected--;
					}
				} finally {
					// Close the socket now that we are done with it
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (IOException e) {
						// Ignore
					}
				}
				boolean match = command.toString().equals(shutdown);
				if (match) {
					log.info("standardServer.shutdownViaPort");
					break;
				} else
					log.warn("StandardServer.await: Invalid command '" + command.toString() + "' received");
			}
		} finally {
			ServerSocket serverSocket = awaitSocket;
			awaitThread = null;
			awaitSocket = null;

			// Close the server socket and return
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	@Override
	public Service findService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Service[] findServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeService(Service service) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getNamingToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDomainInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getObjectNameKeyProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void startInternal() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void stopInternal() throws LifecycleException {
		// TODO Auto-generated method stub

	}

}
