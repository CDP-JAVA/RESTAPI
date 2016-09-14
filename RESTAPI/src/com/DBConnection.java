package com;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class DBConnection 
{
	public static Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    public static Session session = cluster.connect("roomautomation");

}
