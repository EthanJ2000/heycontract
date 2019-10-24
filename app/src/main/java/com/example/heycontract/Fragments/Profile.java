package com.example.heycontract.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.example.heycontract.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends Fragment {
	ProgressBar loadingWheel_Profile;
	ImageButton btnSaveChanges_Profile;
	ImageButton btnChangeProfilePicture;
	CircleImageView circleImageView;
	EditText edtName_Profile;
	EditText edtEmail_Profile;
	EditText edtPhoneNumber_Profile;
	EditText edtAddress_Profile;
	String fullName;
	String email;
	String phoneNumber;
	String address;
	private static final String TAG = "Profile";

	public Profile() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		//OnActivityResult start
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 1) {
				Uri selectedImage = data.getData();

				try {
					final Bitmap bitmap =
							MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
									selectedImage);
					ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageBytes);
					byte[] imageData = imageBytes.toByteArray();

					FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("ProfilePicture")
							.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							Log.i(TAG, "Successfully updated profile picture");
							circleImageView.setImageBitmap(bitmap);
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Log.e(TAG, "onFailure: ", e);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		}
		//OnActivityResult end
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		loadingWheel_Profile = getView().findViewById(R.id.loadingWheel_Profile);
		btnSaveChanges_Profile = getView().findViewById(R.id.btnSaveChanges_Profile);
		edtAddress_Profile = getView().findViewById(R.id.edtAddress_Profile);
		edtPhoneNumber_Profile = getView().findViewById(R.id.edtPhoneNumber_Profile);
		edtEmail_Profile = getView().findViewById(R.id.edtEmail_Profile);
		edtName_Profile = getView().findViewById(R.id.edtName_Profile);
		circleImageView = getView().findViewById(R.id.circleImageView);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initStorage();
		backend.initAuth();
		backend.initDB();
		loadProfilePicture();
		loadProfileInfo();
		btnChangeProfilePicture = getView().findViewById(R.id.btnChangeProfilePicture);


		//OnClicks
		btnChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i(TAG, "Change Profile Button Clicked");
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				String[] mimeTypes = {"image/jpeg", "image/png"};
				intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
				startActivityForResult(intent, 1);
			}
		});

		btnSaveChanges_Profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				loadingWheel_Profile.setVisibility(View.VISIBLE);
				FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
						.child("Profile").child("AccountType").addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if (dataSnapshot.exists()) {
							String accountType = dataSnapshot.getValue(String.class);
							fullName = edtName_Profile.getText().toString();
							email = edtEmail_Profile.getText().toString();
							phoneNumber = edtPhoneNumber_Profile.getText().toString();
							address = edtAddress_Profile.getText().toString();
							User updatedUser = new User(accountType, fullName, email, phoneNumber, address);
							FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").setValue(updatedUser);
							loadingWheel_Profile.setVisibility(View.GONE);
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {

					}
				});
			}
		});
	}

	public void loadProfilePicture() {
		loadingWheel_Profile.setVisibility(View.VISIBLE);
		FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("ProfilePicture")
				.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
				String url = uri.toString();
				Glide.with(getActivity().getApplicationContext()).load(url).into(circleImageView);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e(TAG, "onFailure: ", e);
			}
		});
	}

	public void loadProfileInfo() {
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					Log.i(TAG, "onDataChange: " + dataSnapshot);
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						Log.i(TAG, "onDataChange child: " + child.getKey());
						switch (child.getKey()) {
							case "FullName":
								fullName = child.getValue(String.class);
								edtName_Profile.setText(fullName);
								edtName_Profile.setCompoundDrawables(null, null, null, null);
								break;
							case "Email":
								email = child.getValue(String.class);
								edtEmail_Profile.setText(email);
								edtEmail_Profile.setCompoundDrawables(null, null, null, null);
								break;
							case "PhoneNumber":
								phoneNumber = child.getValue(String.class);
								edtPhoneNumber_Profile.setText(phoneNumber);
								edtPhoneNumber_Profile.setCompoundDrawables(null, null, null, null);
								break;
							case "Address":
								address = child.getValue(String.class);
								edtAddress_Profile.setText(address);
								edtAddress_Profile.setCompoundDrawables(null, null, null, null);
								break;
						}
					}
					loadingWheel_Profile.setVisibility(View.GONE);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}


		});


	}
}
