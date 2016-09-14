package com.roomautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

	private final Properties CONFIG = new Properties();
	private InputStream IS = this.getClass().getClassLoader().getResourceAsStream("cassandra.properties");

	// returning ip for cassandra connection
	public String getIP() throws IOException {

		CONFIG.load(IS);
		return CONFIG.getProperty("LOCALHOST");
	}

	// returning keyspace name for cassandra connection
	public String getKeyspaceName() throws IOException {

		CONFIG.load(IS);
		return CONFIG.getProperty("DBNAME");
	}
}
