package com.eng.ashm.buschool.data;

import android.os.Parcelable;

import java.io.Serializable;

public interface IDataModel extends Serializable, Parcelable {
    String document();
    String collection();
}
