package com.eng.ashm.buschool.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.eng.ashm.buschool.data.datamodel.RTDatabaseRepository;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;
import java.util.Observer;

public class LocationViewModel extends AndroidViewModel {

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public final MutableLiveData<Boolean> isLocationSent = new MutableLiveData<>();
    public final MutableLiveData<String> retrievedLocation = new MutableLiveData<>();

    private RTDatabaseRepository rtDatabaseRepository = RTDatabaseRepository.getInstance();

    //Observer
    java.util.Observer writeLocationObserver = (o, arg) -> {
        if (arg != null)
        isLocationSent.setValue((Boolean)arg);
        rtDatabaseRepository.isDataWritten.deleteObservers();
    };
    Observer readLocationObserver = (o, arg) -> {
        if (arg != null){
            String location = (String)arg;
        retrievedLocation.setValue(location);
        }
        rtDatabaseRepository.dataReadFromDB.deleteObservers();
    };
    /**
     *
     * @param point
     */
    public void addLocation(String point){
        rtDatabaseRepository.addData(point);
        rtDatabaseRepository.isDataWritten.addObserver(writeLocationObserver);
    }
    /**
     *
     */
    public void getLastUpdatedLocation() {
        rtDatabaseRepository.readData();
        rtDatabaseRepository.dataReadFromDB.addObserver(readLocationObserver);
    }

}
