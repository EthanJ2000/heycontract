package com.example.heycontract.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Models.ContractorModel;
import com.example.heycontract.Models.RequestModel;
import com.example.heycontract.Models.User;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestDetails extends Fragment {
	private CircleImageView detailsDisplayPicture;
	private TextView detailsDisplayName;
	private EditText edtTypeOfWork_Details;
	private EditText edtDetails_Details;
	private ImageButton btnDeclineQuote;
	private ImageButton btnQuote;
	private FragmentActivity requestDetails;
	public static String name;
	public String requesterEmail;
	private static final String TAG = "RequestDetails";

	public RequestDetails() {
		// Required empty public constructor
	}
	
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		requestDetails = getActivity();
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
		getRequesterEmail();
		init();
		addInfo();
	}
	
	private void init() {
		detailsDisplayPicture = getView().findViewById(R.id.detailsDisplayPicture);
		detailsDisplayName = getView().findViewById(R.id.detailsDisplayName);
		edtTypeOfWork_Details = getView().findViewById(R.id.edtTypeOfWork_Details);
		edtDetails_Details = getView().findViewById(R.id.edtDetails_Details);
		btnDeclineQuote = getView().findViewById(R.id.btnDeclineQuote);
		btnQuote = getView().findViewById(R.id.btnQuote);
	}
	
	private void addInfo() {
		detailsDisplayName.setText(name);
		
		if (Dashboard.accountType.equals("Landlord")){
//		loadPicture();
		btnDeclineQuote.setVisibility(View.GONE);
		btnQuote.setVisibility(View.GONE);
		}

		if (Dashboard.accountType.equals("Tenant")){
//		loadPicture();
		btnDeclineQuote.setVisibility(View.GONE);
		btnQuote.setVisibility(View.GONE);

		}
		
		if (Dashboard.accountType.equals("Contractor")){
			FirebaseBackend.dbRef.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists())
					{
						for (DataSnapshot child : dataSnapshot.getChildren())
						{
							RequestModel requestModel = child.child("Request Info").getValue(RequestModel.class);
							if (requestModel.getContractorName().equals(Requests.businessName)){
								edtTypeOfWork_Details.setText(requestModel.getTypeOfWork());
								edtDetails_Details.setText(requestModel.getDetails());
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
	
	private void loadPicture() {
		Log.i(TAG, "loadPicture: called");
		if (Dashboard.accountType.equals("Contractor")){
			FirebaseBackend.storage.getReference().child(requesterEmail).child("ProfilePicture")
					.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
				@Override
				public void onSuccess(Uri uri) {
					Log.i(TAG, "onSuccess: success");
					String url = uri.toString();
					Glide.with(requestDetails).load(url).into(detailsDisplayPicture);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.e(TAG, "onFailure: ", e);
				}
			});
		}
	}
	
	public void getRequesterEmail(){
		FirebaseBackend.dbRef.child("users").orderByChild("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()){
						Log.i(TAG, "getRequesterEmail: "+child.getValue());
						if (!(child.hasChild("Business Information"))){
							Log.i(TAG, "getValue: "+child.child("Profile").getValue());
							Map<String, String> mapinfo = (Map<String, String>) child.child("Profile").getValue();
							if (mapinfo != null){
								if ((mapinfo.get("FullName").equals(name))||(mapinfo.get("fullName").equals(name))){
									requesterEmail = mapinfo.get("Email");
									Log.i(TAG, "requesterEmail: "+requesterEmail);
									if (requesterEmail == null){
										requesterEmail = mapinfo.get("email");
									}
									loadPicture();
								}
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
	
	
}
