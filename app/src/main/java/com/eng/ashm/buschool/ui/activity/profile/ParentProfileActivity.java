package com.eng.ashm.buschool.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.DataCollections;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.ParentProfileActivityBinding;

public class ParentProfileActivity extends AppCompatActivity {

    ParentProfileActivityBinding binding;
    Parent parent = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ParentProfileActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.parentProfileCallBtn.setOnClickListener(parentCallListener);
        Intent intent = getIntent();
        Parent parent = (Parent) intent.getSerializableExtra(Parent.COLLECTION);
        displayData(parent);
    }
    private void displayData(Parent parent){
        if (parent == null){
            Toast.makeText(this, "لا يوجد بيانات ولى الامر", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.parentProfileName.setText(parent.name);
        binding.parentProfileEmail.setText(parent.email);
        binding.parentProfileAddress.setText(parent.address);
        binding.parentProfileJob.setText(parent.job);
        binding.parentProfileAge.setText(parent.age+"");
        binding.parentProfilePhone.setText(parent.phone);
        binding.parentProfileUsername.setText(parent.username);
        binding.parentProfilePassword.setText(parent.password);
    }

    View.OnClickListener parentCallListener = v->{
        if (parent != null)
        {
            String phoneNum = parent.phone;
            Intent intent = new Intent(Intent.ACTION_CALL);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
