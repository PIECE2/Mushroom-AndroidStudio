package com.example.mushroomer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class LoginSucess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sucess);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // Call the function of transition to Login (when pressing a mechanical button)
    public void onBackPressed() {
        openQuitDialog();
    }

    //Exit
    private void openQuitDialog() {
        finish();
    }
}
