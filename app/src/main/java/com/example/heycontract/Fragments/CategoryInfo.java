package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heycontract.R;

public class CategoryInfo extends Fragment {
	
	private RecyclerView categoryInfo_recyclerview;
	private TextView lblCategoryTitle;
	public static String categoryTitle;
	
	public CategoryInfo() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_category_info, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//Inits
		lblCategoryTitle = getView().findViewById(R.id.lblCategoryTitle);
		categoryInfo_recyclerview = getView().findViewById(R.id.categoryInfo_recyclerview);
		
		lblCategoryTitle.setText(categoryTitle);
	}
}
