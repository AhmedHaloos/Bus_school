package com.eng.ashm.buschool.ui.fragment.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ParentTripFragmentBinding;
import com.eng.ashm.buschool.ui.activity.profile.TripProfileActivity;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;

import java.util.ArrayList;

public class ParentTripFragment extends Fragment {

    ParentTripFragmentBinding binding;
    TripAdapter tripAdapter = null;
    TripViewModel tripViewModel = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      binding = ParentTripFragmentBinding.inflate(inflater);
      if(tripAdapter == null)
        tripAdapter = new TripAdapter();
      binding.parentTripLisRv.setAdapter(tripAdapter);
      binding.parentTripLisRv.setLayoutManager(new LinearLayoutManager(getContext()));
        initView();
        return binding.getRoot();

    }
    private void initView(){
        //adapter

        tripAdapter.setOnItemClickListener((OnItemClickListener<Parent>) parent -> {
            if (parent != null){
                startActivity(new Intent(getActivity(), TripProfileActivity.class));
            }
        });
        //binding
        tripViewModel  = new ViewModelProvider(getActivity()).get(TripViewModel.class);
        tripViewModel.getAllTrips();
        tripViewModel.requestTripListResult.observe(getActivity(), trips -> {
            tripAdapter.addTripList((ArrayList<Trip>) trips);
        });

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tripViewModel = null;
        tripAdapter = null;
    }

    /**
     *
     * @param i
     * @return
     */
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
