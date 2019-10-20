package com.example.heycontract;

public class User
{
	public String AccountType;
	public String FullName;
	public String Email;
	public String PhoneNumber;
	
	
	public User(String accountType, String fullName, String email, String phoneNumber)
	{
		this.AccountType = accountType;
		this.FullName = fullName;
		this.Email = email;
		this.PhoneNumber = phoneNumber;
	}
}
