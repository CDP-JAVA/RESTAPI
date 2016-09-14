package com.roomautomation.service;

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
	final static Logger logger = Logger.getLogger(UserService.class);

	public User aunticationUser(String userName, String password) {
		//calling dao layer method
		User user = userDao.authenticateUser(userName, password);
		return user;
	}

	public int findMaxId() {
		logger.info("call to user service find max id method");
		return userDao.findMaxId();
	}

	public String addUser(User user) {

		return userDao.addUser(user);

	}

	public String updateUser(String firstName, String lastName, String userName, int userId, String role) {
		return userDao.updateUser(firstName, lastName, userName, userId, role);

	}

	public String deleteUser(String userName, int userId) {
		return userDao.deleteUser(userName, userId);
	}

	public List<User> showAllUsers() {
		return userDao.showAllUsers();
	}
	/*
	 * public String getRoomHealth(int roomid) {
	 * 
	 * return userDao.getRoomHealth(roomid); }
	 */

}
