package com.eng.ashm.buschool.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TripViewModel extends AndroidViewModel {

    public MutableLiveData<Trip> requestTripResult = new MutableLiveData<>();
    public MutableLiveData<List<Trip>> requestTripListResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> addTripResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteTripResult = new MutableLiveData<>();
    public MutableLiveData<List<Trip>> searchTripResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateTripResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> tripStartedResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> tripStoppedResult = new MutableLiveData<>();


    private MainRepository repository = null;

    public TripViewModel(@NonNull Application application){
        super(application);
       // if(repository == null){
            repository = MainRepository.getInstance(application.getApplicationContext());
       // }
    }
    /**
     * Trips Observer
     */
    //trip state start observer
     private Observer stateStartObserver = (o, arg)->{
        if (arg == null)
            tripStartedResult.setValue(false);
        else
            tripStartedResult.setValue(true);
    };
     // trip state stop
     private Observer stateStopObserver = (o, arg)->{
         if (arg == null)
             tripStoppedResult.setValue(false);
         else
             tripStoppedResult.setValue(true);
     };
    // add observer
    private Observer addObserver = (o, arg) -> {
        if (arg == null)
            addTripResult.setValue(false);
        else
            addTripResult.setValue(true);
    };
    //delete observer
    private Observer deleteObserver = (o, arg) -> {
        if (arg == null)
            deleteTripResult.setValue(false);
        else
            deleteTripResult.setValue(true);
    };
    // update observer
    private Observer updateObserver = (o, arg) -> {
        if (arg == null)
            updateTripResult.setValue(false);
        else updateTripResult.setValue(true);
    };
    //request list observer
    private Observer requestListObserver = (o, arg) ->{
        if (arg != null) {
            requestTripListResult.setValue((List<Trip>)arg);
        }
    };
    // request data observer
    private Observer searchObserver = (o, arg) ->{
        if (arg != null) {
            searchTripResult.setValue((List<Trip>)arg);
        }
    };
    /**
     *
     * @param trip
     */
    public void addNewTrip(Trip trip){
        repository.addData(trip);
        repository.mainAddObservable.addObserver(addObserver);
    }
    /**
     *
     * @param trip
     */
    public void deleteTrip(Trip trip){
        repository.deleteData(trip);
        repository.mainDeleteObservable.addObserver(deleteObserver);
    }
    /**
     *
     * @param trip
     */
    public void updateTripData(Trip trip){
        repository.updateData(trip);
        repository.mainUpdateObservable.addObserver(updateObserver);
    }
    /**
     *
     */
    public void getAllTrips(){
        repository.requestList(Trip.class);
        repository.mainRequestListObservable.addObserver(requestListObserver);
    }
    /**
     *
     * @param tripNum
     */
    public void searchTrip(String tripNum){
            repository.searchData( tripNum, Student.class);
            repository.mainSearchListObservable.addObserver(searchObserver);
    }
    /**
     *
     * @param trip
     */
    public void startTrip(Trip trip){
        trip.tripState = Trip.ACTIVE_TRIP;
        repository.updateData(trip);
        repository.mainUpdateObservable.addObserver(stateStartObserver);
    }
    /**
     *
     * @param trip
     */
    public void stopTrip(Trip trip){
        trip.tripState = Trip.INACTIVE_TRIP;
        repository.updateData(trip);
        repository.mainUpdateObservable.addObserver(stateStopObserver);
    }

}
