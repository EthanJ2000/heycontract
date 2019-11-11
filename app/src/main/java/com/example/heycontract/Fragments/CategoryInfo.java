package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heycontract.Adapters.ContractorsAdapter;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.ContractorModel;
import com.example.heycontract.Models.User;
import com.example.heycontract.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CategoryInfo extends Fragment {
	
	private ArrayList<String> arrBusinessNames = new ArrayList<>();
	private ArrayList<String> arrPhoneNumbers = new ArrayList<>();
	private RecyclerView categoryInfo_recyclerview;
	private TextView lblCategoryTitle;
	private TextView txtNoContractors;
	public static String categoryTitle;
	private static final String TAG = "CategoryInfo";
	
	public CategoryInfo() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_category_info, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Inits
		txtNoContractors = getView().findViewById(R.id.txtNoContractors);
		lblCategoryTitle = getView().findViewById(R.id.lblCategoryTitle);
		categoryInfo_recyclerview = getView().findViewById(R.id.categoryInfo_recyclerview);
		arrBusinessNames.clear();
		arrPhoneNumbers.clear();
		initArray();
//		if (arrBusinessNames.size() == 0){
//			txtNoContractors.setVisibility(View.VISIBLE);
//		}
		
		lblCategoryTitle.setText(categoryTitle);
	}
	
	public void initArray(){
		getBusinessNames();
		getPhoneNumber();
	}
	
	private void getPhoneNumber() {
		//add check for type of contractor
		final String[] typeOf = new String[1];
		FirebaseBackend.dbRef.child("users").orderByChild("Business Information").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						FirebaseBackend.dbRef.child("users").child(child.getKey()).addValueEventListener(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
								if (dataSnapshot.exists()){
									if (dataSnapshot.hasChild("Business Information")){
										for (DataSnapshot child : dataSnapshot.getChildren()) {
											if (child.getKey().equals("Business Information")){
												Map<String,String> infoMap = (Map<String, String>)child.getValue();
												typeOf[0] = infoMap.get("typeOfContractor");
											}
											if ((child.getKey().equals("Profile"))&&(typeOf[0].equals(categoryTitle))){
												Map<String,String> userMap = (Map<String, String>)child.getValue();
												arrPhoneNumbers.add(userMap.get("phoneNumber"));
												initRecyclerView();
											}
										}
									}
								}else {
									Log.i(TAG, "onDataChange: doesnt exist");
								}
							}
							
							@Override
							public void onCancelled(@NonNull DatabaseError databaseError) {
							
							}
						});
					}
				}else {
					Log.i(TAG, "onDataChange: doesnt exist");
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
	
	private void getBusinessNames() {
		FirebaseBackend.dbRef.child("users").orderByChild("Business Information").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						FirebaseBackend.dbRef.child("users").child(child.getKey()).addValueEventListener(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
								if (dataSnapshot.exists()){
									for (DataSnapshot child : dataSnapshot.getChildren()) {
										if (child.getKey().equals("Business Information")){
											ContractorModel contractorModel = child.getValue(ContractorModel.class);
											if (contractorModel.getTypeOfContractor().equals(categoryTitle)){
												arrBusinessNames.add(contractorModel.getBusinessName());
											}
											
										}
									}
								}else {
									Log.i(TAG, "onDataChange: doesnt exist");
								}
							}
							
							@Override
							public void onCancelled(@NonNull DatabaseError databaseError) {
							
							}
						});
					}
				}else {
					Log.i(TAG, "onDataChange: doesnt exist");
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
	
	public void initRecyclerView(){
		ContractorsAdapter contractorsAdapter = new ContractorsAdapter(arrBusinessNames,arrPhoneNumbers,getContext());
		categoryInfo_recyclerview.setAdapter(contractorsAdapter);
		categoryInfo_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
	}
}
