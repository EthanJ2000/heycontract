package com.example.heycontract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heycontract.Adapters.ViewPagerAdapter;
import com.example.heycontract.Fragments.ContactsFragmentOne;
import com.example.heycontract.Fragments.ContactsFragmentTwo;
import com.example.heycontract.Fragments.MessageFragmentOne;
import com.example.heycontract.Fragments.MessageFragmentTwo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class Contacts extends AppCompatActivity {
	
	private Toolbar toolbar;
	private TextView txtTitle;
	private ImageButton btnBack_Toolbar;
	private FloatingActionButton fab_Contacts;
	private TabLayout tabs_Contacts;
	private ViewPager viewPager_Contacts;
	private ViewPagerAdapter viewPagerAdapterContacts;
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this,Messages.class));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		init();
		
		
		//Add account type check to change fragment titles
		viewPagerAdapterContacts.addFragment(new ContactsFragmentOne(),"Tenant");
		viewPagerAdapterContacts.addFragment(new ContactsFragmentTwo(),"Contractors");
		
		tabs_Contacts = findViewById(R.id.tabs_Contacts);
		viewPager_Contacts.setAdapter(viewPagerAdapterContacts);
		tabs_Contacts.setupWithViewPager(viewPager_Contacts);
		
		//OnClicks
		btnBack_Toolbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getApplicationContext(),Messages.class));
			}
		});
	}
	
	private void init() {
		viewPager_Contacts = findViewById(R.id.viewPager_Contacts);
		fab_Contacts = findViewById(R.id.fab_Contacts);
		btnBack_Toolbar = findViewById(R.id.btnBack_Toolbar);
		toolbar = findViewById(R.id.toolbar);
		txtTitle = findViewById(R.id.txtTitle);
		txtTitle.setText("Contacts");
		viewPagerAdapterContacts = new ViewPagerAdapter(getSupportFragmentManager());
		setSupportActionBar(toolbar);
	}
}
