package cn.zhouyafeng.catalina;

public interface Service extends Lifecycle {
	public Server getServer();

	public void setServer(Server server);

	public Container getContainer();

	public void setContainer(Container container);

	public void setContainer(Engine engine);

}
