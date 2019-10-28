package com.example.heycontract;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TenantAdapter extends RecyclerView.Adapter<TenantAdapter.Viewholder> {
	private ArrayList<String> arrTenants;
	private ArrayList<String> arrAddress;
	private Context context;
	private static final String TAG = "TenantAdapter";
	
	public TenantAdapter(ArrayList<String> arrAddress,ArrayList<String> arrTenants,
	                     Context context) {
		this.arrTenants = arrTenants;
		this.arrAddress = arrAddress;
		this.context = context;
	}
	
	@NonNull
	@Override
	public TenantAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.properties_list,
				parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull TenantAdapter.Viewholder holder, final int position) {
		FirebaseBackend backend = new FirebaseBackend();
		backend.initStorage();
		backend.initAuth();
		backend.initDB();
		
		Glide.with(context.getApplicationContext()).load(R.drawable.default_icon).into(holder.tenant_preview);
		
		holder.txtTenant.setText(arrTenants.get(position));
		holder.txtAddress.setText(arrAddress.get(position));
		
		holder.recycler_parent_layout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				PopupMenu popup = new PopupMenu(context.getApplicationContext(), view);
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.popup, popup.getMenu());
				popup.setGravity(Gravity.END);
				
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem menuItem) {
						Log.i(TAG, "onMenuItemClick: "+arrAddress.get(position));
						if (menuItem.getTitle().toString().equals("Delete")){
							deleteTenant(position);
						}
						return false;
					}
				});
				popup.show();
				return false;
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return arrAddress.size();
	}
	
	public class Viewholder extends RecyclerView.ViewHolder {
		TextView txtTenant;
		TextView txtAddress;
		CircleImageView tenant_preview;
		RelativeLayout recycler_parent_layout;
		
		public Viewholder(@NonNull View itemView) {
			super(itemView);
			txtTenant = itemView.findViewById(R.id.txtFeatures);
			txtAddress = itemView.findViewById(R.id.txtAddress);
			tenant_preview = itemView.findViewById(R.id.property_preview);
			recycler_parent_layout = itemView.findViewById(R.id.recycler_parent_layout);
		}
	}
	
	public void deleteTenant(final int position){
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
				.child("Tenants").orderByChild("address").equalTo(arrAddress.get(position)).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					String id = dataSnapshot.getValue().toString().split("=")[0].substring(1);
					FirebaseBackend.dbRef = FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
							                        .child("Tenants").child(id).getRef();
					FirebaseBackend.dbRef.removeValue();
					arrAddress.remove(arrAddress.get(position));
					arrTenants.remove(arrTenants.get(position));
					notifyDataSetChanged();
				}else {
					Log.i(TAG, "onDataChange tenant doesnt exists: ");
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	
	}
	
}
