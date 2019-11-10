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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.heycontract.R;

public class ContractorStepTwo extends Fragment {
	
	private EditText edtEmail_Contractor;
	private EditText edtPhoneNumber_Contractor;
	private EditText edtPassword_Contractor;
	private EditText edtConfirmPassword_Contractor;
	private ImageButton btnSignUp_Contractor;
	

	public ContractorStepTwo() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_contractor_step_two, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
		//Inits
		edtEmail_Contractor = getView().findViewById(R.id.edtEmail_Contractor);
		edtPhoneNumber_Contractor = getView().findViewById(R.id.edtPhoneNumber_Contractor);
		edtPassword_Contractor = getView().findViewById(R.id.edtPassword_Contractor);
		edtConfirmPassword_Contractor = getView().findViewById(R.id.edtConfirmPassword_Contractor);
		btnSignUp_Contractor = getView().findViewById(R.id.btnSignUp_Contractor);
		
		//OnClicks
		btnSignUp_Contractor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String email = edtEmail_Contractor.getText().toString();
				String phoneNumber = edtPhoneNumber_Contractor.getText().toString();
				String password = edtPassword_Contractor.getText().toString();
				String confirmPassword = edtConfirmPassword_Contractor.getText().toString();
				
				if ((email.isEmpty())||(phoneNumber.isEmpty())||(password.isEmpty())||(confirmPassword.isEmpty())){
					Toast.makeText(getContext(),"Please enter all the fields",Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
}
