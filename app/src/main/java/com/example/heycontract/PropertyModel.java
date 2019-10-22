package com.example.heycontract;

public class PropertyModel
{
	String address;
	String numberOfBedrooms;
	String numberOfBathrooms;
	String numberOfGarages;
	String downloadUrl;
	
	public PropertyModel()
	{
	}
	
	public PropertyModel(String address, String numberOfBedrooms, String numberOfBathrooms,
	                     String numberOfGarages, String downloadUrl)
	{
		this.address = address;
		this.numberOfBedrooms = numberOfBedrooms;
		this.numberOfBathrooms = numberOfBathrooms;
		this.numberOfGarages = numberOfGarages;
		this.downloadUrl = downloadUrl;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getNumberOfBedrooms()
	{
		return numberOfBedrooms;
	}
	
	public void setNumberOfBedrooms(String numberOfBedrooms)
	{
		this.numberOfBedrooms = numberOfBedrooms;
	}
	
	public String getNumberOfBathrooms()
	{
		return numberOfBathrooms;
	}
	
	public void setNumberOfBathrooms(String numberOfBathrooms)
	{
		this.numberOfBathrooms = numberOfBathrooms;
	}
	
	public String getNumberOfGarages()
	{
		return numberOfGarages;
	}
	
	public void setNumberOfGarages(String numberOfGarages)
	{
		this.numberOfGarages = numberOfGarages;
	}
	
	public String getDownloadUrl()
	{
		return downloadUrl;
	}
	
	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}
	
	public String getFeature(){
		String feature = getNumberOfBedrooms()+" Bedroom / "+getNumberOfBathrooms()+" Bathroom";
		return feature;
	}
}
