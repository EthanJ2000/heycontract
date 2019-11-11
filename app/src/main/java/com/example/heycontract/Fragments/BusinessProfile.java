package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heycontract.R;

public class BusinessProfile extends Fragment {
	
	private ImageButton btnCall;
	private ImageButton btnRequestAQuote;
	private TextView txtServices;
	private TextView txtBusinessName_Profile;
	public static String businessName;
	
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
	}
}
