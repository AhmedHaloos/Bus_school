package com.eng.ashm.buschool.ui.activity.driver;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.databinding.DriverActivityBinding;
import com.eng.ashm.buschool.ui.fragment.driver.DisplayStudentsFragment;
import com.eng.ashm.buschool.ui.fragment.driver.DriverTripFragment;
import com.google.android.material.navigation.NavigationBarView;

public class DriverMainActivity extends AppCompatActivity {
    DriverActivityBinding driverBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverBinding = DriverActivityBinding.inflate(getLayoutInflater());
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

    }
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(driverBinding.driverFragmentContainer.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }
}
