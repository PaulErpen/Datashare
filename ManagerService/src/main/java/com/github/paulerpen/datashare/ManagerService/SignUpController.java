package com.github.paulerpen.datashare.ManagerService;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.paulerpen.datashare.ManagerService.User.User;
import com.github.paulerpen.datashare.ManagerService.User.UserRepository;

@Controller
public class SignUpController {
	
    @Autowired
    private UserRepository userRepository;
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public void registerClient(@RequestParam(value="username", required=true) String username, 
			@RequestParam(value="password", required=true) String password, 
			@RequestParam(value="repeatPassword", required=true) String repeatPassword ,
			HttpServletResponse response,
			HttpSession session,
			BCryptPasswordEncoder encoder) throws IOException {
		
		System.out.println("Requesting SignUp for:");
		System.out.println(userIsNew(username));
		System.out.println(passwordValid(password));
		System.out.println(repeatPassword.equals(password));
		
		boolean userValid = true;
		if(userIsNew(username)) {
			session.setAttribute("nameTaken", "false");	
		}else {
			session.setAttribute("nameTaken", "nameTaken");
			userValid = false;
		}
		if(passwordValid(password)) {
			session.setAttribute("passwordToShort", "false");	
		}else {
			session.setAttribute("passwordToShort", "passwordToShort");
			userValid = false;
		}
		if(password.equals(repeatPassword)) {
			session.setAttribute("passwordNoMatch", "false");	
		}else {
			session.setAttribute("passwordNoMatch", "passwordNoMatch");
			userValid = false;
		}
		if(userValid) {
			String encryptedPassword = encoder.encode(password);
			userRepository.save(new User(username,encryptedPassword));
			System.out.println("Saved new User: "+username+" Password: "+encryptedPassword);
			response.sendRedirect("/login");
		}else{
			response.sendRedirect("/signup");
		}
		
	}
	/**
	 * Looks up in the database whether there is already a user under the 
	 * specified username
	 * @param username the username which is supposed to be tested for uniqueness
	 * @return the result, which is true if the name is unique
	 */
	public boolean userIsNew(String username) {
		if(userRepository.findByUsername(username)==null) {
			return true;
		}
		return false;
	}
	/**
	 * Test the password for its validity. Mainly implemented
	 * so one can add further specifications later
	 * @param password the password being tested
	 * @return boolean is true if the password fits the specifications
	 */
	public boolean passwordValid(String password) {
		return password.length() > 5;
	}
}
