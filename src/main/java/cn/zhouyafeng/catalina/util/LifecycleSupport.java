package cn.zhouyafeng.catalina.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.zhouyafeng.catalina.Lifecycle;
import cn.zhouyafeng.catalina.LifecycleEvent;
import cn.zhouyafeng.catalina.LifecycleListener;

public class LifecycleSupport {

    private Lifecycle lifecycle;
    private final List<LifecycleListener> listeners = new CopyOnWriteArrayList<>();

    public LifecycleSupport(Lifecycle lifecycle) {
        super();
        this.lifecycle = lifecycle;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners.toArray(new LifecycleListener[0]);
    }

    public void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);
        for (LifecycleListener listener : listeners) {
            listener.lifecycleEvent(event);
        }
    }

    public void removeLifecycleEvent(LifecycleListener listener) {
        listeners.remove(listener);
    }

}
