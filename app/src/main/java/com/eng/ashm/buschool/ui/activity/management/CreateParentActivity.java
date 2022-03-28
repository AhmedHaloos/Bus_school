package com.eng.ashm.buschool.ui.activity.management;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.data.datamodel.LoggedInUser;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.NewParentActivityBinding;
import com.eng.ashm.buschool.ui.viewmodel.LoginViewModel;
import com.eng.ashm.buschool.ui.viewmodel.ParentViewModel;

public class CreateParentActivity extends AppCompatActivity {

    NewParentActivityBinding binding;
    ParentViewModel parentViewModel;
    LoginViewModel loginViewModel;
    private Parent mParent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewParentActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView(){

        parentViewModel = new ViewModelProvider(this).get(ParentViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        parentViewModel.addParentResult.observe(this, addParentObserver);
        loginViewModel.addLoggedUserObservable.observe(this, addLoggedInParent);
        ActivityResultLauncher<String> launcher = registerForActivityResult(contract, resultCallback);
       //listeners
        binding.addNewParent.setOnClickListener(v->{
            Parent parent = getParentData();
            if (parent != null){
                parentViewModel.addNewParent(parent);
                loginViewModel.addLoggedInUser(getLoggedUser(parent));
                mParent = parent;
                resetFields();
                }
        });
        binding.cancelAddParent.setOnClickListener(v->{
            finish();
        });
        binding.addNewParentPic.setOnClickListener(v->{
            launcher.launch("image/*");
        });
    }
    /**
     *
     * @param parent
     * @return
     */
    private LoggedInUser getLoggedUser(Parent parent){
        LoggedInUser user = new LoggedInUser();
        user.phone = parent.phone;
        user.username = parent.username;
        user.email = parent.email;
        user.isLoggedIn = true;
        user.password = parent.password;
        user.userCollection = Parent.COLLECTION;
        return user;
    }
    Observer<Boolean> addLoggedInParent = isAdded -> {
        if (isAdded)
            Toast.makeText(this, "تم اضافة حساب ولى الامر", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "لم يتم اضافة حساب ولى الامر", Toast.LENGTH_SHORT).show();
    };
    /**
     * add new parent profile addParentObserver
     */
    Observer<Boolean> addParentObserver = (isParentAdded)->{
        if(isParentAdded) {
            Toast.makeText(CreateParentActivity.this, "تم اضافة ولى الامر", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Parent.COLLECTION, (Parcelable) mParent);
            setResult(RESULT_OK, intent);
        }
        else Toast.makeText(CreateParentActivity.this, "لم يتم اضافة ولى الامر", Toast.LENGTH_SHORT).show();

    };
    /**
     * contract for getting results from selecting image app
     */
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
            binding.newParentPic.setImageURI(result);
        }
    };
    /**
     *
     */
    private void resetFields(){
        binding.newParentAddress.setText("");
        binding.newParentName.setText("");
        binding.newParentPhone.setText("");
        binding.newParentEmail.setText("");
        binding.newParentJob.setText("");
        binding.newParentRelation.setText("");
        binding.newParentAge.setText("");
        binding.newParentUsername.setText("");
        binding.newParentPassword.setText("");

    }
    /**
     *
     * @return
     */
    private Parent getParentData(){
        Parent parent = new Parent();
        if (!isFieldsFill()){
            new AlertDialog.Builder(this)
                    .setMessage("يجب ملء جميع البيانات لولى الامر ")
                    .setTitle("خطأ")
                    .setPositiveButton("موافق", (dialog, which) -> {

                    })
                    .setCancelable(true)
                    .create()
                    .show();
            return null;
        }
        parent.name = binding.newParentName.getText().toString();
        parent.phone = binding.newParentPhone.getText().toString();
        parent.email = binding.newParentEmail.getText().toString();
        parent.address = binding.newParentAddress.getText().toString();
        parent.age = Integer.valueOf(binding.newParentAge.getText().toString());
        parent.job = binding.newParentJob.getText().toString();
        parent.relation = binding.newParentRelation.getText().toString();
        parent.username = binding.newParentUsername.getText().toString();
        parent.password = binding.newParentPassword.getText().toString();
        return parent;
    }
    private boolean isFieldsFill(){

            if (   binding.newParentName.getText().toString().isEmpty()
                || binding.newParentPhone.getText().toString().isEmpty()
                || binding.newParentEmail.getText().toString().isEmpty()
                || binding.newParentAddress.getText().toString().isEmpty()
                || binding.newParentAge.getText().toString().isEmpty()
                || binding.newParentUsername.getText().toString().isEmpty()
                || binding.newParentPassword.getText().toString().isEmpty()
                || binding.newParentRelation.getText().toString().isEmpty()
                || binding.newParentJob.getText().toString().isEmpty()){
            return false;
             }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        parentViewModel = null;
    }
}
