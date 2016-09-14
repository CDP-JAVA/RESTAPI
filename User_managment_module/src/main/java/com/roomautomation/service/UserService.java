package com.roomautomation.service;

import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.roomautomation.dao.UserDao;
import com.roomautomation.pojo.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	private final static Logger LOGGER = Logger.getLogger(UserService.class);

	public User aunticationUser(String userName, String password) throws IllegalArgumentException, IOException {
		// calling dao layer method
		User user = userDao.authenticateUser(userName, password);
		return user;
	}

	public int findMaxId() throws IllegalArgumentException, IOException {
		LOGGER.info("call to user service find max id method");
		return userDao.findMaxId();
	}

	public String addUser(User user) throws IllegalArgumentException, IOException {

		return userDao.addUser(user);

	}

	public String updateUser(String firstName, String lastName, String userName, int userId, String role)
			throws IllegalArgumentException, IOException {
		return userDao.updateUser(firstName, lastName, userName, userId, role);

	}

	public String deleteUser(String userName, int userId) throws IllegalArgumentException, IOException {
		return userDao.deleteUser(userName, userId);
	}

	public List<User> showAllUsers() throws IllegalArgumentException, IOException {
		return userDao.showAllUsers();
	}

}
