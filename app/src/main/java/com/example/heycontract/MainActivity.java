package com.example.heycontract;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {
	EditText edtEmail_SignIn;
	EditText edtPassword_SignIn;
	EditText edtForgot_Email;
	TextView txtForgotPassword_SignIn;
	TextView txtSignUp_SignIn;
	ImageButton btnYes_Dialog;
	ImageButton btnLogin;
	TextView btnNo_Text;
	ProgressBar loadingWheel_Login;
	public static MainActivity instance;
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Init
		instance = this;
		loadingWheel_Login = findViewById(R.id.loadingWheel_Login);
		edtEmail_SignIn = findViewById(R.id.edtEmail_SignIn);
		edtPassword_SignIn = findViewById(R.id.edtPassword_SignIn);
		txtSignUp_SignIn = findViewById(R.id.txtSignUp_SignIn);
		txtForgotPassword_SignIn = findViewById(R.id.txtForgotPassword_SignIn);
		txtSignUp_SignIn = findViewById(R.id.txtSignUp_SignIn);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		initDialog();
		
		//Auto-Login
		if (FirebaseBackend.auth.getCurrentUser() != null)
		{
			startActivity(new Intent(MainActivity.this, Dashboard.class));
			finish();
		}
		
		//OnClicks
		txtSignUp_SignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, SignUpAs.class));
			}
		});
		
		btnLogin = findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				loadingWheel_Login.setVisibility(View.VISIBLE);
				String email = edtEmail_SignIn.getText().toString();
				String password = edtPassword_SignIn.getText().toString();
				if ((email.isEmpty()) && (password.isEmpty()))
				{
					Toast.makeText(getApplicationContext(), "Please enter all the fields.",
							Toast.LENGTH_LONG).show();
					loadingWheel_Login.setVisibility(View.GONE);
				}
				else
				{
					if (isNetworkAvailable(getApplication()))
					{
						
						FirebaseBackend.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								if (!task.isSuccessful())
								{
									loadingWheel_Login.setVisibility(View.GONE);
									String errorCode =
											((FirebaseAuthException) (task.getException())).getErrorCode();
									switch (errorCode)
									{
										case "ERROR_WRONG_PASSWORD":
											Toast.makeText(getApplicationContext(), "Invalid Password.", Toast.LENGTH_LONG).show();
											break;
										case "ERROR_USER_NOT_FOUND":
											Toast.makeText(getApplicationContext(), "An account with that email does not exist.",
													Toast.LENGTH_LONG).show();
											break;
									}
								}
								else
								{
									startActivity(new Intent(MainActivity.this, Dashboard.class));
									finish();
									loadingWheel_Login.setVisibility(View.GONE);
								}
							}
						});
						
					}else{
						loadingWheel_Login.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(),"No network connection available",Toast.LENGTH_LONG).show();
					}
					
				}
			}
		});
	}
	
	public void initDialog() {
		txtForgotPassword_SignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.forgot_dialog);
				dialog.setTitle("Title");
				edtForgot_Email = dialog.findViewById(R.id.edtForgot_Email);
				btnYes_Dialog = dialog.findViewById(R.id.btnYes_Dialog);
				btnYes_Dialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String forgotEmail = edtForgot_Email.getText().toString();
						FirebaseBackend.auth.sendPasswordResetEmail(forgotEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								if (task.isSuccessful()){
									Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}else {
									Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				});
				
				btnNo_Text = dialog.findViewById(R.id.btnNo_Text);
				btnNo_Text.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}
	
	public static boolean isNetworkAvailable(Context con) {
		try
		{
			ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = null;
			if (cm != null)
			{
				networkInfo = cm.getActiveNetworkInfo();
			}
			
			if (networkInfo != null && networkInfo.isConnected())
			{
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
