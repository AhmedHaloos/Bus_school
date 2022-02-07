package com.eng.ashm.buschool.ui.activity.management;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.databinding.ManagementActivityBinding;

public class ManagementMainActivity extends AppCompatActivity {

    ManagementActivityBinding managementBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managementBinding = ManagementActivityBinding.inflate(getLayoutInflater());
        setContentView(managementBinding.getRoot());
    }
}
