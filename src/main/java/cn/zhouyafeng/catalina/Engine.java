package cn.zhouyafeng.catalina;

public interface Engine extends Container {
	public Service getService();

	public void setService(Service service);

}
