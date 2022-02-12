package com.eng.ashm.buschool.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListLayoutBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;

public class RecyclerTestFragment extends Fragment {
    TripListLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TripListLayoutBinding.inflate(inflater);
        TripAdapter tripAdapter = new TripAdapter(createTestTripList());
        binding.parentTripLisRv.setAdapter(tripAdapter);
        binding.parentTripLisRv.setLayoutManager(new LinearLayoutManager(getContext()));
        tripAdapter.setOnItemClickListener(trip -> {
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    private ArrayList<Trip> createTestTripList() {
        ArrayList<Trip> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(createTrip(i));
        }
        return list;
    }

    private Trip createTrip(int i) {
        Trip t = new Trip();
        t.tripNum = i;
        t.tripDate = "date : " + i;
        t.startTime = "start time : " + i;
        t.endTime = "end time : " + i + 1;
        t.tripState = (i % 2 == 0) ? "finished" : "not finished";
        //car data
        t.bus.carNum = "car number = " + i;
        t.bus.kind = "bus kind : " + i;
        // driver data
        t.tripDriver.name = (i % 2 == 0) ? "mohamed" : "ahmed";
        t.tripDriver.phone = "0514444411";
        return t;
    }
}