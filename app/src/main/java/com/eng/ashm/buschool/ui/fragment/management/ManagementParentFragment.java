package com.eng.ashm.buschool.ui.fragment.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.ManagementParentFragmentBinding;
import com.eng.ashm.buschool.ui.adapter.ParentAdapter;
import com.eng.ashm.buschool.ui.adapter.StudentAdapter;

import java.util.ArrayList;

public class ManagementParentFragment extends Fragment {

    ManagementParentFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ManagementParentFragmentBinding.inflate(inflater);
        binding.managementParentRv.setAdapter(new ParentAdapter(createTestParentList()));
        binding.managementParentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.managementStudentRv.setAdapter(new StudentAdapter(createTestStudentList()));
        binding.managementStudentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private ArrayList<Parent> createTestParentList(){
        ArrayList<Parent> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Parent t = new Parent();
            t.name = "ولى أمر رقم : "+i;
            t.phone = "هاتف : 010123452144";
            list.add(t);
        }
        return list;
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
