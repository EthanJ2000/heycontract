package com.example.heycontract.Fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.heycontract.R;

public class AddTenant extends Fragment
{
	Spinner address_spinner;
	ImageButton btnDone_AddTenant;
	EditText edtFullName_AddTenant;
	EditText edtEmail_AddTenant;
	EditText edtPhoneNumber_AddTenant;
	public AddTenant()
	{
		// Required empty public constructor
	}
	

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_tenant, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		address_spinner = getView().findViewById(R.id.address_spinner);
		btnDone_AddTenant = getView().findViewById(R.id.btnDone_AddTenant);
		edtFullName_AddTenant = getView().findViewById(R.id.edtFullName_AddTenant);
		edtEmail_AddTenant = getView().findViewById(R.id.edtEmail_AddTenant);
		edtPhoneNumber_AddTenant = getView().findViewById(R.id.edtPhoneNumber_AddTenant);
		
	}
}
