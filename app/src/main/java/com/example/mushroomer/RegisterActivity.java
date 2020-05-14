package com.example.mushroomer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn, gotoLoginBtn;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    EditText regName, regGmail, regPassword;

    //Regular expressions to check email
    public static final String VALID_EMAIL_ADDRESS_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    //Regular expressions to check name
    public static final String VALID_NAME_ADDRESS_REGEX = "^[A-Z][a-zA-Z]{1,20}$";

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
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        registerBtn = findViewById(R.id.btnRegLogin);
        gotoLoginBtn = findViewById(R.id.btnGotoLogin);
        regName = findViewById(R.id.etRegName);
        regGmail = findViewById(R.id.etRegGmail);
        regPassword = findViewById(R.id.etRegPassword);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regName.getText().toString().isEmpty() || regPassword.getText().toString().isEmpty() || regGmail.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else if (regName.getText().toString().matches(VALID_NAME_ADDRESS_REGEX) && regPassword.getText().toString().matches(VALID_PASSWORD_ADDRESS_REGEX) && regGmail.getText().toString().matches(VALID_EMAIL_ADDRESS_REGEX)) {
                    auth.createUserWithEmailAndPassword(regGmail.getText().toString(), regPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            DatabaseHelper databaseHelper = new DatabaseHelper();
                            databaseHelper.setName(regName.getText().toString());
                            databaseHelper.setEmail(regGmail.getText().toString());
                            databaseHelper.setPass(regPassword.getText().toString());
                            databaseHelper.setId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                            users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(databaseHelper)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginSucess.class));
                                            finish();
                                        }
                                    });
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gotoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    // Call the function of transition to Login (when pressing a mechanical button)
    public void onBackPressed() {
        openQuitDialog();
    }

    //Login opening function
    private void openQuitDialog() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}