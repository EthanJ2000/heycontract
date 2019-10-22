package com.example.heycontract;

public class User
{
	public String AccountType;
	public String FullName;
	public String Email;
	public String PhoneNumber;
	public String Address;
	
	public User(String accountType, String fullName, String email, String phoneNumber)
	{
		this.AccountType = accountType;
		this.FullName = fullName;
		this.Email = email;
		this.PhoneNumber = phoneNumber;
	}
	
	public User(String accountType, String fullName, String email, String phoneNumber,
	            String address)
	{
		AccountType = accountType;
		FullName = fullName;
		Email = email;
		PhoneNumber = phoneNumber;
		Address = address;
	}
}
