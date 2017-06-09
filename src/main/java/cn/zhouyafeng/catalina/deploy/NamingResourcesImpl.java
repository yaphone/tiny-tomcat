package cn.zhouyafeng.catalina.deploy;

import java.io.Serializable;

import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.util.LifecycleMBeanBase;
import cn.zhouyafeng.tomcat.util.descriptor.web.ContextEnvironment;
import cn.zhouyafeng.tomcat.util.descriptor.web.ContextResource;
import cn.zhouyafeng.tomcat.util.descriptor.web.ContextResourceLink;
import cn.zhouyafeng.tomcat.util.descriptor.web.NamingResources;

public class NamingResourcesImpl extends LifecycleMBeanBase implements Serializable, NamingResources {

	@Override
	public void init() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEnvironment(ContextEnvironment ce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEnvironment(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addResource(ContextResource cr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeResource(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addResourceLink(ContextResourceLink crl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeResourceLink(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getContainer() {
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