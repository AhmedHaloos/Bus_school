package com.eng.ashm.buschool.ui.fragment.management;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.OnItemClickListener;

import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.data.datamodel.Student;
import com.eng.ashm.buschool.databinding.ManagementParentFragmentBinding;
import com.eng.ashm.buschool.ui.activity.management.CreateDriverActivity;
import com.eng.ashm.buschool.ui.activity.management.CreateParentActivity;
import com.eng.ashm.buschool.ui.activity.management.DispStudentListActivity;
import com.eng.ashm.buschool.ui.activity.profile.ParentProfileActivity;
import com.eng.ashm.buschool.ui.adapter.ParentAdapter;
import com.eng.ashm.buschool.ui.viewmodel.ParentViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagementParentFragment extends Fragment {


    ManagementParentFragmentBinding binding;
    ParentAdapter parentAdapter = null;
    ParentViewModel parentViewModel;
    private ActivityResultLauncher<Object> launcher = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(contract, resultCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ManagementParentFragmentBinding.inflate(inflater);
        initAdapter();
        initModelView();
        binding.parentListRv.setAdapter(parentAdapter);
        binding.parentListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.manangeAddNewParent.setOnClickListener(v->{
            launcher.launch(null);
        });
        binding.dispStudentList.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), DispStudentListActivity.class));
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void initAdapter(){
        if (parentAdapter == null)
        parentAdapter = new ParentAdapter();
        parentAdapter.setOnItemClickListener(new OnItemClickListener<Parent>(){
            @Override
            public void onItemClicked(Parent parent) {
                Intent intent = new Intent(getContext(), ParentProfileActivity.class);
                intent.putExtra(Parent.COLLECTION,(Serializable) parent);
                startActivity(intent);
            }
        });
    }
    /**
     *
     */
    private void initModelView(){
        parentViewModel = new ViewModelProvider(this).get(ParentViewModel.class);
        parentViewModel.getAllParents();
        parentViewModel.requestParentListResult.observe(getViewLifecycleOwner(),observer);
    }
    Observer<List<? extends IDataModel>> observer = parents -> {
         parentAdapter.updateParentList((ArrayList<Parent>) parents);
    };
    // start activity
    ActivityResultContract<Object , Parent> contract = new ActivityResultContract<Object, Parent>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object input) {
            Intent intent = new Intent(getActivity(), CreateParentActivity.class);
            return intent;
        }

        @Override
        public Parent parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK){
                return (Parent) intent.getParcelableExtra(Parent.COLLECTION);
            }
            return null;
        }
    };
    ActivityResultCallback<Parent> resultCallback = parent -> {
        if (parent != null)
        parentAdapter.addParent(parent);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentAdapter = null;
        observer = null;
        parentViewModel.requestParentListResult.removeObserver(observer);
        parentViewModel = null;
    }

    private ArrayList<Parent> createTestParentList(){
        ArrayList<Parent> list = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            Parent t = new Parent();
            t.name = "ولى أمر رقم : "+i;
            t.phone = "هاتف : 05522314";
            list.add(t);
        }
        return list;
    }
}
