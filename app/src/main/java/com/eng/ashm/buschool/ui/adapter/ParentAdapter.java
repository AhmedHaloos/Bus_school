package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.ManagementParentItemBinding;

import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private final ArrayList<Parent> parentList = new ArrayList<>();

    public ParentAdapter(ArrayList<Parent> items) {
        parentList.addAll(items);
    }

    @Override
    public ParentAdapter.ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentAdapter.ParentViewHolder(ManagementParentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final ParentViewHolder holder, int position) {
         holder.parentItemBinding.managementParentName.setText(String.valueOf(parentList.get(position).name));
        holder.parentItemBinding.managementParentPhone.setText(String.valueOf(parentList.get(position).phone));
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        ManagementParentItemBinding parentItemBinding;
        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            parentItemBinding = ManagementParentItemBinding.bind(itemView);
            parentItemBinding.getRoot().setClickable(true);
        }
    }
}
