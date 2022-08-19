package com.example.todoapp.adaptors;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.Todo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    EditText titleEditText, descriptionEditText, locationEditText;
    LocationManager locationManager;
    Button addButton, cancelButton;
    ImageButton getLocationButton;
    String latitude, longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.todo_dialog, null, false);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();

        titleEditText = view.findViewById(R.id.textFieldTitle);
        descriptionEditText = view.findViewById(R.id.textFieldDescription);
        locationEditText = view.findViewById(R.id.textFieldLocation);

        addButton = view.findViewById(R.id.buttonAdd);
        cancelButton = view.findViewById(R.id.buttonCancel);

        getLocationButton = view.findViewById(R.id.buttonGetLocation);

        locationManager = (LocationManager)
                requireContext().getSystemService(Context.LOCATION_SERVICE);

        getLocationButton.setOnClickListener(view1 -> {

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
            locationEditText.setText("");

            dismiss();

        });

        addButton.setOnClickListener(view1 -> {
            Random random = new Random();

            realm.beginTransaction();
            Todo todo = realm.createObject(Todo.class);

            todo.setID(String.valueOf(random.nextInt(1000)));
            todo.setTitle(titleEditText.getText().toString());
            todo.setDescription(descriptionEditText.getText().toString());
            todo.setLocation(locationEditText.getText().toString());

            realm.commitTransaction();

            titleEditText.setText("");
            descriptionEditText.setText("");
            locationEditText.setText("");

            dismiss();

        });

        return view;
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new
                AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(" Yes", (dialog, which) -> startActivity(new
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        Geocoder geocoder = new Geocoder(getContext(),
                Locale.getDefault());
        if
        (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new
                            String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
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
                Toast.makeText(getContext(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
