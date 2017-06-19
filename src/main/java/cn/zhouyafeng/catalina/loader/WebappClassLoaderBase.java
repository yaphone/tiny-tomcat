package cn.zhouyafeng.catalina.loader;

import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.Lifecycle;
import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.LifecycleListener;
import cn.zhouyafeng.catalina.LifecycleState;

public class WebappClassLoaderBase extends URLClassLoader implements Lifecycle {

	private static Logger log = LoggerFactory.getLogger(WebappClassLoaderBase.class);

	private volatile LifecycleState state = LifecycleState.NEW;

	protected final ClassLoader parent;

	private ClassLoader javaseClassLoader;

	protected WebappClassLoaderBase() {
		super(new URL[0]);

		ClassLoader p = getParent();
		if (p == null) {
			p = getSystemClassLoader();
		}
		this.parent = p;

		ClassLoader j = String.class.getClassLoader();
		if (j == null) {
			j = getSystemClassLoader();
			while (j.getParent() != null) {
				j = j.getParent();
			}
		}
		this.javaseClassLoader = j;
	}

	protected WebappClassLoaderBase(ClassLoader parent) {
		super(new URL[0], parent);
		ClassLoader p = getParent();
		if (p == null) {
			p = getSystemClassLoader();
		}
		this.parent = p;

		ClassLoader j = String.class.getClassLoader();
		if (j == null) {
			j = getSystemClassLoader();
			while (j.getParent() != null) {
				j = j.getParent();
			}
		}
		this.javaseClassLoader = j;

	}

	@Override
	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		synchronized (getClassLoadingLock(name)) {
			if (log.isDebugEnabled()) {
				log.debug("loadClass(" + name + ", " + resolve + ")");
			}
			Class<?> clazz = null;

			checkStateForClassLoading(name);

			clazz = findLoadedClass0(name);
			if (clazz != null) {
				if (log.isDebugEnabled()) {
					log.debug("  Returning class from cache");
				}
				if (resolve) {
					resolveClass(clazz);
				}
				return clazz;
			}
		}
	}

	protected void checkStateForClassLoading(String className) throws ClassNotFoundException {
		// It is not permitted to load new classes once the web application has
		// been stopped.
		try {
			checkStateForResourceLoading(className);
		} catch (IllegalStateException ise) {
			throw new ClassNotFoundException(ise.getMessage(), ise);
		}
	}

	protected void checkStateForResourceLoading(String resource) throws IllegalStateException {
		// It is not permitted to load resources once the web application has
		// been stopped.
		if (!state.isAvailable()) {
			String msg = "webappClassLoader.stopped" + resource;
			IllegalStateException ise = new IllegalStateException(msg);
			log.info(msg, ise);
			throw ise;
		}
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleListener[] findLifecycleListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws LifecycleException {
		// TODO Auto-generated method stub

	}

	@Override
	public LifecycleState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateName() {
		// TODO Auto-generated method stub
		return null;
	}

}
