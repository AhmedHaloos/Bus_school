package com.eng.ashm.buschool.ui.fragment.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.ManagementDriverFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.CarAdapter;
import com.eng.ashm.buschool.ui.adapter.DriverAdapter;

import java.util.ArrayList;

public class ManagementDriverFragment extends Fragment {

    ManagementDriverFragmentBinding binding;
    CarAdapter carAdapter;
    DriverAdapter driverAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ManagementDriverFragmentBinding.inflate(inflater);
        carAdapter = new CarAdapter(createTestCarList());
        driverAdapter = new DriverAdapter(createTestDriverList());
        binding.managementCarRv.setAdapter(carAdapter);
        binding.managementCarRv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.managementDriverRv.setAdapter(driverAdapter);
        binding.managementDriverRv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false ));
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private ArrayList<Car> createTestCarList(){
        ArrayList<Car> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Car t = new Car();
            t.kind = "نوع الباص : "+i;
            t.carNum ="رقم الباص : " +i;
            list.add(t);
        }
        return list;
    }
    private ArrayList<Driver> createTestDriverList(){
        ArrayList<Driver> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Driver t = new Driver();
            t.name = "سائق رقم : "+i;
            t.phone = "هاتف : 010123452144";
            list.add(t);
        }
        return list;
    }
}
