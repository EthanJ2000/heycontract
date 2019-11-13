package com.example.heycontract.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.heycontract.Dashboard;
import com.example.heycontract.R;

public class Categories extends Fragment implements View.OnClickListener {
	
	private GridLayout gridLayout_Categories;
	private ImageButton btnCarpeting;
	private ImageButton btnCarpenting;
	private ImageButton btnCleaning;
	private ImageButton btnElectrician;
	private ImageButton btnLandscaping;
	private ImageButton btnPainter;
	private ImageButton btnPlumber;
	private ImageButton btnRoofing;
	private ImageButton btnTiling;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private static final String TAG = "Categories";
	
	
	public Categories() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_categories, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
		
		
	}
	
	private void init() {
		//Inits
		gridLayout_Categories = getView().findViewById(R.id.gridLayout_Categories);
		btnCarpeting = getView().findViewById(R.id.btnCarpeting);
		btnCarpeting.setOnClickListener(this);
		
		btnCarpenting = getView().findViewById(R.id.btnCarpenting);
		btnCarpenting.setOnClickListener(this);
		
		btnCleaning = getView().findViewById(R.id.btnCleaning);
		btnCleaning.setOnClickListener(this);
		
		btnElectrician = getView().findViewById(R.id.btnElectrician);
		btnElectrician.setOnClickListener(this);
		
		btnLandscaping = getView().findViewById(R.id.btnLandscaping);
		btnLandscaping.setOnClickListener(this);
		
		btnPainter = getView().findViewById(R.id.btnPainter);
		btnPainter.setOnClickListener(this);
		
		btnPlumber = getView().findViewById(R.id.btnPlumber);
		btnPlumber.setOnClickListener(this);
		
		btnRoofing = getView().findViewById(R.id.btnRoofing);
		btnRoofing.setOnClickListener(this);
		
		btnTiling = getView().findViewById(R.id.btnTiling);
		btnTiling.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		fragmentManager = getActivity().getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.dashboard_fragment_container, Dashboard.categoryInfo);
		fragmentTransaction.commit();
		switch (view.getId()){
			case R.id.btnCarpeting:
				Log.i(TAG, "onClick: btnCarpeting");
				CategoryInfo.categoryTitle = "Carpeting";
				break;
			case R.id.btnCarpenting:
				Log.i(TAG, "onClick: btnCarpenting");
				CategoryInfo.categoryTitle = "Carpentry";
				break;
			case R.id.btnCleaning:
				Log.i(TAG, "onClick: btnCleaning");
				CategoryInfo.categoryTitle = "Cleaning";
				break;
			case R.id.btnElectrician:
				Log.i(TAG, "onClick: btnElectrician");
				CategoryInfo.categoryTitle = "Electrician";
				break;
			case R.id.btnLandscaping:
				Log.i(TAG, "onClick: btnLandscaping");
				CategoryInfo.categoryTitle = "Landscaping";
				break;
			case R.id.btnPainter:
				Log.i(TAG, "onClick: btnPainter");
				CategoryInfo.categoryTitle = "Painting";
				break;
			case R.id.btnPlumber:
				Log.i(TAG, "onClick: btnPlumber");
				CategoryInfo.categoryTitle = "Plumber";
				break;
			case R.id.btnRoofing:
				Log.i(TAG, "onClick: btnRoofing");
				CategoryInfo.categoryTitle = "Roofing";
				break;
			case R.id.btnTiling:
				Log.i(TAG, "onClick: btnTiling");
				CategoryInfo.categoryTitle = "Tiling";
				break;
		}
		
	}
}
