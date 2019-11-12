package com.example.heycontract.Models;

public class RequestModel {
	String contractorName;
	String typeOfWork;
	String Details;
	String requester;
	
	public RequestModel(String contractorName, String typeOfWork, String details,
	                    String requester) {
		this.contractorName = contractorName;
		this.typeOfWork = typeOfWork;
		Details = details;
		this.requester = requester;
	}
	
	public RequestModel() {
	}
	
	public String getContractorName() {
		return contractorName;
	}
	
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	
	public String getTypeOfWork() {
		return typeOfWork;
	}
	
	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}
	
	public String getDetails() {
		return Details;
	}
	
	public void setDetails(String details) {
		Details = details;
	}
	
	public String getRequester() {
		return requester;
	}
	
	public void setRequester(String requester) {
		this.requester = requester;
	}
}
