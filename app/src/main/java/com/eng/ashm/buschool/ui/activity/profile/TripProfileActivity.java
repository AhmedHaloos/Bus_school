package com.eng.ashm.buschool.ui.activity.profile;

import static com.eng.ashm.buschool.GlobalConst.DRIVER_TRIP_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.ERROR_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.LAUNCH_TRIP_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.MANAGEMENT_TRIP_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.PARENT_TRIP_ACTIVITY_REQUEST;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DriverTripActivityBinding;
import com.eng.ashm.buschool.databinding.ManagementTripActivityBinding;
import com.eng.ashm.buschool.databinding.TripProfileLayoutBinding;

public class TripProfileActivity  extends AppCompatActivity {


    TripProfileLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TripProfileLayoutBinding.inflate(getLayoutInflater());
        Trip trip = (Trip)getIntent().getSerializableExtra(Trip.COLLECTION);
        displayTrip(trip);
        setContentView(binding.getRoot());
    }

    /**
     * display the provided fragment in the fragment container view
     * @param fragmentManager
     * @param fragmentContainer
     * @param fragment
     */
    private void showFragment(FragmentManager fragmentManager, @IdRes int fragmentContainer, Fragment fragment){

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContainer, fragment)
                .addToBackStack(fragment.toString())
                .setReorderingAllowed(true)
                .commit();
    }

    /**
     *
     * @param msg
     */
    private void showDialog(String msg){
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {

                })
                .create();
    }

    /**
     *
     * @param trip
     */
    private void displayTrip(Trip trip){
        //driver
        binding.tripProfileDriverName.setText(trip.tripDriver.name);
        binding.tripProfileDriverPhone.setText(trip.tripDriver.phone);
        //car
        binding.tripProfileCarNum.setText(trip.bus.carNum);
        binding.tripProfileCarSeats.setText(trip.bus.noOfSeats);
        //trip data
        binding.tripProfileTripNum.setText(trip.tripNum + "");
        binding.tripProfileTripState.setText(trip.tripState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
