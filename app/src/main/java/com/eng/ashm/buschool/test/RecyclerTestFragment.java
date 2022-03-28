package com.eng.ashm.buschool.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListLayoutBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;

public class RecyclerTestFragment extends Fragment {
    TripListLayoutBinding binding;
    TripSelectedListener onTripSelected;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "list fragment", Toast.LENGTH_SHORT).show();
        binding = TripListLayoutBinding.inflate(inflater);
        TripAdapter tripAdapter = new TripAdapter();
        binding.parentTripLisRv.setAdapter(tripAdapter);
        binding.parentTripLisRv.setLayoutManager(new LinearLayoutManager(getContext()));
        tripAdapter.setOnItemClickListener(trip -> {
          // if (onTripSelected !=null)
             //  onTripSelected.onTripSelected(trip);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setChangeViewListener(TripSelectedListener changeView){
        this.onTripSelected = changeView;
    }
    public void addOnTripSelectedListener(TripSelectedListener onTripSelected){
        this.onTripSelected = onTripSelected;
    }
    /**
     *
     * @return
     */
    private ArrayList<Trip> createTestTripList() {
        ArrayList<Trip> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(createTrip(i));
        }
        return list;
    }

    /**
     *
     * @param i
     * @return
     */
    private Trip createTrip(int i) {
        Trip t = new Trip();
        t.tripNum = i;
        t.tripDate = "date : " + i;
        t.startTime = "بداية توقيت الرحلة : " + i;
        t.endTime = "نهاية توقيت الرحلة : " + i + 1;
        t.tripState = (i % 2 == 0) ? "مستمرة" : "انتهت";
        //car data
        t.bus.carNum = "رقم السيارة = " + i;
        t.bus.kind = "نوع السيارة : " + i;
        // driver data
        t.tripDriver.name = (i % 2 == 0) ? "امانى" : "امنية";
        t.tripDriver.phone = "0514444411";
        return t;
    }

    public interface TripSelectedListener {
        void onTripSelected(Trip trip);
    }

}