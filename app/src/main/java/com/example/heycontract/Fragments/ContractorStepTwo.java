package com.example.heycontract.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.ContractorModel;
import com.example.heycontract.Models.User;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class ContractorStepTwo extends Fragment {
	
	private EditText edtEmail_Contractor;
	private EditText edtPhoneNumber_Contractor;
	private EditText edtPassword_Contractor;
	private EditText edtConfirmPassword_Contractor;
	private ImageButton btnSignUp_Contractor;
	private ProgressBar loadingWheel_ContractorSignUp;

	
	
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
		loadingWheel_ContractorSignUp = getView().findViewById(R.id.loadingWheel_ContractorSignUp);
		edtEmail_Contractor = getView().findViewById(R.id.edtEmail_Contractor);
		edtPhoneNumber_Contractor = getView().findViewById(R.id.edtPhoneNumber_Contractor);
		edtPassword_Contractor = getView().findViewById(R.id.edtPassword_Contractor);
		edtConfirmPassword_Contractor = getView().findViewById(R.id.edtConfirmPassword_Contractor);
		btnSignUp_Contractor = getView().findViewById(R.id.btnSignUp_Contractor);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initDB();
		
		//OnClicks
		btnSignUp_Contractor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String email = edtEmail_Contractor.getText().toString();
				String phoneNumber = edtPhoneNumber_Contractor.getText().toString();
				String password = edtPassword_Contractor.getText().toString();
				String confirmPassword = edtConfirmPassword_Contractor.getText().toString();
				
				if ((email.isEmpty()) || (phoneNumber.isEmpty()) || (password.isEmpty()) || (confirmPassword.isEmpty()))
				{
					Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_LONG).show();
				}else if(!password.equals(confirmPassword)){
					Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
				}
				else
				{
					loadingWheel_ContractorSignUp.setVisibility(View.VISIBLE);
					createNewUser(email,password);
				}
				
			}
		});
	}
	
	
	public void createNewUser(final String email, String password) {
		final FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		FirebaseBackend.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (!task.isSuccessful())
				{
					loadingWheel_ContractorSignUp.setVisibility(View.GONE);
					String errorCode =
							((FirebaseAuthException) task.getException()).getErrorCode();
					switch (errorCode)
					{
						case "ERROR_INVALID_EMAIL":
							Toast.makeText(getContext(), "Invalid Email.",
									Toast.LENGTH_LONG).show();
							break;
						case "ERROR_WEAK_PASSWORD":
							Toast.makeText(getContext(), "Weak Password.",
									Toast.LENGTH_LONG).show();
							Toast.makeText(getContext(), "Password must contain at " +
									                                        "least 6 characters.",
									Toast.LENGTH_LONG).show();
							break;
						case "ERROR_EMAIL_ALREADY_IN_USE":
							Toast.makeText(getContext(), "Email already in use.",
									Toast.LENGTH_LONG).show();
							break;
					}
				}
				else
				{
					Intent intent = getActivity().getIntent();
					String userAccountType = intent.getExtras().getString("AccountType");
					
					String phoneNumber = edtPhoneNumber_Contractor.getText().toString().trim();
					String userID = FirebaseBackend.auth.getCurrentUser().getUid();
					
					User newUser = new User(userAccountType, ContractorStepOne.ownerName, email, phoneNumber,ContractorStepOne.businessAddress);
					ContractorModel contractorModel = new ContractorModel(ContractorStepOne.businessName,ContractorStepOne.spinnerSelection,ContractorStepOne.servicesOffered);
					FirebaseBackend.dbRef.child("users").child(userID).child("Profile").setValue(newUser);
					FirebaseBackend.dbRef.child("users").child(userID).child("Business Information").setValue(contractorModel);
					
					startActivity(new Intent(getContext(), Dashboard.class));
					getActivity().finish();
					loadingWheel_ContractorSignUp.setVisibility(View.GONE);
				}
			}
		});
		
	}
}
