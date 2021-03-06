 package com.example.hereyouare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
         updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s,int i,Bundle bundle){

            }

            @Override
            public void onProviderEnabled(String s){

            }
            @Override
            public void onProviderDisabled(String s){

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null){

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }
    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    public void updateLocationInfo(Location location){
        TextView latTextView =(TextView) findViewById(R.id.latTextView);
        TextView longTextView = (TextView) findViewById(R.id.longTextView);
        TextView accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
        TextView altitudeTextView = (TextView) findViewById(R.id.altitudeTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);

        latTextView.setText("Latitude: "+ Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: "+ Double.toString(location.getLongitude()));
        accuracyTextView.setText("Accuracy: "+ Double.toString(location.getAccuracy()));
        altitudeTextView.setText("Altitude: "+ Double.toString(location.getAltitude()));

        String address = "Searching your address....";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses!=null && listAddresses.size()>0){
                address="Address:\n";
                if (listAddresses.get(0).getThoroughfare() != null){
                    address+= listAddresses.get(0).getThoroughfare() + "\n";
                }

                if (listAddresses.get(0).getLocality() != null){
                    address+= listAddresses.get(0).getLocality() + " ";
                }

                if (listAddresses.get(0).getPostalCode() != null){
                    address+= listAddresses.get(0).getPostalCode() + "\n";

                }
                if (listAddresses.get(0).getAdminArea() != null){
                    address+= listAddresses.get(0).getAdminArea() + "\n";
                }

                if (listAddresses.get(0).getCountryName() != null){
                    address+= listAddresses.get(0).getCountryName() + "\n";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        addressTextView.setText(address);

    }
}