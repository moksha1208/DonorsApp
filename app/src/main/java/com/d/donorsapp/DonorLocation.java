package com.d.donorsapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.ExecutionException;
import java.util.zip.DeflaterOutputStream;

public class DonorLocation extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DonorLocation";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    String phonenumber, donorname, address, password, latitude , longitude, key, number;
    String ngolatitude, ngolongitude;
    int indexBody, indexAddress;
    String connection = "notConnected";
    String ngonumber, ngoname, result;
    String[] ngolocation;
    float[] results = new float[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_location);
        key = getIntent().getStringExtra("key");
        phonenumber = getIntent().getStringExtra("phonenumber");
        donorname = getIntent().getStringExtra("donorname");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        ngoname = getIntent().getStringExtra("ngoname");
        if(key.equals("1")) {
            number = getIntent().getStringExtra("ngonumber");
        }
        if(key.equals("2")) {
            System.out.println("its connected");
            connection = getIntent().getStringExtra("connection");
            ngonumber = getIntent().getStringExtra("indexAddress");
        }


//        System.out.println("lat "+ latitude + "long" + longitude);
        Log.e("onCreate", "in oncreate method");
        if (isServicesOK()) {
            Log.e("isServiceOK", "Service is up to date");
            getLocationPermission();
            Log.e("map", "setting up map");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(DonorLocation.this);
        }
    }

    public boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DonorLocation.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.e("Service", "Proper version");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DonorLocation.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                System.out.println("Permission granted");
                Log.e("Permission", "Gotpermission");
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("Map is ready");
        if (mLocationPermissionGranted) {
            moveCamera(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), DEFAULT_ZOOM);
//            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
//            getNearbyNGOs();
//            refreshSmsInbox();
            if(connection.equals("isConnected")) {
                do {
                    System.out.println("calling bg");
                    BackgroundWorker1 backgroundWorker4 = new BackgroundWorker1(this);
                    phonenumber = "+91" + phonenumber;
                    backgroundWorker4.execute("outtracking", phonenumber, ngoname);
                    try {
                        result = backgroundWorker4.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("im result" + result);
                    ngolocation = result.split(":");
                    ngolatitude = ngolocation[0];
                    ngolongitude = ngolocation[1];
                    LatLng ngo = new LatLng(Double.valueOf(ngolatitude), Double.valueOf(ngolongitude));
                    MarkerOptions options1 = new MarkerOptions().position(ngo);
                    mMap.addMarker(options1);
                    Location.distanceBetween(Double.valueOf(ngolatitude), Double.valueOf(ngolongitude), Double.valueOf(latitude), Double.valueOf(longitude), results);
                }while (results[0] > 100);
            }

            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText, String messageNumber) {
                    if (messageNumber.equals(number) && (messageText.contains("Acknowledgement") || messageText.contains("Start Tracking"))) {
                        System.out.println("Correct message received");
                        Intent intent = new Intent(getApplicationContext(), PopActivity.class);
                        intent.putExtra("ngoname", ngoname);
                        intent.putExtra("indexaddress", messageNumber);
                        intent.putExtra("indexbody", messageText);
                        intent.putExtra("phonenumber", phonenumber);
                        intent.putExtra("donorname", donorname);
                        intent.putExtra("address", address);
                        intent.putExtra("password", password);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        startActivity(intent);
                    }
                }
            });



//            Bundle b = new Bundle();
//            b = getIntent().getExtras();
//            if(b!=null){
//                System.out.println("got lat lng");
//                ngolatitude = b.getString("latitude_from_ngo", "10");
//                ngolongitude = b.getString("longitude_from_ngo", "10");
//                System.out.println("ngolat "+ngolatitude);
//                System.out.println("ngolng "+ngolongitude);
//                MarkerOptions options1 = new MarkerOptions().position(new LatLng(Double.valueOf(ngolatitude), Double.valueOf(ngolongitude)));
//                mMap.addMarker(options1);
                // and any other data that the other app sent
//            }

        }
    }

    private void getDeviceLocation() {
    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    try {
        if(mLocationPermissionGranted) {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
//                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        moveCamera(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)), DEFAULT_ZOOM);
                        System.out.println("Camera is moved");
                    }
                    else {
                        Toast.makeText(DonorLocation.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }catch (SecurityException e){
        e.printStackTrace();
    }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng);
        mMap.addMarker(options);
    }

    public void getNearbyNGOs() {
//        mMap.clear();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=10000&type=ngo&keyword=to donate food&key=AIzaSyAwdBFqQsxzoGOgbFUQHUXKLHh5HU2ATbU";
//        String url = getUrl(latitude, longitude, Restaurant);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
//        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
//        Toast.makeText(MapsActivity.this,"Nearby Restaurants", Toast.LENGTH_LONG).show();
    }

    public void donate(View view) {
        Intent intent = new Intent(getApplicationContext(), Donate.class);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("donorname", donorname);
        intent.putExtra("address", address);
        intent.putExtra("password", password);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    public void logout(View view) {
        SharedPreferences settings = getSharedPreferences(MainActivity.prefname, 0);
        SharedPreferences.Editor editor = settings.edit();
        //Set "hasloggedin" to true
        editor.putBoolean("hasloggedin", false);
        //Commit the edits
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    public  void refreshSmsInbox() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        indexBody = cursor.getColumnIndex("body");
        indexAddress = cursor.getColumnIndex("address");
        if (indexBody < 0 || !cursor.moveToFirst()) return;
        do {
//                    System.out.println("in do");
//            System.out.println("index" + cursor.getString(indexAddress));
//                    System.out.println("no" + phonenumberlist[0]);
//            for (int i = 0; i < (ngonumber.length); i++) {
//                String a = "+91" + ngonumber[i];
//                System.out.println("noo" + a);
//                System.out.println("index" + cursor.getString(indexAddress));
            if (cursor.getString(indexAddress).equals(number) && ( cursor.getString(indexBody).contains("Acknowlegment") || cursor.getString(indexBody).contains("Start Tracking") )) {
                Intent intent = new Intent(getApplicationContext(), PopActivity.class);
                intent.putExtra("indexaddress", cursor.getString(indexAddress));
                intent.putExtra("indexbody", cursor.getString(indexBody));
                startActivity(intent);
//                    String str = "SMS From: " + cursor.getString(indexAddress) + "\n" + cursor.getString(indexBody) + "\n";
//                    arrayAdapter.add(str);
            }
//            }
        } while (cursor.moveToNext());
    }
}
