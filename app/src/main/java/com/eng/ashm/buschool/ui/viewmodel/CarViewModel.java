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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CarViewModel extends AndroidViewModel {

    public MutableLiveData<Car> requestCarResult = new MutableLiveData<>();
    public MutableLiveData<List<Car>> requestCarListResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> addCarResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteCarResult = new MutableLiveData<>();
    public MutableLiveData<List<Car>> searchCarResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateCarResult = new MutableLiveData<>();

    private MainRepository repository = null;

    public CarViewModel(@NonNull Application application){
        super(application);
       // if (repository == null)
            repository = MainRepository.getInstance(application.getApplicationContext());
    }
    /**
     * Observers
     */
    // add observer
    private Observer addObserver = (o, arg) -> {
        if (arg != null) {
            addCarResult.setValue(true);
        }
        repository.mainAddObservable.deleteObservers();
    };
    // update observer
    private Observer updateObserver = (o, arg) -> {
        if (arg != null)
            updateCarResult.setValue(true);
        repository.mainUpdateObservable.deleteObservers();
    };
    // delete observer
    private Observer deleteObserver = (o, arg) -> {
        if (arg != null)
            deleteCarResult.setValue(true);
    };
    // request data observer
    private Observer requestListObserver =
            (o, arg) -> requestCarListResult.setValue((List<Car>) arg);
    // search observer
    private Observer searchObserver = (o, arg) -> searchCarResult.setValue((List<Car>) arg);
    /**
     *
     * @param bus
     */
    public void addNewCar(Car bus) {
        if (bus != null){
            repository.addData(bus);
        repository.mainAddObservable.addObserver(addObserver);
    }
    }
    /**
     *
     * @param bus
     */
    public void deleteCar(Car bus){
        repository.deleteData(bus);
        repository.mainDeleteObservable.addObserver(deleteObserver);
    }
    /**
     *
     * @param updatedBus
     */
    public void updateCarData(Car updatedBus){
        repository.updateData(updatedBus);
        repository.mainUpdateObservable.addObserver(updateObserver);
    }
    /**
     *
     */
    public void getAllCars(){
        repository.requestList(Car.class);
        repository.mainRequestListObservable.addObserver(requestListObserver);
    }
    /**
     *
     * @param carNum
     */
    public void searchCar(String carNum){
        boolean validateState = validateCarNum(carNum);
        if (validateState){
            repository.searchData( carNum, Car.class);
            repository.mainSearchListObservable.addObserver(searchObserver);
        }

    }

    /**
     *
     * @param carNum
     * @return
     */
    private boolean validateCarNum(String carNum){
        boolean isCarNumValid = true;
        for(Character c : carNum.toCharArray()){
            if(isNumber(c))
            {
               continue;
            }
            else if (isAlphabet(c))
            {
                continue;
            }
            else {
                isCarNumValid = false;
                break;
            }
        }
        return isCarNumValid;
    }

    /**
     *
     * @param s
     * @return
     */
    private boolean validateNoOfSeats(String s){
        boolean isValid = true;
        for (Character c: s.toCharArray()) {
            if(isNumber(c)){
                continue;
            }
            else {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
    private boolean isNumber(char c){
        return (c >='0' && c <='9')? true : false;
    }
    private boolean isAlphabet(char c){
        return ((c >= 'a' && c<='z')||(c >= 'A' && c <= 'Z'))? true : false;
    }

    /**
     * Observers
     */


}
