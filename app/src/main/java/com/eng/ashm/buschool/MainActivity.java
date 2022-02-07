package com.eng.ashm.buschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.content.Context;
import android.os.Bundle;

import com.eng.ashm.buschool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    private void initView(){
        binding.managementBtn.setOnClickListener((v)->{

        });
        binding.driverBtn.setOnClickListener((v)->{

        });
        binding.parentBtn.setOnClickListener((v)->{

        });
    }
}