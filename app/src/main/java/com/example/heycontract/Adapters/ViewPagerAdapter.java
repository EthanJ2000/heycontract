package com.example.heycontract.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	
	private final List<Fragment> fragmentList = new ArrayList<>();
	private final List<String> titleList = new ArrayList<>();
	
	public ViewPagerAdapter(@NonNull FragmentManager fm) {
		super(fm);
	}
	
	@NonNull
	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}
	
	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		position += 1;
		String title = "";
		switch (position){
			case 1:
				title = "Tenants";
				break;
			case 2:
				title = "Contractors";
				break;
		}
		return title;
	}
	
	public void addFragment(Fragment fragment, String title){
		fragmentList.add(fragment);
		titleList.add(title);
	}
	
}
