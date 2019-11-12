package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Jobs extends Fragment {
	
	private static final String TAG = "Jobs";
	private ProgressBar loadingWheel_Jobs;
	private FloatingActionButton fab_Jobs;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RecyclerView pendingjobs_recyclerview;
	private RecyclerView activejobs_recyclerview;
	private TextView txtNoPendingRequest;
	private TextView txtNoActiveJobs;
	
	
	
	public Jobs() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_jobs, container, false);
	}
	
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
		
		//OnClicks
		fab_Jobs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fragmentTransaction.replace(R.id.dashboard_fragment_container, Dashboard.categories);
				fragmentTransaction.commit();
			}
		});
		
	}
	
	private void init() {
		pendingjobs_recyclerview = getView().findViewById(R.id.pendingjobs_recyclerview);
		activejobs_recyclerview = getView().findViewById(R.id.activejobs_recyclerview);
		txtNoPendingRequest = getView().findViewById(R.id.txtNoPendingRequest);
		txtNoActiveJobs = getView().findViewById(R.id.txtNoActiveJobs);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initDB();
		backend.initStorage();
		initArray();
		getAccountType();
		
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		loadingWheel_Jobs = getView().findViewById(R.id.loadingWheel_Jobs);
		fab_Jobs = getView().findViewById(R.id.fab_Jobs);
	}
	
	private void getAccountType() {
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					for (DataSnapshot child : dataSnapshot.getChildren())
					{
						if (child.getKey().equals("accountType"))
						{
							if (child.getValue().equals("Contractor"))
							{
								fab_Jobs.hide();
							}
						}
					}
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
	
	public void initArray(){
		//Check if data doesnt exist
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Jobs").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (!dataSnapshot.exists()){
					Log.i(TAG, "singlevalue: doesnt exist");
					loadingWheel_Jobs.setVisibility(View.GONE);
//					txtNoJobs.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
}
