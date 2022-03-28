package com.eng.ashm.buschool.ui.activity.driver;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.DriverActivityBinding;
import com.eng.ashm.buschool.databinding.MapLayoutBinding;
import com.eng.ashm.buschool.ui.fragment.driver.DisplayStudentsFragment;
import com.eng.ashm.buschool.ui.fragment.driver.DriverTripFragment;
import com.eng.ashm.buschool.ui.viewmodel.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Observable;

public class DriverMainActivity extends AppCompatActivity {

    public static final int LOCATION_REQUEST_CODE = 25;
    public static final String TAG = "debug";
    public static final String PERMISSION_REQUEST_STATUS = "permission request";
    public static final String IS_PERMISSION_REQUESTED = "is permission requested";

    private GoogleMap googleMap = null;
    private MapLayoutBinding binding;
    private boolean isLocPermGranted = false;
    private Location lastKnownLocation;
    private LatLng selectedLocation = null;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LatLng defaultLocation = new LatLng(12.544855,10.654894);
    private int count = 0;
    private LocationManager locationManager = null;
    private LocationViewModel locationViewModel = null;


    DriverActivityBinding driverBinding;
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverBinding = DriverActivityBinding.inflate(getLayoutInflater());
        locationManager = getSystemService(LocationManager.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        fusedLocationProviderClient = new FusedLocationProviderClient(getApplicationContext());
        setContentView(driverBinding.getRoot());
        driverBinding.driverBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.driver_trip_nav:
                    showFragment(new DriverTripFragment());
                    break;
                case R.id.driver_student_nav:
                    showFragment(new DisplayStudentsFragment());
                    break;
            }
            return true;
        });
        getLocationPermission();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            new AlertDialog.Builder(this)
                    .setCancelable(true)
            .setTitle("تفعيل ال GPS")
            .setMessage("يجب تفعيل ال GPS لمتابعة الرحلة ")
            .setPositiveButton("تفعيل ال GPS", (dialog, which) -> {
               startActivity(new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            })
                    .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
    }
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(driverBinding.driverFragmentContainer.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }

    /**
     *
     */
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void getLocationPermission(){
        int finePerm = checkSelfPermission(ACCESS_FINE_LOCATION) ;
        int coarsePerm = checkSelfPermission(ACCESS_COARSE_LOCATION);
        SharedPreferences preferences = getSharedPreferences(PERMISSION_REQUEST_STATUS, MODE_PRIVATE);
        Boolean isPermissionRequested = preferences.getBoolean(IS_PERMISSION_REQUESTED, false);
        if ( finePerm == PERMISSION_GRANTED || coarsePerm == PERMISSION_GRANTED ){
            isLocPermGranted = true;
            getDeviceLocation();
        }
        else if (finePerm == PackageManager.PERMISSION_DENIED || coarsePerm == PackageManager.PERMISSION_DENIED)
        {
            isLocPermGranted = false;
            if (!isPermissionRequested)
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PERMISSION_GRANTED
                    || grantResults[1] == PERMISSION_GRANTED)) {
                isLocPermGranted = true;
            } else {
                isLocPermGranted = false;
                SharedPreferences preferences = getSharedPreferences(PERMISSION_REQUEST_STATUS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(IS_PERMISSION_REQUESTED, true);
                editor.apply();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /**
     * get the last known location on the device using fused location provider client
     */
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void getDeviceLocation() {
        try {
            if (isLocPermGranted) {
                LocationRequest request = LocationRequest.create();
                request.setInterval(5 * 1000);
                request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                 fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, null);
            }
            else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // add location observer
    Observer<Boolean> isLocationAddedObserver = isAdded -> {
        if (!isAdded){
            Toast.makeText(getApplicationContext(), "لم يتم تحديد موقع الجهاز", Toast.LENGTH_SHORT).show();
        }
        };

    OnCompleteListener<Location> onCompleteListener = task -> {
        if (task.isSuccessful()) {
            // Set the map's camera position to the current location of the device.
            lastKnownLocation = task.getResult();
            if (lastKnownLocation != null) {
                LatLng point = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                locationViewModel.addLocation(Student.getStringLocation(point));
                locationViewModel.isLocationSent.observe(this, isLocationAddedObserver);
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
     * Location callback
     */
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            lastKnownLocation = locationResult.getLastLocation();
            if (lastKnownLocation != null){
                LatLng point = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                locationViewModel.addLocation(Student.getStringLocation(point));
                locationViewModel.isLocationSent.observe(DriverMainActivity.this, isLocationAddedObserver);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        driverBinding = null;
        //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
