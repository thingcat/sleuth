package com.sleuth.core.storage.sample;

import java.util.List;

public class User extends UserSchema {
	
	private String firstName;
    private String lastName;
    private String email;
    private List<User> friends;
    
    public User() {
    	
    }
    
    public User(String firstName, String lastName) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    }
    
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

}
