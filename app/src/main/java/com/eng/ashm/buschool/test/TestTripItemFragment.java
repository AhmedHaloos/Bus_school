package com.eng.ashm.buschool.test;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.databinding.TripProfileLayoutBinding;

public class TestTripItemFragment extends Fragment {

   TripProfileLayoutBinding binding;
    Trip trip = null;

    public TestTripItemFragment(Trip trip){
        this.trip = trip;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "item fragment", Toast.LENGTH_SHORT).show();
        binding = TripProfileLayoutBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (trip != null)
            fillTripProfile(trip, view);
        super.onViewCreated(view, savedInstanceState);
    }
    /**
     *
     * @param trip
     */
    private void fillTripProfile(Trip trip, View v){
/*
        TripProfileLayoutBinding tripProfileBinding = TripProfileLayoutBinding.bind(v);
        tripProfileBinding.parentItemCarKindTv.setText(trip.bus.kind);
        tripProfileBinding.parentItemCarNumTv.setText(trip.bus.carNum);
        tripProfileBinding.parentItemDriverNameTv.setText(trip.tripDriver.name);
        tripProfileBinding.parentItemDriverPhoneTv.setText(trip.tripDriver.phone);
        tripProfileBinding.parentItemTripStDateTv.setText(trip.startTime);
        tripProfileBinding.parentItemTripEndDateTv.setText(trip.endTime);*/
    }
}
