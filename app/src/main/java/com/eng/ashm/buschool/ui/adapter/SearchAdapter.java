package com.eng.ashm.buschool.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eng.ashm.buschool.data.datamodel.SearchDataModel;
import com.eng.ashm.buschool.databinding.SearchListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH> {

    ArrayList<SearchDataModel> searchList = new ArrayList<>();
    public SearchAdapter(){}
    public SearchAdapter(ArrayList<SearchDataModel> searchList){
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SearchListItemBinding binding = SearchListItemBinding.inflate(inflater, parent, false);
        return new SearchVH(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVH holder, int position) {
        holder.binding.searchItemFirstField.setText(searchList.get(position).firstField);
        holder.binding.searchItemSecondField.setText(searchList.get(position).secondField);
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
    /**
     *
     * @param list
     */
    public void updateSearchList(ArrayList<SearchDataModel> list){
        searchList.clear();
        searchList.addAll(list);
        notifyDataSetChanged();
    }
    /**
     *
     * @return
     */
    public List<SearchDataModel> getUpdatedSearchList(){
        return searchList;
    }
    class SearchVH extends RecyclerView.ViewHolder{
        SearchListItemBinding binding;
        SearchVH(View view){
            super(view);
            binding = SearchListItemBinding.bind(view);
            binding.searchItemSelecttion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        searchList.get(getAbsoluteAdapterPosition()).isSelected = true;
                    else searchList.get(getAbsoluteAdapterPosition()).isSelected = false;
                }
            });
        }
    }
}
