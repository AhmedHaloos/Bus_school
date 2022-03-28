package com.eng.ashm.buschool.data.datamodel;

import android.os.Build;
import android.os.Parcel;

import com.eng.ashm.buschool.data.IDataModel;

public class LoggedInUser implements IDataModel {

    public static final String COLLECTION = "loggedInUser";
    public String username = "";
    public String password = "";
    public String email = "";
    public String phone = "";
    public String userCollection = "";
    public Boolean isLoggedIn = false;



    public static Creator<LoggedInUser> CREATOR = new Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel source) {
            LoggedInUser user = new LoggedInUser();
            user.username = source.readString();
            user.password = source.readString();
            user.email = source.readString();
            user.phone = source.readString();
            user.userCollection = source.readString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                user.isLoggedIn = source.readBoolean();
            }

            return user;
        }
        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };
    @Override
    public String document() {
        return "/"+username + "-" + password;
    }

    @Override
    public String collection() {
        return COLLECTION;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(userCollection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            dest.writeBoolean(isLoggedIn);
    }
}
