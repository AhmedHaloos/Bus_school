package com.eng.ashm.buschool.ui.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eng.ashm.buschool.data.datamodel.DataCollections;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.StudentProfileActivityBinding;

public class StudentProfileActivity extends AppCompatActivity {

    StudentProfileActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StudentProfileActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.studentProfileCallBtn.setOnClickListener(studentCallListener);
        Intent intent = getIntent();
        Student student = (Student)intent.getSerializableExtra(Student.COLLECTION);
        displayData(student);
    }

    private void displayData(Student student) {
        if (student == null) {
            Toast.makeText(this, "لا يوجد بيانات للطالب", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.studentProfileName.setText(student.name);
        binding.studentProfilePhone.setText(student.phone);
        binding.studentProfileEmail.setText(student.email);
        binding.studentProfileAge.setText(student.age+"");
        binding.studentProfileAddress.setText(student.address);
        binding.studentProfileParentName.setText(student.parentName);
        binding.studentProfileYear.setText(student.yearOFStudy+"");
    }
    /**
     *
     */
    View.OnClickListener studentCallListener = new View.OnClickListener() {
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
