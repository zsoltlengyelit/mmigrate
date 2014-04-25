package com.lengyelzsolt.mmigrate;

/**
 * 
 * @author lzsolt
 * 
 */
public class Main {

	public static void main(String[] args) {

		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);

		new Main().run();
	}

	private void run() {
		MMigration migration = new MMigration();

		migration.scanDir("C:/work/workspace/mmigrate/mmigrate/src/main/resources/mmigrations");
		migration.write("C:/work/workspace/mmigrate/mmigrate/src/main/resources/out");
	}

}
