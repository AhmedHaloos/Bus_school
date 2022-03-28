package com.eng.ashm.buschool.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DriverViewModel extends AndroidViewModel {

    public MutableLiveData<Driver> requestDriverResult = new MutableLiveData<>();
    public MutableLiveData<List<Driver>> requestDriverListResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> addDriverResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteDriverResult = new MutableLiveData<>();
    public MutableLiveData<List<Driver>> searchDriverResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateDriverResult = new MutableLiveData<>();

    private MainRepository repository = null;

    public DriverViewModel(@NonNull Application application){
        super(application);
       // if (repository == null){
            repository = MainRepository.getInstance(application);
        //}
    }
    /**
     * @param driver
     */
    public void addNewDriver(Driver driver) {
        if (driver != null){
            repository.addData(driver);
        repository.mainAddObservable.addObserver(addObserver);
    }
}
    /**
     *
     * @param driver
     */
    public void deleteDriver(Driver driver){
        repository.deleteData(driver);
        repository.mainDeleteObservable.addObserver(deleteObserver);
    }
    /**
     *
     * @param updatedDriver
     */
    public void updateDriverData(Driver updatedDriver){
        repository.updateData(updatedDriver);
        repository.mainUpdateObservable.addObserver(updateObserver);
    }
    /**
     *
     */
    public void getAllDrivers(){
        repository.requestList(Driver.class);
        repository.mainRequestListObservable.addObserver(requestListObserver);
    }
    /**
     *
     * @param name
     */
    public void searchDriverByName(String name){
       // if (validateName(name)){
            repository.searchData( name, Driver.class);
            repository.mainSearchListObservable.addObserver(searchListObserver);
       // }
    }
    /**
     * Observers
     */
    // request list observer
    private Observer requestListObserver = (o, arg) -> {
        requestDriverListResult.setValue((List<Driver>) arg);
        repository.mainRequestListObservable.deleteObservers();
    };
    // search observer
    private Observer searchListObserver = (o, arg) -> {
        searchDriverResult.setValue((List<Driver>) arg);
        repository.mainSearchListObservable.deleteObservers();
    };
    //add observer
    private Observer addObserver = (o, arg) -> {
        if (arg == null)
            addDriverResult.setValue(false);
        else if (arg != null)
            addDriverResult.setValue(true);
        repository.mainAddObservable.deleteObservers();
    };
    // delete observer
    private Observer deleteObserver = (o, arg) -> {
        if (arg != null)
            deleteDriverResult.setValue(true);
        else if (arg == null)
            deleteDriverResult.setValue(false);
        repository.mainDeleteObservable.deleteObservers();
    };
    // update observer
    private Observer updateObserver = (o, arg) -> {
        if (arg == null)
            updateDriverResult.setValue(false);
        else if (arg != null)
            addDriverResult.setValue(true);
        repository.mainUpdateObservable.deleteObservers();
    };

    /**
     *
     * @param name
     * @return
     */
    private boolean validateName(String name){
        for (Character c: name.toCharArray()) {
            if (isAlphabet(c)){
                continue;
            }
            else return false;
        }
        return true;
    }
    /**
     *
     * @param phone
     * @return
     */
    private boolean validatePhone(String phone){
        for (Character c: phone.toCharArray()) {
            if (isNumber(c))
                continue;
            else return false;
        }
        return true;
    }

    /**
     *
     * @param c
     * @return
     */
    private boolean isNumber(char c){
        return (c >='0' && c <='9')? true : false;
    }
    /**
     *
     * @param c
     * @return
     */
    private boolean isAlphabet(char c){ return ((c >= 'a' && c<='z')||(c >= 'A' && c <= 'Z'))? true : false;
    }
}
