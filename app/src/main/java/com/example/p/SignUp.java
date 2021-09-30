package com.example.p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {
    TextInputLayout USERNAME,EMAIL,PASS,PHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        USERNAME = findViewById(R.id.Username);
        EMAIL = findViewById(R.id.Email);
        PASS = findViewById(R.id.Password);
        PHONE = findViewById(R.id.Phone);
    }
    public  void  chosenButtonNew(View view)
    {
        if(view.getId() == R.id.haveAccount)
        {
            Intent intent = new Intent(getApplicationContext(), com.example.p.Login.class);
            startActivity(intent);
            finish();
        }
        if(view.getId() == R.id.signupbtn)
        {
            Intent getToDash = new Intent(this,DashBoard.class);
            String Username = USERNAME.getEditText().getText().toString();
            String email = EMAIL.getEditText().getText().toString();
            String pass = PASS.getEditText().getText().toString();
            String phone = PHONE.getEditText().getText().toString();
            if(TextUtils.isEmpty(Username)|| TextUtils.isEmpty(email)|| TextUtils.isEmpty(pass)|| TextUtils.isEmpty(phone))
            {
                Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
            }
            else
            {
                getToDash.putExtra("USERNAME",Username);
                getToDash.putExtra("EMAIL",email);
                getToDash.putExtra("PASS",pass);
                getToDash.putExtra("PHONE",phone);
                startActivity(getToDash);
                finish();
            }
        }
    }
}
