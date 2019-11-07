package com.example.heycontract.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.heycontract.API.APIInterface;
import com.example.heycontract.API.APIPropertyModel;
import com.example.heycontract.API.APIUtils;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Adapters.PropertiesAdapter;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("All")
public class PropertyInfo extends Fragment {
	private ProgressBar loadingWheel_PropertyInfo;
	private CardView property_picture_card_PropertyInfo;
	private ImageView property_picture_PropertyInfo;
	private TextView txtAddress_PropertyInfo;
	private TextView txtStatus_PropertyInfo;
	private TextView txtNumBedrooms_PropertyInfo;
	private TextView txtNumBathrooms_PropertyInfo;
	private TextView txtNumGarages_PropertyInfo;
	private ImageButton btnSaveChanges_PropertyInfo;
	private ImageButton btnListProperty;
	private APIInterface apiService;
	
	private String listedBy;
	private String address;
	private String propertyType = "test";
	private int numBedrooms;
	private int numBathrooms;
	private int numGarages;
	
	private static final String TAG = "PropertyInfo";
	
	
	public PropertyInfo() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_property_info, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Init
		apiService = APIUtils.getAPIService();
		btnSaveChanges_PropertyInfo = getView().findViewById(R.id.btnSaveChanges_PropertyInfo);
		btnListProperty = getView().findViewById(R.id.btnListProperty);
		loadingWheel_PropertyInfo = getView().findViewById(R.id.loadingWheel_PropertyInfo);
		property_picture_card_PropertyInfo = getView().findViewById(R.id.property_picture_card_PropertyInfo);
		property_picture_PropertyInfo = getView().findViewById(R.id.property_picture_PropertyInfo);
		txtAddress_PropertyInfo = getView().findViewById(R.id.txtAddress_PropertyInfo);
		txtStatus_PropertyInfo = getView().findViewById(R.id.txtStatus_PropertyInfo);
		txtNumBedrooms_PropertyInfo = getView().findViewById(R.id.txtNumBedrooms_PropertyInfo);
		txtNumBathrooms_PropertyInfo = getView().findViewById(R.id.txtNumBathrooms_PropertyInfo);
		txtNumGarages_PropertyInfo = getView().findViewById(R.id.txtNumGarages_PropertyInfo);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initStorage();
		backend.initDB();
		getUserFullName();
		addInfo();
		
		//OnClicks
		btnListProperty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i(TAG, "onClick: list clicked");
				listProperty();
			}
		});
	}
	
	public void addInfo() {
		//Add Image
		FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(PropertiesAdapter.address).getDownloadUrl()
				.addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						if (isAdded())
						{
							Glide.with(getActivity()).load(uri).into(property_picture_PropertyInfo);
						}
						
					}
				});
		
		
		//Add Info
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Properties").orderByChild("address").equalTo(PropertiesAdapter.address)
				.addChildEventListener(new ChildEventListener() {
					@Override
					public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
					                         @Nullable String s) {
						if (dataSnapshot.exists())
						{
							for (DataSnapshot child : dataSnapshot.getChildren())
							{
								switch (child.getKey())
								{
									case "address":
										address = child.getValue(String.class);
										txtAddress_PropertyInfo.setText(child.getValue(String.class));
										break;
									case "numberOfBedrooms":
										numBedrooms = Integer.parseInt(child.getValue(String.class));
										txtNumBedrooms_PropertyInfo.setText(child.getValue(String.class));
										break;
									
									case "numberOfBathrooms":
										numBathrooms = Integer.parseInt(child.getValue(String.class));
										txtNumBathrooms_PropertyInfo.setText(child.getValue(String.class));
										break;
									case "numberOfGarages":
										numGarages = Integer.parseInt(child.getValue(String.class));
										txtNumGarages_PropertyInfo.setText(child.getValue(String.class));
										break;
								}
							}
						}
					}
					
					@Override
					public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
					                           @Nullable String s) {
						
					}
					
					@Override
					public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
					
					}
					
					@Override
					public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
					                         @Nullable String s) {
						
					}
					
					@Override
					public void onCancelled(@NonNull DatabaseError databaseError) {
					
					}
				});
		
		//Check Property Status
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Tenants")
				.orderByChild("address").equalTo(PropertiesAdapter.address).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					txtStatus_PropertyInfo.setText("Occupied");
				}
				else
				{
					txtStatus_PropertyInfo.setText("Vacant");
					btnListProperty.setVisibility(View.VISIBLE);
					
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
		
	}
	
	public void listProperty(){
		loadingWheel_PropertyInfo.setVisibility(View.VISIBLE);
		APIPropertyModel apiPropertyModel = new APIPropertyModel(address,propertyType,numBedrooms,numBathrooms,numGarages,listedBy);
		apiService.savePost(apiPropertyModel).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<APIPropertyModel>() {
			@Override
			public void onCompleted() {
				Toast.makeText(getContext(),"Posted Successfully",Toast.LENGTH_LONG).show();
				Log.i(TAG, "onCompleted: Listed Successfully");
				
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				Properties properties = new Properties();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, properties);
				fragmentTransaction.commit();
				loadingWheel_PropertyInfo.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onError(Throwable e) {
				Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
				loadingWheel_PropertyInfo.setVisibility(View.GONE);
				Log.e(TAG, "onError: ",e);
			}
			
			@Override
			public void onNext(APIPropertyModel apiPropertyModel) {
			
			}
		});
		
	}
	
	public void getUserFullName(){
		//Get User Full Name
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						if (child.getKey().equals("FullName")){
							listedBy = child.getValue(String.class);
						}
					}
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
}

