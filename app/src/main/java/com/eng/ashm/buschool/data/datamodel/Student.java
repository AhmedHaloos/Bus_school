package com.eng.ashm.buschool.data.datamodel;

import androidx.room.Entity;

import com.eng.ashm.buschool.data.IFirestoreDataModel;


@Entity(primaryKeys ={"email", "phone"})
public class Student implements IFirestoreDataModel {
    private int yearOFStudy;
    private Parent parent;
    public String name;
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
