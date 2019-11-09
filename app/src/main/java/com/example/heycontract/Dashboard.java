package com.example.heycontract;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.heycontract.Fragments.Home;
import com.example.heycontract.Fragments.Jobs;
import com.example.heycontract.Fragments.Notifications;
import com.example.heycontract.Fragments.Profile;
import com.example.heycontract.Fragments.Properties;
import com.example.heycontract.Fragments.Settings;
import com.example.heycontract.Fragments.Tenants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class Dashboard extends AppCompatActivity implements InternetConnectivityListener {
	BottomNavigationView bottomNavigationView;
	RelativeLayout dashboard_fragment_container;
	ImageButton btnSettings;
	ImageButton btnMessages_Dashboard;
	ImageButton btnNotifications_Dashboard;
	private static final String TAG = "Dashboard";
	
	//Fragments
	public static Home home;
	public static Settings settingsFragment;
	public static Notifications notificationsFragment;
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		//Init
		btnNotifications_Dashboard = findViewById(R.id.btnNotifications_Dashboard);
		btnMessages_Dashboard = findViewById(R.id.btnMessages_Dashboard);
		bottomNavigationView = findViewById(R.id.bottomNavigationView);
		dashboard_fragment_container = findViewById(R.id.dashboard_fragment_container);
		btnSettings = findViewById(R.id.btnSettings_Dashboard);
		InternetAvailabilityChecker.init(this);
		InternetAvailabilityChecker mInternetAvailabilityChecker =
				InternetAvailabilityChecker.getInstance();
		mInternetAvailabilityChecker.addInternetConnectivityListener(this);
		final FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		backend.initDB();
		
		inflateBottomNav();
		
		//OnClicks
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				settingsFragment = new Settings();
				fragmentManager = getSupportFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, settingsFragment);
				fragmentTransaction.commit();
			}
		});
		
		btnMessages_Dashboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(Dashboard.this, Messages.class));
			}
		});
		
		btnNotifications_Dashboard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				notificationsFragment = new Notifications();
				fragmentManager = getSupportFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, notificationsFragment);
				fragmentTransaction.commit();
			}
		});
		
		//Add Home Fragment
		home = new Home();
		fragmentTransaction.add(R.id.dashboard_fragment_container, home);
		fragmentTransaction.commit();
		
		//Bottom Navigation onClick
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				switch (menuItem.getItemId())
				{
					
					case R.id.bottomnav_home:
						fragmentTransaction.replace(R.id.dashboard_fragment_container, home);
						fragmentTransaction.commit();
						break;
					
					case R.id.bottomnav_properties:
						Properties properties = new Properties();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, properties);
						fragmentTransaction.commit();
						break;
					
					case R.id.bottomnav_tenants:
						Tenants tenants = new Tenants();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, tenants);
						fragmentTransaction.commit();
						break;
					
					case R.id.bottomnav_jobs:
						Jobs jobs = new Jobs();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, jobs);
						fragmentTransaction.commit();
						break;
					
					case R.id.bottomnav_profile:
						Profile profile = new Profile();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, profile);
						fragmentTransaction.commit();
						break;
				}
				return true;
			}
		});
		
		
	}
	
	private void inflateBottomNav() {
		String accountType = getAccountType();
	}
	
	private String getAccountType() {
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
				.child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						if (child.getKey().equals("AccountType")){
							switch (child.getValue(String.class)){
								case "Landlord":
									Log.i(TAG, "onDataChange: Landlord");
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_landlord);
									break;
								case "Tenant":
									Log.i(TAG, "onDataChange: Tenant");
									break;
								case "Contractor":
									Log.i(TAG, "onDataChange: Contractor");
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
		
		return null;
	}
	
	
	@Override
	public void onInternetConnectivityChanged(boolean isConnected) {
		Log.i(TAG, "onInternetConnectivityChanged: called");
		if (isConnected)
		{
			Snackbar.make(bottomNavigationView, "Connected", Snackbar.LENGTH_SHORT).show();
			Log.i(TAG, "onInternetConnectivityChanged: connected");
		}
		else
		{
			Snackbar.make(bottomNavigationView, "No network connection available",
					Snackbar.LENGTH_SHORT).show();
			Log.i(TAG, "onInternetConnectivityChanged: not connected");
		}
		
	}
	
	
	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		if ((settingsFragment != null) && (settingsFragment.isVisible()))
		{
			Log.i(TAG, "onBackPressed: called");
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container, home);
			fragmentTransaction.commit();
		} else if((notificationsFragment != null)&&(notificationsFragment.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container, home);
			fragmentTransaction.commit();
			
		} else{
			new AlertDialog.Builder(this)
					.setMessage("Are you sure you want to exit?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Dashboard.super.onBackPressed();
						}
					})
					.setNegativeButton("No", null)
					.show();
		}
	}
}
