package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.DriverListItemBinding;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private final ArrayList<Driver> driverList = new ArrayList<>();
    OnItemClickListener<Driver> itemClickListener;
    //constructors
    public DriverAdapter(){}
    public DriverAdapter(ArrayList<Driver> items) {
        driverList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public DriverAdapter.DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new DriverAdapter.DriverViewHolder(DriverListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final DriverAdapter.DriverViewHolder holder, int position) {
        holder.driverItemBinding.driverItemName.setText(driverList.get(position).name);
        holder.driverItemBinding.driverItemPhone.setText(driverList.get(position).phone);
        }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    /**
     *
     * @param dataList
     */
    public void addDriverList(ArrayList<Driver> dataList){
        for (Driver driver: dataList) {
            if (!driverList.contains(driver))
                driverList.add(driver);
            notifyItemInserted(driverList.size());
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param dataList
     */
    public void updateDriverList(ArrayList<Driver> dataList){
        driverList.clear();
        ArrayList<Driver> list = new ArrayList<>();
        for (Driver driver: dataList) {
            if (!driverList.contains(driver)){
                list.add(driver);
           // notifyItemInserted(driverList.size());
            }
        }
        driverList.addAll(list);
        notifyDataSetChanged();
    }

    public void addDriver(Driver driver){
        if (!driverList.contains(driver))
            driverList.add(driver);
        notifyItemInserted(driverList.size());
    }


    public void deleteDriver(Driver driver){
        int index = driverList.indexOf(driver);
        driverList.remove(driver);
        notifyItemRemoved(index);
    }
    public boolean isExist(Driver driver){
        if (driverList.contains(driver))
            return true;
        return false;
    }

    /**
     *
     */
    private void reOrderList(){
    }
    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class  DriverViewHolder extends RecyclerView.ViewHolder {
        DriverListItemBinding driverItemBinding;
        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            driverItemBinding = DriverListItemBinding.bind(itemView);
            driverItemBinding.getRoot().setClickable(true);
            driverItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                    itemClickListener.onItemClicked(driverList.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}
