package com.eng.ashm.buschool.ui.login;

import static com.eng.ashm.buschool.GlobalConst.DRIVER_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.ERROR_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.LAUNCH_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.MANAGEMENT_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.PARENT_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.USER_PROFILE_KEY;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eng.ashm.buschool.data.FirestoreDataSource;
import com.eng.ashm.buschool.data.FirestroreRepository;
import com.eng.ashm.buschool.data.IDataModel;
import com.eng.ashm.buschool.data.MainRepository;
import com.eng.ashm.buschool.data.Result;
import com.eng.ashm.buschool.data.datamodel.Driver;
import com.eng.ashm.buschool.data.datamodel.LoggedInUser;
import com.eng.ashm.buschool.data.datamodel.Parent;
import com.eng.ashm.buschool.databinding.LoginLayoutBinding;
import com.eng.ashm.buschool.ui.activity.driver.DriverMainActivity;
import com.eng.ashm.buschool.ui.activity.management.ManagementMainActivity;
import com.eng.ashm.buschool.ui.activity.parent.ParentMainActivity;
import com.eng.ashm.buschool.ui.viewmodel.LoginViewModel;

import java.util.Observable;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginLayoutBinding loginBinding;
    private int requestDiv = ERROR_REQUEST;
    private String loginCollection = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = LoginLayoutBinding.inflate(getLayoutInflater());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(loginBinding.getRoot());
        Intent intent = getIntent();
        requestDiv = intent.getIntExtra(LAUNCH_LOGIN_ACTIVITY_REQUEST , ERROR_REQUEST);
        loginBinding.loginBtn.setOnClickListener(loginClickListener);
    }
    /**************** Observer *************/
    Observer<IDataModel> loginObserver  = dataModel -> {
        if(dataModel != null){
            if (dataModel instanceof LoggedInUser){
                //for test
                LoggedInUser user = (LoggedInUser) dataModel;
                if (user.username.equals("test")){
                    switch (requestDiv){
                        case DRIVER_LOGIN_ACTIVITY_REQUEST:
                         user.userCollection = Driver.COLLECTION;
                            break;
                        case PARENT_LOGIN_ACTIVITY_REQUEST:
                            user.userCollection = Parent.COLLECTION;
                            break;
                        case MANAGEMENT_LOGIN_ACTIVITY_REQUEST:
                        user.userCollection = ManagementMainActivity.MANAG_COLLECTION;
                            break;
                    }
                }

                launchUserActivity(dataModel);
            }
        }
        else
            Toast.makeText(this, " : اسم المستخدم او كلمة السر خطأ", Toast.LENGTH_SHORT).show();
    };
    /**
     *  1 - get the username and password from the user
     *  2 - request for login auth.
     *  3 -  if the user is authenticated the direct the user to the acc.
     *  4 - else if not ->> no reply
     */
    View.OnClickListener loginClickListener = v -> {
       login();
    };
    /**
     * login to the user using the username an password
     */
    private void login(){
        String username = loginBinding.username.getText().toString();
        String password = loginBinding.password.getText().toString();
        if (username.isEmpty() || username == null){
            Toast.makeText(this, "يجب ادخال اسم المستخدم", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (password.isEmpty() || password == null){
            Toast.makeText(this, "يجب ادخال كلمة السر", Toast.LENGTH_SHORT).show();
            return ;
        }
       loginViewModel.login(username, password);
        loginViewModel.loggedInUser.observe(this, loginObserver);
    }
    /**
     *
     * @param dataModel
     */
    private void launchUserActivity(IDataModel dataModel){
        Intent intent ;
        switch (requestDiv){
            case DRIVER_LOGIN_ACTIVITY_REQUEST:
                //auth. driver login
                intent  = new Intent(LoginActivity.this, DriverMainActivity.class);
                intent.putExtra(USER_PROFILE_KEY, (Parcelable) dataModel);
                if (((LoggedInUser)dataModel).userCollection.equals(Driver.COLLECTION)){
                    Toast.makeText(this, "تم تسجيل الدخول ", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else
                    Toast.makeText(this, "اسم المستخدم او كلمة السر خطأ", Toast.LENGTH_SHORT).show();
                break;
            case PARENT_LOGIN_ACTIVITY_REQUEST:
                // auth. parent login
                intent = new Intent(LoginActivity.this, ParentMainActivity.class);
                intent.putExtra(USER_PROFILE_KEY, (Parcelable) dataModel);
                if (((LoggedInUser)dataModel).userCollection.equals(Parent.COLLECTION)){
                    Toast.makeText(this, "تم تسجيل الدخول ", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else
                    Toast.makeText(this, "اسم المستخدم او كلمة السر خطأ", Toast.LENGTH_SHORT).show();
                break;
            case MANAGEMENT_LOGIN_ACTIVITY_REQUEST:
                // auth. management login
                intent = new Intent(LoginActivity.this, ManagementMainActivity.class);
                intent.putExtra(USER_PROFILE_KEY, (Parcelable) dataModel);
                if (((LoggedInUser)dataModel).userCollection.equals(ManagementMainActivity.MANAG_COLLECTION)){
                    Toast.makeText(this, "تم تسجيل الدخول ", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else
                    Toast.makeText(this, "اسم المستخدم او كلمة السر خطأ", Toast.LENGTH_SHORT).show();
                break;
            default:
                showDialog("اسم المستخدم او الرقم السرى به خطأ");
        }
    }
    /**
     *
     * @param msg
     */
    private void showDialog(String msg){
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginBinding = null;
        getViewModelStore().clear();
    }
}