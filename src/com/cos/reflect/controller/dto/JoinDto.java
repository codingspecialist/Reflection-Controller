package com.cos.reflect.controller.dto;

public class JoinDto {
	private String username;
	private String password;
	private String email;
	
	@Override
	public String toString() {
		return "JoinDto [username=" + username + ", password=" + password + ", email=" + email + "]";
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
