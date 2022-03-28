package com.eng.ashm.buschool.ui.fragment.parent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.data.datamodel.LoggedInUser;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.ParentActivityBinding;
import com.eng.ashm.buschool.databinding.ParentProfileActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.ParentViewModel;

public class ParentProfileFragment extends Fragment {

    ParentProfileActivityBinding binding;
    Parent parentProfile = null;
    private ParentViewModel parentViewModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            LoggedInUser loggedInParent = getArguments().getParcelable(LoggedInUser.COLLECTION);
            parentViewModel = new ViewModelProvider(this).get(ParentViewModel.class);
            parentViewModel.requestParentData(loggedInParent.phone, loggedInParent.email);
            parentViewModel.requestParentResult.observe(this, parentObserver);
        }
        else
            Toast.makeText(getContext(), "arguments are null", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ParentProfileActivityBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void fillParentView(){
        if (binding != null && parentProfile != null) {
            binding.parentProfilePassword.setText(parentProfile.password);
            binding.parentProfileUsername.setText(parentProfile.username);
            binding.parentProfilePhone.setText(parentProfile.phone);
            binding.parentProfileAge.setText(parentProfile.age + "");
            binding.parentProfileJob.setText(parentProfile.job);
            binding.parentProfileAddress.setText(parentProfile.address);
            binding.parentProfileName.setText(parentProfile.name);
            binding.parentProfileEmail.setText(parentProfile.email);
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /**
     * observer for parent view model
     */
    Observer<Parent> parentObserver = new Observer<Parent>() {
        @Override
        public void onChanged(Parent parent) {
            parentProfile = parent;
            fillParentView();
        }
    };
}
