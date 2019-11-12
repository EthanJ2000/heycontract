package com.example.heycontract.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.github.kimkevin.cachepot.CachePot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class GetAQuote extends Fragment {
	
	private EditText edtBusinessName_GetAQuote;
	private EditText edtFullName_GetAQuote;
	private EditText edtTypeOfWork_GetAQuote;
	private EditText edtDetails_GetAQuote;
	private ImageButton btnSendRequest;
	private static final String TAG = "GetAQuote";

	public GetAQuote() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_get_aquote, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Inits
		edtBusinessName_GetAQuote = getView().findViewById(R.id.edtBusinessName_GetAQuote);
		edtFullName_GetAQuote = getView().findViewById(R.id.edtFullName_GetAQuote);
		edtTypeOfWork_GetAQuote = getView().findViewById(R.id.edtTypeOfWork_GetAQuote);
		edtDetails_GetAQuote = getView().findViewById(R.id.edtDetails_GetAQuote);
		btnSendRequest = getView().findViewById(R.id.btnSendRequest);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		backend.initStorage();
		setInfo();
		
		
		//OnClicks
		btnSendRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String requestDetails = edtDetails_GetAQuote.getText().toString();
				if (requestDetails.isEmpty()){
					Toast.makeText(getContext(),"Please add detail",Toast.LENGTH_LONG).show();
				}else{
				
				}
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		edtBusinessName_GetAQuote.setText(BusinessProfile.businessName);
	}
	
	private void setInfo() {
		Log.i(TAG, "setInfo: "+BusinessProfile.businessName);
			edtBusinessName_GetAQuote.setText(BusinessProfile.businessName);
		
		//Get Full Name
		final String[] businessName = new String[1];
		FirebaseBackend.dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()){
						FirebaseBackend.dbRef.child("users").child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
								if (dataSnapshot.exists()){
									for (DataSnapshot child : dataSnapshot.getChildren()){
										if (child.getKey().equals("Business Information")){
											Map<String, String> infoMap = (Map<String, String>) child.getValue();
											edtTypeOfWork_GetAQuote.setText(infoMap.get("typeOfContractor"));
											businessName[0] = infoMap.get("businessName");
											Log.i(TAG, "blahblahblah: "+businessName[0]);
											Log.i(TAG, "blahblahblah: "+BusinessProfile.businessName);
										}
										if ((child.getKey().equals("Profile"))&&(businessName[0].equals(BusinessProfile.businessName))){
											Log.i(TAG, "profile called: ");
											Map<String, String> profileMap = (Map<String, String>) child.getValue();
											Log.i(TAG, "profileMap: "+profileMap.get("fullName"));
											if (profileMap.get("fullName")!=null){
												edtFullName_GetAQuote.setText(profileMap.get("fullName"));
											}
											break;
										}
									}
								}
							}
							
							@Override
							public void onCancelled(@NonNull DatabaseError databaseError) {
							
							}
						});
					}
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				
			}
		});
		
		//Get Type of Work
		
		
	}
}
