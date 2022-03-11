package com.eng.ashm.buschool.ui.activity.management;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.MapsActivity;
import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.NewTripActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.TripViewModel;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class CreateTripActivity extends AppCompatActivity {

    //search requests
    public static final String DRIVER_REQUEST = Driver.COLLECTION;
    public static final String STUDENT_REQUEST = Student.COLLECTION;
    public static final String CAR_REQUEST = Car.COLLECTION;
    public static final String NO_REQUEST = "no request";
    public static final String SEARCH_REQUEST_KEY = "search_request";
    public static final String SEARCH_RESULT_KEY = "search_request";

    //fields
    private NewTripActivityBinding binding;
    private TripViewModel tripViewModel = null;
    private String request = NO_REQUEST;
    private Trip trip = null;
    private long tripNum = -1;
    private Driver tripDriver;
    private Car tripCar ;
    private ArrayList<Student> tripStudents;
    private ActivityResultLauncher<String> launcher = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = NewTripActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        //search launcher
        launcher = registerForActivityResult(contract, resultCallback);
    }
    private void initView(){
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        binding.addNewTripBtn.setOnClickListener(addTripListener);
        binding.addNewTripBus.setOnClickListener(addBusListener);
        binding.addNewTripDriver.setOnClickListener(addDriverListener);
        binding.addNewTripPoints.setOnClickListener(addPointsListener);
        binding.addNewTripStudent.setOnClickListener(addStudentsListener);
        binding.cancelAddTripBtn.setOnClickListener(v -> { finish(); });
    }
    Observer<Boolean> observer = isTripAdded -> {
        if (isTripAdded){
            Toast.makeText(CreateTripActivity.this, "تم اضافة الرحلة", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Trip.COLLECTION, (Parcelable) trip);
            setResult(RESULT_OK, intent);
        }
        else
            Toast.makeText(CreateTripActivity.this, "لم يتم اضافة الرحلة", Toast.LENGTH_SHORT).show();
    };

    View.OnClickListener addTripListener = v -> {
        boolean isTripFilled = fillTripData();
       if (isTripFilled)
       {
           tripViewModel.addNewTrip(trip);
           resetFields();
       }
       else
           Toast.makeText(CreateTripActivity.this, "لم يتم اكتمال بيانات الرحلة", Toast.LENGTH_SHORT).show();
        tripViewModel.addTripResult.observe(CreateTripActivity.this, observer);
    };

    /**
     *
     */
    private void resetFields(){
        binding.newTripNum.setText("");
    }
    /**
     *
     * @return
     */
    private boolean fillTripData(){
        trip = new Trip();
        if (checkFieldsFilled()){
            trip.tripDriver = tripDriver;
            trip.tripStudents = tripStudents;
            trip.bus = tripCar;
            trip.tripNum = tripNum;
            return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    private boolean checkFieldsFilled(){
        String str = binding.newTripNum.getText().toString();
        if (str == null || str.isEmpty()){
            Toast.makeText(this, "لم يتم اضافة رقم الرحلة", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tripDriver == null){
            Toast.makeText(this, "لم يتم اضافة سائق الرحلة", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tripCar == null){
            Toast.makeText(this, "لم يتم اضافة حافلة الرحلة", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (tripStudents == null || tripStudents.isEmpty()){
            Toast.makeText(this, "لم يتم اضافة طلبة الرحلة", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            tripNum = Long.valueOf(str);
        }
        return true;
    }
    /**
     * Listeners
     */
    View.OnClickListener addPointsListener = v -> {
       // startActivity(new Intent(CreateTripActivity.this, MapsActivity.class));
    };
    View.OnClickListener addBusListener = v -> launcher.launch(Car.COLLECTION);
    View.OnClickListener addDriverListener = v -> launcher.launch(Driver.COLLECTION);
    View.OnClickListener addStudentsListener = v -> launcher.launch(Student.COLLECTION);
    /**
     * activity result
     */
    ActivityResultCallback<List<? extends Parcelable>> resultCallback =
            result ->{ fillTripData(result);};


    ActivityResultContract< String, List<? extends Parcelable>> contract = new ActivityResultContract<String, List<? extends Parcelable>>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String  input) {
            Intent intent = new Intent(CreateTripActivity.this, SearchActivity.class);
            intent.putExtra(SEARCH_REQUEST_KEY, input);
            return intent;
        }

        @Override
        public List<? extends Parcelable> parseResult(int resultCode, @Nullable Intent intent) {
            ArrayList<? extends Parcelable> dataModels = null;
            if (intent != null)
            dataModels =  intent.getParcelableArrayListExtra(SEARCH_RESULT_KEY);
            return dataModels;
        }
    };

    /**
     *
     * @param dataList
     */
    private void fillTripData(List<? extends Parcelable> dataList){
        if (dataList ==  null)
        {
            Toast.makeText(this, "لم تتم الاضافة", Toast.LENGTH_SHORT).show();
            return;
        }
        IDataModel dataObject = (IDataModel) dataList.get(0);
        if (!dataList.isEmpty()){
            if (dataObject instanceof Driver){
                tripDriver = (Driver) dataObject;
            }
            else if (dataObject instanceof Student){
                tripStudents = new ArrayList<>();
                for (Student student : (ArrayList<Student>)dataList ){
                    tripStudents.add(student);
                }
            }
            else if (dataObject instanceof Car){
                tripCar = (Car) dataObject;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        tripViewModel = null;
    }
}
