package com.eng.ashm.buschool;

import static com.eng.ashm.buschool.GlobalConst.DRIVER_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.LAUNCH_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.MANAGEMENT_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.PARENT_LOGIN_ACTIVITY_REQUEST;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eng.ashm.buschool.databinding.ActivityMainBinding;
import com.eng.ashm.buschool.ui.activity.driver.DriverMainActivity;
import com.eng.ashm.buschool.ui.activity.management.ManagementMainActivity;
import com.eng.ashm.buschool.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        binding.managementBtn.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(LAUNCH_LOGIN_ACTIVITY_REQUEST, MANAGEMENT_LOGIN_ACTIVITY_REQUEST);
            startActivity(intent);
        });
        binding.driverBtn.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(LAUNCH_LOGIN_ACTIVITY_REQUEST, DRIVER_LOGIN_ACTIVITY_REQUEST);
            startActivity(intent);
        });
        binding.parentBtn.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(LAUNCH_LOGIN_ACTIVITY_REQUEST, PARENT_LOGIN_ACTIVITY_REQUEST);
            startActivity(intent);
        });
    }
}