package com.eng.ashm.buschool.data;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FirestroreRepository {

    //test field
    public boolean state = false;

    public final DataListObservable<IDataModel> requestListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> searchListObservable = new DataListObservable();
    public final DataListObservable<IDataModel> addObservable = new DataListObservable();
    public final DataListObservable<IDataModel> updateObservable = new DataListObservable();
    public final DataListObservable<IDataModel> deleteObservable = new DataListObservable();

    private DataListObservable<IDataModel> repositoryData = null;

    private static FirestoreDataSource firestoreDataSource = null;
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

    private Observer resultListObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            ArrayList<IDataModel> dataModels = new ArrayList<>();
            repositoryData.clearList();
            for (Result<IDataModel> result: (List<Result<IDataModel>>)arg ) {
                if (result.STATE == Result.ERROR){
                    dataModels.add(null);
                    break;
                }
                else if (result.STATE == Result.SUCCEED) {
                    dataModels.add(result.getResult());
                }
            }
            repositoryData.updateList(dataModels);
        }
    };
    private Observer resultDataObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            repositoryData.clearList();
            Result<IDataModel> result = (Result<IDataModel>)arg;
            if (result.STATE == Result.SUCCEED)
                repositoryData.addElement(result.getResult());
            else if ((result.STATE == Result.ERROR))
                repositoryData.addElement(null);
        }
    };

    public void requestDataFirestore(String dataURI){
         }

    public void requestListFirestore(Class<? extends IDataModel> dataClass){
        String collection = getCollection(dataClass);
        firestoreDataSource.requestDataList(collection, dataClass);
        repositoryData = requestListObservable;
        bindResultList(firestoreDataSource.resultList);
    }
    public void searchDataFirestore(String field , Object value,Class<? extends IDataModel> c ){
        String collection = getCollection(c);
        firestoreDataSource.searchData(collection, field, value, c);
        repositoryData = searchListObservable;
        bindResultList(firestoreDataSource.resultList);
    }
    public void updateDataFirestore(IDataModel updatedObject, Class<? extends IDataModel> c  ){
        if (isUriValid(updatedObject)){
        firestoreDataSource.updateData(updatedObject, c);
        repositoryData = updateObservable;
        bindResult(firestoreDataSource.resultList);
        }

    }
    public void addDataFirestore(IDataModel dataModel){
        if (isUriValid(dataModel)){
        firestoreDataSource.addData(dataModel);
        repositoryData = addObservable;
        bindResult(firestoreDataSource.resultList);
        }
    }
    public void deleteDataFirestore(IDataModel dataModel){
        if (isUriValid(dataModel))
        firestoreDataSource.addData(dataModel);
        repositoryData = deleteObservable;
        bindResult(firestoreDataSource.resultList);
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

    /**
     *
     * @param dataSource
     */
    private void bindResultList(DataListObservable<Result<IDataModel>> dataSource){
        dataSource.addObserver(resultListObserver);
    }

    /**
     *
     * @param dataSource
     */
    private void bindResult(DataListObservable<Result<IDataModel>> dataSource){
        dataSource.addObserver(resultDataObserver);
    }

    /**
     *
     */
    private void closeObserver(){
        if (resultDataObserver != null){
            resultDataObserver = null;
        }
        if (resultListObserver != null){
            resultListObserver = null;
        }
    }
}
