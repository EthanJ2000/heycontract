package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heycontract.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestDetails extends Fragment {
	private CircleImageView detailsDisplayPicture;
	private TextView detailsDisplayName;
	private EditText edtTypeOfWork_Details;
	private EditText edtDetails_Details;
	private ImageButton btnDeclineQuote;
	private ImageButton btnQuote;

	public RequestDetails() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_request_details, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init() {
		detailsDisplayPicture = getView().findViewById(R.id.detailsDisplayPicture);
		detailsDisplayName = getView().findViewById(R.id.detailsDisplayName);
		edtTypeOfWork_Details = getView().findViewById(R.id.edtTypeOfWork_Details);
		edtDetails_Details = getView().findViewById(R.id.edtDetails_Details);
		btnDeclineQuote = getView().findViewById(R.id.btnDeclineQuote);
		btnQuote = getView().findViewById(R.id.btnQuote);
	}
}
