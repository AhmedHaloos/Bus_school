package com.eng.ashm.buschool.ui.activity.management;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.databinding.ManagementActivityBinding;
import com.eng.ashm.buschool.ui.fragment.management.ManagementDriverFragment;
import com.eng.ashm.buschool.ui.fragment.management.ManagementParentFragment;
import com.eng.ashm.buschool.ui.fragment.management.ManagementTripFragment;
import com.eng.ashm.buschool.ui.viewmodel.LocationViewModel;
import com.google.android.gms.maps.model.LatLng;

public class ManagementMainActivity extends AppCompatActivity {

    public static final String MANAG_COLLECTION = "Management";

    ManagementActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ManagementActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    /**
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.fragmentContainerView.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }
}
