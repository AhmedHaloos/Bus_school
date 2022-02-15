package com.eng.ashm.buschool.data;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;

public class FirestroreRepository {

    public final DataListObservable<IFirestoreDataModel> requestListObservable = new DataListObservable();
    public final DataListObservable<IFirestoreDataModel> searchListObservable = new DataListObservable();
    public final DataListObservable<IFirestoreDataModel> addObservable = new DataListObservable();
    public final DataListObservable<IFirestoreDataModel> updateObservable = new DataListObservable();
    public final DataListObservable<IFirestoreDataModel> deleteObservable = new DataListObservable();

    public boolean isSucceed = true;

    FirestoreDataSource firestoreDataSource = FirestoreDataSource.createInstance();
    private static FirestroreRepository repositoryInstance = null;

    class Collections{
        public static final String CAR = "car";
        public static final String DRIVER = "driver";
        public static final String PARENT = "parent";
        public static final String STUDENT = "student";
        public static final String TRIP = "trip";
    }

    private FirestroreRepository(){}
    /**
     * create instance from the FirestoreRepository class
     * @return
     */
    public static final FirestroreRepository getInstance(){
        if (repositoryInstance == null)
            repositoryInstance = new FirestroreRepository();
        return repositoryInstance;
    }
    public void requestDataFirestore(IFirestoreDataModel dataModel){
         }

    public void requestListFirestore(Class<? extends IFirestoreDataModel> dataClass){
        String collection = getCollection(dataClass);
        firestoreDataSource.requestDataList(collection, dataClass);
        bindResults(firestoreDataSource.list, requestListObservable);
    }
    public void searchDataFirestore(String field , Object value,Class<? extends IFirestoreDataModel> c ){
        String collection = getCollection(c);
        firestoreDataSource.searchData(collection, field, value, c);
       bindResults(firestoreDataSource.list, searchListObservable);
    }
    public void updateDataFirestore(IFirestoreDataModel updatedObject, Class<? extends IFirestoreDataModel> c  ){
        firestoreDataSource.updateData(updatedObject, c);
        bindResults(firestoreDataSource.list, updateObservable);
        }
    public void addDataFirestore(IFirestoreDataModel dataModel, Class<? extends IFirestoreDataModel> sourceClass){
        firestoreDataSource.addData(dataModel);
        bindResults(firestoreDataSource.list, addObservable);
          }
    public void deleteDataFirestore(IFirestoreDataModel dataModel){
        firestoreDataSource.addData(dataModel);
        bindResults(firestoreDataSource.list, deleteObservable);
         }

    private String getCollection(Class<? extends IFirestoreDataModel> dataClass){
        String collection = null;
        if (dataClass == Car.class){
            collection = Collections.CAR;
        }
        else if (dataClass == Trip.class){
            collection = Collections.TRIP;
        }
        else if (dataClass == Student.class){
            collection = Collections.STUDENT;
        }
        else if (dataClass == Driver.class){
            collection = Collections.DRIVER;
        }
        else if (dataClass == Parent.class){
            collection = Collections.PARENT;
        }
        return collection;
    }

    private void bindResults(DataListObservable<Result<IFirestoreDataModel>> dataSource, DataListObservable<IFirestoreDataModel> repositoryData){
        dataSource.addObserver((o, arg) -> {
            for (Result<IFirestoreDataModel> result: dataSource.getList() ) {
                if (result.STATE == Result.ERROR){
                    break;
                }
                repositoryData.addElement(result.getResult());
            }
        });
    }
}
