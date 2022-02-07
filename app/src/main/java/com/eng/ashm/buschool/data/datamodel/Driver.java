package com.eng.ashm.buschool.data.datamodel;

import android.graphics.drawable.Drawable;

import androidx.room.Entity;

import com.eng.ashm.buschool.data.IFirestoreDataModel;

@Entity(primaryKeys ={"name", "phone"})
public class Driver implements IFirestoreDataModel {

    public String name;
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
