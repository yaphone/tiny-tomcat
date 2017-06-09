package cn.zhouyafeng.catalina;

import java.util.EventObject;

public class LifecycleEvent extends EventObject{
    private static final long serialVersionUID = 1L;
    private final String type;
    private final Object data;

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
    
    

}
