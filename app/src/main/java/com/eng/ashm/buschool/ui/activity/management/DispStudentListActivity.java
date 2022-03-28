package com.eng.ashm.buschool.ui.activity.management;

import static com.eng.ashm.buschool.MapsActivity.SOURCE_OF_MAP;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.MainActivity;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.DispStudentListActivityBinding;
import com.eng.ashm.buschool.ui.activity.profile.CarProfileActivity;
import com.eng.ashm.buschool.ui.activity.profile.StudentProfileActivity;
import com.eng.ashm.buschool.ui.adapter.StudentAdapter;
import com.eng.ashm.buschool.ui.viewmodel.StudentViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DispStudentListActivity extends AppCompatActivity {

    DispStudentListActivityBinding binding = null;
    StudentAdapter studentAdapter = null;
    StudentViewModel studentViewModel = null;
    private ActivityResultLauncher<Object> launcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DispStudentListActivityBinding.inflate(getLayoutInflater());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        binding.manageStudentListRv.addItemDecoration(dividerItemDecoration);
        setContentView(binding.getRoot());
        launcher = registerForActivityResult(contract, resultCallback);
        initAdapter();
        initViewModel();
        binding.manageStudentListRv.setAdapter(studentAdapter);
        binding.manageStudentListRv.setLayoutManager(new LinearLayoutManager(this));
        binding.manageAddNewStudent.setOnClickListener(v->{
            startActivity(new Intent(DispStudentListActivity.this, CreateStudentActivity.class));
        });
    }

    private void initAdapter(){
        if (studentAdapter == null)
        studentAdapter = new StudentAdapter(new ArrayList<>());
        studentAdapter.setOnItemClickListener((OnItemClickListener<Student>)
                student -> {
            Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
            intent.putExtra(Student.COLLECTION, (Serializable) student);
            startActivity(intent);
        });
    }
    private void initViewModel(){
        if(studentViewModel == null)
            studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents();
        studentViewModel.requestStudentListResult.observe(this, students ->{
            if ( students != null && !students.isEmpty()){
                if (students.get(0).getClass().equals(Student.class))
                studentAdapter.updateStudentList((ArrayList<Student>) students);
            }
        });
    }
    // start activity
    ActivityResultContract<Object , Student> contract = new ActivityResultContract<Object, Student>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(DispStudentListActivity.this, CreateStudentActivity.class);
            intent.putExtra(SOURCE_OF_MAP, Student.COLLECTION);
            return intent;
        }

        @Override
        public Student parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return  intent.getParcelableExtra(Student.COLLECTION);
            }
            return null;
        }
    };
    ActivityResultCallback<Student> resultCallback = student -> {
        if (student != null){
            if (student.getClass().equals(Student.class))
            studentAdapter.addStudent(student);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /**
     *
     * @return
     */
    private ArrayList<Student> createTestStudentList(){
        ArrayList<Student> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Student t = new Student();
            t.name = "طالب رقم : "+i;
            t.address = "عنوان الطالب";
            list.add(t);
        }
        return list;
    }
}
