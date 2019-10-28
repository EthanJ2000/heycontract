package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.example.heycontract.TenantModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class AddTenant extends Fragment {
	Spinner address_spinner;
	ImageButton btnDone_AddTenant;
	EditText edtFullName_AddTenant;
	EditText edtEmail_AddTenant;
	EditText edtPhoneNumber_AddTenant;
	String spinnerSelection;
	RecyclerView tenants_recyclerview;
	private ArrayList<String> arrPropertyList = new ArrayList<>();

	private static final String TAG = "AddTenant";

	public AddTenant() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_tenant, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		address_spinner = getView().findViewById(R.id.address_spinner);
		btnDone_AddTenant = getView().findViewById(R.id.btnDone_AddTenant);
		edtFullName_AddTenant = getView().findViewById(R.id.edtFullName_AddTenant);
		edtEmail_AddTenant = getView().findViewById(R.id.edtEmail_AddTenant);
		edtPhoneNumber_AddTenant = getView().findViewById(R.id.edtPhoneNumber_AddTenant);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		backend.initStorage();

		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Properties")
				.orderByChild("address").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				for (DataSnapshot child : dataSnapshot.getChildren()) {
					if (child.getKey().equals("address")) {
						Log.i(TAG, "onChildAdded AddTenant: " + child.getValue());
						arrPropertyList.add(child.getValue(String.class));
					}
				}

				//Adapter for Spinner
				if (getContext() != null){
					ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
							android.R.layout.simple_spinner_dropdown_item, arrPropertyList);
					address_spinner.setAdapter(spinnerAdapter);
				}

				//OnClicks
				address_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(
							AdapterView<?> adapterView, View view, int i,
							long l) {
						spinnerSelection = adapterView.getItemAtPosition(i).toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> adapterView) {

					}
				});

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

		btnDone_AddTenant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String fullName = edtFullName_AddTenant.getText().toString();
				String email = edtEmail_AddTenant.getText().toString();
				String phoneNumber = edtPhoneNumber_AddTenant.getText().toString();

				if ((fullName.isEmpty()) || (email.isEmpty()) || (phoneNumber.isEmpty()) ||
					(spinnerSelection.isEmpty())) {
					Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
				} else {
					TenantModel tenantModel = new TenantModel(fullName, email, phoneNumber, spinnerSelection);
					FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child(
							"Tenants").push().setValue(tenantModel);

					Tenants tenants = new Tenants();
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.dashboard_fragment_container, tenants);
					fragmentTransaction.commit();
					getActivity().getSupportFragmentManager().beginTransaction().remove(AddTenant.this);

				}
			}
		});


	}

}
