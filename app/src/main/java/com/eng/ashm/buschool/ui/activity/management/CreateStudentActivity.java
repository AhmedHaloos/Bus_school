package com.eng.ashm.buschool.ui.activity.management;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.MapsActivity;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.NewStudentActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.StudentViewModel;
import com.google.android.gms.maps.model.LatLng;

public class CreateStudentActivity extends AppCompatActivity {

    private static final int Filled  = 10;
    private static final int NOT_FILLED =  -10;
    public static final int LOCATION_REQUEST_CODE = 25;
    public static final String PERMISSION_REQUEST_STATUS = "permission request";
    public static final String IS_PERMISSION_REQUESTED = "is permission requested";

    private NewStudentActivityBinding binding;
    private StudentViewModel studentViewModel;
    private Boolean isPermissionRequested = false;
    private boolean isLocPermGranted = false;
    private Student mStudent = null;
    private LocationManager locationManager = null;
    //launcher
    ActivityResultLauncher<String> locationLauncher = null;
    ActivityResultLauncher<String> imgLauncher = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewStudentActivityBinding.inflate(getLayoutInflater());
        SharedPreferences preferences = getSharedPreferences(PERMISSION_REQUEST_STATUS, MODE_PRIVATE);
        isPermissionRequested = preferences.getBoolean(IS_PERMISSION_REQUESTED, false);
        locationManager = getSystemService(LocationManager.class);
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        mStudent = new Student();
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.addStudentResult.observe(this, studentAddedObserver);
        ActivityResultLauncher<String> imgLauncher = registerForActivityResult(imgContract, imgResultCallback);
        locationLauncher = registerForActivityResult(mapContract, mapsResultCallback);
        binding.addNewStudentPic.setOnClickListener(view->{
            imgLauncher.launch("image/*");
        });
        binding.addNewStudent.setOnClickListener(view->{
            int state = fillStudentData();
            if (state == Filled){
            studentViewModel.addNewStudent(mStudent);
            studentViewModel.addStudentResult.observe(CreateStudentActivity.this, studentAddedObserver);
            Intent intent = new Intent();
            intent.putExtra(Student.COLLECTION,(Parcelable) mStudent);
            setResult(RESULT_OK, intent);
            resetFields();
            }
        });
        binding.cancelAddStudent.setOnClickListener(view->{
            finish();
        });
        binding.studentAddLocation.setOnClickListener(v->{
            getLocationPermission();
            if (isLocPermGranted)
                locationLauncher.launch("");
        });
    }

    /**
     * *********************** Location Permission **************************************
     */
    private void getLocationPermission(){
        int finePerm = checkSelfPermission(ACCESS_FINE_LOCATION) ;
        int coarsePerm = checkSelfPermission(ACCESS_COARSE_LOCATION);
        if ( finePerm == PERMISSION_GRANTED || coarsePerm == PERMISSION_GRANTED ){
            isLocPermGranted = true;
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            showAlertMessageNoGps();
        }
        else if (finePerm == PackageManager.PERMISSION_DENIED || coarsePerm == PackageManager.PERMISSION_DENIED) {
            isLocPermGranted = false;
            if (!isPermissionRequested)
                requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            else
                showPermissionDialog();
        }
    }
    /**
     * Alert dialog to inform the user that he needs to allow location permission
     */
    private void showPermissionDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage("يجب اعطاء الاذونات لاستخدام الخريطة")
                .setNegativeButton("cancel", (dialog1,
                                              which) -> {
                    dialog1.cancel();
                })
                .setPositiveButton("settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + "com.eng.ashm.buschool"));
                            startActivity(intent);
                        } catch ( ActivityNotFoundException e ) {
                            e.printStackTrace();
                            //Open the generic Apps page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            startActivity(intent);
                        }
                    }
                })
                .create();
        dialog.show();
    }
    /**
     *
     */
    private void showAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("الGPS مغلق .. هل تريد تفعيله؟")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && (grantResults[0] == PERMISSION_GRANTED
                    || grantResults[1] == PERMISSION_GRANTED)) {
                isLocPermGranted = true;
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    showAlertMessageNoGps();
                else locationLauncher.launch("");
            }
            else {
                isLocPermGranted = false;
                if (isPermissionRequested){
                    showPermissionDialog();
                }
                else {
                    SharedPreferences preferences = getSharedPreferences(PERMISSION_REQUEST_STATUS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(IS_PERMISSION_REQUESTED, true);
                    editor.apply();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    /****************************************************************************************************/
    /**
     *
     */
    Observer<Boolean> studentAddedObserver = (isStudentAdded)->{
        if (isStudentAdded){
            Toast.makeText(CreateStudentActivity.this, "تم اضافة الطالب", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Student.COLLECTION, (Parcelable) mStudent);
            setResult(RESULT_OK, intent);
        }
        else
            Toast.makeText(CreateStudentActivity.this, "لم يتم اضافة الطالب", Toast.LENGTH_SHORT).show();
    };
    /**
     *
     */
    private void resetFields(){
        binding.newStudentName.setText("");
        binding.newStudentPhone.setText("");
        binding.newStudentEmail.setText("");
        binding.newStudentAddress.setText("");
        binding.newStudentAge.setText("");
        binding.newStudentYear.setText("");
        binding.newStudentParentName.setText("");
        binding.newStudentUsername.setText("");
        binding.newStudentPassword.setText("");
    }
    /**
     *
     * @return
     */
    private int fillStudentData(){

        if(isAnyFieldEmpty()){
            new AlertDialog.Builder(this)
                    .setMessage("يجب ملء جميع البيانات للسائق ")
                    .setTitle("خطأ")
                    .setPositiveButton("موافق", (dialog, which) -> {
                    })
                    .setCancelable(true)
                    .create()
                    .show();
            return NOT_FILLED;
        }
        mStudent.name = binding.newStudentName.getText().toString();
        mStudent.phone = binding.newStudentPhone.getText().toString();
        mStudent.email = binding.newStudentEmail.getText().toString();
        mStudent.address = binding.newStudentAddress.getText().toString();
        mStudent.yearOFStudy = Integer.valueOf(binding.newStudentYear.getText().toString());
        mStudent.parentName = binding.newStudentParentName.getText().toString();
        mStudent.age = Integer.valueOf(binding.newStudentAge.getText().toString());
        return Filled;
    }

    /**
     *
     * @return
     */
    private boolean isAnyFieldEmpty(){
            if (   binding.newStudentName.getText().toString().isEmpty()
                || binding.newStudentPhone.getText().toString().isEmpty()
                || binding.newStudentEmail.getText().toString().isEmpty()
                || binding.newStudentAddress.getText().toString().isEmpty()
                || binding.newStudentAge.getText().toString().isEmpty()
                || binding.newStudentUsername.getText().toString().isEmpty()
                || binding.newStudentPassword.getText().toString().isEmpty()
                || binding.newStudentYear.getText().toString().isEmpty()
                    || mStudent.location == null){
            return true;
            }
        return false;
    }

    /**
     *
     * @return the
     */
    private Drawable getSelectedPic(){
        Intent intent = new Intent();
        return null;
    }
    /**
     * contract and result callback for getting results from selecting image app
     */
    ActivityResultContract<String, Uri> imgContract = new ActivityResultContract<String, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(input);
            return intent;
        }

        @Override
        public Uri parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return intent.getData();
            }
            return null;
        }
    };
    ActivityResultCallback<Uri> imgResultCallback = new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            binding.newStudentPic.setImageURI(result);
        }
    };

    /**
     * contract and result callback for getting results from selecting image app
     */
    ActivityResultContract<String, LatLng> mapContract = new ActivityResultContract<String, LatLng>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra(MapsActivity.SOURCE_OF_MAP, Student.COLLECTION);
            return intent;
        }

        @Override
        public LatLng parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                LatLng point = intent.getParcelableExtra(Student.COLLECTION);
                return point;
            }
            return null;
        }
    };
    ActivityResultCallback<LatLng> mapsResultCallback =
            point ->{
        if (point != null){
            mStudent.location = Student.getStringLocation(point);
        }
    } ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        studentViewModel.addStudentResult.removeObservers(this);
        studentViewModel = null;
    }
}
