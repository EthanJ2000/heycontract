package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.PropertiesAdapter;
import com.example.heycontract.PropertyModel;
import com.example.heycontract.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class Properties extends Fragment {
	ProgressBar loadingWheel_Properties;
	RecyclerView properties_recyclerview;
	FloatingActionButton fab_Add_Property;
	private ArrayList<String> arrFeatures = new ArrayList<>();
	private ArrayList<String> arrAddress = new ArrayList<>();
	private static final String TAG = "Properties";
	public static Properties instance;

	public Properties() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_properties, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		instance = this;
		loadingWheel_Properties = getView().findViewById(R.id.loadingWheel_Properties);
		loadingWheel_Properties.setVisibility(View.VISIBLE);
		properties_recyclerview = getView().findViewById(R.id.properties_recyclerview);
		fab_Add_Property = getView().findViewById(R.id.fab_Add_Property);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		backend.initStorage();

		initArray();
		initPropertiesRecyclerView();
		//OnClicks
		fab_Add_Property.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AddProperty addProperty = new AddProperty();
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, addProperty);
				fragmentTransaction.commit();
			}
		});
	}

	public void initArray() {
		final PropertyModel newModel = new PropertyModel();
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Properties").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.exists()) {
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						Log.i(TAG, "onChildAdded: " + child);
						Log.i(TAG, "size: " + arrFeatures.size());

						switch (child.getKey()) {
							case "address":
								arrAddress.add(child.getValue(String.class));
								break;
							case "numberOfBedrooms":
								newModel.setNumberOfBedrooms(child.getValue(String.class));
								break;

							case "numberOfBathrooms":
								newModel.setNumberOfBathrooms(child.getValue(String.class));
								break;
						}

					}

					Log.i(TAG, "features: " + newModel.getFeature());
					arrFeatures.add(newModel.getFeature());
					Log.i(TAG, "arrayString: " + arrFeatures.toString());


					loadingWheel_Properties.setVisibility(View.GONE);
				}

			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
				
				if (dataSnapshot.exists()){
					Log.i(TAG, "onChildRemoved: called");
				}
			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.e(TAG, "onCancelled: ", databaseError.toException());
				loadingWheel_Properties.setVisibility(View.GONE);
			}
		});
	}

	public void initPropertiesRecyclerView() {
		Log.i(TAG, "initPropertiesRecyclerView: called");
		PropertiesAdapter propertiesAdapter = new PropertiesAdapter(arrFeatures, arrAddress, getContext());
		properties_recyclerview.setAdapter(propertiesAdapter);
		properties_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
	}

}
