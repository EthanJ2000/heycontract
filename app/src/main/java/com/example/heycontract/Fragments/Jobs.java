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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heycontract.Adapters.RequestAdapter;
import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.RequestModel;
import com.example.heycontract.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

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
	private TextView txtPendingJobs;
	private TextView txtActiveJobs;
	private TextView txtContractorActiveJobs;
	private RecyclerView ContractorActiveJobs_recyclerview;
	private ArrayList<String> arrRequests = new ArrayList<>();
	private ArrayList<String> arrActiveJobs = new ArrayList<>();
	public String accountType;
	
	
	
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
		fab_Jobs.setOnClickListener(view1 ->
		{
			fragmentTransaction.replace(R.id.dashboard_fragment_container, Dashboard.categories);
			fragmentTransaction.commit();
		});
		
	}
	
	private void init() {
		getAccountType();
		arrRequests.clear();
		ContractorActiveJobs_recyclerview = getView().findViewById(R.id.ContractorActiveJobs_recyclerview);
		txtContractorActiveJobs = getView().findViewById(R.id.txtContractorActiveJobs);
		txtActiveJobs = getView().findViewById(R.id.txtActiveJobs);
		txtPendingJobs = getView().findViewById(R.id.txtPendingJobs);
		arrRequests.clear();
		loadingWheel_Jobs = getView().findViewById(R.id.loadingWheel_Jobs);
		loadingWheel_Jobs.setVisibility(View.VISIBLE);
		pendingjobs_recyclerview = getView().findViewById(R.id.pendingjobs_recyclerview);
		activejobs_recyclerview = getView().findViewById(R.id.activejobs_recyclerview);
		txtNoPendingRequest = getView().findViewById(R.id.txtNoPendingRequest);
		txtNoActiveJobs = getView().findViewById(R.id.txtNoActiveJobs);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initDB();
		backend.initStorage();
		initRequestArray();
		
		
		
		
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
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
						if ((child.getKey().equals("accountType"))||(child.getKey().equals("AccountType")))
						{
							accountType = child.getValue(String.class);
							Dashboard.accountType = accountType;
							if (!(child.getValue().equals("Contractor")))
							{
								txtPendingJobs.setVisibility(View.VISIBLE);
								pendingjobs_recyclerview.setVisibility(View.VISIBLE);
								txtActiveJobs.setVisibility(View.VISIBLE);
								activejobs_recyclerview.setVisibility(View.VISIBLE);
								fab_Jobs.show();
								
							}else {
								txtContractorActiveJobs.setVisibility(View.VISIBLE);
								ContractorActiveJobs_recyclerview.setVisibility(View.VISIBLE);
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
	
	public void initRequestArray() {
		//add check so it matches each user
		FirebaseBackend.dbRef.child("Requests").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.exists())
				{
					for (DataSnapshot child : dataSnapshot.getChildren())
					{
						RequestModel newRequestModel = child.getValue(RequestModel.class);
						Log.i(TAG, "requester: "+newRequestModel.getRequester());
						Log.i(TAG, "accounttypeinfo: "+accountType);
						if (Dashboard.accountType!=null){
							if (Dashboard.accountType.equals("Contractor")){
								arrRequests.add(newRequestModel.getRequester());
							}else{
								arrRequests.add(newRequestModel.getContractorName());
							}
							Log.i(TAG, "size: "+arrRequests.size());
							initRequestRecyclerView();
						}
					}
				}else{
					txtNoPendingRequest.setVisibility(View.VISIBLE);
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
	
	public void initRequestRecyclerView(){
		Log.i(TAG, "initRequestRecyclerView: "+arrRequests.size());
		RequestAdapter requestAdapter = new RequestAdapter(arrRequests,getContext());
		pendingjobs_recyclerview.setAdapter(requestAdapter);
		pendingjobs_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
		loadingWheel_Jobs.setVisibility(View.GONE);
	}
	
}

