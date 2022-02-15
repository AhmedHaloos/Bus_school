package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.ui.animation.ListItemAnimation;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripListViewHolder> {

    private final ArrayList<Trip> tripList = new ArrayList<>();
    private OnItemClickListener itemClickListener = null;
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
        holder.tripItemBinding.testTripItemNum.setText("رحلة رقم : " + tripList.get(position).tripNum);
        holder.tripItemBinding.testTripItemState.setText(tripList.get(position).tripState);
        holder.tripItemBinding.parentItemTripStDateTv.setText(tripList.get(position).startTime);
        holder.tripItemBinding.parentItemTripEndDateTv.setText(tripList.get(position).endTime);
        // car data
        holder.tripItemBinding.parentItemCarKindTv.setText(tripList.get(position).bus.kind);
        holder.tripItemBinding.parentItemCarNumTv.setText("باص رقم : "+ tripList.get(position).bus.carNum);
        //driver data
        holder.tripItemBinding.parentItemDriverNameTv.setText(tripList.get(position).tripDriver.name);
        holder.tripItemBinding.parentItemDriverPhoneTv.setText(tripList.get(position).tripDriver.phone);

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    /**
     *
     */
    public interface OnItemClickListener{
        void onItemClicked(Trip trip);
    }

    public class TripListViewHolder extends RecyclerView.ViewHolder {
        TripListItemBinding tripItemBinding;
        public TripListViewHolder(@NonNull View itemView) {
            super(itemView);
            tripItemBinding = TripListItemBinding.bind(itemView);
            tripItemBinding.parentTripItemCardview.setOnClickListener(cardClickListener);
            tripItemBinding.parentItemCarProfileBtn.setOnClickListener(carProfileListener);
            tripItemBinding.parentItemDriverProfileBtn.setOnClickListener(driverProfileListener);
            tripItemBinding.parentItemExpandBtn.setOnClickListener(expandViewListener);
            tripItemBinding.parentDisplayTripBtn.setOnClickListener(displayTripListener);
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
                tripItemBinding.parentItemExpandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, 0, 0);
                ViewPropertyAnimator animator = tripItemBinding.parentItemExpandBtn.animate();
                if (animator != null) animator.cancel();
                animator.rotation(180).setDuration(300);
            } else if (view.getVisibility() == View.GONE) {
                ListItemAnimation.expand(view);
                tripItemBinding.parentItemExpandBtn
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_arrow, 0, 0, 0);
                ViewPropertyAnimator animator = tripItemBinding.parentItemExpandBtn.animate();
                if (animator != null) animator.cancel();
                animator.rotation(180).setDuration(300);
            }

        };

        View.OnClickListener displayTripListener = v -> {

        };

    }
}