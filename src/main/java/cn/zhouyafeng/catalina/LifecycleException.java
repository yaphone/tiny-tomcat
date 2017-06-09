package cn.zhouyafeng.catalina;

public class LifecycleException extends Exception {

    private static final long serialVersionUID = 1L;

    public LifecycleException() {
        super();
    }

    public LifecycleException(String msg) {
        super(msg);
    }

    public LifecycleException(Throwable t) {
        super(t);
    }

    public LifecycleException(String msg, Throwable t) {
        super(msg, t);
    }

}
