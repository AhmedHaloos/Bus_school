package com.eng.ashm.buschool.test;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;

import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.MapsActivity;
import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.FirestoreDataSource;
import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.Result;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ParentProfileActivityBinding;
import com.eng.ashm.buschool.databinding.TestActivityBinding;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.databinding.TripProfileLayoutBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.fragment.parent.ParentTripFragment;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.sql.DataSource;

import io.grpc.internal.ManagedChannelImplBuilder;

public class TestActivity extends AppCompatActivity {


    FirestroreRepository repository;
    private  FirestoreDataSource dataSource;
    private MainRepository mainRepository;
    TestActivityBinding binding;
    private static Context context = null;
    FusedLocationProviderClient fusedLocationProvider = null;
    @RequiresApi(api = Build.VERSION_CODES.S)
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION })
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        binding = TestActivityBinding.inflate(getLayoutInflater());
       binding.testData.setOnClickListener((v)->{
           startActivity(new Intent(TestActivity.this, MapsActivity.class));
       });
        // maps api
        LocationManager locationManager = getSystemService(LocationManager.class);
        fusedLocationProvider = new FusedLocationProviderClient(this);
        LocationRequest request = new LocationRequest();
        request.setInterval(10*1000);
        requestLocationPermission();
        fusedLocationProvider.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

            }
        }, getMainLooper());
        setContentView(binding.getRoot());
    }

    /**
     * ***************************************************
     * Location manager handling
     * ***************************************************
     */
    private void requestLocationPermission(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED){

        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION }, 25);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 25){
            if (grantResults.length > 0 ){
                if (grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED ){

                }
                else {

                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /************************************************************************************/
    private  Observer o1 = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            Toast.makeText(context, "LIST >>  : " +arg.getClass().getName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "LIST >>  : " +dataSource.resultList.countObservers(), Toast.LENGTH_SHORT).show();
        }
    };
    private  Observer o2 = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            Toast.makeText(context, "ADD >>  : " + arg.getClass().getName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "ADD >>  : " +dataSource.resultList.countObservers(), Toast.LENGTH_SHORT).show();
        }
    };
    ActivityResultContract<String, Uri> contract = new ActivityResultContract<String, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(input);
            return intent;
        }

        @Override
        public Uri parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
               return intent.getData();
            }
            return null;
        }
    };
    ActivityResultCallback<Uri> resultCallback = new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
           // newDriverActivityBinding.newDriverPic.setImageURI(result);
        }
    };
    /**
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment, @IdRes int fragmentContainerID, View v, String transName){
        Transition transition = new Fade() ;
        fragment.setEnterTransition(transition);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContainerID,fragment )
                   .setReorderingAllowed(true)
                 //  .addSharedElement(v, transName)
                   .commit();

    }
    private Drawable pickImage(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
return null;
    }
    public static final int EXPAND = 1;
    public static final int COLLAPSE = 2;
    /**
     *
     * @param view
     * @param transition
     * @param height
     */
    private void expandAnimation(View view, int transition, final int height){
    }
    /**
     *
     * @param v
     */
    private void zoomIn(View v){
        if (v.getAnimation() != null){
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }
        v.animate().scaleX(2).scaleY(2).start();
    }
    /**
     *
     * @param v
     */
    private void zoomOut(View v){
        if (v.getAnimation() != null)
        {
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }
        v.animate().scaleX(0.5f).scaleY(0.5f).start();
    }
    /**
     *
     */
    private void transit(){
        /*Scene scene = Scene.getSceneForLayout(binding.getRoot(), R.layout.trip_profile_layout,this);
        Transition transition  =new Slide();
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        transition.setDuration(2000);
        TransitionManager.go(scene, transition);*/
    }
    /**
     *
     * @return
     */
    private ArrayList<Trip> createTestTripList() {
        ArrayList<Trip> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(createTrip(i));
        }
        return list;
    }

    /**
     *
     * @param i
     * @return
     */
    private Trip createTrip(int i) {
        Trip t = new Trip();
        t.tripNum = i;
        t.tripDate = "date : " + i;
        t.startTime = "بداية توقيت الرحلة : " + i;
        t.endTime = "نهاية توقيت الرحلة : " + i + 1;
        t.tripState = (i % 2 == 0) ? "مستمرة" : "انتهت";
        //car data
        t.bus.carNum = "رقم السيارة = " + i;
        t.bus.kind = "نوع السيارة : " + i;
        // driver data
        t.tripDriver.name = (i % 2 == 0) ? "امانى" : "امنية";
        t.tripDriver.phone = "0514444411";
        return t;
    }
}
