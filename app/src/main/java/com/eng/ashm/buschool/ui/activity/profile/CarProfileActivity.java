package com.eng.ashm.buschool.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.DataCollections;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.CarProfileActivityBinding;

public class CarProfileActivity extends AppCompatActivity {

    CarProfileActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CarProfileActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Car car = (Car)intent.getSerializableExtra(Car.COLLECTION);
        displayData(car);
    }
    private void displayData(Car car){
        if (car == null){
            Toast.makeText(this, "لا يوجد بيانات للباص", Toast.LENGTH_SHORT).show();
            return ;
        }
        binding.carProfileNum.setText(car.carNum);
        binding.carProfileKind.setText(car.kind);
        binding.carProfileModel.setText(car.model);
        binding.carProfileColor.setText(car.color);
        binding.carProfileSeats.setText(car.noOfSeats+"");

    }
}
