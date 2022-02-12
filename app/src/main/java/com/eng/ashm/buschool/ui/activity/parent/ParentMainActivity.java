package com.eng.ashm.buschool.ui.activity.parent;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.databinding.ParentActivityBinding;
import com.eng.ashm.buschool.ui.fragment.parent.ParentProfileFragment;
import com.eng.ashm.buschool.ui.fragment.parent.ParentTripFragment;
import com.google.android.material.navigation.NavigationBarView;

public class ParentMainActivity extends AppCompatActivity {

    ParentActivityBinding parentBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentBinding = ParentActivityBinding.inflate(getLayoutInflater());
        parentBinding.parentNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.parent_profile_nav:
                        showFragment(new ParentProfileFragment());
                        break;
                    case R.id.parent_trip_nav:
                        showFragment(new ParentTripFragment());
                        break;
                }
                return true;
            }
        });
        setContentView(parentBinding.getRoot());
    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(parentBinding.parentFragmentContainer.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }
}
