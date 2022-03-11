package com.eng.ashm.buschool.ui.fragment.management;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.ManagementDriverFragmentBinding;
import com.eng.ashm.buschool.ui.activity.management.CreateDriverActivity;
import com.eng.ashm.buschool.ui.activity.management.DispBusListActivity;
import com.eng.ashm.buschool.ui.adapter.CarAdapter;
import com.eng.ashm.buschool.ui.adapter.DriverAdapter;
import com.eng.ashm.buschool.ui.activity.profile.DriverProfileActivity;
import com.eng.ashm.buschool.ui.viewmodel.DriverViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagementDriverFragment extends Fragment {

    ManagementDriverFragmentBinding binding;
    CarAdapter carAdapter;
    DriverAdapter driverAdapter;
    DriverViewModel driverViewModel;
    ActivityResultLauncher<Object> launcher = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(contract, resultCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (binding == null)
        binding = ManagementDriverFragmentBinding.inflate(inflater);
        initAdapter();
        initModelView();
        binding.driverListRv.setAdapter(driverAdapter);
        binding.driverListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.manageAddNewDriver.setOnClickListener(v->{
            launcher.launch(null);
        });
        binding.dispBusList.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), DispBusListActivity.class));
        });
        return  binding.getRoot();
    }
    // start activity
    ActivityResultContract<Object , Driver> contract = new ActivityResultContract<Object, Driver>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(getActivity(), CreateDriverActivity.class);
            return intent;
        }

        @Override
        public Driver parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return (Driver) intent.getParcelableExtra(Driver.COLLECTION);
            }
            return null;
        }
    };
    ActivityResultCallback<Driver> resultCallback = new ActivityResultCallback<Driver>() {
        @Override
        public void onActivityResult(Driver driver) {
            if (driver != null)
            driverAdapter.addDriver(driver);
        }
    };
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void initAdapter(){
        if (driverAdapter == null)
            driverAdapter = new DriverAdapter(new ArrayList<>());
        driverAdapter.setOnItemClickListener(new OnItemClickListener<Driver>(){
            @Override
            public void onItemClicked(Driver driver) {
              Intent intent = new Intent(getActivity(), DriverProfileActivity.class);
              intent.putExtra(Driver.COLLECTION, (Serializable) driver);
                startActivity(intent);
            }
        });
    }
    private void initModelView(){
        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
        driverViewModel.getAllDrivers();
        driverViewModel.requestDriverListResult.observe(getViewLifecycleOwner(), observer);
    }
    Observer<List<? extends IDataModel>> observer = drivers -> {
        if (drivers.get(0).getClass().equals(Driver.class))
        Toast.makeText(getContext(), "driver list observer called with name =  : "
                 + ((Driver)drivers.get(0)).name, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "driver list observer called from other place =  : "
                    + ((Driver)drivers.get(0)).name, Toast.LENGTH_SHORT).show();
       // driverAdapter.updateDriverList((ArrayList<Driver>) drivers);
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        driverViewModel.requestDriverListResult.removeObservers(this);
        driverViewModel = null;
        driverAdapter = null;
    }
    /**
     *
     * @return
     */
    private ArrayList<Driver> createTestDriverList(){
        ArrayList<Driver> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Driver t = new Driver();
            t.name = "سائق رقم : "+i;
            t.phone = "هاتف : 0551245";
            list.add(t);
        }
        return list;
    }
}
