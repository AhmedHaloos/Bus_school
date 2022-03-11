package com.eng.ashm.buschool;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.MapLayoutBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int LOCATION_REQUEST_CODE = 25;
    public static final String TAG = "debug";

    private GoogleMap googleMap = null;
    private MapLayoutBinding binding;
    private boolean isLocPermGranted = false;
    private Location lastKnownLocation;
    private LatLng selectedLocation = null;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LatLng defaultLocation = new LatLng(12.544855,10.654894);
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapLayoutBinding.inflate(getLayoutInflater());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_container);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        getLocationPermission();
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        setContentView(binding.getRoot());
    }
    /**
     * *********************** Location Permission **************************************
     */
    private void getLocationPermission(){
        boolean finePerm = checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
        boolean coarsePerm = checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
        if ( finePerm || coarsePerm ){
            isLocPermGranted = true;
        }
        else {
            isLocPermGranted = false;
           // showPermissionDialog();
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }


    /**
     * Alert dialog to inform the user that he needs to allow location permission
     */
    private void showPermissionDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("يجب اعطاء الاذونات لاستخدام الخريطة")
                .setNegativeButton("cancel", (dialog1,
                                              which) -> {
                    dialog1.cancel();
                    finish();
                })
                .setPositiveButton("settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + "com.eng.ashm.buschool"));
                            startActivity(intent);
                        } catch ( ActivityNotFoundException e ) {
                            e.printStackTrace();
                            //Open the generic Apps page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            startActivity(intent);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PERMISSION_GRANTED
                    || grantResults[1] == PERMISSION_GRANTED) ) {
                isLocPermGranted = true;
            } else {
                isLocPermGranted = false;
                showPermissionDialog();
            }
        }
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * ********************************* handle map actions ************************************************
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("الGPS حقك مغلق .. هل تريد تفعيله؟")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(markerClickListener);
        updateLocationUI();
        getDeviceLocation();
    }
    /**
     *listener for long click on map
     */
    GoogleMap.OnMapLongClickListener longClickListener = latLng -> {
        googleMap.addMarker(
                new MarkerOptions()
                .position(latLng)
                .visible(true)
        );
        addLocConfirmation(latLng);
    };
    /**
     *
     */
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(@NonNull Marker marker) {
            marker.remove();
            return true;
        }
    };
    /**
     * initialize the map with the given point
     * @param point
     */
    private void initMapUI(LatLng point){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(point, 15, 1, 2));
        googleMap.animateCamera(cameraUpdate);
        googleMap.setOnMapLongClickListener(longClickListener);
    }
    /**
     * update the map to show current location indicator
     */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (isLocPermGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    /**
     * get the last known location on the device using fused location provider client
     */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void getDeviceLocation() {
        try {
            if (isLocPermGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this,onCompleteListener);
            }
            else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * On complete request location from fused location provider client
     */
    OnCompleteListener<Location> onCompleteListener = task -> {
        if (task.isSuccessful()) {
            // Set the map's camera position to the current location of the device.
            lastKnownLocation = task.getResult();
            if (lastKnownLocation != null) {
                LatLng point = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                initMapUI(point);
            }
        }
        else {
            Log.d(TAG, "Current location is null. Using defaults.");
            Log.e(TAG, "Exception: %s", task.getException());
            googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(defaultLocation, 10));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    };

    /**
     *  show dialog to confirm from the user that he needs to add the student location
     */
    private void addLocConfirmation(LatLng point){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("هل تريد اضافة العنوان ؟")
                .setNegativeButton("لا", (dialog1, which) -> {
                    dialog1.cancel();
                })
                .setPositiveButton("نعم", (dialog12, which) -> {
                    try {
                        selectedLocation = point;
                        Intent intent = new Intent();
                        intent.putExtra(Student.COLLECTION, point);
                        setResult(RESULT_OK, intent);
                    } catch ( ActivityNotFoundException e ) {
                        e.printStackTrace();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onCompleteListener = null;
        binding = null;
        googleMap = null;
    }


}
