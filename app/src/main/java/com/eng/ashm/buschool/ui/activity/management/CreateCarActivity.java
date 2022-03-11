package com.eng.ashm.buschool.ui.activity.management;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.databinding.NewBusActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.CarViewModel;

public class CreateCarActivity extends AppCompatActivity {
    NewBusActivityBinding binding;
    CarViewModel carViewModel;
    private Car mBus = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding == null)
        binding = NewBusActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        binding.cancelAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.addNewBus.setOnClickListener(addCarListener);
        if (carViewModel == null)
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        carViewModel.addCarResult.observe(this,observer);
    }
    /**
     *
     */
    View.OnClickListener addCarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Car bus = getCarData();
            carViewModel.addNewCar(bus);
            carViewModel.addCarResult.observe(CreateCarActivity.this, observer);
            if (bus != null)
            mBus = bus;
        }
    };
    Observer<Boolean> observer = isCarAdded -> {
        if (isCarAdded){
            Toast.makeText(CreateCarActivity.this, "تم اضافة الحافلة", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Car.COLLECTION, (Parcelable) mBus);
            setResult(RESULT_OK, intent);

        }
        else {
            Toast.makeText(CreateCarActivity.this, "لم يتم اضافة الحافلة", Toast.LENGTH_SHORT).show();
        }
        resetFields();
    };

    /**
     *
     */
    private void resetFields(){
        binding.newCarColor.setText("");
        binding.newCarKind.setText("");
        binding.newCarNum.setText("");
        binding.newCarModel.setText("");
        binding.newCarSeats.setText("");
    }
    /**
     *
     * @return
     */
    private Car getCarData(){
        Car bus = new Car();
        if(isAnyFieldEmpty()){
            new AlertDialog.Builder(this)
                    .setMessage("يجب ملء جميع البيانات للباص ")
                    .setTitle("خطأ")
                    .setPositiveButton("موافق", (dialog, which) -> {

                    })
                    .setCancelable(true)
                    .create()
                    .show();
            return null;
        }

        bus.kind = binding.newCarKind.getText().toString();
        bus.model = binding.newCarModel.getText().toString();
        bus.color = binding.newCarColor.getText().toString();
        bus.carNum = binding.newCarNum.getText().toString();
        bus.noOfSeats = Integer.valueOf(binding.newCarSeats.getText().toString());
        return bus;
    }
    /**
     *
     * @return
     */
    private boolean isAnyFieldEmpty(){
              if(  binding.newCarKind.getText().toString().isEmpty()
                || binding.newCarModel.getText().toString().isEmpty()
                || binding.newCarColor.getText().toString().isEmpty()
                || binding.newCarSeats.getText().toString().isEmpty()
                || binding.newCarNum.getText().toString().isEmpty()){
            return true;
              }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        carViewModel = null;
    }
}
