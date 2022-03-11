package com.eng.ashm.buschool.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.DataCollections;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.DriverProfileActivityBinding;

public class DriverProfileActivity extends AppCompatActivity {

    DriverProfileActivityBinding binding;
    Driver driver = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DriverProfileActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Driver driver = (Driver)intent.getSerializableExtra(Driver.COLLECTION);
        displayData(driver);
        binding.driverProfileCallBtn.setOnClickListener(callDriverListener);
    }

    private void displayData(Driver driver){
        if (driver == null){
            Toast.makeText(this, "لا يوجد بيانات للسائق", Toast.LENGTH_SHORT).show();
            return ;
        }
        binding.driverProfileName.setText(driver.name);
        binding.driverProfilePhone.setText(driver.phone);
        binding.driverProfileEmail.setText(driver.email);
        binding.driverProfileAddress.setText(driver.address);
        binding.driverProfileAge.setText(driver.age+"");
        binding.driverProfileUsername.setText(driver.username);
        binding.driverProfilePassword.setText(driver.password);
    }
    View.OnClickListener callDriverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
