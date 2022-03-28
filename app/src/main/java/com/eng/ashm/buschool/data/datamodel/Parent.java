package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.eng.ashm.buschool.data.IDataModel;


@Entity(primaryKeys ={"email", "phone"})
public class Parent implements IDataModel {
    public static final String COLLECTION = "Parent";
    public String name = "";
    @NonNull
    public String phone = "";
    public String relation;
    public String job;
    public String secondContact;
    @Ignore
    public Student[] relatedStudents;
    @NonNull
    public String email = "";
    public String address;
    public String picURI;
    public int age;
    public String username;
    public String password;

    public Parent(){}
    private Parent(Parcel in){
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        job = in.readString();
        relation = in.readString();
        username = in.readString();
        password = in.readString();
        age = in.readInt();
    }
    @Override
    public String document(){
        if (phone == null || email == null)
            return null;
        return  "/" + phone + "-" + email ;
    }

    @Override
    public String collection() {
        return COLLECTION;
    }

    public static final Creator<Parent> CREATOR = new Creator<Parent>() {
        @Override
        public Parent createFromParcel(Parcel source) {
            return new Parent(source);
        }

        @Override
        public Parent[] newArray(int size) {
            return new Parent[0];
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
        dest.writeString(job);
        dest.writeString(relation);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(age);
    }
}
