package com.example.p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GetPassword extends AppCompatActivity {
    TextInputLayout username , email , phone ;
    Button backToLoginActivity, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);

        username =(TextInputLayout)findViewById(R.id.Usnamep);
        email =(TextInputLayout)findViewById(R.id.passemailp);
        phone =(TextInputLayout)findViewById(R.id.Phonep);

        backToLoginActivity = (Button)findViewById(R.id.U);
        login = (Button)findViewById(R.id.back);
    }

    public void passWithoutPassword(View view)
    {
       if(view.getId() == R.id.AltLog)
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
                LoginRegisterHelper loginRegisterHelper = new LoginRegisterHelper(this);
                loginRegisterHelper.execute(type,name,mail,numberphone);
            }
        }
        if(view.getId() == R.id.back)
        {
            Intent i = new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            finish();
        }

    }
}
