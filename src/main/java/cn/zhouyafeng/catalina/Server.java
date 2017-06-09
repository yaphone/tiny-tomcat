package cn.zhouyafeng.catalina;

import org.apache.catalina.deploy.NamingResourcesImpl;

public interface Server extends Lifecycle{
    public NamingResourcesImpl getGlobalNamingResources();

}
