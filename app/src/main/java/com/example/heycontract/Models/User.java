package com.example.heycontract.Models;

public class User {
	public String accountType;
	public String fullName;
	public String email;
	public String phoneNumber;
	public String address;
	
	public User() {
	}
	
	public User(String accountType, String fullName, String email, String phoneNumber) {
		this.accountType = accountType;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	public User(String accountType, String fullName, String email, String phoneNumber,
	            String address) {
		this.accountType = accountType;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
