package com.eng.ashm.buschool.ui.fragment.management;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static com.eng.ashm.buschool.GlobalConst.REQUEST_PROFILE_KEY;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eng.ashm.buschool.MapsActivity;
import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.ItemLongClickListener;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ManagementTripFragmentBinding;
import com.eng.ashm.buschool.databinding.NewStudentActivityBinding;
import com.eng.ashm.buschool.ui.activity.management.CreateTripActivity;
import com.eng.ashm.buschool.ui.activity.management.ManagementMainActivity;
import com.eng.ashm.buschool.ui.activity.profile.TripProfileActivity;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.viewmodel.StudentViewModel;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class ManagementTripFragment extends Fragment {


    public static final int LOCATION_REQUEST_CODE = 25;
    public static final String PERMISSION_REQUEST_STATUS = "permission request";
    public static final String IS_PERMISSION_REQUESTED = "is permission requested";

    private Boolean isPermissionRequested = false;
    private boolean isLocPermGranted = false;
    private Student mStudent = null;
    private LocationManager locationManager = null;
    //launcher
    ActivityResultLauncher<String> locationLauncher = null;
    ManagementTripFragmentBinding binding;
    TripViewModel tripViewModel;
    TripAdapter tripAdapter = null;
    private ActivityResultLauncher<Object> launcher = null;

    public ManagementTripFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(contract, resultCallback);
        locationManager = getActivity().getSystemService(LocationManager.class);
        initAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = ManagementTripFragmentBinding.inflate(inflater, container, false);

      initViewModel();
      initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    // request list observer
    Observer<List<? extends IDataModel>>   observer = trips -> {
        if ((trips != null) && (!trips.isEmpty())){
            if (!trips.get(0).getClass().equals(Trip.class))
                return;
            tripAdapter.updateTripList((ArrayList<Trip>) trips);
        }
    };
    /**
     *
     */
    private void initAdapter(){
        if (tripAdapter == null){
            tripAdapter = new TripAdapter();
            tripAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
    ItemLongClickListener itemLongClickListener = new ItemLongClickListener() {
        @Override
        public void onItemLongClick(IDataModel dataModel) {

        }
    };
    /**
     *onItemClickListener
     */
    OnItemClickListener<Trip> onItemClickListener = new OnItemClickListener<Trip>() {
        @Override
        public void onItemClicked(Trip trip) {
            Intent intent = new Intent(getActivity(), TripProfileActivity.class);
            intent.putExtra(Trip.COLLECTION, (Parcelable) trip );
            intent.putExtra(REQUEST_PROFILE_KEY, ManagementMainActivity.MANAG_COLLECTION);
            startActivity(intent);
        }

        @Override
        public void onItemBtnClicked(Trip trip) {
            getLocationPermission();
            if (isLocPermGranted){
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra(MapsActivity.SOURCE_OF_MAP,ManagementMainActivity.MANAG_COLLECTION);
            intent.putExtra(Trip.COLLECTION, (Parcelable) trip);
            startActivity(intent);
            }
        }
    };
    // initView model
    private void initViewModel(){
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getAllTrips();
        tripViewModel.requestTripListResult.observe(getViewLifecycleOwner(), observer);
    }
    // init recyclerview
    private void initRecyclerView(){
        binding.manageStudentListRv.setAdapter(tripAdapter);
        binding.manageStudentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.manageAddTrip.setOnClickListener(v->{
            startActivity(new Intent(getContext(), CreateTripActivity.class));
        });
    }
    // start activity contract and result callback
    ActivityResultContract<Object , Trip> contract = new ActivityResultContract<Object, Trip>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(getActivity(), CreateTripActivity.class);
            return intent;
        }

        @Override
        public Trip parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return (Trip) intent.getParcelableExtra(Trip.COLLECTION);
            }
            return null;
        }
    };

    ActivityResultCallback<Trip> resultCallback = trip -> {
        if (trip != null)
        tripAdapter.addTrip(trip);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tripAdapter = null;
        getActivity().getViewModelStore().clear();
    }
    private void getLocationPermission(){
        int finePerm = getActivity().checkSelfPermission(ACCESS_FINE_LOCATION) ;
        int coarsePerm = getActivity().checkSelfPermission(ACCESS_COARSE_LOCATION);
        if ( finePerm == PERMISSION_GRANTED || coarsePerm == PERMISSION_GRANTED ){
            isLocPermGranted = true;
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                showAlertMessageNoGps();
        }
        else if (finePerm == PackageManager.PERMISSION_DENIED || coarsePerm == PackageManager.PERMISSION_DENIED) {
            isLocPermGranted = false;
            if (!isPermissionRequested)
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            else
                showPermissionDialog();
        }
    }
    /**
     * Alert dialog to inform the user that he needs to allow location permission
     */
    private void showPermissionDialog(){
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setMessage("يجب اعطاء الاذونات لاستخدام الخريطة")
                .setNegativeButton("cancel", (dialog1,
                                              which) -> {
                    dialog1.cancel();
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
    /**
     *
     */
    private void showAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("الGPS مغلق .. هل تريد تفعيله؟")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PERMISSION_GRANTED
                    || grantResults[1] == PERMISSION_GRANTED)) {
                isLocPermGranted = true;
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    showAlertMessageNoGps();
                else startActivity(new Intent(getActivity(), MapsActivity.class));
            }
            else {
                isLocPermGranted = false;
                if (isPermissionRequested){
                    showPermissionDialog();
                }
                else {
                    SharedPreferences preferences = getActivity().getSharedPreferences(PERMISSION_REQUEST_STATUS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(IS_PERMISSION_REQUESTED, true);
                    editor.apply();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}