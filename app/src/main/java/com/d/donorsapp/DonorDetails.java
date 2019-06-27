package com.d.donorsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonorDetails extends AppCompatActivity {

    private static final String TAG = "DonorDetails";
    EditText name, pass, repass, addr;
    String phonenumber, donorname, address, password;
    Address addresss;
    Double latitude, longitude;
    String lat, lng;
//    private StorageReference mStorage;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore mFirestore;
//    AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);
        phonenumber = getIntent().getStringExtra("phonenumber");
        name = findViewById(R.id.name);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        addr = findViewById(R.id.addr);
//        mStorage = FirebaseStorage.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
//        mFirestore = FirebaseFirestore.getInstance();
//        String apiKey = getString(R.string.google_maps_key);
//        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//            getPlace();
//        }
        System.out.println("In donor details");
        getaddress();
    }

//    public void getPlace() {
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//    }

    public void getaddress() {
        addr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    System.out.println("heard action" + actionId);
                    geoLocate();
                }
                return false;
            }
        });
    }

    public void createaccount(View view) {
        if (name.getText().toString() != null) {
            if (addr.getText().toString() != null) {
                if (pass.getText().toString().equals(repass.getText().toString())) {
                    donorname = name.getText().toString();
                    address = addr.getText().toString();
                    password = pass.getText().toString();
                    lat = String.valueOf(latitude);
                    lng = String.valueOf(longitude);

//                    mAuth.createUserWithEmailAndPassword(phonenumber, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()) {
//                                String user_id = mAuth.getCurrentUser().getUid();
//                                Map<String, Object> userMap = new HashMap<>();
//                                userMap.put("name", donorname);
//                                userMap.put("address", address);
//                                userMap.put("latitude", lat);
//                                userMap.put("longitude", lng);
//                                mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
//                                        intent.putExtra("phonenumber", phonenumber);
//                                        intent.putExtra("donorname", donorname);
//                                        intent.putExtra("address", address);
//                                        intent.putExtra("password", password);
//                                        intent.putExtra("location", addresss.toString());
//                                        intent.putExtra("latitude", lat);
//                                        intent.putExtra("longitude", lng);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                            }
//                            else {
//                                Toast.makeText(DonorDetails.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                    BackgroundWorker1 backgroundWorker2 = new BackgroundWorker1(getApplicationContext());
                    backgroundWorker2.execute("adddonordetails",phonenumber, donorname, address, password, lat, lng);
                    SharedPreferences settings = getSharedPreferences(MainActivity.prefname, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    //Set "hasloggedin" to true
                    editor.putBoolean("hasloggedin", true);
                    editor.putString("phonenumber", phonenumber);
                    editor.putString("donorname", donorname);
                    editor.putString("address", address);
                    editor.putString("password", password);
                    editor.putString("latitude", lat);
                    editor.putString("longitude", lat);
                    //Commit the edits
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
                    intent.putExtra("key", "0");
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("donorname", donorname);
                    intent.putExtra("address", address);
                    intent.putExtra("password", password);
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lng);
                    startActivity(intent);
                    finish();
                }
                else {
                    repass.setError("Password not matching");
                }
            }
            else {
                addr.setError("Please enter address");
            }
        }
        else {
            name.setError("Please enter name");
        }
    }

    private void geoLocate() {
        System.out.println("From geo");
        String addre = addr.getText().toString();
        Geocoder geocoder = new Geocoder(DonorDetails.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(addre, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0) {
            addresss = list.get(0);
            latitude = addresss.getLatitude();
            longitude = addresss.getLongitude();
            System.out.println("in geo lat "+latitude+"long"+longitude);
//            String[] lat = addresss.split(",latitude=");
//            String[] lati = lat[1].split(",");
//            latitude = lati[0];
//            System.out.println("Latitude " + latitude);
//            String[] lng = addr.split(",longitude=");
//            String[] longi = lng[1].split(",");
//            longitude = longi[0];
//            System.out.println("Longitude " + longitude);
//            System.out.println("Address " + addresss.toString());
        }
    }
}
