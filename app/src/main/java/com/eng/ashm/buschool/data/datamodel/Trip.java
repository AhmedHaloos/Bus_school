package com.eng.ashm.buschool.data.datamodel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eng.ashm.buschool.data.IFirestoreDataModel;

import java.util.ArrayList;
import java.util.List;

//@Entity
public class Trip implements IFirestoreDataModel {
    // @PrimaryKey
    //@NonNull
    public long tripNum = 0;
    public String startTime = "";
    public String endTime = "";
    public String tripDate = "";
    public String tripState = "";
    public ArrayList<String> stopPoints = new ArrayList<>();
    public Car bus = new Car();
    public Driver tripDriver = new Driver();
    public ArrayList<Student> tripStudents = new ArrayList<>();

    @Override
    public String document(){
        return null;
    }
    @Override
    public String collection() {
        return null;
    }
}
