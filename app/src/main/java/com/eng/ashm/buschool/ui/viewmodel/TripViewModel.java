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

    private MainRepository repository = null;

    public TripViewModel(@NonNull Application application){
        super(application);
       // if(repository == null){
            repository = MainRepository.getInstance(application.getApplicationContext());
       // }
    }

    /**
     *
     * @param trip
     */
    public void addNewTrip(Trip trip){
        repository.addData(trip);
        repository.mainAddObservable.addObserver((o, arg) -> {
            if (arg == null)
                addTripResult.setValue(false);
            else if (arg != null)
                addTripResult.setValue(true);
        });
    }
    /**
     *
     * @param trip
     */
    public void deleteTrip(Trip trip){
        repository.deleteData(trip);
        repository.mainDeleteObservable.addObserver((o, arg) -> {
            if (arg == null)
                deleteTripResult.setValue(false);
            else
                deleteTripResult.setValue(true);
        });
    }
    /**
     *
     * @param trip
     */
    public void updateTripData(Trip trip){
        repository.updateData(trip);
        repository.mainUpdateObservable.addObserver((o, arg) -> {
            if (arg == null)
                updateTripResult.setValue(false);
            else updateTripResult.setValue(true);
        });
    }
    /**
     *
     */
    public void getAllTrips(){
        repository.requestList(Trip.class);
        repository.mainRequestListObservable.addObserver(
                (o, arg) -> requestTripListResult.setValue((List<Trip>)arg));
    }
    /**
     *
     * @param tripNum
     */
    public void searchTrip(String tripNum){
            repository.searchData( tripNum, Student.class);
            repository.mainSearchListObservable.addObserver(
                    (o, arg) -> searchTripResult.setValue((List<Trip>)arg));
    }

}
