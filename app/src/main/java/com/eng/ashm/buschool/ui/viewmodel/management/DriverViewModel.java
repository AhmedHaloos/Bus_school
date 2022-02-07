package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;

public class DriverViewModel extends ViewModel {

    MutableLiveData<Driver> driversData = new MutableLiveData<>();
    MainRepository mainRepository = MainRepository.getInstance();

    public void addNewDriver(Driver createdDriver){
    }
    public void deleteDriver(Driver deletedDriver){
    }
    public void updateDriverData(Driver updatedDriver){
    }
    public void getAllDrivers(){
    }
    public void getNumOfDrivers(int numOfDrivers){
    }
    public void searchDriverByName(String name){
    }
    public void searchDriverByPhone(String phone){
    }
}
