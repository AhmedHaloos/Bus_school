package com.eng.ashm.buschool.data;

import android.content.Context;

import androidx.room.Room;

import com.eng.ashm.buschool.data.datamodel.CacheDataSource;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

import java.util.ArrayList;
import java.util.List;

public class CacheRepository {
    private static CacheDataSource db = null;

    private static CacheRepository repositoryInstance = null;

    private CacheRepository(){}
    /**
     * create instance from the FirestoreRepository class
     * @return
     */
    public static final CacheRepository getInstance(Context context){
        if (repositoryInstance == null){
            repositoryInstance = new CacheRepository();
            db = Room.databaseBuilder(context, CacheDataSource.class, "app_cache")
                    .allowMainThreadQueries()
                    .build();
        }
        return repositoryInstance;
    }
    /**
     *
     * @param listClass
     */
    public List<? extends IDataModel> requestDataList(Class<? extends IDataModel> listClass){
        if (listClass.equals(Car.class)){
           return db.carDao().getAllCars();
        }
        else if(listClass.equals(Driver.class)){
            return db.driverDao().getAllDrivers();
        }
        else if (listClass.equals(Parent.class)){
            return db.parentDao().getAllParents();
        }
        else if (listClass.equals(Student.class)){
            return db.studentDao().getAllStudents();
        }
        else if (listClass.equals(Trip.class)){
            return db.tripDao().getAllTrips();
        }
        return null;
    }
    public void requestDataCache(){}
    /**
     *
     * @param dataModel
     */
    public boolean updateDataCache(IDataModel dataModel){
        if (dataModel instanceof Car){
            int count = db.carDao().updateCars((Car) dataModel);
            if (count > 0)
                return true;
            else return false;
        }
        else if(dataModel instanceof Driver){
            int count = db.driverDao().updateDrivers((Driver) dataModel);
            if (count > 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Parent){
            int count = db.parentDao().updateParent((Parent) dataModel);
            if (count > 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Student){
            int count = db.studentDao().updateStudents((Student) dataModel);
            if (count > 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Trip){
            int count = db.tripDao().updateTrip((Trip) dataModel);
            if (count > 0)
                return true;
            else return false;
        }
        return false;
    }
    /**
     *
     * @param dataModel
     */
    public boolean addDataCache(IDataModel dataModel){

        if (dataModel instanceof Car){
            if (db.carDao().getCar(((Car) dataModel).carNum) == null){
            long rowid = db.carDao().addCar((Car) dataModel);
            if (rowid >= 0)
                return true;
            else return false;
            }
            else return false;
        }
        else if(dataModel instanceof Driver){
            if (db.driverDao().getDriver(((Driver)dataModel).phone, ((Driver)dataModel).email) == null) {
                long rowid = db.driverDao().addDriver((Driver) dataModel);
                if (rowid >= 0)
                    return true;
                else return false;
            }
                else return false;

        }
        else if (dataModel instanceof Parent){
            if (db.parentDao().getParent(((Parent)dataModel).phone, ((Parent)dataModel).email) == null) {
                long rowid = db.parentDao().addParent((Parent) dataModel);
                if (rowid >= 0)
                    return true;
                else return false;
            }
                else return false;

        }
        else if (dataModel instanceof Student){
            if (db.studentDao().getStudent(((Student)dataModel).phone, ((Student)dataModel).email) == null) {
                long rowid = db.studentDao().addStudent((Student) dataModel);
                if (rowid >= 0)
                    return true;
                else return false;
            }
                else return false;

        }
        else if (dataModel instanceof Trip){
            if (db.tripDao().getTrip(((Trip)dataModel).tripNum) == null) {
                long rowid = db.tripDao().addTrip((Trip) dataModel);
                if (rowid >= 0)
                    return true;
                else return false;
            }
            else return false;

        }
        return false;
    }
    /**
     *
     * @param dataModel
     */
    public boolean deleteDataCache(IDataModel dataModel){
        if (dataModel instanceof Car){
            int count = db.carDao().deleteCar((Car) dataModel);
            if (count >= 0)
                return true;
            else return false;
        }
        else if(dataModel instanceof Driver){
            int count = db.driverDao().deleteDriver((Driver) dataModel);
            if (count >= 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Parent){
            int count = db.parentDao().deleteParent((Parent) dataModel);
            if (count >= 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Student){
            int count = db.studentDao().deleteStudent((Student) dataModel);
            if (count >= 0)
                return true;
            else return false;
        }
        else if (dataModel instanceof Trip){
            int count = db.tripDao().deleteTrip((Trip) dataModel);
            if (count >= 0)
                return true;
            else return false;
        }
        return false;
    }
    /**
     *
     * @param condition
     * @param searchClass
     */
    public List<? extends IDataModel> searchData(Object condition, Class<? extends IDataModel> searchClass){

        if (searchClass.equals(Car.class)){
           return db.carDao().searchCars((Integer) condition);
        }
        else if(searchClass.equals(Driver.class)){
            return db.driverDao().searchDrivers((String) condition);
        }
        else if (searchClass.equals(Parent.class)){
           return db.parentDao().searchParents((String)condition);
        }
        else if (searchClass.equals(Student.class)){
           return db.studentDao().searchStudent((String)condition);
        }
        else if (searchClass.equals(Trip.class)){
           return db.tripDao().searchTrip((Integer) condition);
        }
        return null;
    }

}
