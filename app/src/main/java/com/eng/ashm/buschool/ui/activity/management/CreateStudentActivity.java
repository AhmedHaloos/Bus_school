package com.eng.ashm.buschool.ui.activity.management;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

    NewStudentActivityBinding binding;
    StudentViewModel studentViewModel;
    private Student mStudent = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewStudentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.addStudentResult.observe(this, observer);
        ActivityResultLauncher<String> imgLauncher = registerForActivityResult(imgContract, imgResultCallback);
        ActivityResultLauncher<String> locationLauncher = registerForActivityResult(mapContract, maprResultCallback);
        binding.addNewStudentPic.setOnClickListener(view->{
            imgLauncher.launch("image/*");
        });
        binding.addNewStudent.setOnClickListener(view->{
            Student  student = getStudentData();
            if (student != null){
            studentViewModel.addNewStudent(student);
            mStudent = student;
            studentViewModel.addStudentResult.observe(CreateStudentActivity.this,  observer);
            resetFields();
            }
        });
        binding.cancelAddStudent.setOnClickListener(view->{
            finish();
        });
        binding.studentAddLocation.setOnClickListener(v->{
            locationLauncher.launch("");
        });
    }
    Observer<Boolean> observer = (isStudentAdded)->{
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
    private Student getStudentData(){
        Student student = new Student();
        if(isAnyFieldEmpty()){
            new AlertDialog.Builder(this)
                    .setMessage("يجب ملء جميع البيانات للسائق ")
                    .setTitle("خطأ")
                    .setPositiveButton("موافق", (dialog, which) -> {
                    })
                    .setCancelable(true)
                    .create()
                    .show();
            return null;
        }
        student.name = binding.newStudentName.getText().toString();
        student.phone = binding.newStudentPhone.getText().toString();
        student.email = binding.newStudentEmail.getText().toString();
        student.address = binding.newStudentAddress.getText().toString();
        student.yearOFStudy = Integer.valueOf(binding.newStudentYear.getText().toString());
        student.parentName = binding.newStudentParentName.getText().toString();
        student.age = Integer.valueOf(binding.newStudentAge.getText().toString());
        return student;
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
                || binding.newStudentYear.getText().toString().isEmpty()){
            return true; }
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
    ActivityResultCallback<LatLng> maprResultCallback =
            point -> mStudent.location = point.latitude + "+" + point.latitude;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        studentViewModel = null;
        observer = null;
    }
}
