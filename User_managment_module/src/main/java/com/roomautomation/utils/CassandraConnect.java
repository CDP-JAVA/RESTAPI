package com.roomautomation.utils;

import java.io.IOException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnect {

	PropertyUtils utils = new PropertyUtils();

	// creating and returning cassandra session
	public Session getSession() throws IOException {
		Cluster cluster = Cluster.builder().addContactPoint(utils.getIP()).build();
		Session session = cluster.connect(utils.getKeyspaceName());
		return session;
	}
}
