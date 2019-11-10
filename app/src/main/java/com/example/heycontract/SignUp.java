package com.example.heycontract;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heycontract.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUp extends AppCompatActivity {
	private static final String TAG = "SignUp";
	ImageButton btnSignUp_SignUp;
	EditText edtFullName_SignUp;
	EditText edtEmail_SignUp;
	EditText edtPhoneNumber_SignUp;
	EditText edtPassword_SignUp;
	EditText edtConfirmPassword_SignUp;
	ProgressBar loadingWheel_SignUp;
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, SignUpAs.class));
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		//Init
		loadingWheel_SignUp = findViewById(R.id.loadingWheel_SignUp);
		btnSignUp_SignUp = findViewById(R.id.btnSignUp_SignUp);
		edtFullName_SignUp = findViewById(R.id.edtFullName_SignUp);
		edtEmail_SignUp = findViewById(R.id.edtEmail_SignUp);
		edtPhoneNumber_SignUp = findViewById(R.id.edtPhoneNumber_SignUp);
		edtPassword_SignUp = findViewById(R.id.edtPassword_SignUp);
		edtConfirmPassword_SignUp = findViewById(R.id.edtConfirmPassword_SignUp);

		//onClicks
		btnSignUp_SignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (edtFullName_SignUp.getText().toString().trim().equals("")
					|| edtEmail_SignUp.getText().toString().trim().equals("")
					|| edtPhoneNumber_SignUp.getText().toString().trim().equals("")
					|| edtPassword_SignUp.getText().toString().trim().equals("")
					|| edtConfirmPassword_SignUp.getText().toString().trim().equals("")) {

					Toast.makeText(getApplicationContext(), "Please enter all the fields.",
							Toast.LENGTH_LONG).show();
				} else if (!edtPassword_SignUp.getText().toString().trim().equals(edtConfirmPassword_SignUp.getText().toString().trim())) {
					Toast.makeText(getApplicationContext(), "Passwords do not match.",
							Toast.LENGTH_LONG).show();
				} else {
					loadingWheel_SignUp.setVisibility(View.VISIBLE);
					createNewUser(edtEmail_SignUp.getText().toString().trim(),
							edtPassword_SignUp.getText().toString().trim());
				}
			}
		});

	}

	public void createNewUser(String email, String password) {
		final FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		FirebaseBackend.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (!task.isSuccessful()) {
					loadingWheel_SignUp.setVisibility(View.GONE);
					String errorCode =
							((FirebaseAuthException) task.getException()).getErrorCode();
					switch (errorCode) {
						case "ERROR_INVALID_EMAIL":
							Toast.makeText(getApplicationContext(), "Invalid Email.",
									Toast.LENGTH_LONG).show();
							break;
						case "ERROR_WEAK_PASSWORD":
							Toast.makeText(getApplicationContext(), "Weak Password.",
									Toast.LENGTH_LONG).show();
							Toast.makeText(getApplicationContext(), "Password must contain at " +
																	"least 6 characters.",
									Toast.LENGTH_LONG).show();
							break;
						case "ERROR_EMAIL_ALREADY_IN_USE":
							Toast.makeText(getApplicationContext(), "Email already in use.",
									Toast.LENGTH_LONG).show();
							break;
					}
				} else {
					Intent intent = getIntent();
					String userAccountType = intent.getExtras().getString("AccountType");
					String fullName = edtFullName_SignUp.getText().toString().trim();
					String email = edtEmail_SignUp.getText().toString().trim();
					String phoneNumber = edtPhoneNumber_SignUp.getText().toString().trim();
					String userID = FirebaseBackend.auth.getCurrentUser().getUid();

					User newUser = new User(userAccountType, fullName, email, phoneNumber);
					FirebaseBackend.dbRef.child("users").child(userID).child("Profile").setValue(newUser);

					startActivity(new Intent(getApplicationContext(), Dashboard.class));
					finish();
					loadingWheel_SignUp.setVisibility(View.GONE);
				}
			}
		});

	}


}
