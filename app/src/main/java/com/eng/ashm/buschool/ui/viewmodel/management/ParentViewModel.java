package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Parent;

public class ParentViewModel extends ViewModel {

    // result liveData
    MutableLiveData<Parent> parentsData = new MutableLiveData<>();
    MainRepository mainRepository = MainRepository.getInstance();
    FirestroreRepository firestroreRepository = FirestroreRepository.getInstance();

    public void addNewParent(Parent createdParent){
    }
    public void deleteParent(Parent deletedParent){
    }
    public void updateParentData(Parent updatedParent){
    }
    public void getAllParents(){
    }
    public void getNumOfParents(int numOfParents){
    }
    public void searchParentByName(String name){
    }
    public void searchParentByPhone(String phone){
    }
}
