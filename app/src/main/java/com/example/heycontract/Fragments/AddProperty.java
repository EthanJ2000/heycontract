package com.example.heycontract.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.PropertyModel;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddProperty extends Fragment {
	ProgressBar loadingWheel_AddProperty;
	EditText edtAddress_AddProperty;
	EditText edtNumBedrooms;
	EditText edtNumBathrooms;
	EditText edtNumGarages;
	ImageButton btnDone_AddProperty;
	ImageButton btnAddPropertyImage;
	CardView property_picture_card;
	ImageView property_picture;
	private static final String TAG = "AddProperty";

	public AddProperty() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_property, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		loadingWheel_AddProperty = getView().findViewById(R.id.loadingWheel_AddProperty);
		edtAddress_AddProperty = getView().findViewById(R.id.edtAddress_AddProperty);
		edtNumBedrooms = getView().findViewById(R.id.edtNumBedrooms);
		edtNumBathrooms = getView().findViewById(R.id.edtNumBathrooms);
		edtNumGarages = getView().findViewById(R.id.edtNumGarages);
		btnDone_AddProperty = getView().findViewById(R.id.btnDone_AddProperty);
		property_picture = getView().findViewById(R.id.property_picture);
		property_picture_card = getView().findViewById(R.id.property_picture_card);
		btnAddPropertyImage = getView().findViewById(R.id.btnAddPropertyImage);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initStorage();
		backend.initDB();

		//OnClick
		btnAddPropertyImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i(TAG, "btnAddPropertyImage clicked");
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				String[] mimeTypes = {"image/jpeg", "image/png"};
				intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
				startActivityForResult(intent, 2);
			}
		});

		btnDone_AddProperty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String NewAddress = edtAddress_AddProperty.getText().toString();
				final String numBedrooms = edtNumBedrooms.getText().toString();
				final String numBathrooms = edtNumBathrooms.getText().toString();
				final String numGarages = edtNumGarages.getText().toString();
				
				if ((NewAddress.isEmpty()) || (numBedrooms.isEmpty()) || (numBathrooms.isEmpty()) ||
					(numGarages.isEmpty())||(property_picture.getDrawable()==null)) {
					Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_LONG).show();
				} else {
					Log.i(TAG, "onClick: " + NewAddress);
					loadingWheel_AddProperty.setVisibility(View.VISIBLE);

					if (property_picture.getDrawable() != null) {
						property_picture.setDrawingCacheEnabled(true);
						Bitmap propertyBitmap =
								Bitmap.createBitmap(property_picture.getDrawingCache());
						property_picture.setDrawingCacheEnabled(false);

						ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
						propertyBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageBytes);
						byte[] imageData = imageBytes.toByteArray();

						FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(NewAddress)
								.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
							@Override
							public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
								Log.i(TAG, "Successfully updated property image");

								FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail())
										.child("Properties").child(NewAddress).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
									@Override
									public void onSuccess(Uri uri) {
										String downloadUrl = uri.toString();
										PropertyModel propertyModel = new PropertyModel(NewAddress,
												numBedrooms, numBathrooms, numGarages,
												downloadUrl);
										FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Properties").push().setValue(propertyModel);
										loadingWheel_AddProperty.setVisibility(View.GONE);

										Properties properties = new Properties();
										FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
										FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
										fragmentTransaction.replace(R.id.dashboard_fragment_container, properties);
										fragmentTransaction.commit();
										getActivity().getSupportFragmentManager().beginTransaction().remove(AddProperty.this);

									}
								}).addOnFailureListener(new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										Log.e(TAG, "onFailure: ", e);
										loadingWheel_AddProperty.setVisibility(View.GONE);
									}
								});

							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Log.e(TAG, "onFailure: ", e);
								loadingWheel_AddProperty.setVisibility(View.GONE);
							}
						});


					}
				}

			}


		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//OnActivityResult start
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 2) {
				Uri selectedImage = data.getData();

				try {
					final Bitmap bitmap =
							MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
									selectedImage);
					property_picture.setImageBitmap(bitmap);
					property_picture.setScaleType(ImageView.ScaleType.CENTER_CROP);
				} catch (IOException e) {
					e.printStackTrace();
				}


			}
		}
		//OnActivityResult end

	}
}
