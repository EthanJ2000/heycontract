package com.example.heycontract.API;

import com.example.heycontract.Models.PropertyModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface APIInterface {
	
	@POST("/property/properties")
	Observable<APIPropertyModel> savePost(@Body APIPropertyModel propertyModel);
}
