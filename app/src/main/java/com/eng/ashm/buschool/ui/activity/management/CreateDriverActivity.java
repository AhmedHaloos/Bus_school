package com.eng.ashm.buschool.ui.activity.management;

import android.content.Context;
import android.content.DialogInterface;
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

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.NewDriverActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.DriverViewModel;

public class CreateDriverActivity extends AppCompatActivity {

    NewDriverActivityBinding binding;
    DriverViewModel driverViewModel;
    private Driver mDriver = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewDriverActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        iniView();
    }
    private void iniView(){

        driverViewModel = new ViewModelProvider(this).get(DriverViewModel.class);
        driverViewModel.addDriverResult.observe(this, observer);
        // listeners
        ActivityResultLauncher<String> launcher = registerForActivityResult(contract, resultCallback);
        binding.addNewDriverPic.setOnClickListener(view->{
            launcher.launch("image/*");
        });
        binding.addNewDriver.setOnClickListener(view->{
            Driver driver = getDriverData();
            if (driver != null){
                driverViewModel.addNewDriver(driver);
                mDriver = driver;
                resetFields();
                }
        });
        binding.cancelAddDriver.setOnClickListener(view->{
            finish();
        });

    }
    Observer<Boolean> observer = (isDriverAdded)->{
        if (isDriverAdded) {
            Toast.makeText(CreateDriverActivity.this, "تم اضافة السائق", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Driver.COLLECTION, (Parcelable) mDriver);
            setResult(RESULT_OK, intent);
        }
        else Toast.makeText(CreateDriverActivity.this, "لم يتم اضافة السائق", Toast.LENGTH_SHORT).show();

    };
    /**
     *
     */
    private void resetFields(){
        binding.newDriverName.setText("");
        binding.newDriverPhone.setText("");
        binding.newDriverEmail.setText("");
        binding.newDriverAddress.setText("");
        binding.newDriverAge.setText("");
        binding.newDriverUsername.setText("");
        binding.newDriverPassword.setText("");
    }
    /**
     *
     * @return
     */
    private Driver getDriverData(){
        Driver driver = new Driver();
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
        driver.name = binding.newDriverName.getText().toString();
        driver.phone = binding.newDriverPhone.getText().toString();
        driver.email = binding.newDriverEmail.getText().toString();
        driver.address = binding.newDriverAddress.getText().toString();
        driver.age = Integer.valueOf(binding.newDriverAge.getText().toString());
        driver.username = binding.newDriverUsername.getText().toString();
        driver.password = binding.newDriverPassword.getText().toString();
        return driver;
    }
    private boolean isAnyFieldEmpty(){
        if (binding.newDriverName.getText().toString().isEmpty()
            || binding.newDriverPhone.getText().toString().isEmpty()
            || binding.newDriverEmail.getText().toString().isEmpty()
            || binding.newDriverAddress.getText().toString().isEmpty()
            || binding.newDriverAge.getText().toString().isEmpty()
            || binding.newDriverUsername.getText().toString().isEmpty()
            || binding.newDriverPassword.getText().toString().isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        driverViewModel = null;
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
     * contract for getting results from selecting image app
     */
    ActivityResultContract<String, Uri> contract = new ActivityResultContract<String, Uri>() {
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
    ActivityResultCallback<Uri> resultCallback = new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            binding.newDriverPic.setImageURI(result);
        }
    };
}
