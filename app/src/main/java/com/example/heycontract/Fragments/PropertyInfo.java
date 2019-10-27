package com.example.heycontract.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.PropertiesAdapter;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PropertyInfo extends Fragment {
	ProgressBar loadingWheel_PropertyInfo;
	CardView property_picture_card_PropertyInfo;
	ImageView property_picture_PropertyInfo;
	TextView txtAddress_PropertyInfo;
	TextView txtStatus_PropertyInfo;
	TextView txtNumBedrooms_PropertyInfo;
	TextView txtNumBathrooms_PropertyInfo;
	TextView txtNumGarages_PropertyInfo;
	ImageButton btnSaveChanges_PropertyInfo;
	ImageButton btnListProperty;
	private static final String TAG = "PropertyInfo";
	
	
	public PropertyInfo() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_property_info, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Init
		btnSaveChanges_PropertyInfo = getView().findViewById(R.id.btnSaveChanges_PropertyInfo);
		btnListProperty = getView().findViewById(R.id.btnListProperty);
		loadingWheel_PropertyInfo = getView().findViewById(R.id.loadingWheel_PropertyInfo);
		property_picture_card_PropertyInfo = getView().findViewById(R.id.property_picture_card_PropertyInfo);
		property_picture_PropertyInfo = getView().findViewById(R.id.property_picture_PropertyInfo);
		txtAddress_PropertyInfo = getView().findViewById(R.id.txtAddress_PropertyInfo);
		txtStatus_PropertyInfo = getView().findViewById(R.id.txtStatus_PropertyInfo);
		txtNumBedrooms_PropertyInfo = getView().findViewById(R.id.txtNumBedrooms_PropertyInfo);
		txtNumBathrooms_PropertyInfo = getView().findViewById(R.id.txtNumBathrooms_PropertyInfo);
		txtNumGarages_PropertyInfo = getView().findViewById(R.id.txtNumGarages_PropertyInfo);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initStorage();
		backend.initDB();
		addInfo();
	}
	
	public void addInfo() {
		//Add Image
		FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(PropertiesAdapter.address).getDownloadUrl()
				.addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						if (isAdded())
						{
							Glide.with(getActivity()).load(uri).into(property_picture_PropertyInfo);
						}
						
					}
				});
		
		//Add Info
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Properties").orderByChild("address").equalTo(PropertiesAdapter.address)
				.addChildEventListener(new ChildEventListener() {
					@Override
					public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
					                         @Nullable String s) {
						if (dataSnapshot.exists())
						{
							for (DataSnapshot child : dataSnapshot.getChildren())
							{
								switch (child.getKey())
								{
									case "address":
										txtAddress_PropertyInfo.setText(child.getValue(String.class));
										break;
									case "numberOfBedrooms":
										txtNumBedrooms_PropertyInfo.setText(child.getValue(String.class));
										break;
									
									case "numberOfBathrooms":
										txtNumBathrooms_PropertyInfo.setText(child.getValue(String.class));
										break;
									case "numberOfGarages":
										txtNumGarages_PropertyInfo.setText(child.getValue(String.class));
										break;
								}
							}
						}
					}
					
					@Override
					public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
					                           @Nullable String s) {
						
					}
					
					@Override
					public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
					
					}
					
					@Override
					public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
					                         @Nullable String s) {
						
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
					
					}
				});
		
		//Check Property Status
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Tenants")
				.orderByChild("address").equalTo(PropertiesAdapter.address).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					txtStatus_PropertyInfo.setText("Occupied");
				}
				else
				{
					txtStatus_PropertyInfo.setText("Vacant");
					btnListProperty.setVisibility(View.VISIBLE);
					
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
		
	}
}

