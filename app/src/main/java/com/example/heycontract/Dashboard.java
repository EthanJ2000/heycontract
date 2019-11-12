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

import com.example.heycontract.Fragments.BusinessProfile;
import com.example.heycontract.Fragments.Categories;
import com.example.heycontract.Fragments.CategoryInfo;
import com.example.heycontract.Fragments.GetAQuote;
import com.example.heycontract.Fragments.Home;
import com.example.heycontract.Fragments.Jobs;
import com.example.heycontract.Fragments.Notifications;
import com.example.heycontract.Fragments.Profile;
import com.example.heycontract.Fragments.Properties;
import com.example.heycontract.Fragments.RequestDetails;
import com.example.heycontract.Fragments.Requests;
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
	public static String accountType;
	public static String currentUsername;
	private static final String TAG = "Dashboard";
	
	//Fragments
	public static RequestDetails requestDetails;
	public static GetAQuote getAQuote;
	public static BusinessProfile businessProfile;
	public static CategoryInfo categoryInfo;
	public static Jobs jobs;
	public static Categories categories;
	public static Home home;
	public static Requests requests;
	public static Settings settingsFragment;
	public static Notifications notificationsFragment;
	FragmentManager fragmentManager = getSupportFragmentManager();
	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		init();
		
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
						jobs = new Jobs();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, jobs);
						fragmentTransaction.commit();
						break;
					
					case R.id.bottomnav_profile:
						Profile profile = new Profile();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, profile);
						fragmentTransaction.commit();
						break;
						
					case R.id.bottomnav_requests:
						requests = new Requests();
						fragmentTransaction.replace(R.id.dashboard_fragment_container, requests);
						fragmentTransaction.commit();
						
				}
				return true;
			}
		});
		
		
	}
	
	private void init() {
		//Init
		requestDetails = new RequestDetails();
		getAQuote = new GetAQuote();
		businessProfile = new BusinessProfile();
		categoryInfo = new CategoryInfo();
		categories = new Categories();
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
		getCurrentUsername();
	}
	
	
	private void inflateBottomNav() {
		FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid())
				.child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()){
					for (DataSnapshot child : dataSnapshot.getChildren()) {
						if ((child.getKey().equals("accountType"))||(child.getKey().equals("AccountType"))){
							Log.i(TAG, "acounttype: "+child.getValue());
							switch (child.getValue(String.class)){
								case "Landlord":
									Log.i(TAG, "onDataChange: Landlord");
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_landlord);
									break;
								case "Tenant":
									Log.i(TAG, "onDataChange: Tenant");
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_tenant);
									break;
								case "Contractor":
									Log.i(TAG, "onDataChange: Contractor");
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_contractor);
									break;
							}
						}
					}
				}else{
					Log.i(TAG, "this: doesnt exist");
				}
				
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			
			}
		});
		
		FirebaseBackend.dbRef.child("contractors").child(FirebaseBackend.auth.getCurrentUser().getUid())
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
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_tenant);
									break;
								case "Contractor":
									Log.i(TAG, "onDataChange: Contractor");
									bottomNavigationView.getMenu().clear();
									bottomNavigationView.inflateMenu(R.menu.bottom_nav_contractor);
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
			
		}else if((categories != null)&&(categories.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container,jobs);
			fragmentTransaction.commit();
			
		} else if((categoryInfo != null) && (categoryInfo.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container,categories);
			fragmentTransaction.commit();
		}else if((businessProfile!=null)&&(businessProfile.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container,categoryInfo);
			fragmentTransaction.commit();
		}else if((getAQuote!=null)&&(getAQuote.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container,businessProfile);
			fragmentTransaction.commit();
		}else if((requestDetails!=null)&&(requestDetails.isVisible())){
			fm = getSupportFragmentManager();
			fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.dashboard_fragment_container,requests);
			fragmentTransaction.commit();
		}
		else{
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
	
		public void getCurrentUsername(){
		//change FullName to fullName
			FirebaseBackend.dbRef.child("users").child(FirebaseBackend.auth.getCurrentUser().getUid()).child("Profile").child("FullName").addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists()){
						currentUsername = dataSnapshot.getValue(String.class);
					}
				}
				
				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
				
				}
			});
		}
}
