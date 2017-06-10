package cn.zhouyafeng.catalina.core;

import java.io.File;

import javax.naming.Context;

import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.Server;
import cn.zhouyafeng.catalina.Service;
import cn.zhouyafeng.catalina.deploy.NamingResourcesImpl;
import cn.zhouyafeng.catalina.startup.Catalina;
import cn.zhouyafeng.catalina.util.LifecycleMBeanBase;

public class StandardServer extends LifecycleMBeanBase implements Server {

	private int port = 8005;
	private volatile Thread awaitThread = null;
	private volatile boolean stopAwait = false;

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
