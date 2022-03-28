package com.eng.ashm.buschool.ui.fragment.driver;

import static com.eng.ashm.buschool.MapsActivity.SOURCE_OF_MAP;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eng.ashm.buschool.MapsActivity;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DriverTripFragmentBinding;
import com.eng.ashm.buschool.ui.activity.profile.TripProfileActivity;
import com.eng.ashm.buschool.ui.adapter.CarAdapter;
import com.eng.ashm.buschool.ui.adapter.DriverAdapter;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class DriverTripFragment extends Fragment {

    DriverTripFragmentBinding driverBinding;
    TripAdapter tripAdapter = null;
    TripViewModel tripViewModel = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        driverBinding = DriverTripFragmentBinding.inflate(inflater);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getAllTrips();
        tripViewModel.requestTripListResult.observe(getViewLifecycleOwner(),tripListObserver);
        if (tripAdapter == null)
        tripAdapter = new TripAdapter(Driver.COLLECTION);
        tripAdapter.setOnItemClickListener(onItemClickListener);
        driverBinding.manageStudentListRv.setAdapter(tripAdapter);
        driverBinding.manageStudentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return  driverBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    /**
     * onItemClickListener
     */
    OnItemClickListener<Trip> onItemClickListener = new OnItemClickListener<Trip>() {
        @Override
        public void onItemClicked(Trip trip) {
            if (trip != null){
                Intent intent = new Intent(getActivity(), TripProfileActivity.class);
                intent.putExtra(Trip.COLLECTION, (Serializable) trip );
                startActivity(intent);
            }
        }
        @Override
        public void onItemBtnClicked(Trip trip) {
            if (trip != null){
                //updateTripState(trip);
                if (trip.getClass().equals(Trip.class)){
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra(SOURCE_OF_MAP, Driver.COLLECTION);
                    intent.putExtra(Trip.COLLECTION, (Serializable) trip);
                    startActivity(intent);
                }
            }
        }
    };
    /**
     * update the trip state to indicate that the driver started the trip
     * @param trip
     */
    private void updateTripState(Trip trip){
        trip.tripState = Trip.ACTIVE_TRIP;
        tripViewModel.updateTripData(trip);
        tripViewModel.updateTripResult.observe(getViewLifecycleOwner(), updateTripObserver);
    }
    /**
     * Observers
     */
    Observer<List<Trip>> tripListObserver = trips -> {
        if (trips != null && !trips.isEmpty()){
            if (trips.get(0).getClass().equals(Trip.class))
            tripAdapter.updateTripList((ArrayList<Trip>) trips);
        }
       };
    // update trip state observer
    Observer<Boolean> updateTripObserver = isTripUpdated -> {
        if (isTripUpdated){
            Toast.makeText(getContext(), "تم تفعيل الرحلة", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "لم يتم تفعيل الرحلة", Toast.LENGTH_SHORT).show();

    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        driverBinding = null;
    }

    private ArrayList<Trip> createTestTripList(){
        ArrayList<Trip> list = new ArrayList<>();
        Trip trip=  null;
        for(int i = 0; i< 10; i++){
            trip = createTrip(i);
            list.add(((trip== null)? createTrip(15): trip));
        }
        return list;
    }

    /**
     *
     * @param i
     * @return
     */
    private Trip createTrip(int i){
        if (i == 2) return null;
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