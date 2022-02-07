package com.eng.ashm.buschool.ui.viewmodel.management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;

public class StudentViewModel extends ViewModel {

    MutableLiveData<Car> studentsData = new MutableLiveData<>();
    MainRepository mainRepository = MainRepository.getInstance();

    public void addNewStudent(Driver createdStudent){
    }
    public void deleteStudent(Driver deletedStudent){
    }
    public void updateStudentData(Driver updatedStudent){
    }
    public void getAllStudents(){
    }
    public void getNumOfStudents(int numOfStudents){
    }
    public void searchStudentByName(String name){
    }
    public void searchStudentByPhone(String phone){
    }
}
