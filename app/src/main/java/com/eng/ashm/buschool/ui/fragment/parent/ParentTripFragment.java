package com.eng.ashm.buschool.ui.fragment.parent;

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
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ParentTripFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class ParentTripFragment extends Fragment {

    ParentTripFragmentBinding tripBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      tripBinding = ParentTripFragmentBinding.inflate(inflater);

      tripBinding.parentTripLisRv.setAdapter(new TripAdapter(createTestTripList()));
      tripBinding.parentTripLisRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return tripBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private ArrayList<Trip> createTestTripList(){
        ArrayList<Trip> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
           list.add(createTrip(i));
        }
        return list;
    }
    private Trip createTrip(int i){
        Trip t = new Trip();
        t.tripNum = i;
        t.tripDate =  "date : " + i;
        t.startTime = "start time : "+i;
        t.endTime = "end time : "+i + 1;
        t.tripState = (i%2==0)?"finished":"not finished";
        //car data
        t.bus.carNum = "car number = "+i;
        t.bus.kind = "bus kind : "+ i;
       // driver data
        t.tripDriver.name = (i%2==0)?"mohamed":"ahmed";
        t.tripDriver.phone = "0514444411";
         return t;
    }
}
