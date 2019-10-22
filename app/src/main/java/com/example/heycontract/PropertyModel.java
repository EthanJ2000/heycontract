package com.example.heycontract;

public class PropertyModel
{
	String Address;
	String NumberOfBedrooms;
	String NumberOfBathrooms;
	String NumberOfGarages;
	String DownloadUrl;
	
	public PropertyModel(String address, String numberOfBedrooms, String numberOfBathrooms,
	                     String numberOfGarages, String downloadUrl)
	{
		Address = address;
		NumberOfBedrooms = numberOfBedrooms;
		NumberOfBathrooms = numberOfBathrooms;
		NumberOfGarages = numberOfGarages;
		DownloadUrl = downloadUrl;
	}
}
