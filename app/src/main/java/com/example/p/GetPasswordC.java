package com.example.p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;
import com.example.p.helpers.LoginRegisterHelperC;
import com.google.android.material.textfield.TextInputLayout;

public class GetPasswordC extends AppCompatActivity {
    TextInputLayout username , email , phone ;
    Button backToLoginActivity, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password_c);

        username =(TextInputLayout)findViewById(R.id.UsnamepC);
        email =(TextInputLayout)findViewById(R.id.passemailpC);
        phone =(TextInputLayout)findViewById(R.id.PhonepC);

        backToLoginActivity = (Button)findViewById(R.id.C);
        login = (Button)findViewById(R.id.backC);
    }

    public void passWithoutPasswordC(View view)
    {
        if(view.getId() == R.id.AltLogC)
        {
            String name = username.getEditText().getText().toString();
            String mail = email.getEditText().getText().toString();
            String numberphone = phone.getEditText().getText().toString();
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(numberphone))
            {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            }
            else
            {
                String type = "ForgotPassword";
                LoginRegisterHelperC loginRegisterHelper = new LoginRegisterHelperC(this);
                loginRegisterHelper.execute(type,name,mail,numberphone);
            }
        }
        if(view.getId() == R.id.backC)
        {
            Intent i = new Intent(getApplicationContext(),LoginC.class);
            startActivity(i);
            finish();
        }
    }
}
