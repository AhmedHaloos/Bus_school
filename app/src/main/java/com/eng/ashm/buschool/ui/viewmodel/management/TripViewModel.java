package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;

public class TripViewModel extends ViewModel {

    MutableLiveData<Car> tripData = new MutableLiveData<>();
    MainRepository mainRepository = MainRepository.getInstance();

    public void addNewTrip(){
    }
    public void deleteTrip(){
    }
    public void updateTripData(){
    }
    public void getAllTrip(){
    }
    public void getNumOfTrip(){
    }
    public void searchTrip(){
    }
}
