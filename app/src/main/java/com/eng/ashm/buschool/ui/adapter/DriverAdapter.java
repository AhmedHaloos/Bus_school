package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.ManagementDriverItemBinding;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private final ArrayList<Driver> driverList = new ArrayList<>();

    public DriverAdapter(ArrayList<Driver> items) {
        driverList.addAll(items);
    }

    @Override
    public DriverAdapter.DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DriverAdapter.DriverViewHolder(ManagementDriverItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final DriverAdapter.DriverViewHolder holder, int position) {
        holder.driverItemBinding.managementDriverName.setText(String.valueOf(driverList.get(position).name));
        holder.driverItemBinding.managementDriverPhone.setText(String.valueOf(driverList.get(position).phone));
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class  DriverViewHolder extends RecyclerView.ViewHolder {
        ManagementDriverItemBinding driverItemBinding;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverItemBinding = ManagementDriverItemBinding.bind(itemView);
            driverItemBinding.getRoot().setClickable(true);
        }
    }
}
