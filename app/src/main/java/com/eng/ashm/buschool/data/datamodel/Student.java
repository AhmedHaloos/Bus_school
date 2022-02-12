package com.eng.ashm.buschool.data.datamodel;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.eng.ashm.buschool.data.IFirestoreDataModel;


@Entity(primaryKeys ={"name", "phone"})
public class Student implements IFirestoreDataModel {
    public int yearOFStudy;
    //private Parent parent;
    @NonNull
    public String name;
    @NonNull
    public String phone;
    public String email;
    public String address;
    public String picURI;
    public int age;
    @Override
    public String document(){
        return null;
    }

    @Override
    public String collection() {
        return null;
    }
}
