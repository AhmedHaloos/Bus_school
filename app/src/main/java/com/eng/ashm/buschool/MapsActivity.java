package com.eng.ashm.buschool;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.MapLayoutBinding;
import com.eng.ashm.buschool.ui.activity.management.ManagementMainActivity;
import com.eng.ashm.buschool.ui.viewmodel.LocationViewModel;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "debug";
    public static final String SOURCE_OF_MAP = "source of map";
    public static final String CUSTOM_PROVIDER = "custom provider";
    // private constants to mapsActivity
    private static final String MAP_SHARED_PREFERENCES = "shared preferences";
    private static final String STOP_POINTS_KEY = "stop points";

    private GoogleMap googleMap = null;
    private LocationSource.OnLocationChangedListener listener = null;
    private Trip trip = null;
    private Intent receivedIntent = null;
    private MapLayoutBinding binding;
    private Location lastKnownLocation;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationViewModel locationViewModel = null;
    private String mapSource = "";
    private LatLng schoolCoordinates = null;
    private int stopPoints =  - 1;
    private TripViewModel tripViewModel = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_container);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        receivedIntent = getIntent();
        mapSource = receivedIntent.getStringExtra(SOURCE_OF_MAP);
        schoolCoordinates = new LatLng(30.5648451, 32.2606481 );
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getLastUpdatedLocation();
        locationViewModel.retrievedLocation.observe(this, locationUpdateObserver);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.tripStartedResult.observe(this, tripStartedObserver);
        // start trip for driver
        if(mapSource.equals(Driver.COLLECTION)) {
            trip = (Trip) receivedIntent.getParcelableExtra(Trip.COLLECTION);
            if (trip != null) {
                stopPoints = trip.tripStudents.size();
                tripViewModel.startTrip(trip);
            }
        }
    }
    /*********************** handle driver maps ************************/
    /**
     * Location update observer
     */
    Observer<String> locationUpdateObserver = latLng ->{
        LatLng point = Student.getLatLng(latLng);
        Location location = createLocation(point);
        if (listener != null)
        listener.onLocationChanged(location);
    };
    Observer<Boolean> tripStartedObserver = isStarted -> {
        if (isStarted)
            Toast.makeText(this, "بدأت الرحلة", Toast.LENGTH_SHORT).show();
        tripViewModel.tripStartedResult.removeObservers(this);
    };
    /**
     ********************************** handle map actions **********************************
     */
    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (googleMap == null) {
            Toast.makeText(this, "الخريطة غير متاحة حاليا", Toast.LENGTH_SHORT).show();
            return;
        } else {
            googleMap.setOnMarkerClickListener(markerClickListener);
            if (receivedIntent != null){
                if (mapSource != null)
                updateLocationUI();
            if (mapSource != null && mapSource.equals(Student.COLLECTION)) {
                getDeviceLocation();
                googleMap.setOnMapLongClickListener(longClickListener);
                LatLng point = null;
                if (lastKnownLocation != null)
                    point = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    else
                        point = schoolCoordinates;
                    initMapUI(point);

            }
            else {
                initMapUI(schoolCoordinates);
            }
        }
    }
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
    GoogleMap.OnMarkerClickListener markerClickListener = marker -> {
        if (mapSource.equals(Driver.COLLECTION)){
            /**
             * show info window for the driver with dialog to
             * enable him to inform that he reached the point
             */
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setMessage("هل وصلت الى عنوان الطالب ؟")
                    .setNegativeButton("لا", (dialog1, which) -> dialog1.dismiss())
                    .setPositiveButton("نعم", (dialog12, which) -> {
                        getSharedPreferences(MAP_SHARED_PREFERENCES, MODE_PRIVATE).edit().putInt(STOP_POINTS_KEY, stopPoints);
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        if (--stopPoints > 0)
                        {
                            Toast.makeText(this, "تبقى لك " +stopPoints +"  طلبة قبل انتهاء الرحلة" , Toast.LENGTH_SHORT).show();
                            getSharedPreferences(MAP_SHARED_PREFERENCES, MODE_PRIVATE).edit().putInt(STOP_POINTS_KEY, stopPoints);
                        }
                        else {
                            tripViewModel.stopTrip(trip);
                            tripViewModel.tripStoppedResult.observe(this, tripStartedObserver);
                            Toast.makeText(this, "انتهت الرحلة", Toast.LENGTH_SHORT).show();
                            tripViewModel.tripStoppedResult.removeObservers(this);
                        }
                    })
                    .setTitle("محطة وصول")
                    .create();
            dialog.show();
        }
        else if(mapSource.equals(Student.COLLECTION))
            marker.remove();
        else {}
        return true;
    };
    /**
     * initialize the map with the given point
     * @param point
     */
    private void initMapUI(LatLng point){
        if(googleMap == null){
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(point, 15, 1, 2));
        googleMap.animateCamera(cameraUpdate);
        if (mapSource != null && mapSource.equals(Student.COLLECTION))
        googleMap.setOnMapLongClickListener(longClickListener);
            addStudentLocMarkers();
    }
    /**
     * add markers on the maps for the students locations
     */
    private void addStudentLocMarkers(){
        if (receivedIntent != null) {
            Trip trip = (Trip)receivedIntent.getSerializableExtra(Trip.COLLECTION);
            if (trip != null){
                ArrayList<Student> students = trip.tripStudents;
                if (googleMap != null){
                    Marker marker = null;
            for (Student student : students) {
                 marker = googleMap.addMarker(
                        new MarkerOptions()
                        .position(Student.getLatLng(student.location))
                        .visible(true)
                        .title("'طالب : "+ student.name)
                        .snippet("السنة الدراسية : " + student.yearOFStudy)
                );
                 marker.setTag(student);
            }
                }
                else return;
            }
            else {
                if (!mapSource.equals(Student.COLLECTION))
                Toast.makeText(this, "الرحلة غير متاحة", Toast.LENGTH_SHORT).show();
                return ;
            }
        }

    }
    /**
     * update the map to show current location indicator
     */
    @RequiresPermission(allOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //location source
            if (mapSource != null){
                if(mapSource.equals(Student.COLLECTION)) {
                    googleMap.setLocationSource(null);
                }
                else if (mapSource.equals(ManagementMainActivity.MANAG_COLLECTION )|| mapSource.equals(Parent.COLLECTION)){
                    googleMap.setLocationSource(new CustomLocationSource());
                }
                else if (mapSource.equals(Driver.COLLECTION)){
                    googleMap.setLocationSource(null);
                }
        }
            else
                Toast.makeText(this, "خطا فى تحديد الخريطة", Toast.LENGTH_SHORT).show();
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
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this,onDeviceLocationAvailable);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    /**
     * On complete request location from fused location provider client
     */
    OnCompleteListener<Location> onDeviceLocationAvailable = task -> {
        if (task.isSuccessful()) {
            // Set the map's camera position to the current location of the device.
            lastKnownLocation = task.getResult();
        }
        else {
            Toast.makeText(getApplicationContext(), " الموقع الحالى غير متاح", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent();
                        intent.putExtra(Student.COLLECTION, point);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch ( ActivityNotFoundException e ) {
                        e.printStackTrace();
                    }
                })
                .create();
        dialog.show();
    }
    /**
     *
     * @param point
     * @return
     */
    private Location createLocation(LatLng point){
        Location location = new Location(CUSTOM_PROVIDER);
        location.setLatitude(point.latitude);
        location.setLongitude(point.longitude);
        location.setTime(Calendar.getInstance().getTimeInMillis());
        location.setBearing(90);
        location.setAccuracy(100);
        return location;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDeviceLocationAvailable = null;
        binding = null;
        googleMap.clear();
        googleMap = null;
    }


    /**
     * location source class
     */
    class CustomLocationSource implements LocationSource{

        public CustomLocationSource(){}
        @Override
        public void activate(@NonNull OnLocationChangedListener onLocationChangedListener) {
            if (onLocationChangedListener != null){
                listener = onLocationChangedListener;
            }
        }
        @Override
        public void deactivate() {
            listener = null;
        }

    }
}
