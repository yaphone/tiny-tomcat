package cn.zhouyafeng.catalina.startup;

public class Bootstrap {


    public void init(){
        Catalina startupInstance = new Catalina();
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
    }

}
