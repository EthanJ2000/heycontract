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

import com.example.heycontract.Adapters.NotificationAdapter;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.NotificationModel;
import com.example.heycontract.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class Notifications extends Fragment {
	private RecyclerView notification_recyclerview;
	private ArrayList<String> arrMessages = new ArrayList<>();
	private static final String TAG = "Notifications";
	
	public Notifications() {
		// Required empty public constructor
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_notifications, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
		initArray();
	}
	
	private void init() {
		notification_recyclerview = getView().findViewById(R.id.notification_recyclerview);
	}
	
	public void initArray(){
		FirebaseBackend.dbRef.child("Notifications").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.exists()){
					NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
						arrMessages.add(notificationModel.getMessage());
				}
				initRecyclerView();
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
		NotificationAdapter notificationAdapter = new NotificationAdapter(arrMessages,getContext());
		notification_recyclerview.setAdapter(notificationAdapter);
		notification_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
	}
}
