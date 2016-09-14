package com.roomautomation.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.roomautomation.pojo.User;

import com.datastax.driver.core.*;

public class UserDao {

	public final Logger logger = Logger.getLogger(UserDao.class.getName());
	User user;
	Cluster cluster;
	Session session;
	PreparedStatement statement;
	BoundStatement boundStatement;

	// method for authenticating user
	public User authenticateUser(String userName, String password) {
		user = new User();
		String pass = null;
		int userid = 0;
		String firstName = null;
		String lastName = null;
		String role = null;
		String name = userName;

		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao authenticate user method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}
		statement = session.prepare("SELECT * FROM users WHERE username=? ALLOW FILTERING");
		// create the bound statement and initialise it with your prepared
		// statement

		boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(userName));

		// check is there any data with inserted username
		if (results != null) {
			for (Row row : results) {
				pass = row.getString("password");
				firstName = row.getString("firstname");
				lastName = row.getString("lastname");
				role = row.getString("role");
				userid = row.getInt("userid");
			}
		} else {
			return user;
		}
		// check password is set if not return error
		if (pass != null) {
			if (pass.equals(password)) {
				user.setUsername(name);
				user.setPassword(password);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setRole(role);
				user.setUserId(userid);
			} else {
				logger.info("username or password incoreect");
			}
		} else {
			return user;
		}
		session.close();
		return user;
	}

	public int findMaxId() {
		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao find max id method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}

		// getting max of userid from database
		ResultSet results = session.execute("select MAX(userid) from users");
		// generating id according to maximum id we got
		if (results.isExhausted()) {
			// System.out.println("No data");
			return 1;
		} else {
			// System.out.println("Data");
			int i = 0;

			for (Row row : results) {
				i = row.getInt(0);
				
			}
			session.close();
			return (i + 1);
		}

	}

	public String addUser(User user2) {

		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao add user method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}

		// preparing statement and binding it later with url parameters
		PreparedStatement statement = session
				.prepare("INSERT INTO users(userid,username,firstname,lastname,password,role) VALUES (?,?,?,?,?,?)");
		BoundStatement boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(user2.getUserId(), user2.getUserName(),
				user2.getFirstName(), user2.getLastName(), user2.getPassword(), user2.getRole()));

		for (Row row : results) {
			// checking the atleast one row is inserted if not retuen error
			if (row.getInt(0) < 0) {
				logger.info("record not inserted");
				return "error";

			}
		}
	
		logger.info("record inserted");
		session.close();
		return "success";

	}

	public String updateUser(String firstName, String lastName, String userName, int userId, String role) {
		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao add user method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}
		// preparing statement and binding it later with url parameters
		PreparedStatement statement = session
				.prepare("update users set firstname=?,lastname=?,role=? where username=? and userid=?");
		BoundStatement boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(firstName, lastName, role, userName, userId));
		// checking if record is updated or not
		if (results != null) {
			logger.info("updation success");
			session.close();
			return "success";
		} else {
			logger.info("updation failed");
			session.close();
			return "fail";
		}
	}

	public String deleteUser(String userName, int userId) {
		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao add user method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}
		PreparedStatement statement = session.prepare("delete from users  where username=? and userid=?");
		BoundStatement boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(userName, userId));
		// checking if record is deleted or not
		if (results != null) {
			logger.info("deleted successfully");
			session.close();
			return "success";
		} else {
			logger.error("deletion failed");
			session.close();
			return "fail";
		}
	}

	public List<User> showAllUsers() {
		int i = 0;
		try {
			// opening connection with cassandra
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("roomautomation");
			logger.info("in dao add user method and db connected");
		} catch (Exception e) {
			logger.error("db not connected");
		}
		// to add the users
		List<User> list = new ArrayList<>();

		ResultSet results = session.execute("select * from users");
		// populating list with the records and returning the same
		for (Row row : results) {
			user = new User();
			
			user.setUserId(row.getInt("userid"));
			user.setLastName(row.getString("lastname"));
			user.setUserName(row.getString("username"));
			user.setRole(row.getString("role"));
			user.setPassword(row.getString("password"));
			user.setFirstName(row.getString("firstname"));
			list.add(i, user);
			i = i + 1;
		}
		session.close();
		return list;
	}


	
	/*
	 * public String getRoomHealth(int roomid) { String[]
	 * tablename={"Temperature","Pressure","Humidity","Lux"}; List<Double>
	 * data_avg=new ArrayList<>(); for(int i=0;i<tablename.length;i++) {
	 * data_avg.add(i,cal_temp_roomid(tablename[i],roomid));
	 * 
	 * } System.out.println(data_avg); if (data_avg.get(0) >= 20 &&
	 * data_avg.get(0) <= 40 && data_avg.get(1) >= 20 && data_avg.get(1) <= 40
	 * && data_avg.get(2) >= 20 && data_avg.get(2) <= 50 && data_avg.get(3) >=
	 * 20 && data_avg.get(3) <= 40 ) return "good";
	 * 
	 * else return "bad"; }
	 */

	/*
	 * public static double cal_temp_roomid(String tablename, int roomid) {
	 * final Logger logger = Logger.getLogger(UserDao.class.getName()); double
	 * temp_avg = 0; Cluster cluster; double temp_sum = 0; Session session =
	 * null; try { cluster =
	 * Cluster.builder().addContactPoint("127.0.0.1").build(); session =
	 * cluster.connect("roomautomation"); logger.info(
	 * "in dao cal_temp_roomid method and db connected");
	 * 
	 * List<Double> list = new ArrayList<>(); PreparedStatement statement;
	 * statement = session.prepare(
	 * "select * from ? where roomid=? allow filtering"); BoundStatement
	 * boundStatement; boundStatement = new BoundStatement(statement);
	 * 
	 * ResultSet results; results =
	 * session.execute(boundStatement.bind(tablename, roomid)); if (results !=
	 * null) {
	 * 
	 * for (Row row : results) { list.add(row.getDouble("freq_val")); }
	 * System.out.println(" in method general" + list); // System.out.println(
	 * "in dao temp list is: " + tempList); }
	 * 
	 * for (double temp : list) { temp_sum += temp; } temp_avg = temp_sum /
	 * list.size();
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("db not connected"); } return temp_avg; }
	 */
}