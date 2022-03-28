package com.eng.ashm.buschool.data.datamodel;

import androidx.work.Operation;

import com.eng.ashm.buschool.data.Result;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;
import java.util.Observer;

public class RTDatabaseRepository {

    public final DataListObservable<Boolean> isDataWritten = new DataListObservable<>();
    public final DataListObservable<String> dataReadFromDB = new DataListObservable<>();

    private RealtimeFBDatasource datasource = new RealtimeFBDatasource();
    private static RTDatabaseRepository repository = null;

    private RTDatabaseRepository(){}
    public static RTDatabaseRepository getInstance(){
        if (repository == null)
            repository = new RTDatabaseRepository();
        return repository;
    }
    //Observers
    Observer writeDataObserver = (o, arg) -> {
        if (arg != null){
            Result<Boolean> result = (Result<Boolean>)arg;
            isDataWritten.addElement(result.getResult());
        }
        isDataWritten.deleteObservers();
    };
    Observer readDataObserver = (o, arg) -> {
        if (arg != null) {
            Result<String> result = (Result<String>)arg;
            dataReadFromDB.addElement(result.getResult());
        }
        dataReadFromDB.deleteObservers();
    };

    /**
     *
     * @param point
     */
    public void addData(String point){
        datasource.writeData(point);
        datasource.dataWritten.addObserver(writeDataObserver);
    }
    /**
     *
     */
    public void readData(){
        datasource.readData();
        datasource.dataRead.addObserver(readDataObserver);
    }


}
