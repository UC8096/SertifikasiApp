package com.example.todoapp.adaptors;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.todoapp.R;
import com.example.todoapp.models.Todo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    RadioGroup jenisKelaminRG;
    EditText namaEditText, alamatEditText, noHpEditText, locationEditText;
    LocationManager locationManager;
    Button addButton, cancelButton;
    ImageButton getLocationButton, getImageButton;
    String jenisKelamin, latitude, longitude;

    ImageView viewImage;
    final int kodeGalery = 100;
    Uri imageUri;
    byte[] blobImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.todo_dialog, null, false);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();

        namaEditText = view.findViewById(R.id.textFieldNama);
        alamatEditText = view.findViewById(R.id.textFieldAlamat);
        noHpEditText = view.findViewById(R.id.textFieldNoHp);
        locationEditText = view.findViewById(R.id.textFieldLocation);

        jenisKelaminRG = view.findViewById(R.id.radioGroup);

        addButton = view.findViewById(R.id.buttonAdd);
        cancelButton = view.findViewById(R.id.buttonCancel);

        getImageButton = view.findViewById(R.id.buttonImage);
        getLocationButton = view.findViewById(R.id.buttonGetLocation);
        viewImage = view.findViewById(R.id.imagePreview);

        locationManager = (LocationManager)
                requireContext().getSystemService(Context.LOCATION_SERVICE);

        jenisKelaminRG.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.radio_button_1:
                    jenisKelamin = "laki-Laki";
                    break;
                case R.id.radio_button_2:
                    jenisKelamin = "Perempuan";
                    break;

            }
        });

        getLocationButton.setOnClickListener(view1 -> {
            if
            (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
            }
        });

        getImageButton.setOnClickListener(view12 ->

        {
            Intent intentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGalery, kodeGalery);
        });

        cancelButton.setOnClickListener(view1 ->

        {

            namaEditText.setText("");
            alamatEditText.setText("");
            locationEditText.setText("");
            noHpEditText.setText("");
            jenisKelaminRG.clearCheck();
            dismiss();

        });

        addButton.setOnClickListener(view1 ->

        {
            Random random = new Random();

            realm.beginTransaction();
            Todo todo = realm.createObject(Todo.class);

            todo.setID(String.valueOf(random.nextInt(1000)));
            todo.setNama(namaEditText.getText().toString());
            todo.setAlamat(alamatEditText.getText().toString());
            todo.setPhone(noHpEditText.getText().toString());
            todo.setSex(jenisKelamin);
            todo.setLocation(locationEditText.getText().toString());
            todo.setImage(blobImage);

            realm.commitTransaction();

            namaEditText.setText("");
            alamatEditText.setText("");
            noHpEditText.setText("");
            locationEditText.setText("");
            jenisKelaminRG.clearCheck();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kodeGalery && resultCode == getActivity().RESULT_OK) {
            imageUri = data.getData();
            viewImage.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                blobImage = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
