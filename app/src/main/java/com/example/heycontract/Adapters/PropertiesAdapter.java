package com.example.heycontract.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heycontract.Dashboard;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.Fragments.Properties;
import com.example.heycontract.Fragments.PropertyInfo;
import com.example.heycontract.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.Viewholder> {
	private ArrayList<String> arrFeatures;
	private ArrayList<String> arrAddress;
	private Context context;
	private static final String TAG = "PropertiesAdapter";
	public static String address;
	
	
	public PropertiesAdapter(ArrayList<String> arrFeatures, ArrayList<String> arrAddress,
	                         Context context) {
		this.arrFeatures = arrFeatures;
		this.arrAddress = arrAddress;
		this.context = context;
	}
	
	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Log.i(TAG, "onCreateViewHolder: " + viewType);
		Log.i(TAG, "onCreateViewHolder2: " + parent);
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.properties_list,
				parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
		Log.i(TAG, "onBindViewHolder: " + holder);
		Log.i(TAG, "onBindViewHolder2: " + position);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initStorage();
		backend.initAuth();
		backend.initDB();
		
	
			FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(arrAddress.get(position)).getDownloadUrl()
					.addOnSuccessListener(new OnSuccessListener<Uri>() {
						@Override
						public void onSuccess(Uri uri) {
							Glide.with(context.getApplicationContext()).load(uri).into(holder.property_preview);
						}
					});
		
			
		
		holder.txtFeatures.setText(arrFeatures.get(position));
		holder.txtAddress.setText(arrAddress.get(position));
		
		//OnClick
		holder.recycler_parent_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				address = arrAddress.get(position);
				
				
				((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_fragment_container, Dashboard.propertyInfo).commit();
				((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().remove(Properties.instance);
				
			}
		});
		
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
						if (menuItem.getTitle().toString().equals("Delete"))
						{
							FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
									.child("Tenants").orderByChild("address").equalTo(arrAddress.get(position)).addValueEventListener(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									if (dataSnapshot.exists())
									{
										String id =
												dataSnapshot.getValue().toString().split("=")[0].substring(1);
										FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
												.child("Tenants").child(id).child("fullName").addValueEventListener(new ValueEventListener() {
											@Override
											public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
												if (dataSnapshot.exists())
												{
													Log.i(TAG, "onDataChange: " + dataSnapshot);
													AlertDialog.Builder builder =
															new AlertDialog.Builder(context, R.style.dialogTheme);
													builder.setMessage("Are you sure you want to " +
															                   "delete this " +
															                   "property? " + dataSnapshot.getValue() + " lives here.")
															.setPositiveButton("Yes",
																	new DialogInterface.OnClickListener() {
																@Override
																public void onClick(DialogInterface dialogInterface, int i) {
																	deleteProperty(position);
																}
															})
															.setNegativeButton("Cancel",
																	new DialogInterface.OnClickListener() {
																@Override
																public void onClick(DialogInterface dialogInterface, int i) {
																	dialogInterface.cancel();
																}
															}).show();
												}
											}
											
											@Override
											public void onCancelled(@NonNull DatabaseError databaseError) {
											
											}
										});
										
									}
									else
									{
										deleteProperty(position);
									}
								}
								
								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {
								
								}
							});
							
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
		TextView txtFeatures;
		TextView txtAddress;
		CircleImageView property_preview;
		RelativeLayout recycler_parent_layout;
		
		public Viewholder(@NonNull View itemView) {
			super(itemView);
			txtFeatures = itemView.findViewById(R.id.txtFeatures);
			txtAddress = itemView.findViewById(R.id.txtAddress);
			property_preview = itemView.findViewById(R.id.property_preview);
			recycler_parent_layout = itemView.findViewById(R.id.recycler_parent_layout);
		}
	}
	
	public void deleteProperty(final int position) {
		Log.i(TAG, "onMenuItemClick: " + arrAddress.get(position) + " deleted");
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
				.child("Properties").orderByChild("address").equalTo(arrAddress.get(position)).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists())
				{
					Log.i(TAG, "onDataChange: exists");
					String id = dataSnapshot.getValue().toString().split("=")[0].substring(1);
					FirebaseBackend.dbRef =
							FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
									.child("Properties").child(id).getRef();
					FirebaseBackend.dbRef.removeValue();
					arrAddress.remove(arrAddress.get(position));
					arrFeatures.remove(arrFeatures.get(position));
					notifyDataSetChanged();
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
		
	}
}
