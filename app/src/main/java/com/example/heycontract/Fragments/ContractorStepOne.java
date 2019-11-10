package com.example.heycontract.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.heycontract.R;

import java.util.ArrayList;

public class ContractorStepOne extends Fragment {
	private ArrayList<String> arrContractorTypes = new ArrayList<>();
	private Spinner contractor_spinner;
	private ImageButton btnNext_ContractorSignUp;
	private EditText edtBusinessAddress;
	private EditText edtBusinessName;
	private EditText edtBusinessOwner;
	private String spinnerSelection;
	
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	
	public ContractorStepOne() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_contractor_step_one, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Inits
		edtBusinessOwner = getView().findViewById(R.id.edtBusinessOwner);
		edtBusinessAddress = getView().findViewById(R.id.edtBusinessAddress);
		edtBusinessName = getView().findViewById(R.id.edtBusinessName);
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		btnNext_ContractorSignUp = getView().findViewById(R.id.btnNext_ContractorSignUp);
		contractor_spinner = getView().findViewById(R.id.contractor_spinner);
		initDropDown();
		
		//OnClicks
		contractor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				spinnerSelection = adapterView.getItemAtPosition(i).toString();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			
			}
		});
		
		btnNext_ContractorSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String businessName = edtBusinessName.getText().toString();
				String businessAddress = edtBusinessAddress.getText().toString();
				
				if ((businessName.isEmpty())||(businessAddress.isEmpty())||(spinnerSelection.isEmpty())){
					Toast.makeText(getContext(),"Please enter all the fields",Toast.LENGTH_LONG).show();
				}else {
					ContractorStepTwo contractorStepTwo = new ContractorStepTwo();
					fragmentTransaction.replace(R.id.SignUpFragmentContainer, contractorStepTwo);
					fragmentTransaction.commit();
				}
			}
		});
	}
	
	private void initDropDown() {
		//init array
		arrContractorTypes.add("Carpeting");
		arrContractorTypes.add("Carpentry");
		arrContractorTypes.add("Cleaning");
		arrContractorTypes.add("Electrician");
		arrContractorTypes.add("Landscaping");
		arrContractorTypes.add("Painter");
		arrContractorTypes.add("Plumber");
		arrContractorTypes.add("Roofing");
		arrContractorTypes.add("Tiling");
		
		if (getContext()!=null){
			ArrayAdapter<String> contractorSpinnerAdapter = new ArrayAdapter<>(getContext(),
					android.R.layout.simple_spinner_dropdown_item, arrContractorTypes);
			contractor_spinner.setAdapter(contractorSpinnerAdapter);
		}
		
		
	}
}
