package com.example.heycontract.API;

public class APIUtils {
	
	public APIUtils() {
	}
	
	public static final String BASE_URL = "https://pacific-temple-68068.herokuapp.com";
	
	public static APIInterface getAPIService() {
		
		return APIClient.getClient(BASE_URL).create(APIInterface.class);
	}
	
}
