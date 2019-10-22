package com.example.heycontract;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.heycontract.Fragments.Home;
import com.example.heycontract.Fragments.Jobs;
import com.example.heycontract.Fragments.Profile;
import com.example.heycontract.Fragments.Properties;
import com.example.heycontract.Fragments.Settings;
import com.example.heycontract.Fragments.Tenants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity
{
	BottomNavigationView bottomNavigationView;
	RelativeLayout dashboard_fragment_container;
	ImageButton btnSettings;
	private static final String TAG = "Dashboard";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		//Init
		bottomNavigationView = findViewById(R.id.bottomNavigationView);
		dashboard_fragment_container = findViewById(R.id.dashboard_fragment_container);
		btnSettings = findViewById(R.id.btnSettings_Dashboard);
		final FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		
		//OnClicks
		btnSettings.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Settings settings = new Settings();
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.dashboard_fragment_container, settings);
				fragmentTransaction.commit();
			}
		});
		
		//Add Home Fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		final Home home = new Home();
		fragmentTransaction.add(R.id.dashboard_fragment_container, home);
		fragmentTransaction.commit();
		
		//Bottom Navigation onClick
		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
			{
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
	
	
}
