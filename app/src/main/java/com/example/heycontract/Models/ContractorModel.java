package com.example.heycontract.Models;

public class ContractorModel {
	String businessName;
	String typeOfContractor;
	String servicesOffered;
	
	public String getBusinessName() {
		return businessName;
	}
	
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	public String getTypeOfContractor() {
		return typeOfContractor;
	}
	
	public void setTypeOfContractor(String typeOfContractor) {
		this.typeOfContractor = typeOfContractor;
	}
	
	public String getServicesOffered() {
		return servicesOffered;
	}
	
	public void setServicesOffered(String servicesOffered) {
		this.servicesOffered = servicesOffered;
	}
	
	public ContractorModel(String businessName, String typeOfContractor, String servicesOffered) {
		this.businessName = businessName;
		this.typeOfContractor = typeOfContractor;
		this.servicesOffered = servicesOffered;
	}
}
