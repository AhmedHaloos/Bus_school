package com.eng.ashm.buschool.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;

import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.R;
import com.eng.ashm.buschool.data.datamodel.Trip;
import com.eng.ashm.buschool.databinding.ParentProfileActivityBinding;
import com.eng.ashm.buschool.databinding.TestActivityBinding;
import com.eng.ashm.buschool.databinding.TripListItemBinding;
import com.eng.ashm.buschool.databinding.TripProfileLayoutBinding;
import com.eng.ashm.buschool.ui.adapter.TripAdapter;
import com.eng.ashm.buschool.ui.fragment.parent.ParentTripFragment;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
/*
    public static final String SELECTED_IMAGE = "selected image";
    TestActivityBinding binding;
    TripListItemBinding tripItemBinding;
    ManagementNewDriverActivityBinding newDriverActivityBinding;
    ManagementNewParentActivityBinding parentActivityBinding;
    ParentProfileActivityBinding parentProfileActivityBinding;
    TripProfileLayoutBinding tripProfilelayout;
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* newDriverActivityBinding = ManagementNewDriverActivityBinding.inflate(getLayoutInflater());
        binding = TestActivityBinding.inflate(getLayoutInflater());
        tripItemBinding = TripListItemBinding.inflate(getLayoutInflater());
        parentProfileActivityBinding = ParentProfileActivityBinding.inflate(getLayoutInflater());
        parentActivityBinding = ManagementNewParentActivityBinding.inflate(getLayoutInflater());
        tripProfilelayout = TripProfileLayoutBinding.inflate(getLayoutInflater());
        View v = tripItemBinding.tripItemNum;
        String transName = tripItemBinding.tripItemNum.getTransitionName();
        setContentView(newDriverActivityBinding.getRoot());
        ActivityResultLauncher<String> launcher = registerForActivityResult(contract, resultCallback);
        newDriverActivityBinding.addNewDriverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });

        RecyclerTestFragment listFragment = new RecyclerTestFragment();
        listFragment.addOnTripSelectedListener(trip ->
                showFragment(new TestTripItemFragment(trip), binding.tripFragmentContainer.getId(),v, transName ));
       // showFragment(listFragment, binding.tripListFragment.getId());*/
    }
    ActivityResultContract<String, Uri> contract = new ActivityResultContract<String, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(input);
            return intent;
        }

        @Override
        public Uri parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
               return intent.getData();
            }
            return null;
        }
    };
    ActivityResultCallback<Uri> resultCallback = new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
           // newDriverActivityBinding.newDriverPic.setImageURI(result);
        }
    };
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
    private Drawable pickImage(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
return null;
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
        /*Scene scene = Scene.getSceneForLayout(binding.getRoot(), R.layout.trip_profile_layout,this);
        Transition transition  =new Slide();
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        transition.setDuration(2000);
        TransitionManager.go(scene, transition);*/
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
