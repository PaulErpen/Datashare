package com.github.paulerpen.datashare.ManagerService.User;

import org.springframework.data.annotation.Id;

/**
 * Object represantation of the user class,
 * used to login in the mypush app
 * @author paul
 *
 */
public class User {

    @Id
    private String id;//id used by mongodb for uniqueness
    private String username;
    private String password;

    protected User() {}
    /**
     * constructor if the User Object
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, username='%s', password='%s']",
                id, username, password);
    }

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
    

}