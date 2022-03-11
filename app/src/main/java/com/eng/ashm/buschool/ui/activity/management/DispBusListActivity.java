package com.eng.ashm.buschool.ui.activity.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DispBusListActivityBinding;
import com.eng.ashm.buschool.ui.activity.profile.CarProfileActivity;
import com.eng.ashm.buschool.ui.adapter.CarAdapter;
import com.eng.ashm.buschool.ui.viewmodel.CarViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DispBusListActivity extends AppCompatActivity {

    DispBusListActivityBinding binding;
    CarAdapter carAdapter = null;
    CarViewModel carViewModel = null;
    private ActivityResultLauncher<Object> launcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DispBusListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        launcher  = registerForActivityResult(contract, resultCallback);
        initAdapter();
        initViewModel();
        binding.manageBusListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.manageBusListRv.setAdapter(carAdapter);
        binding.manageAddNewBus.setOnClickListener(v->{
            launcher.launch(null);
        });
    }
    // start activity
    ActivityResultContract<Object , Car> contract = new ActivityResultContract<Object, Car>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(DispBusListActivity.this, CreateCarActivity.class);
            return intent;
        }

        @Override
        public Car parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return (Car) intent.getParcelableExtra(Car.COLLECTION);
            }
            return null;
        }
    };
    ActivityResultCallback<Car> resultCallback = new ActivityResultCallback<Car>() {
        @Override
        public void onActivityResult(Car car) {
            if(car != null)
            carAdapter.addCar(car);
        }
    };

    private void initAdapter(){
        if(carAdapter == null)
            carAdapter = new CarAdapter(new ArrayList<>());
        carAdapter.setOnItemClickListener(new OnItemClickListener<Car>() {
            @Override
            public void onItemClicked(Car car) {
                Intent intent = new Intent(getApplicationContext(), CarProfileActivity.class);
                intent.putExtra(Car.COLLECTION, (Serializable) car);
                startActivity(intent);
            }
        });
    }
    private void initViewModel(){
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        carViewModel.getAllCars();
       carViewModel.requestCarListResult.observe(this,
                cars ->{
                    Toast.makeText(this, "car list observer called : ", Toast.LENGTH_SHORT).show();
                    //carAdapter.updateCarList((ArrayList<Car>) cars);
        }
                    );

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        carAdapter = null;
        carViewModel = null;
    }

    /**
     *
     * @return
     */
    private ArrayList<Car> createTestCarList(){
        ArrayList<Car> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Car t = new Car();
            t.kind = "نوع الباص : "+i;
            t.carNum ="رقم الباص : " +i;
            list.add(t);
        }
        return list;
    }
}
