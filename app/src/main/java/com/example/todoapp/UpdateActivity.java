package com.example.todoapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.todoapp.models.Todo;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class UpdateActivity extends AppCompatActivity {

//    ShareUpdateDataInterface shareUpdateDataInterface;

    EditText titleEditText, descriptionEditText, locationEditText;
    Button updateButton, cancelButton;
    ImageButton getLocationButton;
    LocationManager locationManager;
    String latitude, longitude;

    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        titleEditText = findViewById(R.id.textFieldTitleUpdate);
        descriptionEditText = findViewById(R.id.textFieldDescriptionUpdate);
        locationEditText = findViewById(R.id.textFieldLocationUpdate);
        updateButton = findViewById(R.id.buttonUpdate);
        cancelButton = findViewById(R.id.buttonCancelUpdate);
        getLocationButton = findViewById(R.id.buttonGetLocationUpdate);

        ActivityCompat.requestPermissions(this,new
                String[]{ACCESS_FINE_LOCATION}, 1);

        final String getId = getIntent().getExtras().getString("id");

        Realm realm = Realm.getDefaultInstance();

        todo = realm.where(Todo.class).equalTo("id", getId).findFirst();

        titleEditText.setText(todo.getTitle());
        descriptionEditText.setText(todo.getDescription());
        locationEditText.setText(todo.getLocation());
        locationManager = (LocationManager)
              getSystemService(Context.LOCATION_SERVICE);

        getLocationButton.setOnClickListener(view -> {
            if
            (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
            }
        });

        cancelButton.setOnClickListener(view1 -> {

            titleEditText.setText("");
            descriptionEditText.setText("");

            finish();

        });

        updateButton.setOnClickListener(view1 -> {
            realm.executeTransactionAsync(
                    realm1 -> {
                        Todo todo = realm1.where(Todo.class).equalTo("id", getId).findFirst();

                        todo.setTitle(titleEditText.getText().toString());
                        todo.setDescription(descriptionEditText.getText().toString());
                        todo.setLocation(locationEditText.getText().toString());

                        finish();
                    },
                    () -> Toast.makeText(UpdateActivity.this, "Succeced", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_LONG).show()
            );


        });


    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new
                AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", (dialog, which) -> startActivity(new
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        Geocoder geocoder = new Geocoder(this,
                Locale.getDefault());
        if
        (ActivityCompat.checkSelfPermission(UpdateActivity.this,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(UpdateActivity.this,
                ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]
                            {ACCESS_FINE_LOCATION},
                    1);
        } else {
            Location LocationGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(lat, longi, 1);
                    String AddressLine =
                            addresses.get(0).getAddressLine(0);
                    locationEditText.setText(AddressLine);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

