package com.example.heycontract.Models;

public class NotificationModel {
	String sender;
	String reciever;
	String message;
	
	public NotificationModel() {
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getReciever() {
		return reciever;
	}
	
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public NotificationModel(String sender, String reciever, String message) {
		this.sender = sender;
		this.reciever = reciever;
		this.message = message;
	}
}
