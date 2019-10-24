package com.example.heycontract.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.heycontract.FirebaseBackend;
import com.example.heycontract.MainActivity;
import com.example.heycontract.R;

public class Settings extends Fragment {
	RelativeLayout signOut;

	public Settings() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//Init
		FirebaseBackend backend = new FirebaseBackend();
		backend.initAuth();
		signOut = getView().findViewById(R.id.signOut);

		//OnClicks
		signOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),
						R.style.dialogTheme);
				builder.setMessage("Log out?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								FirebaseBackend.auth.getInstance().signOut();
								startActivity(new Intent(getContext(), MainActivity.class));
								getActivity().finish();
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						}).show();

			}
		});

	}
}
