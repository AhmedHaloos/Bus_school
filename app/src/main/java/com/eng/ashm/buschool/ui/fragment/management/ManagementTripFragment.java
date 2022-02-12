package com.eng.ashm.buschool.ui.fragment.management;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ManagementTripFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A fragment representing a list of Items.
 */
public class ManagementTripFragment extends Fragment {

    ManagementTripFragmentBinding binding;
    private final ArrayList<Trip> tripList = new ArrayList<>();
    public ManagementTripFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // get arguments with arraylist
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = ManagementTripFragmentBinding.inflate(inflater, container, false);
      binding.driverTripList.setAdapter(new TripAdapter(createTestData()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private ArrayList<Trip> createTestData(){
        ArrayList<Trip> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Trip t = new Trip();
            t.tripNum = i;
            t.tripDate =  Calendar.getInstance().getTime().toGMTString();
            list.add(t);
        }
        return list;
    }
}