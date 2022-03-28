package com.eng.ashm.buschool.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.ItemLongClickListener;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.ui.animation.ListItemAnimation;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripListViewHolder> {

    private final ArrayList<Trip> tripList = new ArrayList<>();
    private OnItemClickListener <Trip>itemClickListener = null;
    private ItemLongClickListener itemLongClickListener = null;
    private String tripSource = null;


    //constructors
    public TripAdapter(){}
    public TripAdapter(String tripSource) {
        this.tripSource = tripSource;
    }

    @Override
    public TripListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TripListViewHolder(TripListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final TripListViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        StringBuilder tripState = null;
        if (trip.tripState.equals(Trip.ACTIVE_TRIP)){
            holder.tripItemBinding.tripItemDisplayMap.setBackgroundColor(Color.GREEN);
            tripState = new StringBuilder("جارية الان");
        }
        else if (trip.tripState.equals(Trip.INACTIVE_TRIP)){
            holder.tripItemBinding.tripItemDisplayMap.setBackgroundColor(Color.GRAY);
            holder.tripItemBinding.tripItemDisplayMap.setEnabled(false);
            tripState = new StringBuilder("ليست جارية الان");
        }
        if (tripSource != null && tripSource.equals(Driver.COLLECTION)){
            holder.tripItemBinding.tripItemDisplayMap.setText("أبدأ الرحلة");
            holder.tripItemBinding.tripItemDisplayMap.setEnabled(true);
        }
        //trip data
        holder.tripItemBinding.tripItemNum.setText("رحلة رقم : " + trip.tripNum);
        holder.tripItemBinding.tripItemState.setText(tripState.toString());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    /**
     *
     * @param dataList
     */
    public void addTripList(ArrayList<Trip> dataList){
        for (Trip trip: dataList) {
            if (!tripList.contains(trip))
                tripList.add(trip);
            //notifyItemInserted(tripList.size());
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param dataList
     */
    public void updateTripList(ArrayList<Trip> dataList){
        tripList.clear();
        for (Trip trip: dataList) {
            if (!tripList.contains(trip))
            {
                tripList.add(trip);
            notifyItemInserted(tripList.size());
            }
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param trip
     */
    public void addTrip(Trip trip){
        if (!tripList.contains(trip))
        tripList.add(trip);
        notifyItemInserted(tripList.size());
        notifyDataSetChanged();
    }

    /**
     *
     * @param trip
     */
    public void deleteTrip(Trip trip){
        int index = tripList.indexOf(trip);
        tripList.remove(trip);
        notifyItemRemoved(index);
    }
    public boolean isExist(Trip trip){
        if (tripList.contains(trip))
            return true;
        return false;
    }
    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public void setOnItemLongClick(ItemLongClickListener itemLongClickListener){
        this.itemLongClickListener = itemLongClickListener;
    }

    public class TripListViewHolder extends RecyclerView.ViewHolder {
        TripListItemBinding tripItemBinding;
        public TripListViewHolder(@NonNull View itemView) {
            super(itemView);
            tripItemBinding = TripListItemBinding.bind(itemView);
            tripItemBinding.parentTripItemCardview.setOnClickListener(cardClickListener);
            tripItemBinding.tripItemExpandBtn.setOnClickListener(expandViewListener);
            tripItemBinding.tripItemDisplayMap.setOnClickListener(displayTripListener);
        }

        /**
         *
         */
        View.OnClickListener cardClickListener = v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(tripList.get(getAbsoluteAdapterPosition()));
        };
        View.OnClickListener carProfileListener = v -> {

        };
        View.OnClickListener driverProfileListener = v -> {

        };
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Trip trip = tripList.get(getAbsoluteAdapterPosition());
                if (itemLongClickListener != null){
                    itemLongClickListener.onItemLongClick(trip);
                }
                return true;
            }
        };
        /**
         *
         */
        View.OnClickListener expandViewListener = v -> {
            View view = tripItemBinding.parentItemDetailsLayout;

            if (view.getVisibility() == View.VISIBLE) {
                ListItemAnimation.collapse(view);
                tripItemBinding.tripItemExpandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
                ViewPropertyAnimator animator = tripItemBinding.tripItemExpandBtn.animate();
                if (animator != null) animator.cancel();
                animator.rotation(180).setDuration(300);
            } else if (view.getVisibility() == View.GONE) {
                ListItemAnimation.expand(view);
                tripItemBinding.tripItemExpandBtn
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow, 0, 0, 0);
                ViewPropertyAnimator animator = tripItemBinding.tripItemExpandBtn.animate();
                if (animator != null) animator.cancel();
                animator.rotation(180).setDuration(300);
            }
        };
        View.OnClickListener displayTripListener = v -> {
            itemClickListener.onItemBtnClicked(tripList.get(getAbsoluteAdapterPosition()));
        };

    }
}