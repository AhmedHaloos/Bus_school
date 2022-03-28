package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ManagementStudentItemBinding;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final ArrayList<Student> studentList = new ArrayList<>();
    OnItemClickListener<Student> itemClickListener = null;

    //constructors
    public StudentAdapter(){}
    public StudentAdapter(ArrayList<Student> items) {
        studentList.addAll(items);
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new StudentViewHolder(ManagementStudentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final StudentViewHolder holder, int position) {
        holder.studentItemBinding.studentItemName.setText(studentList.get(position).name);
        holder.studentItemBinding.studentItemYear.setText(studentList.get(position).yearOFStudy+"");
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
    /**
     *
     * @param dataList
     */
    public void addStudentList(ArrayList<Student> dataList){
      if (dataList != null || !dataList.isEmpty())
          studentList.addAll(dataList);
        notifyDataSetChanged();
    }
    /**
     *
     * @param dataList
     */
    public void updateStudentList(ArrayList<Student> dataList){
        studentList.clear();
        studentList.addAll(dataList);
        notifyDataSetChanged();
    }
    /**
     *
     * @param student
     */
    public void addStudent(Student student){
        if (!studentList.contains(student))
            studentList.add(student);
        notifyItemInserted(studentList.size());
        notifyDataSetChanged();
    }
    /**
     *
     * @param student
     */
    public void deleteStudent(Student student){
        int index = studentList.indexOf(student);
        studentList.remove(student);
        notifyItemRemoved(index);
    }
    /**
     *
     * @param student
     * @return
     */
    public boolean isExist(Student student){
        if (studentList.contains(student))
            return true;
        return false;
    }
    /**
     *
     */
    private void reOrderList(){
        for (Student student: studentList) {

        }
    }
    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder {
        ManagementStudentItemBinding studentItemBinding;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentItemBinding = ManagementStudentItemBinding.bind(itemView);
            studentItemBinding.getRoot().setClickable(true);
            studentItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClicked(studentList.get(getAbsoluteAdapterPosition()));
                }
            });

        }
    }
}
