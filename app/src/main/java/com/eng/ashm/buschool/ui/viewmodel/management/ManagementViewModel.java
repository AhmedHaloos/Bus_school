package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.IFirestoreDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.Result;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

import java.util.List;

/**
 *
 */
public class ManagementViewModel extends ViewModel {

    public final MutableLiveData<List<IFirestoreDataModel>> requestedDataList = new MutableLiveData<>();
    public final MutableLiveData<List<IFirestoreDataModel>> searchList = new MutableLiveData<>();
    public final MutableLiveData<List<IFirestoreDataModel>> RequestLimDataList = new MutableLiveData<>();
    public final MutableLiveData<IFirestoreDataModel> requestedData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> addResult  = new MutableLiveData<>();
    public final MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
    public final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

    MainRepository mainRepository = MainRepository.getInstance();
    FirestroreRepository firestroreRepository = FirestroreRepository.getInstance();


    public void createNewData(IFirestoreDataModel dataModel){
        Class<? extends IFirestoreDataModel> dataClass = getDataClass(dataModel);
        firestroreRepository.addDataFirestore(dataModel, dataClass);

    }
    public void deleteData(){
    }
    public void updateData(){
    }
    public void getAllList(){
    }
    public void getLimitedList(int noOfItems){
    }
    public void searchData(){
    }

    private Class<? extends IFirestoreDataModel> getDataClass(IFirestoreDataModel dataModel){
            Class<? extends IFirestoreDataModel > dataClass = null;
            if (dataModel instanceof Car){
                dataClass = Car.class;
            }
            else if (dataModel instanceof Trip){
                dataClass = Trip.class;
            }
            else if (dataModel instanceof Driver){
                dataClass = Driver.class;
            }
            else if (dataModel instanceof Student){
                dataClass = Student.class;
            }
            else if (dataModel instanceof Parent){
                dataClass = Parent.class;
            }
            return dataClass;
    }
    private void bindResults(DataListObservable<IFirestoreDataModel> repositoryData, MutableLiveData<IFirestoreDataModel> liveData){
        repositoryData.addObserver((o, arg) -> {

        });
    }

}
