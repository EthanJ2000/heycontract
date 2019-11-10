package com.example.heycontract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.heycontract.Fragments.ContractorStepOne;
import com.example.heycontract.Fragments.ContractorStepTwo;

public class ContractorSignUp extends AppCompatActivity {
	
	private RelativeLayout SignUpFragmentContainer;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	//Fragments
	private ContractorStepOne contractorStepOne;
	private ContractorStepTwo contractorStepTwo;
	
	@Override
	public void onBackPressed() {
		if (contractorStepOne.isVisible()){
			startActivity(new Intent(this,SignUpAs.class));
		}else{
			FragmentManager newFragmentManager = getSupportFragmentManager();
			FragmentTransaction newFragmentTransaction = newFragmentManager.beginTransaction();
			newFragmentTransaction.replace(R.id.SignUpFragmentContainer, contractorStepOne);
			newFragmentTransaction.commit();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contractor_sign_up);
		
		//Inits
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		SignUpFragmentContainer = findViewById(R.id.SignUpFragmentContainer);
		contractorStepOne = new ContractorStepOne();
		contractorStepTwo = new ContractorStepTwo();
		
		//Add First Fragment
		fragmentTransaction.add(R.id.SignUpFragmentContainer, contractorStepOne);
		fragmentTransaction.commit();
		
	}
	

}
