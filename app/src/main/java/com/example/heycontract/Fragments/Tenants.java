package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heycontract.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Tenants extends Fragment
{
	RecyclerView tenants_recyclerview;
	FloatingActionButton fab_Tenants;
	public Tenants()
	{
		// Required empty public constructor
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_tenants, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		//Init
		tenants_recyclerview = getView().findViewById(R.id.tenants_recyclerview);
		fab_Tenants = getView().findViewById(R.id.fab_Tenants);
		
		//OnClicks
		fab_Tenants.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				AddTenant addTenant = new AddTenant();
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, addTenant);
				fragmentTransaction.commit();
			}
		});
	}
}
