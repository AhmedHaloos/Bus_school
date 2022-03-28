package com.eng.ashm.buschool.data;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class FirestroreRepository {

    //test field
    public boolean state = false;

    public final DataListObservable<IDataModel> requestListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> searchListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> resultDataObservable = new DataListObservable();
    public final DataListObservable<IDataModel> addObservable = new DataListObservable();
    public final DataListObservable<IDataModel> updateObservable = new DataListObservable();
    public final DataListObservable<IDataModel> deleteObservable = new DataListObservable();

    private DataListObservable<IDataModel> repositoryData = null;

    private static  FirestoreDataSource firestoreDataSource = null ;
    private static FirestroreRepository repositoryInstance = null;

     private FirestroreRepository(){}
    /**
     * create instance from the FirestoreRepository class
     * @return
     */
    public static final FirestroreRepository getInstance(){
        if (repositoryInstance == null) {
            repositoryInstance = new FirestroreRepository();
            firestoreDataSource =  FirestoreDataSource.createInstance();
        }
        return repositoryInstance;
    }
    /**
     *
     */
    private Observer resultListObserver = (o, arg) -> {
        List<IDataModel> list = new ArrayList<>();
        Class c = list.getClass();
       if (arg.getClass().equals(c)) {
           ArrayList<IDataModel> dataModels = new ArrayList<>();
           requestListObservable.clearList();
           for (Result<IDataModel> result : (List<Result<IDataModel>>) arg) {
               if (result.STATE == Result.ERROR) {
                   continue;
               } else if (result.STATE == Result.SUCCEED) {
                   dataModels.add(result.getResult());
               }
           }
           requestListObservable.updateList(dataModels);
       }
    };
    /**
     * result data observer
     */
    private Observer resultDataObserver = (o, arg) -> {
        Result<IDataModel> resultClass = Result.createResult(null, null);
        Class c = resultClass.getClass();
        if (arg.getClass().equals(c) && arg != null) {
            repositoryData.clearList();
            Result<IDataModel> result = (Result<IDataModel>) arg;
            if (result.STATE == Result.SUCCEED)
                resultDataObservable.addElement(result.getResult());
            else if (result.STATE == Result.ERROR)
                updateObservable.addElement(null);
        }
    };
    /**
     * update data observer
     */
    private Observer resultUpdateObserver = (o, arg) -> {
        Result<IDataModel> resultClass = Result.createResult(null, null);
        Class c = resultClass.getClass();
        if (arg.getClass().equals(c) && arg != null ) {
            repositoryData.clearList();
            Result<IDataModel> result = (Result<IDataModel>) arg;
            if (result.STATE == Result.SUCCEED)
                updateObservable.addElement(result.getResult());
            else if (result.STATE == Result.ERROR)
                updateObservable.addElement(null);
        }
    };
    /**
     * delete data observer
     */
    private Observer resultDeleteObserver = (o, arg) -> {
        Result<IDataModel> resultClass = Result.createResult(null, null);
        Class c = resultClass.getClass();
        if (arg.getClass().equals(c) && arg != null ) {
            repositoryData.clearList();
            Result<IDataModel> result = (Result<IDataModel>) arg;
            if (result.STATE == Result.SUCCEED)
                deleteObservable.addElement(result.getResult());
            else if (result.STATE == Result.ERROR)
                deleteObservable.addElement(null);
        }
    };
    /**
     *
     */
    private Observer resultAddObserver = (o, arg) -> {
        Result<IDataModel> resultClass = Result.createResult(null, null);
        Class c = resultClass.getClass();
        if (arg.getClass().equals(c) && arg != null ) {
            repositoryData.clearList();
            Result<IDataModel> result = (Result<IDataModel>) arg;
            if (result.STATE == Result.SUCCEED)
                addObservable.addElement(result.getResult());
            else if (result.STATE == Result.ERROR)
                addObservable.addElement(null);
        }
    };
    /********************* DATA REQUESTS **************************/

    public void requestDataFirestore(String collection, String document, Class<? extends IDataModel> c){
        firestoreDataSource.requestData(collection, document, c);
        repositoryData = resultDataObservable;
        firestoreDataSource.resultList.addObserver(resultDataObserver);
    }
    /**
     *
     * @param dataClass
     */
    public void requestListFirestore(Class<? extends IDataModel> dataClass){
        String collection = getCollection(dataClass);
        firestoreDataSource.requestDataList(collection, dataClass);
        repositoryData = requestListObservable;
        firestoreDataSource.resultList.addObserver(resultListObserver);
    }
    /**
     *
     * @param field
     * @param value
     * @param c
     */
    public void searchDataFirestore(String field , Object value,Class<? extends IDataModel> c ){
        String collection = getCollection(c);
        firestoreDataSource.searchData(collection, field, value, c);
        repositoryData = searchListObservable;
        firestoreDataSource.resultList.addObserver(resultListObserver);
    }
    /**
     *
     * @param updatedObject
     * @param c
     */
    public void updateDataFirestore(IDataModel updatedObject, Class<? extends IDataModel> c  ){
        if (isUriValid(updatedObject)){
        firestoreDataSource.updateData(updatedObject, c);
        repositoryData = updateObservable;
        firestoreDataSource.resultList.addObserver(resultUpdateObserver);
        }
    }
    /**
     *
     * @param dataModel
     */
    public void addDataFirestore(IDataModel dataModel){
        if (isUriValid(dataModel)){
        firestoreDataSource.addData(dataModel);
        repositoryData = addObservable;
        firestoreDataSource.resultList.addObserver(resultAddObserver);
        }
    }
    /**
     *
     * @param dataModel
     */
    public void deleteDataFirestore(IDataModel dataModel){
        if (isUriValid(dataModel))
        firestoreDataSource.deleteData(dataModel.collection(), dataModel.document(), dataModel.getClass());
        repositoryData = deleteObservable;
        firestoreDataSource.resultList.addObserver(resultDeleteObserver);
    }
    /**
     *
      * @param dataModel
     * @return
     */
    private boolean isUriValid(IDataModel dataModel){
       if (dataModel.document() == null || dataModel.collection() == null){
           return false;
       }
        return true;
    }
    private String getCollection(Class<? extends IDataModel> dataClass){
        String collection = null;
        if (dataClass == Car.class){
            collection = Car.COLLECTION;
        }
        else if (dataClass == Trip.class){
            collection = Trip.COLLECTION;
        }
        else if (dataClass == Student.class){
            collection = Student.COLLECTION;
        }
        else if (dataClass == Driver.class){
            collection = Driver.COLLECTION;
        }
        else if (dataClass == Parent.class){
            collection = Parent.COLLECTION;
        }
        return collection;
    }
}
