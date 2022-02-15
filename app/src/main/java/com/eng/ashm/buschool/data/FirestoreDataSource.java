package com.eng.ashm.buschool.data;

import androidx.annotation.NonNull;

import com.eng.ashm.buschool.data.datamodel.DataListObservable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

/**
 * this DataSource class is responsible for CRUD operations with firestore database
 */
public class FirestoreDataSource {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final DataListObservable<Result<IFirestoreDataModel>> list = new DataListObservable();
    private static FirestoreDataSource dataSource = null;

    /**
     *
     */
    private FirestoreDataSource(){}
    public static final FirestoreDataSource createInstance(){
        if(dataSource == null)
            dataSource = new FirestoreDataSource();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
        return dataSource;
    }
    /**
     *
     * @param dataModel
     */
    public void addData(@NonNull IFirestoreDataModel dataModel){
        db.collection(dataModel.collection())
          .document(dataModel.document())
          .set(dataModel).addOnCompleteListener(task -> {
            Result<IFirestoreDataModel> result = null;
              if (task.isSuccessful()){
                  result = Result.createResult(dataModel, null);
             result.STATE = Result.SUCCEED;
             list.addElement(result);
              }
              else {
                  result = Result.createResult(null, new Exception("data did not added"));
                  result.STATE = Result.ERROR;
              }
          });
    }

    /**
     *
     * @param collection
     * @param c
     */
    public void requestDataList(String collection, Class<? extends IFirestoreDataModel> c){

        db.collection(collection)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Result<IFirestoreDataModel>  result = Result.createResult(document.toObject(c), null);
                    result.STATE = Result.SUCCEED;
                    list.addElement(result);
                }
            }
            else{
                Result<IFirestoreDataModel> result = Result.createResult(null, new Exception("no data retrieved"));
                list.addElement(result);
            }
        });
    }

    /**
     *
     * @param collection
     * @param field
     * @param condition
     * @param dataClass
     */
    public void searchData(String collection,String field, Object condition, Class<? extends IFirestoreDataModel> dataClass){
        db.collection(collection).whereEqualTo(field, condition )
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                       List<DocumentSnapshot> documents  = task.getResult().getDocuments();
                        for (DocumentSnapshot document: documents) {
                            Result<IFirestoreDataModel> result = Result.createResult(document.toObject(dataClass), null);
                            list.addElement(result);
                        }
                    }
                    else{
                        Result<IFirestoreDataModel> result = Result.createResult(null, new Exception("no data exist"));
                        list.addElement(result);

                    }
        });
    }
    /**
     *
     * @param collection
     * @param document
     */
    public void requestData(String collection, String document, Class<? extends IFirestoreDataModel> c){
        db.collection(collection).document(document)
                .get().addOnCompleteListener((task -> {
                    if (task.isSuccessful()){
                        Result<IFirestoreDataModel> result = Result.createResult(task.getResult().toObject(c), null);
                    }
        }));
    }

    /**
     *
     * @param dataModel
     * @param c
     */
    public void updateData(IFirestoreDataModel dataModel, Class<? extends IFirestoreDataModel> c ){
        db.collection(dataModel.collection())
                .document(dataModel.document())
                .set(dataModel).addOnCompleteListener(task -> {
            Result<IFirestoreDataModel> result = null;
                    if (task.isSuccessful()){
                        result = Result.createResult(dataModel, null);
                    result.STATE = Result.SUCCEED;
                    }
                    else {
                        result = Result.createResult(null, new Exception("data did not updated"));
                        result.STATE = Result.ERROR;
                    }
        });
    }

    /**
     *
     * @param collection
     * @param document
     */
    public void deleteData(String collection, String document){
        db.collection(collection).document(document).delete().addOnCompleteListener(task -> {
            Result<IFirestoreDataModel> result = Result.createResult(null, null);
            if (task.isSuccessful()){
                result.STATE = Result.SUCCEED;
            }
            else result.STATE = Result.ERROR;
        });
    }
}
