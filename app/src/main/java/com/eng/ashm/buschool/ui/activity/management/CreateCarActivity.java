package com.eng.ashm.buschool.ui.activity.management;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.databinding.ManagementNewBusActivityBinding;

public class CreateCarActivity extends AppCompatActivity {
    ManagementNewBusActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ManagementNewBusActivityBinding.inflate(getLayoutInflater());
        binding.addNewBus.setOnClickListener(addCarListener);
    }
    /**
     *
     */
    View.OnClickListener addCarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    /**
     *
     * @return
     */
    private Car getCarData(){
        Car bus = new Car();
        bus.kind = binding.newCarKind.getText().toString();
        bus.model = binding.newCarModel.getText().toString();
        bus.color = Integer.valueOf(binding.newCarColor.getText().toString());
        bus.carNum = binding.newCarNum.getText().toString();
        bus.noOfSeats = Integer.valueOf(binding.newCarSeats.getText().toString());
        return bus;
    }
}
