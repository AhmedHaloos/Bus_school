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

import com.eng.ashm.buschool.databinding.DriverTripActivityBinding;
import com.eng.ashm.buschool.databinding.ManagementTripActivityBinding;
import com.eng.ashm.buschool.databinding.ParentTripActivityBinding;

public class TripProfileActivity  extends AppCompatActivity {

    DriverTripActivityBinding driverBinding;
    ManagementTripActivityBinding managementBinding;
    ParentTripActivityBinding parentBinding;
    private int requestingDiv = ERROR_REQUEST;
    View currentView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverBinding = DriverTripActivityBinding.inflate(getLayoutInflater());
        managementBinding = ManagementTripActivityBinding.inflate(getLayoutInflater());
        parentBinding = ParentTripActivityBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        requestingDiv = intent.getIntExtra(LAUNCH_TRIP_ACTIVITY_REQUEST , ERROR_REQUEST);
        switch (requestingDiv){
            case DRIVER_TRIP_ACTIVITY_REQUEST:
                currentView = driverBinding.getRoot();
                break;
            case PARENT_TRIP_ACTIVITY_REQUEST:
                currentView = parentBinding.getRoot();
                break;
            case MANAGEMENT_TRIP_ACTIVITY_REQUEST:
                currentView = managementBinding.getRoot();
                break;
            default:
                showDialog("no data to display on the activity");
                finish();
        }
        setContentView(currentView);
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
}
