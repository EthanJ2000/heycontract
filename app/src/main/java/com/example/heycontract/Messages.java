package com.example.heycontract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.heycontract.Fragments.MessageFragmentOne;
import com.example.heycontract.Fragments.MessageFragmentTwo;
import com.google.android.material.tabs.TabLayout;

public class Messages extends AppCompatActivity {
	private Toolbar toolbar;
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;
	private TabLayout tabLayout;
	private ImageButton btnBack_Messages;
	private ImageButton btnSearch_Messages;
	private static final String TAG = "Messages";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		
		btnBack_Messages = findViewById(R.id.btnBack_Messages);
		btnSearch_Messages = findViewById(R.id.btnSearch_Messages);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager = findViewById(R.id.viewPager);
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		viewPagerAdapter.addFragment(new MessageFragmentOne(),"Tenants");
		viewPagerAdapter.addFragment(new MessageFragmentTwo(),"Contractors");
		tabLayout = findViewById(R.id.tabs);
		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		
		//OnClicks
		btnBack_Messages.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i(TAG, "onClick: back button clicked");
			}
		});
		
	}
}
