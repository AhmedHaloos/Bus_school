package com.eng.ashm.buschool.ui.activity.management;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.databinding.ManagementActivityBinding;
import com.eng.ashm.buschool.ui.fragment.management.ManagementDriverFragment;
import com.eng.ashm.buschool.ui.fragment.management.ManagementParentFragment;
import com.eng.ashm.buschool.ui.fragment.management.ManagementTripFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ManagementMainActivity extends AppCompatActivity {

    ManagementActivityBinding managementBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managementBinding = ManagementActivityBinding.inflate(getLayoutInflater());
        setContentView(managementBinding.getRoot());
        managementBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.management_driver_nav:
                    showFragment(new ManagementDriverFragment());
                    break;
                case R.id.management_parent_nav:
                    showFragment(new ManagementParentFragment());
                    break;
                case R.id.management_trip_nav:
                    showFragment(new ManagementTripFragment());
            }
            return true;
        });
    }
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(managementBinding.fragmentContainerView.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }
}
