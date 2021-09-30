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

public class LoginC extends AppCompatActivity {
    TextInputLayout Username,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_c);
        Username = (TextInputLayout)findViewById(R.id.usernameC);
        Password = (TextInputLayout)findViewById(R.id.passwordC);
    }
    public void chosenButton(View view)
    {
        if(view.getId() == R.id.login_btnC){
            String Uname = Username.getEditText().getText().toString();
            String Pword = Password.getEditText().getText().toString();
            if( TextUtils.isEmpty(Uname) || TextUtils.isEmpty(Pword))
            {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            }
            else
            {
                String type = "Login";
                LoginRegisterHelperC loginRegisterHelperC = new LoginRegisterHelperC(this);
                loginRegisterHelperC.execute(type,Uname,Pword);
            }
        }
        if(view.getId() == R.id.signup_btnC){
            Intent intent = new Intent(getApplicationContext(),SignUpC.class);
            startActivity(intent);
            finish();
        }
        if(view.getId() == R.id.forgotpassC)
        {
            Intent intent = new Intent(getApplicationContext(),GetPasswordC.class);
            startActivity(intent);
            finish();
        }

    }
}
