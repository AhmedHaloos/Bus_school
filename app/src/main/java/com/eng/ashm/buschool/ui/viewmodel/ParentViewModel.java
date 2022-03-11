package com.eng.ashm.buschool.ui.viewmodel;

import android.app.Application;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.contentcapture.ContentCaptureContext;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ParentViewModel extends AndroidViewModel {

    // result liveData
    public MutableLiveData<Parent> requestParentResult = new MutableLiveData<>();
    public MutableLiveData<List<Parent>> requestParentListResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> addParentResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteParentResult = new MutableLiveData<>();
    public MutableLiveData<List<Parent>> searchParentResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateParentResult = new MutableLiveData<>();

    private MainRepository repository = null;

    public ParentViewModel(@NonNull Application application){
        super(application);
       // if (repository == null){
            repository = MainRepository.getInstance(application.getApplicationContext());
       // }
    }
    /**
     *
     * @param parent
     */
    public void addNewParent(Parent parent){
        if (parent != null){
        repository.addData(parent);
        repository.mainAddObservable.addObserver((o, arg) -> {
            if (arg != null){
                addParentResult.setValue(true);
            }
        });
    }
    }
    /**
     *
     * @param parent
     */
    public void deleteParent(Parent parent){
        repository.deleteData(parent);
        repository.mainDeleteObservable.addObserver((o, arg) -> {
            if (arg == null)
                deleteParentResult.setValue(false);
            else if (arg != null)
                deleteParentResult.setValue(true);
        });
    }
    /**
     *
     * @param updatedParent
     */
    public void updateParentData(Parent updatedParent){
        repository.updateData(updatedParent);
        repository.mainUpdateObservable.addObserver((o, arg) -> {
            if (arg == null)
                deleteParentResult.setValue(false);
            else if (arg != null)
                deleteParentResult.setValue(true);
        });
    }
    /**
     *
     */
    public void getAllParents(){
        repository.requestList(Parent.class);
        repository.mainRequestListObservable.addObserver(
                (o, arg) -> requestParentListResult.setValue((List<Parent>) arg));
    }
    /**
     *
     * @param name
     */
    public void searchParentByName(String name){
        if (validateName(name)){
            repository.searchData(name, Driver.class);
            repository.mainSearchListObservable.addObserver(
                    (o, arg) -> searchParentResult.setValue((List<Parent>) arg));
        }
    }
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

    private boolean isNumber(char c){
        return (c >='0' && c <='9')? true : false;
    }
    private boolean isAlphabet(char c){
        return ((c >= 'a' && c<='z')||(c >= 'A' && c <= 'Z'))? true : false;
    }

    /**
     *
     * @param age
     * @return
     */
    private boolean validateAge(int age){
        char[] chAge = (age+"").toCharArray();
        for (char c:chAge) {
            if (!isNumber(c)){
                return false;
            }
        }
        if (age <110 && age > 20){
            return true;
        }
        return false;
    }
}
