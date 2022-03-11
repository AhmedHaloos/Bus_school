package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.ui.animation.ListItemAnimation;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripListViewHolder> {

    private final ArrayList<Trip> tripList = new ArrayList<>();
    private OnItemClickListener <Trip>itemClickListener = null;


    //constructors
    public TripAdapter(){}
    public TripAdapter(ArrayList<Trip> items) {
        tripList.addAll(items);
    }

    @Override
    public TripListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TripListViewHolder(TripListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final TripListViewHolder holder, int position) {
        //trip data
        holder.tripItemBinding.tripItemNum.setText("رحلة رقم : " + tripList.get(position).tripNum);
        holder.tripItemBinding.tripItemState.setText(tripList.get(position).tripState);
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
    private void reOrderList(){
        for (Trip trip: tripList) {

        }
    }
    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class TripListViewHolder extends RecyclerView.ViewHolder {
        TripListItemBinding tripItemBinding;
        public TripListViewHolder(@NonNull View itemView) {
            super(itemView);
            tripItemBinding = TripListItemBinding.bind(itemView);
            tripItemBinding.parentTripItemCardview.setOnClickListener(cardClickListener);
            //tripItemBinding.parentItemCarProfileBtn.setOnClickListener(carProfileListener);
            //tripItemBinding.parentItemDriverProfileBtn.setOnClickListener(driverProfileListener);
            tripItemBinding.tripItemExpandBtn.setOnClickListener(expandViewListener);
          //  tripItemBinding.parentDisplayTripBtn.setOnClickListener(displayTripListener);
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
        /**
         *
         */
        View.OnClickListener expandViewListener = v -> {
            int state = tripItemBinding.parentItemDetailsLayout.getVisibility();
            int heightOfView = tripItemBinding.parentItemDetailsLayout.getHeight();
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
        };

    }
}