package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.eng.ashm.buschool.data.IDataModel;


@Entity(primaryKeys ={"email", "phone"})
public class Student implements IDataModel {
    public static final String COLLECTION = "Student";
    @Ignore
    private Parent parent;
    @NonNull
    public String name;
    @NonNull
    public String phone;
    public int yearOFStudy;
    @NonNull
    public String email;
    public String address;
    public String picURI;
    public String parentName;
    public String location = null;
    public int age;

    public Student(){}
    private Student(Parcel in){
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        address = in.readString();
        picURI = in.readString();
        parentName = in.readString();
        location = in.readString();
        yearOFStudy = in.readInt();
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

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[0];
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
        dest.writeString(picURI);
        dest.writeString(parentName);
        dest.writeString(location);
        dest.writeInt(yearOFStudy);
        dest.writeInt(age);
    }
}
