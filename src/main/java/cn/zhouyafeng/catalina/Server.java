package cn.zhouyafeng.catalina;

import java.io.File;

import cn.zhouyafeng.catalina.deploy.NamingResourcesImpl;
import cn.zhouyafeng.catalina.startup.Catalina;

public interface Server extends Lifecycle {

	/**
	 * @return the global naming resources.
	 */
	public NamingResourcesImpl getGlobalNamingResources();

	/**
	 * Set the global naming resources.
	 *
	 * @param globalNamingResources
	 *            The new global naming resources
	 */
	public void setGlobalNamingResources(NamingResourcesImpl globalNamingResources);

	/**
	 * @return the global naming resources context.
	 */
	public javax.naming.Context getGlobalNamingContext();

	/**
	 * @return the port number we listen to for shutdown commands.
	 */
	public int getPort();

	/**
	 * Set the port number we listen to for shutdown commands.
	 *
	 * @param port
	 *            The new port number
	 */
	public void setPort(int port);

	/**
	 * @return the address on which we listen to for shutdown commands.
	 */
	public String getAddress();

	/**
	 * Set the address on which we listen to for shutdown commands.
	 *
	 * @param address
	 *            The new address
	 */
	public void setAddress(String address);

	/**
	 * @return the shutdown command string we are waiting for.
	 */
	public String getShutdown();

	/**
	 * Set the shutdown command we are waiting for.
	 *
	 * @param shutdown
	 *            The new shutdown command
	 */
	public void setShutdown(String shutdown);

	/**
	 * @return the parent class loader for this component. If not set, return
	 *         {@link #getCatalina()} {@link Catalina#getParentClassLoader()}.
	 *         If catalina has not been set, return the system class loader.
	 */
	public ClassLoader getParentClassLoader();

	/**
	 * Set the parent class loader for this server.
	 *
	 * @param parent
	 *            The new parent class loader
	 */
	public void setParentClassLoader(ClassLoader parent);

	/**
	 * @return the outer Catalina startup/shutdown component if present.
	 */
	public Catalina getCatalina();

	/**
	 * Set the outer Catalina startup/shutdown component if present.
	 *
	 * @param catalina
	 *            the outer Catalina component
	 */
	public void setCatalina(Catalina catalina);

	/**
	 * @return the configured base (instance) directory. Note that home and base
	 *         may be the same (and are by default). If this is not set the
	 *         value returned by {@link #getCatalinaHome()} will be used.
	 */
	public File getCatalinaBase();

	/**
	 * Set the configured base (instance) directory. Note that home and base may
	 * be the same (and are by default).
	 *
	 * @param catalinaBase
	 *            the configured base directory
	 */
	public void setCatalinaBase(File catalinaBase);

	/**
	 * @return the configured home (binary) directory. Note that home and base
	 *         may be the same (and are by default).
	 */
	public File getCatalinaHome();

	/**
	 * Set the configured home (binary) directory. Note that home and base may
	 * be the same (and are by default).
	 *
	 * @param catalinaHome
	 *            the configured home directory
	 */
	public void setCatalinaHome(File catalinaHome);

	// --------------------------------------------------------- Public Methods

	/**
	 * Add a new Service to the set of defined Services.
	 *
	 * @param service
	 *            The Service to be added
	 */
	public void addService(Service service);

	/**
	 * Wait until a proper shutdown command is received, then return.
	 */
	public void await();

	/**
	 * Find the specified Service
	 *
	 * @param name
	 *            Name of the Service to be returned
	 * @return the specified Service, or <code>null</code> if none exists.
	 */
	public Service findService(String name);

	/**
	 * @return the set of Services defined within this Server.
	 */
	public Service[] findServices();

	/**
	 * Remove the specified Service from the set associated from this Server.
	 *
	 * @param service
	 *            The Service to be removed
	 */
	public void removeService(Service service);

	/**
	 * @return the token necessary for operations on the associated JNDI naming
	 *         context.
	 */
	public Object getNamingToken();

}
