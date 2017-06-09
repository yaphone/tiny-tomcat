package cn.zhouyafeng.catalina.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.Server;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

	private Catalina catalina = null;

	public void init() {
		catalina = new Catalina();
	}

	public static void main(String[] args) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.init();

		String command = "start";
		if (args.length > 0) {
			command = args[args.length - 1];
		}

		if (command.equals("startd")) {
			args[args.length - 1] = "start";
			bootstrap.load(args);
			bootstrap.start();
		} else if (command.equals("stopd")) {
			args[args.length - 1] = "stop";
			bootstrap.stop();
		} else if (command.equals("start")) {
			bootstrap.setAwait(true);
			bootstrap.load(args);
			bootstrap.start();
		} else if (command.equals("stop")) {
			bootstrap.stopServer(args);
		} else if (command.equals("configtest")) {
			bootstrap.load(args);
			if (null == bootstrap.getServer()) {
				System.exit(1);
			}
			System.exit(0);
		} else { // TODO
		}

	}

	private void load(String[] args) {
		catalina.load(args);
	}

	public void start() {
		if (catalina == null) {
			init();
		}
		catalina.start();
	}

	public void stop() {
		catalina.stop();
	}

	public void setAwait(boolean await) {
		catalina.setAwait(await);
	}

	public void stopServer(String[] args) {
		catalina.stopServer(args);
	}

	public Server getServer() {
		return catalina.getServer();
	}

}
