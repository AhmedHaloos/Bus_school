package com.eng.ashm.buschool.data.datamodel;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.eng.ashm.buschool.data.IDataModel;
import com.google.android.gms.maps.model.LatLng;


@Entity(primaryKeys ={"email", "phone"})
public class Student implements IDataModel {
    public static final String COLLECTION = "Student";
    @Ignore
    private Parent parent;

    public String name = "";
    @NonNull
    public String phone = "";
    public int yearOFStudy;
    @NonNull
    public String email = "";
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
    /**
     *
     * @param location
     * @return
     */
    public static LatLng getLatLng(String location){
        int seperationIndx = 0;
        char[] chars = location.toCharArray();
        int length = location.length();//2.2555:6.6582
        for (int i = 0; i< length; i++){
            if (chars[i] == ':'){
                seperationIndx = i;
                break;
            }
        }
        double latitude = Double.valueOf(location.substring(0, seperationIndx));
        double longitude  = Double.valueOf(location.substring(seperationIndx + 1));
        return new LatLng(latitude, longitude);
    }
    /**
     *
     * @param point
     * @return
     */
    public static String getStringLocation(LatLng point){
        return point.latitude + ":" + point.longitude;
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
