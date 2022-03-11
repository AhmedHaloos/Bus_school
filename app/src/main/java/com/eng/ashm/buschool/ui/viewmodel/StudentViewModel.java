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
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class StudentViewModel extends AndroidViewModel {

    public MutableLiveData<Student> requestStudentResult = new MutableLiveData<>();
    public MutableLiveData<List<Student>> requestStudentListResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> addStudentResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteStudentResult = new MutableLiveData<>();
    public MutableLiveData<List<Student>> searchStudentResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> updateStudentResult = new MutableLiveData<>();

    private MainRepository repository = null;

    public StudentViewModel(@NonNull Application application){
        super(application);
        //if (repository == null){
            repository = MainRepository.getInstance(application.getApplicationContext());
       // }
    }
    /**
     *
     * @param student
     */
    public void addNewStudent(Student student) {
        if (student != null) {
            repository.addData(student);
            repository.mainAddObservable.addObserver((o, arg) -> {
                if (arg != null) {
                    addStudentResult.setValue(true);
                } else if (arg == null) {
                    addStudentResult.setValue(false);
                }
            });
    }
    }
    public void deleteStudent(Student student){
        if (student != null) {
        repository.deleteData(student);
        repository.mainDeleteObservable.addObserver((o, arg) -> {
            if (arg == null)
                deleteStudentResult.setValue(false);
            else if (arg != null)
                deleteStudentResult.setValue(true);
        });
        }
    }
    public void updateStudentData(Student updatedStudent) {
        if (updatedStudent != null) {
            repository.updateData(updatedStudent);
            repository.mainUpdateObservable.addObserver((o, arg) -> {
                if (arg == null)
                    updateStudentResult.setValue(false);
                else if (arg != null)
                    updateStudentResult.setValue(true);
            });
        }
    }
    /**
     *
     */
    public void getAllStudents(){
        repository.requestList(Student.class);
        repository.mainRequestListObservable.addObserver(
                (o, arg) -> requestStudentListResult.setValue((List<Student>) arg));
    }
    /**
     *
     * @param name
     */
    public void searchStudentByName(String name){

            repository.searchData(name, Student.class);
            repository.mainSearchListObservable.addObserver(
                    (o, arg) -> searchStudentResult.setValue((List<Student>) arg));
    }
    /**
     *
     * @param name
     * @return
     */
    private boolean validateName(String name){
        if (name == null || name.isEmpty())
            return false;
        for (Character c: name.toCharArray()) {
            if (isAlphabet(c)){
                continue;
            }
            else return false;
        }
        return true;
    }
    private boolean validatePhone(String phone){
        if (phone == null || phone.isEmpty())
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

}
