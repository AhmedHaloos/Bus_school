package com.eng.ashm.buschool.ui.activity.driver;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eng.ashm.buschool.databinding.DriverTripActivityBinding;

/**
 * trip activity display trips requested by the management as a list fragment
 * and then enable the driver to open a trip and accept the trip: path, students,
 * and bus.
 * required fragments are: TripListFragment, ParentTripFragment, AcceptCarFragment
 */
public class TripActivity extends AppCompatActivity {
    DriverTripActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DriverTripActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    private void showFragment(FragmentManager fragmentManager,@IdRes int fragmentContainer, Fragment fragment){

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContainer, fragment)
                .addToBackStack(fragment.toString())
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
