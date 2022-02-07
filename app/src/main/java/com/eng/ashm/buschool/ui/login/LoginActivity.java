package com.eng.ashm.buschool.ui.login;

import static com.eng.ashm.buschool.GlobalConst.DRIVER_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.ERROR_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.LAUNCH_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.MANAGEMENT_LOGIN_ACTIVITY_REQUEST;
import static com.eng.ashm.buschool.GlobalConst.PARENT_LOGIN_ACTIVITY_REQUEST;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.eng.ashm.buschool.databinding.DriverActivityBinding;
import com.eng.ashm.buschool.databinding.LoginLayoutBinding;
import com.eng.ashm.buschool.databinding.ManagementActivityBinding;
import com.eng.ashm.buschool.databinding.ParentActivityBinding;
import com.eng.ashm.buschool.ui.activity.driver.DriverMainActivity;
import com.eng.ashm.buschool.ui.activity.management.ManagementMainActivity;
import com.eng.ashm.buschool.ui.activity.parent.ParentMainActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginLayoutBinding loginBinding;
    private int requestingDiv = ERROR_REQUEST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        loginBinding.loginBtn.setOnClickListener(loginClickListener);
        Intent intent = getIntent();
        requestingDiv = intent.getIntExtra(LAUNCH_LOGIN_ACTIVITY_REQUEST , ERROR_REQUEST);
    }
    View.OnClickListener loginClickListener = v -> {
        switch (requestingDiv){
            case DRIVER_LOGIN_ACTIVITY_REQUEST:
                startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
                break;
            case PARENT_LOGIN_ACTIVITY_REQUEST:
                startActivity(new Intent(LoginActivity.this, ParentMainActivity.class));
                break;
            case MANAGEMENT_LOGIN_ACTIVITY_REQUEST:
                startActivity(new Intent(LoginActivity.this, ManagementMainActivity.class));
                break;
            default:
                showDialog("no data to display on the activity");
                finish();
        }
    };

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
}