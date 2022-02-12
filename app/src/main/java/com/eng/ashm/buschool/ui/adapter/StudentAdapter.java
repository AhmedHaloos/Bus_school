package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.ManagementStudentItemBinding;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final ArrayList<Student> studentList = new ArrayList<>();

    public StudentAdapter(ArrayList<Student> items) {
        studentList.addAll(items);
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new StudentViewHolder(ManagementStudentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final StudentViewHolder holder, int position) {
        holder.studentItemBinding.managementStudentName.setText(String.valueOf(studentList.get(position).name));
        holder.studentItemBinding.managementStudentAddress.setText(String.valueOf(studentList.get(position).address));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        ManagementStudentItemBinding studentItemBinding;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentItemBinding = ManagementStudentItemBinding.bind(itemView);
            studentItemBinding.getRoot().setClickable(true);
        }
    }
}
