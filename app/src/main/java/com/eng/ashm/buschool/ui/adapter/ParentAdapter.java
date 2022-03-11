package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.OnItemClickListener;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ParentListItemBinding;

import java.util.ArrayList;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private final ArrayList<Parent> parentList = new ArrayList<>();
    private OnItemClickListener<Parent> itemClickListener = null;
    public ParentAdapter(){}
    public ParentAdapter(ArrayList<Parent> items) {
        parentList.addAll(items);
    }

    @Override
    public ParentAdapter.ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ParentAdapter.ParentViewHolder(ParentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(final ParentViewHolder holder, int position) {
         holder.parentItemBinding.parentItemName.setText(String.valueOf(parentList.get(position).name));
        holder.parentItemBinding.parentItemPhone.setText(String.valueOf(parentList.get(position).phone));
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }
    /**
     *
     * @param dataList
     */
    public void addParentList(ArrayList<Parent> dataList){
        for (Parent parent: dataList) {
            if (!parentList.contains(parent))
                parentList.add(parent);
            notifyItemInserted(parentList.size());
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param dataList
     */
    public void updateParentList(ArrayList<Parent> dataList){
        parentList.clear();
        for (Parent parent: dataList) {
            if (!parentList.contains(parent))
            {
                parentList.add(parent);
                notifyItemInserted(parentList.size());
            }
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param parent
     */
    public void addParent(Parent parent){
        if (!parentList.contains(parent))
            parentList.add(parent);
        notifyItemInserted(parentList.size());
    }

    /**
     *
     * @param parent
     */
    public void deleteParent(Parent parent){
        int index = parentList.indexOf(parent);
        parentList.remove(parent);
        notifyItemRemoved(index);
    }
    public boolean isExist(Parent parent){
        if (parentList.contains(parent))
            return true;
        return false;
    }
    private void reOrderList(){
        for (Parent parent: parentList) {

        }
    }
    /**
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    public class ParentViewHolder extends RecyclerView.ViewHolder {
        ParentListItemBinding parentItemBinding;
        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            parentItemBinding =ParentListItemBinding.bind(itemView);
            parentItemBinding.getRoot().setClickable(true);
            parentItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClicked(parentList.get(getAbsoluteAdapterPosition()));
                }
            });
        }
    }
}
