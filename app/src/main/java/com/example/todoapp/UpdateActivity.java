package com.example.todoapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.todoapp.models.Todo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class UpdateActivity extends AppCompatActivity {

//    ShareUpdateDataInterface shareUpdateDataInterface;

    EditText namaEditText, alamatEditText, locationEditText, noTelpEditText;
    Button updateButton, cancelButton;
    ImageButton getLocationButton, getImageButton;
    LocationManager locationManager;
    String latitude, longitude;
    RadioButton radioButton1, radioButton2;
    Todo todo;

    RadioGroup jenisKelaminUpdate;
    String textRadioUpdate;

    ImageView viewImage;
    final int kodeGalery = 100;
    Uri imageUri;
    byte[] blobImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        namaEditText = findViewById(R.id.textFieldNamaUpdate);
        alamatEditText = findViewById(R.id.textFieldAlamatUpdate);
        locationEditText = findViewById(R.id.textFieldLocationUpdate);
        noTelpEditText = findViewById(R.id.textFieldNoHpUpdate);
        updateButton = findViewById(R.id.buttonUpdate);
        cancelButton = findViewById(R.id.buttonCancelUpdate);
        getLocationButton = findViewById(R.id.buttonGetLocationUpdate);
        viewImage = findViewById(R.id.imagePreviewUpdate);
        getImageButton = findViewById(R.id.buttonImageUpdate);
        jenisKelaminUpdate = findViewById(R.id.radioGroupUpdate);
        radioButton1 = findViewById(R.id.radio_button_1_Update);
        radioButton2 = findViewById(R.id.radio_button_2_Update);

        ActivityCompat.requestPermissions(this, new
                String[]{ACCESS_FINE_LOCATION}, 1);

        final String getId = getIntent().getExtras().getString("id");

        Realm realm = Realm.getDefaultInstance();

        todo = realm.where(Todo.class).equalTo("id", getId).findFirst();

        namaEditText.setText(todo.getNama());
        alamatEditText.setText(todo.getAlamat());
        noTelpEditText.setText(todo.getPhone());
        locationEditText.setText(todo.getLocation());

        blobImage = todo.getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(blobImage);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

        viewImage.setImageBitmap(bitmap);
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

        if (todo.getSex().equals("laki-Laki")) {
            jenisKelaminUpdate.check(R.id.radio_button_1_Update);
            textRadioUpdate = "laki-Laki";
        } else {
            jenisKelaminUpdate.check(R.id.radio_button_2_Update);
            textRadioUpdate = "Perempuan";

        }

        jenisKelaminUpdate.setOnCheckedChangeListener((radioGroup, id) -> {


            switch (id) {
                case R.id.radio_button_1_Update:
                    textRadioUpdate = "laki-Laki";
                    break;
                case R.id.radio_button_2_Update:
                    textRadioUpdate = "Perempuan";
                    break;

            }
        });

        getImageButton.setOnClickListener(view12 ->

        {
            Intent intentGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGalery, kodeGalery);
        });

        cancelButton.setOnClickListener(view1 -> {

            namaEditText.setText("");
            alamatEditText.setText("");

            finish();

        });

        updateButton.setOnClickListener(view1 -> {
            realm.executeTransactionAsync(
                    realm1 -> {
                        Todo todo = realm1.where(Todo.class).equalTo("id", getId).findFirst();

                        todo.setNama(namaEditText.getText().toString());
                        todo.setAlamat(alamatEditText.getText().toString());
                        todo.setPhone(noTelpEditText.getText().toString());
                        todo.setLocation(locationEditText.getText().toString());
                        todo.setSex(textRadioUpdate);
                        todo.setImage(blobImage);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kodeGalery && resultCode == RESULT_OK) {
            imageUri = data.getData();
            viewImage.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                blobImage = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

