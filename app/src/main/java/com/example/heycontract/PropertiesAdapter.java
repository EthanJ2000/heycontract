package com.example.heycontract;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.Viewholder> {
	private ArrayList<String> arrFeatures;
	private ArrayList<String> arrAddress;
	private Context context;
	private static final String TAG = "PropertiesAdapter";

	public PropertiesAdapter(ArrayList<String> arrFeatures, ArrayList<String> arrAddress,
							 Context context) {
		this.arrFeatures = arrFeatures;
		this.arrAddress = arrAddress;
		this.context = context;
	}

	@NonNull
	@Override
	public Viewholder onCreateViewHolder(@NonNull ViewGroup parent,
										 int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.properties_list, parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}

	@Override
	public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
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
	}


	@Override
	public int getItemCount() {
		return arrAddress.size();
	}

	public class Viewholder extends RecyclerView.ViewHolder {
		TextView txtFeatures;
		TextView txtAddress;
		CircleImageView property_preview;

		public Viewholder(@NonNull View itemView) {
			super(itemView);
			txtFeatures = itemView.findViewById(R.id.txtFeatures);
			txtAddress = itemView.findViewById(R.id.txtAddress);
			property_preview = itemView.findViewById(R.id.property_preview);
		}
	}
}
