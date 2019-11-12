package com.example.heycontract.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heycontract.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Viewholder> {
	private ArrayList<String> arrRequesters;
	private Context context;
	private static final String TAG = "RequestAdapter";
	
	public RequestAdapter(ArrayList<String> arrRequesters, Context context) {
		this.arrRequesters = arrRequesters;
		this.context = context;
	}
	
	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Log.i(TAG, "onCreateViewHolder: called");
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull Viewholder holder, int position) {
		holder.listUsername.setText(arrRequesters.get(position));
	}
	
	@Override
	public int getItemCount() {
		return arrRequesters.size();
	}
	
	public class Viewholder extends RecyclerView.ViewHolder {
		CircleImageView listProfilePicture;
		TextView listUsername;
		public Viewholder(@NonNull View itemView) {
			super(itemView);
			listProfilePicture = itemView.findViewById(R.id.listProfilePicture);
			listUsername = itemView.findViewById(R.id.listUsername);
		}
	}
}
