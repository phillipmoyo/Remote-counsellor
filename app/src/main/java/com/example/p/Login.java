package com.example.p;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {
    TextInputLayout Username,Password;
    String[] ids = new String[2];
    String Pword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (TextInputLayout)findViewById(R.id.username);
        Password = (TextInputLayout)findViewById(R.id.password);


    }
    public void chosenButton(View view)
    {

        if(view.getId() == R.id.login_btn)
        {
            Pword = Password.getEditText().getText().toString();
            String Uname = Username.getEditText().getText().toString();
            if(isConnected()) {
                if (TextUtils.isEmpty(Uname) || TextUtils.isEmpty(Pword)) {

                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();

                } else {
                    getMatched(Uname);
                }
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                String networkError = "There is no internet connection \nCheck your internet connection and try again";

                alertDialog.setTitle("Error : ");
                alertDialog.setMessage(networkError);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
        if(view.getId() == R.id.signup_btn){
            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);
            finish();
        }
        if(view.getId() == R.id.forgotpass)
        {
            Intent intent = new Intent(getApplicationContext(),GetPassword.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isConnected() {
        try{
            ConnectivityManager connect =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = connect.getActiveNetworkInfo();

            return network != null && network.isConnected();

        }catch(NullPointerException nullP){
            nullP.printStackTrace();
            return false;
        }



    }

    public void getMatched(final String username){
        OkHttpClient   client = new OkHttpClient();
         HttpUrl.Builder   url = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/getId.php").newBuilder()
                    .addQueryParameter("myId",username);
            Request request = new Request.Builder().url(url.build().toString()).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String responseData = response.body().string();
                        System.out.println("ids from database-----> "+responseData);
                        try{

                            JSONObject object = new JSONObject(responseData);

                            ids[0] = object.getString("MyId");
                            ids[1] = object.getString("Matched");

                            Login.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Pword = Password.getEditText().getText().toString();
                                    String type = "Login";
                                    LoginRegisterHelper loginRegisterHelper = new LoginRegisterHelper(Login.this);
                                    loginRegisterHelper.execute(type,username,Pword,ids[1]);
                                }
                            });



                        }catch (JSONException J){
                            J.printStackTrace();
                        }
                    }else{
                        throw new IOException();
                    }
                }
            });
    }
}
