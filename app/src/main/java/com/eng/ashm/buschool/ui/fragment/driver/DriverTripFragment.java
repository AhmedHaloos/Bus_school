package com.eng.ashm.buschool.ui.fragment.driver;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DriverTripFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.CarAdapter;
import com.eng.ashm.buschool.ui.adapter.DriverAdapter;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A fragment representing a list of Items.
 */
public class DriverTripFragment extends Fragment {

    DriverTripFragmentBinding driverBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        driverBinding = DriverTripFragmentBinding.inflate(inflater);
        driverBinding.driverTripList.setAdapter(new TripAdapter(createTestTripList()));
        driverBinding.driverTripList.setLayoutManager(new LinearLayoutManager(getContext()));
        return  driverBinding.getRoot();
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
        t.tripState = (i%2==0)?"انتهت":"جارية الان";
        //car data
        t.bus.carNum = "رقم السيارة = "+i;
        t.bus.kind = "نوع السيارة : "+ i;
        // driver data
        t.tripDriver.name = (i%2==0)?"amany":"hanan";
        t.tripDriver.phone = "0514444411";
        return t;
    }

}