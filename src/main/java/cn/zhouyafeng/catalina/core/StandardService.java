package cn.zhouyafeng.catalina.core;

import java.beans.PropertyChangeSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.Container;
import cn.zhouyafeng.catalina.Engine;
import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.Server;
import cn.zhouyafeng.catalina.Service;
import cn.zhouyafeng.catalina.util.LifecycleMBeanBase;

public class StandardService extends LifecycleMBeanBase implements Service {
	private static Logger log = LoggerFactory.getLogger(StandardService.class);

	private String name = null;

	private Server server = null;

	protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

	protected Container container = null;

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

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServer(Server server) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void setContainer(Container container) {
		setContainer((Engine) container);
	}

	@Override
	public void setContainer(Engine engine) {
		Container oldContainer = this.container;
		if ((oldContainer != null) && (oldContainer instanceof Engine)) {
			((Engine) oldContainer).setService(null);
		}
		this.container = engine;
		if ((this.container != null) && (this.container instanceof Engine)) {
			((Engine) this.container).setService(this);
		}
		if (getState().isAvailable() && (this.container != null)) {
			try {
				this.container.start();
			} catch (LifecycleException e) {
				// TODO: handle exception
			}
		}
		support.firePropertyChange("container", oldContainer, this.container);

	}

}
