package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;

public class CarViewModel extends ViewModel {

    MutableLiveData<Car> carsData = new MutableLiveData<>();
    MainRepository mainRepository = MainRepository.getInstance();

    public void addNewCar(){
    }
    public void deleteCar(){
    }
    public void updateCarData(){
    }
    public void getAllCars(){
    }
    public void getNumOfCars(){
    }
    public void searchCar(){
    }

}
