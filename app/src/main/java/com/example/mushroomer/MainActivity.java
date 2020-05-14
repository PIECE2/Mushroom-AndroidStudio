package com.example.mushroomer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private TextView tvRegister;
    private EditText etLoginGmail,etLoginPassword;
    private Button loginButton;

    //Regular expressions to check email
    public static final String VALID_EMAIL_ADDRESS_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    //Regular expressions to check password
    //^                 tart-of-string
    //(?=.*[0-9])       a digit must occur at least once
    // (?=.*[a-z])      a lower case letter must occur at least once
    //(?=.*[A-Z])       an upper case letter must occur at least once
    //@#$%^&+=])        a special character must occur at least once
    //(?=\S+$)          no whitespace allowed in the entire string
    //.{8,}             anything, at least eight places though
    //$                 end-of-string
    public static final String VALID_PASSWORD_ADDRESS_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,32}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvRegister = findViewById(R.id.tvRegister);
        etLoginGmail = findViewById(R.id.etLogGmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginPassword.getText().toString().trim().isEmpty() || etLoginGmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else if (etLoginPassword.getText().toString().trim().matches(VALID_PASSWORD_ADDRESS_REGEX) && etLoginGmail.getText().toString().trim().matches(VALID_EMAIL_ADDRESS_REGEX)) {
                    auth.signInWithEmailAndPassword(etLoginGmail.getText().toString().trim(), etLoginPassword.getText().toString().trim()) .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(MainActivity.this, LoginSucess.class));
                            finish();
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });

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
