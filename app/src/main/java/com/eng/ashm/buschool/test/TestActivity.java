package com.eng.ashm.buschool.test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ManagementNewDriverActivityBinding;
import com.eng.ashm.buschool.databinding.ManagementNewParentActivityBinding;
import com.eng.ashm.buschool.databinding.ParentProfileActivityBinding;
import com.eng.ashm.buschool.databinding.TestActivityBinding;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.databinding.TripProfileLayoutBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.fragment.parent.ParentTripFragment;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    TestActivityBinding binding;
    TripListItemBinding tripItemBinding;
    ManagementNewDriverActivityBinding newDriverActivityBinding;
    ManagementNewParentActivityBinding parentActivityBinding;
    ParentProfileActivityBinding parentProfileActivityBinding;
    TripProfileLayoutBinding tripProfilelayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newDriverActivityBinding = ManagementNewDriverActivityBinding.inflate(getLayoutInflater());
        binding = TestActivityBinding.inflate(getLayoutInflater());
        tripItemBinding = TripListItemBinding.inflate(getLayoutInflater());
        parentProfileActivityBinding = ParentProfileActivityBinding.inflate(getLayoutInflater());
        parentActivityBinding = ManagementNewParentActivityBinding.inflate(getLayoutInflater());
        tripProfilelayout = TripProfileLayoutBinding.inflate(getLayoutInflater());
        View v = tripItemBinding.testTripItemNum;
        String transName = tripItemBinding.testTripItemNum.getTransitionName();
        setContentView(tripProfilelayout.getRoot());

        RecyclerTestFragment listFragment = new RecyclerTestFragment();
        listFragment.addOnTripSelectedListener(trip ->
                showFragment(new TestTripItemFragment(trip), binding.tripFragmentContainer.getId(),v, transName ));
       // showFragment(listFragment, binding.tripListFragment.getId());
    }
    /**
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment, @IdRes int fragmentContainerID, View v, String transName){
        Transition transition = new Fade() ;
        fragment.setEnterTransition(transition);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContainerID,fragment )
                   .setReorderingAllowed(true)
                 //  .addSharedElement(v, transName)
                   .commit();

    }
    public static final int EXPAND = 1;
    public static final int COLLAPSE = 2;
    /**
     *
     * @param view
     * @param transition
     * @param height
     */
    private void expandAnimation(View view, int transition, final int height){
    }
    /**
     *
     * @param v
     */
    private void zoomIn(View v){
        if (v.getAnimation() != null){
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }
        v.animate().scaleX(2).scaleY(2).start();
    }
    /**
     *
     * @param v
     */
    private void zoomOut(View v){
        if (v.getAnimation() != null)
        {
            v.getAnimation().reset();
            //binding.name.setText("reset animation");
        }
        v.animate().scaleX(0.5f).scaleY(0.5f).start();
    }
    /**
     *
     */
    private void transit(){
        Scene scene = Scene.getSceneForLayout(binding.getRoot(), R.layout.trip_profile_layout,this);
        Transition transition  =new Slide();
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        transition.setDuration(2000);
        TransitionManager.go(scene, transition);
    }
    /**
     *
     * @return
     */
    private ArrayList<Trip> createTestTripList() {
        ArrayList<Trip> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(createTrip(i));
        }
        return list;
    }

    /**
     *
     * @param i
     * @return
     */
    private Trip createTrip(int i) {
        Trip t = new Trip();
        t.tripNum = i;
        t.tripDate = "date : " + i;
        t.startTime = "بداية توقيت الرحلة : " + i;
        t.endTime = "نهاية توقيت الرحلة : " + i + 1;
        t.tripState = (i % 2 == 0) ? "مستمرة" : "انتهت";
        //car data
        t.bus.carNum = "رقم السيارة = " + i;
        t.bus.kind = "نوع السيارة : " + i;
        // driver data
        t.tripDriver.name = (i % 2 == 0) ? "امانى" : "امنية";
        t.tripDriver.phone = "0514444411";
        return t;
    }
}
