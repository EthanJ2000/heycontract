package com.example.heycontract.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContractorsAdapter extends RecyclerView.Adapter<ContractorsAdapter.Viewholder> {
	private ArrayList<String> arrBusinessNames;
	private ArrayList<String> arrPhoneNumbers;
	private Context context;
	private static final String TAG = "ContractorsAdapter";
	
	public ContractorsAdapter(ArrayList<String> arrBusinessNames,
	                          ArrayList<String> arrPhoneNumbers, Context context) {
		this.arrBusinessNames = arrBusinessNames;
		this.arrPhoneNumbers = arrPhoneNumbers;
		this.context = context;
	}
	
	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contractor_list,
				parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
		Log.i(TAG, "onBindViewHolder: called");
		FirebaseBackend backend = new FirebaseBackend();
		backend.initStorage();
		backend.initAuth();
		backend.initDB();
		
		FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(arrBusinessNames.get(position)).getDownloadUrl()
				.addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Glide.with(context.getApplicationContext()).load(uri).into(holder.business_preview);
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.i(TAG, "onFailure: called");
			}
		});
		
		holder.txtBusinessName.setText(arrBusinessNames.get(position));
		holder.txtPhoneNumber.setText(arrPhoneNumbers.get(position));
	}
	
	@Override
	public int getItemCount() {
		return arrBusinessNames.size();
	}
	
	public class Viewholder extends RecyclerView.ViewHolder {
			CircleImageView business_preview;
			TextView txtBusinessName;
			TextView txtPhoneNumber;
			
		public Viewholder(@NonNull View itemView) {
			super(itemView);
			business_preview = itemView.findViewById(R.id.business_preview);
			txtBusinessName = itemView.findViewById(R.id.txtBusinessName);
			txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
		}
	}
}
