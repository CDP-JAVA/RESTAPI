package com.roomautomation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.roomautomation.pojo.User;

import com.roomautomation.service.PasswordGeneration;
import com.roomautomation.service.UserService;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class UserController {
	private final static Logger LOGGER = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	String role = "invalid";

	// user authentication method
	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String aunthenticateUser(@RequestBody User dummyUser) throws IllegalArgumentException, IOException {
		User user = userService.aunticationUser(dummyUser.getUserName(), dummyUser.getPassword());

		// checking if user is authenticated or not
		if (user.getUserId() == 0) {
			LOGGER.error("username or password incorrect");
			role = "username or password is incorrect";
		} else {
			role = user.getRole();
		}
		return role;

	}

	// add user method
	@RequestMapping(value = "/addUser/{firstName}/{lastName}/{userName}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String addUser(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String userName)
			throws ParseException, IllegalArgumentException, IOException {
		User user = new User();
		// calling method of password generation class
		String password = PasswordGeneration.generateRandomPassword();

		user.setUserId(userService.findMaxId());
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRole("user");
		user.setPassword(password);
		user.setUsername(userName);

		return userService.addUser(user);

	}

	// update user method
	@RequestMapping(value = "/updateUser/{firstName}/{lastName}/{userName}/{userId}/{role}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String updateUser(@PathVariable String firstName, @PathVariable String lastName,
			@PathVariable String userName, @PathVariable int userId, @PathVariable String role)
			throws ParseException, IllegalArgumentException, IOException {
		// calling update user method of service layer alongwith all the
		// parameters

		String result = userService.updateUser(firstName, lastName, userName, userId, role);
		return result;
	}

	// delete user method
	@RequestMapping(value = "/deleteUser/{userName}/{userId}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public String deleteUser(@PathVariable String userName, @PathVariable int userId)
			throws ParseException, IllegalArgumentException, IOException {
		String result = userService.deleteUser(userName, userId);
		return result;
	}

	// show all users method
	@RequestMapping(value = "/showAllUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> showAllUsers() throws ParseException, IllegalArgumentException, IOException {

		java.util.List<User> result = userService.showAllUsers();

		return result;
	}

}
