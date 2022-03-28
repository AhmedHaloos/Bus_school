package com.eng.ashm.buschool.data.datamodel;

import androidx.annotation.NonNull;

import com.eng.ashm.buschool.data.Result;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeFBDatasource {

    //constants
    private static final String DRIVER_LOCATION_KEY = "driver_locations";
    //Results observables
    public final DataListObservable<Result<Boolean>> dataWritten  = new DataListObservable<>();
    public final DataListObservable<Result<String>> dataRead = new DataListObservable<>();

    private FirebaseDatabase RTdatabase  = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = RTdatabase.getReference();

    /**
     *
     * @param location
     */
    public void writeData(String location){
      databaseReference.child(DRIVER_LOCATION_KEY).setValue(location).addOnCompleteListener(onDataWritten);
    }
    /**
     *
     */
    public void readData(){
        databaseReference.child(DRIVER_LOCATION_KEY).addValueEventListener(valueEventListener);
    }
    /**
     * data written and read listener for listening to read and write data
     */
    OnCompleteListener<Void> onDataWritten = task -> {
     if (task.isSuccessful()){
         Result<Boolean> isDataWritten = Result.createResult(true, null);
         isDataWritten.STATE = Result.SUCCEED;
         dataWritten.addElement(isDataWritten);
     }
     else {
         Result<Boolean> isDataWritten = Result.createResult(false, new Exception("location not sent"));
         isDataWritten.STATE = Result.ERROR;
         dataWritten.addElement(isDataWritten);
     }
     this.dataWritten.deleteObservers();
    };
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           String location =  snapshot.getValue(String.class);
           if (snapshot.exists()){
               Result<String> resultLocation = Result.createResult(location, null);
               resultLocation.STATE = Result.SUCCEED;
               dataRead.addElement(resultLocation);
           }
           else{
               Result<String> resultLocation = Result.createResult(null, new Exception("no location retrieved"));
               resultLocation.STATE = Result.ERROR;
               dataRead.addElement(resultLocation);
           }
           dataRead.deleteObservers();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

}
