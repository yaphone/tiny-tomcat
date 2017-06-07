package cn.zhouyafeng.catalina.startup;

import org.apache.log4j.Logger;

/**
 * Bootstrap类，tomcat的启动入口
 * 
 * @author zhouyafeng
 * @date 2017年6月7日
 */
public class Bootstrap {
    private static final Logger LOG = Logger.getLogger(Bootstrap.class);

    private static Bootstrap daemon = null;
    private Object catalinaDaemon = null;

    protected ClassLoader commonLoader = null;
    protected ClassLoader catalinaLoader = null;
    protected ClassLoader sharedLoader = null;

    private void initClassLoader() {

    }

    /**
     * 初始化daemon
     */
    public void init() throws Exception {
        //TODO
    }

    public static void main(String[] args) {
        if(daemon == null){
            Bootstrap bootstrap = new Bootstrap();
            try{
                bootstrap.init();
            }catch (Throwable t) {
                handleThrowable(t);
                t.printStackTrace();
                return;
            }
        }
        LOG.info("Hello World");
    }
    
    private static void handleThrowable(Throwable t){
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        }
    }
}
