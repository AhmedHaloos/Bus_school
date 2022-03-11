package com.eng.ashm.buschool.data.datamodel;


import android.graphics.drawable.Drawable;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.eng.ashm.buschool.data.IDataModel;

@Entity(primaryKeys ={"email", "phone"})
public class Driver implements IDataModel {
    public static final String COLLECTION = "Driver";
    public String name;
    @NonNull
    public String phone = "";
    @NonNull
    public String email  = "";
    public String address;
    @Ignore
    public String picURI;
    @Ignore
    public Drawable pic;
    public int age;
    public String username;
    public String password;

    public Driver(){}
    private Driver(Parcel in){
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        username = in.readString();
        password = in.readString();
        age = in.readInt();
    }
    @Override
    public String document(){
        if (phone == null || email == null)
            return null;
        return "/" + phone + "-" + email ;
    }
    @Override
    public String collection() {
        return COLLECTION;
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel source) {
            return new Driver(source);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(age);

    }
}
