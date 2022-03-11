package com.eng.ashm.buschool.data;

import androidx.annotation.NonNull;
import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * this DataSource class is responsible for CRUD operations with firestore database
 */
public class FirestoreDataSource {

    private static final int KILOBYTE = 1024;
    private static final int MEGABYTE = 1024 * KILOBYTE;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final DataListObservable<Result<IDataModel>> resultList = new DataListObservable();
    private static FirestoreDataSource dataSource = null;
    IDataModel dataModel = null;
    Class<? extends IDataModel> customClass = null;

    /**
     *
     */
    private FirestoreDataSource(){}
    public static final FirestoreDataSource createInstance(){
       if(dataSource == null){
            dataSource = new FirestoreDataSource();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(10 * MEGABYTE)
                .build();
        db.setFirestoreSettings(settings);}
        return dataSource;
    }
    /**
     *
     * @param dataModel
     */
    public void addData(@NonNull IDataModel dataModel){
        this.dataModel = dataModel;
        String document = dataModel.collection() + dataModel.document();
        db.document(document)
          .set(dataModel).addOnCompleteListener(addCompleteListener);
    }

    /**
     *
     * @param collection
     * @param c
     */
    public void requestDataList(String collection, Class<? extends IDataModel> c){
        customClass = c;
        db.collection(collection)
                .get().addOnCompleteListener(listCompleteListener);
        }

    /**
     *
     * @param collection
     * @param field
     * @param condition
     * @param dataClass
     */
    public void searchData(String collection,String field, Object condition, Class<? extends IDataModel> dataClass){
        db.collection(collection).orderBy(field).startAt(condition).endAt((String)condition + '\uf8ff')
                .get().addOnCompleteListener(task -> {
                    ArrayList<Result<IDataModel>> arrayList = new ArrayList<>();
                    if (task.isSuccessful()){
                       List<DocumentSnapshot> documents  = task.getResult().getDocuments();
                        for (DocumentSnapshot document: documents) {
                            Result<IDataModel> result = Result.createResult(document.toObject(dataClass), null);
                            arrayList.add(result);
                        }
                    }
                    else{
                        Result<IDataModel> result = Result.createResult(null, new Exception("no data exist"));
                        arrayList.add(result);
                    }
                    resultList.updateList(arrayList);
        });
    }
    /**
     *
     * @param collection
     * @param document
     */
    public void requestData(String collection, String document, Class<? extends IDataModel> c){
        customClass = c;
        db.collection(collection).document(document)
                .get().addOnCompleteListener(requestDataListener);
    }

    /**
     *
     * @param dataModel
     * @param c
     */
    public void updateData(IDataModel dataModel, Class<? extends IDataModel> c ){
        customClass = c;
        this.dataModel = dataModel;
        db.collection(dataModel.collection())
                .document(dataModel.document())
                .set(dataModel).addOnCompleteListener(updateCompleteListener);
    }

    /**
     *
     * @param collection
     * @param document
     */
    public void deleteData(String collection, String document){
        db.collection(collection).document(document).delete().addOnCompleteListener(task -> {

        });
    }
    /**
     ** operation tasks
     */
    /**
     *
     */
    OnCompleteListener<Void> addCompleteListener = task -> {
        Result<IDataModel> result = null;
        if (task.isSuccessful()){
            result = Result.createResult(dataModel, null);
            result.STATE = Result.SUCCEED;
            resultList.addElement(result);
        }
        else {
            result = Result.createResult(null, new Exception("data did not added"));
            result.STATE = Result.ERROR;
        }
    };
    /**
     *
     */
    OnCompleteListener<QuerySnapshot> listCompleteListener = task -> {

        ArrayList<Result<IDataModel>> arrayList = new ArrayList<>();
        if (task.isSuccessful()){
            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                Result<IDataModel>  result = Result.createResult(document.toObject(customClass), null);
                result.STATE = Result.SUCCEED;
                arrayList.add(result);
            }
        }
        else{
            Result<IDataModel> result = Result.createResult(null, new Exception("no data retrieved"));
            arrayList.add(result);
        }
        resultList.updateList(arrayList);

    };
    /**
     *
     */
    OnCompleteListener<DocumentSnapshot> deleteCompleteListener = task -> {
        Result<IDataModel> result = Result.createResult(null, null);
        if (task.isSuccessful()){
            result.STATE = Result.SUCCEED;
        }
        else result.STATE = Result.ERROR;
    };
    /**
     *
     */
    OnCompleteListener<Void> updateCompleteListener = task -> {
        Result<IDataModel> result = null;
        if (task.isSuccessful()){
            result = Result.createResult(dataModel, null);
            result.STATE = Result.SUCCEED;
        }
        else {
            result = Result.createResult(null, new Exception("data did not updated"));
            result.STATE = Result.ERROR;
        }
    };
    /**
     *
     */
    OnCompleteListener<DocumentSnapshot> requestDataListener = task -> {

    };

}
