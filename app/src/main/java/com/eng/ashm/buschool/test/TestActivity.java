package com.eng.ashm.buschool.test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.transition.Scene;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.TestActivityBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    TestActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.testLinearLayout.measure(MATCH_PARENT, WRAP_CONTENT);
        final int height =  binding.testLinearLayout.getMeasuredHeight();

    }
    public static final int EXPAND = 1;
    public static final int COLLAPSE = 2;

    private void expandAnimation(View view, int transition, final int height){
    }

    private void zoomIn(View v){
        if (v.getAnimation() != null){
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }

        v.animate().scaleX(2).scaleY(2).start();
    }
    private void zoomOut(View v){
        if (v.getAnimation() != null)
        {
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }
        v.animate().scaleX(0.5f).scaleY(0.5f).start();
    }
    private void transit(@LayoutRes int secondRes, int rootRes){
    }

}
