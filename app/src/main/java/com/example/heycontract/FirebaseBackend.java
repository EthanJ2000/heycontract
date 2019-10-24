package com.example.heycontract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseBackend {
	public static FirebaseAuth auth;
	public static DatabaseReference dbRef;
	public static FirebaseStorage storage;

	public void initAuth() {
		auth = FirebaseAuth.getInstance();
	}

	public void initDB() {
		dbRef = FirebaseDatabase.getInstance().getReference();
	}

	public void initStorage() {
		storage = FirebaseStorage.getInstance();
	}

}
