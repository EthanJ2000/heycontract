package com.example.heycontract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.heycontract.Fragments.AddProperty;
import com.example.heycontract.Fragments.Properties;
import com.example.heycontract.Fragments.PropertyInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.Viewholder> {
	private ArrayList<String> arrFeatures;
	private ArrayList<String> arrAddress;
	private Context context;
	private static final String TAG = "PropertiesAdapter";
	public static int type;
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
		Log.i(TAG, "onCreateViewHolder: "+viewType);
		Log.i(TAG, "onCreateViewHolder2: "+parent);
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.properties_list, parent, false);
		Viewholder viewholder = new Viewholder(view);
		return viewholder;
	}

	@Override
	public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
		Log.i(TAG, "onBindViewHolder: "+holder);
		Log.i(TAG, "onBindViewHolder2: "+position);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initStorage();
		backend.initAuth();
		backend.initDB();

		if (type == 0){
			FirebaseBackend.storage.getReference().child(FirebaseBackend.auth.getCurrentUser().getEmail()).child("Properties").child(arrAddress.get(position)).getDownloadUrl()
					.addOnSuccessListener(new OnSuccessListener<Uri>() {
						@Override
						public void onSuccess(Uri uri) {
							Glide.with(context.getApplicationContext()).load(uri).into(holder.property_preview);
						}
					});
		}

		if (type == 1){
			Log.i(TAG, "onBindViewHolder tenant called");
			Glide.with(context.getApplicationContext()).load(R.drawable.default_icon).into(holder.property_preview);
		}

		holder.txtFeatures.setText(arrFeatures.get(position));
		holder.txtAddress.setText(arrAddress.get(position));
		
		//OnClick
		holder.recycler_parent_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				address = arrAddress.get(position);
				
				PropertyInfo propertyInfo = new PropertyInfo();
				((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_fragment_container, propertyInfo).commit();
				((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().remove(Properties.instance);
				
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
}
