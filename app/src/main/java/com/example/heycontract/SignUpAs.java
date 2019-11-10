package com.example.heycontract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpAs extends AppCompatActivity {
	ImageButton btnNext_SignUp;
	RadioButton rbTenant_SignUp;
	RadioButton rbLandlord_SignUp;
	RadioButton rbContractor_SignUp;
	RadioGroup radioGroup_SignUp;

	private static final String TAG = "SignUpAs";
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_as);

		//Init
		btnNext_SignUp = findViewById(R.id.btnNext_SignUp);
		btnNext_SignUp = findViewById(R.id.btnNext_SignUp);
		initRadioButtons();


	}

	public void initRadioButtons() {
		radioGroup_SignUp = findViewById(R.id.radioGroup_SignUp);
		rbTenant_SignUp = findViewById(R.id.rbTenant_SignUp);
		rbLandlord_SignUp = findViewById(R.id.rbLandlord_SignUp);
		rbContractor_SignUp = findViewById(R.id.rbContractor_SignUp);

		btnNext_SignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int selectedID = radioGroup_SignUp.getCheckedRadioButtonId();

				if (selectedID == -1) {
					Toast.makeText(getApplicationContext(), "Select an Account Type", Toast.LENGTH_LONG).show();
				} else {
					RadioButton selectedItem = findViewById(selectedID);
					
					if (selectedID == R.id.rbContractor_SignUp){
						Intent intent = new Intent(SignUpAs.this, ContractorSignUp.class);
						String accountType = (String) selectedItem.getText();
						intent.putExtra("AccountType", accountType);
						startActivity(intent);
						finish();
						MainActivity.instance.finish();
					}else{
						Intent intent = new Intent(SignUpAs.this, SignUp.class);
						String accountType = (String) selectedItem.getText();
						intent.putExtra("AccountType", accountType);
						startActivity(intent);
						finish();
						MainActivity.instance.finish();
					}
				}
			}
		});
	}
}
