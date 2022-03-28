package com.eng.ashm.buschool.ui.activity.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.GlobalConst;
import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.LoggedInUser;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.ParentActivityBinding;
import com.eng.ashm.buschool.ui.fragment.parent.ParentProfileFragment;
import com.eng.ashm.buschool.ui.fragment.parent.ParentTripFragment;
import com.eng.ashm.buschool.ui.viewmodel.ParentViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class ParentMainActivity extends AppCompatActivity {

    private ParentActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ParentActivityBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        LoggedInUser loggedInParent = intent.getParcelableExtra(GlobalConst.USER_PROFILE_KEY);
        binding.parentNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.parent_profile_nav:
                    ParentProfileFragment fragment = new ParentProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LoggedInUser.COLLECTION, loggedInParent);
                    fragment.setArguments(bundle);
                    showFragment(fragment);
                    break;
                case R.id.parent_trip_nav:
                    showFragment(new ParentTripFragment());
                    break;
            }
            return true;
        });
        setContentView(binding.getRoot());
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
        transaction.replace(binding.parentFragmentContainer.getId(),fragment )
                .setReorderingAllowed(true)
                .commit();
    }
}
