package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Car;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.databinding.CarListItemBinding;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {


    private final ArrayList<Car> carList = new ArrayList<>();
    OnItemClickListener<Car> itemClickListener  = null;

    //constructors
    public CarAdapter(){}
    public CarAdapter(ArrayList<Car> items) {
        carList.addAll(items);
    }

    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CarAdapter.CarViewHolder(CarListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final CarViewHolder holder, int position) {
        holder.carItemBinding.busItemColor.setText(String.valueOf(carList.get(position).color));
        holder.carItemBinding.busItemNum.setText(String.valueOf(carList.get(position).carNum));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    /**
     *
     * @param dataList
     */
    public void addCarList(ArrayList<Car> dataList){
        if(dataList != null || !dataList.isEmpty())
            carList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     *
     * @param dataList
     */
    public void updateCarList(ArrayList<Car> dataList){
        carList.clear();
        if(dataList != null || !dataList.isEmpty())
            carList.addAll(dataList);
        notifyDataSetChanged();
    }
    /**
     *
     * @param car
     */
    public void addCar(Car car){
        if (!carList.contains(car))
            carList.add(car);
        notifyItemInserted(carList.size());
    }
    /**
     *
     * @param car
     */
    public void deleteCar(Car car){
        int index = carList.indexOf(car);
        carList.remove(car);
        notifyItemRemoved(index);
    }
    /**
     *
     * @param car
     * @return
     */
    public boolean isExist(Car car){
        if (carList.contains(car))
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


    public class CarViewHolder extends RecyclerView.ViewHolder {
        CarListItemBinding carItemBinding;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carItemBinding = CarListItemBinding.bind(itemView);
            carItemBinding.getRoot().setClickable(true);
            carItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClicked(carList.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}
