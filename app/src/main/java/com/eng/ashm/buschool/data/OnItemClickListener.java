package com.eng.ashm.buschool.data;

public interface OnItemClickListener<T> {
    void onItemClicked(T t);
    default void onItemBtnClicked(T t){}
}
