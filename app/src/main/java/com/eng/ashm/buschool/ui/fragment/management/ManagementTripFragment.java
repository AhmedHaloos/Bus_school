package com.eng.ashm.buschool.ui.fragment.management;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ManagementTripFragmentBinding;
import com.eng.ashm.buschool.ui.activity.management.CreateTripActivity;
import com.eng.ashm.buschool.ui.activity.profile.TripProfileActivity;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 */
public class ManagementTripFragment extends Fragment {

    ManagementTripFragmentBinding binding;
    TripViewModel tripViewModel;
    TripAdapter tripAdapter = null;
    private ActivityResultLauncher<Object> launcher = null;

    public ManagementTripFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(contract, resultCallback);
        initAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = ManagementTripFragmentBinding.inflate(inflater, container, false);

      initViewModel();
      initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    // request list observer
    Observer<List<? extends IDataModel>>   observer = trips -> {
        if (trips != null || !trips.isEmpty()){
            tripAdapter.updateTripList((ArrayList<Trip>) trips);
        }
    }; ;
    /**
     *
     */
    private void initAdapter(){
        if (tripAdapter == null){
            tripAdapter = new TripAdapter();
            tripAdapter.setOnItemClickListener(onItemClickListener);
        }
    }
    /**
     *onItemClickListener
     */
    OnItemClickListener<Trip> onItemClickListener = new OnItemClickListener<Trip>() {
        @Override
        public void onItemClicked(Trip trip) {
            Intent intent = new Intent();
            intent.putExtra(Trip.COLLECTION, (Serializable ) trip );
            startActivity(new Intent(getContext(), TripProfileActivity.class));
        }
    };
    // initView model
    private void initViewModel(){
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getAllTrips();
        tripViewModel.requestTripListResult.observe(getViewLifecycleOwner(), observer);
    }
    // init recyclerview
    private void initRecyclerView(){
        binding.manageStudentListRv.setAdapter(tripAdapter);
        binding.manageStudentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.manageAddTrip.setOnClickListener(v->{
            startActivity(new Intent(getContext(), CreateTripActivity.class));
        });
    }
    // start activity contract and result callback
    ActivityResultContract<Object , Trip> contract = new ActivityResultContract<Object, Trip>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(getActivity(), CreateTripActivity.class);
            return intent;
        }

        @Override
        public Trip parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return (Trip) intent.getParcelableExtra(Trip.COLLECTION);
            }
            return null;
        }
    };
    ActivityResultCallback<Trip> resultCallback = trip -> {
        if (trip != null)
        tripAdapter.addTrip(trip);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tripAdapter = null;
        tripViewModel.requestTripListResult.removeObserver(observer);
        tripViewModel = null;
        onItemClickListener = null;
    }

    /**
     *
     * @return
     */
    private ArrayList<Trip> createTestData(){
        ArrayList<Trip> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Trip t = new Trip();
            t.tripNum = i;
            t.tripState = (i%2 == 0)? "انتهت": "مستمرة";
            list.add(t);
        }
        return list;
    }
}