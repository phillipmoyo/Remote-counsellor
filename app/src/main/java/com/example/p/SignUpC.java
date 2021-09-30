package com.example.p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;
import com.example.p.helpers.LoginRegisterHelperC;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpC extends AppCompatActivity {
    TextInputLayout USERNAME,EMAIL,PASS,PHONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_c);

        USERNAME = findViewById(R.id.UsernameC);
        EMAIL = findViewById(R.id.EmailC);
        PASS = findViewById(R.id.PasswordC);
        PHONE = findViewById(R.id.PhoneC);
    }

    public  void  chosenButtonNewC(View view)
    {
        if(view.getId() == R.id.haveAccountC)
        {
            Intent intent = new Intent(getApplicationContext(),LoginC.class);
            startActivity(intent);
            finish();
        }

        if(view.getId() == R.id.signupbtnC) {
            Intent goTo = new Intent(this,DeshBoardC.class);

            String Username = USERNAME.getEditText().getText().toString().trim();
            String email = EMAIL.getEditText().getText().toString().trim();
            String pass = PASS.getEditText().getText().toString().trim();
            String phone = PHONE.getEditText().getText().toString().trim();
            if ( TextUtils.isEmpty(Username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(phone)) {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            }else{

                goTo.putExtra("USERNAME",Username);
                goTo.putExtra("EMAIL",email);
                goTo.putExtra("PASS",pass);
                goTo.putExtra("PHONE",phone);
                startActivity(goTo);
                finish();
            }



        }
    }
}
