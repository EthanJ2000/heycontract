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

import com.example.heycontract.Adapters.RequestAdapter;
import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.ContractorModel;
import com.example.heycontract.Models.RequestModel;
import com.example.heycontract.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Requests extends Fragment {
	private RecyclerView requests_recyclerview;
	private ArrayList<String> arrRequests = new ArrayList<>();
	public String accountType;
	private TextView txtNoRequests_Requests;
	public static String businessName;
	private static final String TAG = "Requests";
	
	public Requests() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_requests, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init() {
		getAccountType();
		getBusinessName();
		arrRequests.clear();
		txtNoRequests_Requests = getView().findViewById(R.id.txtNoRequests_Requests);
		requests_recyclerview = getView().findViewById(R.id.requests_recyclerview);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initDB();
		backend.initStorage();
		initRequestArray();
	}
	
	private void getAccountType() {
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					for (DataSnapshot child : dataSnapshot.getChildren())
					{
						if ((child.getKey().equals("accountType"))||(child.getKey().equals("AccountType")))
						{
							accountType = child.getValue(String.class);
							Dashboard.accountType = accountType;
							
						}
					}
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
	
	public void initRequestArray(){
		FirebaseBackend.dbRef.child("Requests").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.exists())
				{
					for (DataSnapshot child : dataSnapshot.getChildren())
					{
						RequestModel newRequestModel = child.getValue(RequestModel.class);
						Log.i(TAG, "requester: "+newRequestModel.getRequester());
						Log.i(TAG, "accounttypeinfo: "+accountType);
						if (Dashboard.accountType!=null){
							if (Dashboard.accountType.equals("Contractor")&&(newRequestModel.getContractorName().equals(businessName))){
								arrRequests.add(newRequestModel.getRequester());
							}
							Log.i(TAG, "size: "+arrRequests.size());
							initRecyclerView();
						}
					}
				}else{
					txtNoRequests_Requests.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
			
			}
			
			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
			
			}
			
			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
			
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
	
	public void initRecyclerView(){
		RequestAdapter requestAdapter = new RequestAdapter(arrRequests,getContext());
		requests_recyclerview.setAdapter(requestAdapter);
		requests_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
	}
	
	public void getBusinessName(){
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Business Information").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					ContractorModel contractorModel = dataSnapshot.getValue(ContractorModel.class);
					businessName = contractorModel.getBusinessName();
					Log.i(TAG, "businessName: "+businessName);
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
}
