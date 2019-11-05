package com.example.heycontract.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIPropertyModel {
	
	public APIPropertyModel(String address, String propertyType, int numBedrooms, int numBathrooms,
	                     int numGarages, String listedBy) {
		this.address = address;
		this.propertyType = propertyType;
		this.numBedrooms = numBedrooms;
		this.numBathrooms = numBathrooms;
		this.numGarages = numGarages;
		this.listedBy = listedBy;
	}
	
	@SerializedName("address")
	@Expose
	private String address;
	
	@SerializedName("propertyType")
	@Expose
	private String propertyType;
	
	@SerializedName("numBedrooms")
	@Expose
	private int numBedrooms;
	
	@SerializedName("numBathrooms")
	@Expose
	private int numBathrooms;
	
	@SerializedName("numGarages")
	@Expose
	private int numGarages;
	
	@SerializedName("listedBy")
	@Expose
	private String listedBy;
	
}
