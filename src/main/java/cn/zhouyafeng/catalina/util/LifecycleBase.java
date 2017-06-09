package cn.zhouyafeng.catalina.util;

import org.apache.log4j.Logger;

import cn.zhouyafeng.catalina.Lifecycle;
import cn.zhouyafeng.catalina.LifecycleException;
import cn.zhouyafeng.catalina.LifecycleListener;
import cn.zhouyafeng.catalina.LifecycleState;

public class LifecycleBase implements Lifecycle{
    private static final Logger LOG = Logger.getLogger(LifecycleBase.class);
    private final LifecycleSupport lifecycle = new LifecycleSupport(this);
    private volatile LifecycleState state = LifecycleState.NEW;

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        
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
