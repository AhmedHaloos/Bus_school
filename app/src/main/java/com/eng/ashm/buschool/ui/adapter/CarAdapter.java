package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.databinding.CarListItemBinding;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {


    private final ArrayList<Car> carList = new ArrayList<>();

    public CarAdapter(ArrayList<Car> items) {
        carList.addAll(items);
    }

    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CarAdapter.CarViewHolder(CarListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final CarViewHolder holder, int position) {
        holder.carItemBinding.managementCarKind.setText(String.valueOf(carList.get(position).kind));
        holder.carItemBinding.managementCarNum.setText(String.valueOf(carList.get(position).carNum));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        CarListItemBinding carItemBinding;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carItemBinding = CarListItemBinding.bind(itemView);
            carItemBinding.getRoot().setClickable(true);
        }
    }
}
