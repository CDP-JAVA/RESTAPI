package com.roomautomation.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.roomautomation.pojo.User;
import com.roomautomation.utils.CassandraConnect;
import com.roomautomation.utils.PropertyUtils;
import com.datastax.driver.core.*;

public class UserDao {

	private final static Logger LOGGER = Logger.getLogger(UserDao.class);
	User user;
	Session session;
	PreparedStatement statement;
	BoundStatement boundStatement;
	PropertyUtils utils = new PropertyUtils();
	CassandraConnect connect = new CassandraConnect();

	// method for authenticating user
	public User authenticateUser(String userName, String password) throws IllegalArgumentException, IOException {
		user = new User();
		String pass = null;
		int userid = 0;
		String firstName = null;
		String lastName = null;
		String role = null;
		String name = userName;
		session = connect.getSession();
		try {
			LOGGER.info("in dao authenticate user method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in authenticate " + e.getMessage());

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
				LOGGER.info("username or password incoreect");
			}
		} else {
			return user;
		}
		session.close();
		return user;
	}

	public int findMaxId() throws IllegalArgumentException, IOException {
		session = connect.getSession();
		try {
			LOGGER.info("in dao find max id method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in findmaxid method " + e.getMessage());
		}

		// getting max of userid from database
		ResultSet results = session.execute("select MAX(userid) from users");
		// generating id according to maximum id we got
		if (results.isExhausted()) {

			return 1;
		} else {

			int i = 0;

			for (Row row : results) {
				i = row.getInt(0);
			}
			session.close();
			return i + 1;
		}

	}

	public String addUser(User user2) throws IllegalArgumentException, IOException {
		session = connect.getSession();
		try {
			LOGGER.info("in dao add user method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in add user method " + e.getMessage());
		}

		// preparing statement and binding it later with url parameters
		statement = session
				.prepare("INSERT INTO users(userid,username,firstname,lastname,password,role) VALUES (?,?,?,?,?,?)");
		boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(user2.getUserId(), user2.getUserName(),
				user2.getFirstName(), user2.getLastName(), user2.getPassword(), user2.getRole()));

		for (Row row : results) {
			// checking the atleast one row is inserted if not retuen error
			if (row.getInt(0) < 0) {
				LOGGER.info("record not inserted");
				return "error";
			}
		}

		LOGGER.info("record inserted");
		session.close();
		return "success";
	}

	public String updateUser(String firstName, String lastName, String userName, int userId, String role)
			throws IllegalArgumentException, IOException {
		session = connect.getSession();
		try {
			LOGGER.info("in dao add user method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in update method " + e.getMessage());
		}
		// preparing statement and binding it later with url parameters
		statement = session.prepare("update users set firstname=?,lastname=?,role=? where username=? and userid=?");
		boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(firstName, lastName, role, userName, userId));
		// checking if record is updated or not
		if (results != null) {
			LOGGER.info("updation success");
			session.close();
			return "success";
		} else {
			LOGGER.info("updation failed");
			session.close();
			return "fail";
		}
	}

	public String deleteUser(String userName, int userId) throws IllegalArgumentException, IOException {
		session = connect.getSession();
		try {
			LOGGER.info("in dao add user method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in delete method " + e.getMessage());
		}
		statement = session.prepare("delete from users  where username=? and userid=?");
		boundStatement = new BoundStatement(statement);

		ResultSet results = session.execute(boundStatement.bind(userName, userId));
		// checking if record is deleted or not
		if (results != null) {
			LOGGER.info("deleted successfully");
			session.close();
			return "success";
		} else {
			LOGGER.error("deletion failed");
			session.close();
			return "fail";
		}
	}

	public List<User> showAllUsers() throws IllegalArgumentException, IOException {
		int i = 0;
		session = connect.getSession();
		try {
			LOGGER.info("in dao add user method and db connected");
		} catch (RuntimeException e) {
			LOGGER.error("error in show all users method " + e.getMessage());
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

}