package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.PropertiesAdapter;
import com.example.heycontract.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class Tenants extends Fragment {
	RecyclerView tenants_recyclerview;
	FloatingActionButton fab_Tenants;
	private ArrayList<String> arrTenants = new ArrayList<>();
	private ArrayList<String> arrAddresses = new ArrayList<>();
	private static final String TAG = "Tenants";

	public Tenants() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_tenants, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		tenants_recyclerview = getView().findViewById(R.id.tenants_recyclerview);
		fab_Tenants = getView().findViewById(R.id.fab_Tenants);
		initArray();
		initTenantsRecyclerView();

		//OnClicks
		fab_Tenants.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AddTenant addTenant = new AddTenant();
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, addTenant);
				fragmentTransaction.commit();
			}
		});
	}

	public void initArray(){
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Tenants").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.exists()) {
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						switch (child.getKey()){
							case "address":
								arrAddresses.add(child.getValue(String.class));
								break;
							case "fullName":
								arrTenants.add(child.getValue(String.class));

						}
					}
				}
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	public void initTenantsRecyclerView(){
		Log.i(TAG, "initTenantsRecyclerView: called");
		PropertiesAdapter propertiesAdapter = new PropertiesAdapter(arrAddresses, arrTenants, getContext());
		tenants_recyclerview.setAdapter(propertiesAdapter);
		tenants_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
	}
}
