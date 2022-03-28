package com.eng.ashm.buschool.ui.fragment.driver;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DriverStudentListFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.StudentAdapter;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.viewmodel.DriverViewModel;
import com.eng.ashm.buschool.ui.viewmodel.StudentViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayStudentsFragment extends Fragment {

    DriverStudentListFragmentBinding studentListBinding;
    StudentViewModel studentViewModel;
    StudentAdapter studentAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       studentListBinding = DriverStudentListFragmentBinding.inflate(inflater);
       if (studentAdapter == null)
           studentAdapter = new StudentAdapter(new ArrayList<>());
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents();
        studentViewModel.requestStudentListResult.observe(getViewLifecycleOwner() ,
                students -> studentAdapter.updateStudentList((ArrayList<Student>) students));

        studentListBinding.driverStudentListRv.setAdapter(studentAdapter);
       studentListBinding.driverStudentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return studentListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        studentListBinding = null;
        studentAdapter = null;
    }

    private ArrayList<Student> createTestStudentList(){
        ArrayList<Student> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Student t = new Student();
            t.name = "طالب رقم : "+i;
            t.address = "عنوان مصر";
            list.add(t);
        }
        return list;
    }
}
