package cn.zhouyafeng.catalina.startup;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.catalina.Globals;
import cn.zhouyafeng.catalina.Server;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);

	private Catalina catalina = null;

	private static final File catalinaBaseFile;
	private static final File catalinaHomeFile;

	static {
		String userDir = System.getProperty("user.dir");
		String home = System.getProperty(Globals.CATALINA_HOME_PROP);
		File homeFile = null;

		if (home != null) {
			File f = new File(home);
			try {
				homeFile = f.getCanonicalFile();
			} catch (IOException ioe) {
				homeFile = f.getAbsoluteFile();
			}
		}

		if (homeFile == null) {
			// First fall-back. See if current directory is a bin directory
			// in a normal Tomcat install
			File bootstrapJar = new File(userDir, "bootstrap.jar");

			if (bootstrapJar.exists()) {
				File f = new File(userDir, "..");
				try {
					homeFile = f.getCanonicalFile();
				} catch (IOException ioe) {
					homeFile = f.getAbsoluteFile();
				}
			}
		}

		if (homeFile == null) {
			// Second fall-back. Use current directory
			File f = new File(userDir);
			try {
				homeFile = f.getCanonicalFile();
			} catch (IOException ioe) {
				homeFile = f.getAbsoluteFile();
			}
		}

		catalinaHomeFile = homeFile;
		System.setProperty(Globals.CATALINA_HOME_PROP, catalinaHomeFile.getPath());

		// Then base
		String base = System.getProperty(Globals.CATALINA_BASE_PROP);
		if (base == null) {
			catalinaBaseFile = catalinaHomeFile;
		} else {
			File baseFile = new File(base);
			try {
				baseFile = baseFile.getCanonicalFile();
			} catch (IOException ioe) {
				baseFile = baseFile.getAbsoluteFile();
			}
			catalinaBaseFile = baseFile;
		}
		System.setProperty(Globals.CATALINA_BASE_PROP, catalinaBaseFile.getPath());
	}

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

	public static String getCatalinaBase() {
		return catalinaBaseFile.getPath();
	}

	public static File getCatalinaHomeFile() {
		return catalinaHomeFile;
	}

	public static File getCatalinaBaseFile() {
		return catalinaBaseFile;
	}

}
