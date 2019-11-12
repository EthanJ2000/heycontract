package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class BusinessProfile extends Fragment {
	
	private ImageButton btnCall;
	private ImageButton btnRequestAQuote;
	private TextView txtServices;
	private TextView txtBusinessName_Profile;
	public static String businessName;
	private static final String TAG = "BusinessProfile";
	
	public BusinessProfile() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_business_profile, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Inits
		btnCall = getView().findViewById(R.id.btnCall);
		btnRequestAQuote = getView().findViewById(R.id.btnRequestAQuote);
		txtServices = getView().findViewById(R.id.txtServices);
		txtBusinessName_Profile = getView().findViewById(R.id.txtBusinessName_Profile);
		txtBusinessName_Profile.setText(businessName);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		backend.initStorage();
		getServicesOffered();
		
		//OnClicks
		btnRequestAQuote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, Dashboard.getAQuote);
				fragmentTransaction.commit();
			
			}
		});
	}
	
	public void getServicesOffered() {
		FirebaseBackend.dbRef.child("users").orderByChild("Business Information").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					for (DataSnapshot child : dataSnapshot.getChildren())
					{
						FirebaseBackend.dbRef.child("users").child(child.getKey()).addValueEventListener(new ValueEventListener() {
							@Override
							public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
								if (dataSnapshot.exists())
								{
									if (dataSnapshot.hasChild("Business Information"))
									{
										for (DataSnapshot child : dataSnapshot.getChildren())
										{
											if (child.getKey().equals("Business Information"))
											{
												Map<String, String> mapinfo = (Map<String, String>) child.getValue();
												Log.i(TAG, "onDataChange: "+mapinfo);
												if (mapinfo.get("businessName").equals(businessName)){
													txtServices.setText(mapinfo.get("servicesOffered"));
												}
											}
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
	}
}
