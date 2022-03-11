package com.eng.ashm.buschool.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class ProgressState {

    private ViewGroup rootview;
    private ProgressBar progressBar;
    public ProgressState(ViewGroup rootView){
        this.rootview = rootView;
    }
    public void showProgressView(){
        if (rootview.getVisibility() == View.VISIBLE){


        }
        else {

        }
    }
    public void hideProgressView(){}
    private void createProgressBar(){
        progressBar = new ProgressBar(rootview.getContext());
        progressBar.setVisibility(View.GONE);
        rootview.addView(progressBar);
        rootview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
