package com.eng.ashm.buschool.ui.viewmodel.parent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.IFirestoreDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Parent;

public class ParentViewModel extends ViewModel {

    MutableLiveData<Parent> parentLiveData = new MutableLiveData<>();
    //MainRepository repository = MainRepository.getInstance();
   // LiveData<IFirestoreDataModel> repositoryLiveData = repository.liveData;


}
