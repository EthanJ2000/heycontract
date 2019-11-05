package com.example.heycontract.API;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
	
	private static Retrofit retrofit = null;
	
	public static Retrofit getClient(String BASE_URL) {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					           .baseUrl(BASE_URL)
					           .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
					           .addConverterFactory(GsonConverterFactory.create())
					           .build();
		}
		return retrofit;
	}
	
}
