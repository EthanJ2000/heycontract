package com.example.heycontract.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Viewholder> {
	private ArrayList<String> arrMessages;
	private Context context;
	private static final String TAG = "NotificationAdapter";
	
	public NotificationAdapter(ArrayList<String> arrMessages, Context context) {
		this.arrMessages = arrMessages;
		this.context = context;
	}
	
	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list,
				parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull Viewholder holder, int position) {
		holder.txtMessage.setText(arrMessages.get(position));
		
		
			holder.cbConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Query query = FirebaseBackend.dbRef.child("Notifications").orderByChild("message").equalTo(arrMessages.get(position));
					query.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists()){
								dataSnapshot.getRef().removeValue();
								arrMessages.remove(position);
								notifyDataSetChanged();
							}
						}
						
						@Override
						public void onCancelled(@NonNull DatabaseError databaseError) {
						
						}
					});
					
				}
			});
			
		
	}
	
	@Override
	public int getItemCount() {
		return arrMessages.size();
	}
	
	public class Viewholder extends RecyclerView.ViewHolder {
		TextView txtMessage;
		CheckBox cbConfirm;
		public Viewholder(@NonNull View itemView) {
			super(itemView);
			cbConfirm = itemView.findViewById(R.id.cbConfirm);
			txtMessage = itemView.findViewById(R.id.txtMessage);
		}
	}
}
