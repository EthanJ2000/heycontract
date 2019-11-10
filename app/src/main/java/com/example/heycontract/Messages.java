package com.example.heycontract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heycontract.Adapters.ViewPagerAdapter;
import com.example.heycontract.Fragments.MessageFragmentOne;
import com.example.heycontract.Fragments.MessageFragmentTwo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Messages extends AppCompatActivity {
	private Toolbar toolbar;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private TabLayout tabLayout;
	private ImageButton btnBack_Toolbar;
	private ImageButton btnSearch_Toolbar;
	private FloatingActionButton fab_Messages;
	private TextView txtTitle;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private static final String TAG = "Messages";
	
	
	@Override
	public void onBackPressed() {
			startActivity(new Intent(this,Dashboard.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		
		
		fab_Messages = findViewById(R.id.fab_Messages);
		btnBack_Toolbar = findViewById(R.id.btnBack_Toolbar);
		btnSearch_Toolbar = findViewById(R.id.btnSearch_Toolbar);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager = findViewById(R.id.viewPager);
		toolbar = findViewById(R.id.toolbar);
		txtTitle = findViewById(R.id.txtTitle);
		txtTitle.setText("Messages");
		setSupportActionBar(toolbar);
		FirebaseBackend backend = new FirebaseBackend();
		backend.initDB();
		backend.initAuth();
		
		//Add account type check to change fragment titles
		addFragments();
		
		tabLayout = findViewById(R.id.tabs);
		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		
		//OnClicks
		btnBack_Toolbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
					startActivity(new Intent(Messages.this,Dashboard.class));
			}
		});
		
		fab_Messages.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),Contacts.class));
			}
		});
		
		
	}
	
	public void addFragments(){
		Log.i(TAG, "addFragments: called");
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
				.child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						if (child.getKey().equals("AccountType")){
							switch (child.getValue(String.class)){
								case "Landlord":
									viewPagerAdapter.addFragment(new MessageFragmentOne(),"Tenants");
									viewPagerAdapter.addFragment(new MessageFragmentTwo(),"Contractors");
									viewPagerAdapter.notifyDataSetChanged();
									break;
								case "Tenant":
									Log.i(TAG, "onDataChange: tenants called");
									viewPagerAdapter.addFragment(new MessageFragmentOne(),"Landlord");
									viewPagerAdapter.addFragment(new MessageFragmentTwo(),"Contractors");
									viewPagerAdapter.notifyDataSetChanged();
									break;
								case "Contractor":
									viewPagerAdapter.addFragment(new MessageFragmentOne(),"Tenants");
									viewPagerAdapter.addFragment(new MessageFragmentTwo(),"Landlords");
									viewPagerAdapter.notifyDataSetChanged();
									break;
							}
						}
					}
				}else{
					Log.i(TAG, "onDataChange: doesnt exist");
				}
				
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
	}
}
