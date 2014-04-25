package com.lengyelzsolt.mmigrate;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * The simpliest way to add migration feature to messages files.
 * 
 * @author lzsolt
 * 
 */
public class MMigration {

	/**
	 * Extension of properties files.
	 */
	private static final String	EXTENSION	  = ".properties";

	/**
	 * In this map we store the properties by file name.
	 */
	private PropertiesMap	    propertiesMap	= new PropertiesMap();

	/**
	 * Adds a properties to the filename (key)
	 * 
	 * @param name
	 *            name of the properties file
	 * @param properties
	 *            properties file content
	 */
	public void addResource(String name, Properties properties) {

		propertiesMap.get(name).putAll(properties);
	}

	/**
	 * Scans the specified directory for migration files, and puts the values to
	 * {@link #propertiesMap}.
	 * 
	 * @param dirName
	 *            absolute path to directory contains migration folders
	 */
	public void scanDir(String dirName) {

		File dir = new File(dirName);

		System.out.println("Scan: " + dirName);

		if (!dir.isDirectory()) {
			throw new IllegalArgumentException("Not a dir: " + dirName);
		}

		File[] migrationVersionFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});

		List<File> migrationVersions = Arrays.asList(migrationVersionFiles);

		// sort by name to keep the migration order strict
		Collections.sort(migrationVersions, new Comparator<File>() {
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		try {

			for (File file : migrationVersions) {
				scanForProperties(file);
			}

		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file
	 *            directory
	 * @throws IOException
	 *             when cannot read the properties file
	 */
	private void scanForProperties(File file) throws IOException {

		File[] propertiesFiles = file.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(EXTENSION);
			}
		});

		for (File propertiesFile : propertiesFiles) {

			String fileName = propertiesFile.getName();

			if (propertiesFile.isFile() && fileName.endsWith(EXTENSION)) {

				InputStream inputStream = new FileInputStream(propertiesFile);

				Properties properties = new Properties();
				properties.load(inputStream);

				System.out.println("Load: " + propertiesFile.getAbsolutePath());
				
				addResource(fileName, properties);

			}
		}

	}

	/**
	 * Writes the files to the specified target path.
	 * 
	 * @param dir
	 *            absolute path to the directory to write out the properties
	 *            files
	 */
	public void write(String dir) {

		File target = new File(dir);

		if (!target.isDirectory() || !target.canWrite()) {
			throw new IllegalArgumentException(dir + " is not a writeable directory");
		}

		try {

			for (Entry<String, Properties> bundle : propertiesMap.entrySet()) {

				String fileName = bundle.getKey();
				Properties properties = bundle.getValue();

				File targetFile = new File(target, fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(targetFile, false);

				System.out.println("Write: " + targetFile.getAbsolutePath());
				properties.store(fileOutputStream, null);
			}

		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {

		return propertiesMap.toString();
	}

}
