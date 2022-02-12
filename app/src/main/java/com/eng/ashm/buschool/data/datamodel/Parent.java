package com.eng.ashm.buschool.data.datamodel;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.eng.ashm.buschool.data.IFirestoreDataModel;


@Entity(primaryKeys ={"name", "phone"})
public class Parent implements IFirestoreDataModel {

    public String maritalState;
    public String job;
    public String secondContact;
    //public Student[] relatedStudents;
    @NonNull
    public String name;
    @NonNull
    public String phone;
    public String email;
    public String address;
    public String picURI;
    public int age;
    public String username;
    public String password;


    @Override
    public String document(){
        return null;
    }

    @Override
    public String collection() {
        return null;
    }
}
