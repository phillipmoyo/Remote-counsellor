package com.example.p;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class counsellorProfile extends AppCompatActivity {

    String counsellor,myId;
    AlertDialog alertDialog;
    TextView description,name,email,phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        counsellor = intent.getStringExtra("counsellor");
        myId = intent.getStringExtra("username");
        if(counsellor.length() != 0){
            getProfileOf(counsellor);
        }


    }

    public void getProfileOf(String counsellor){
                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder  url = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2185695/data.php")
                        .newBuilder()
                        .addQueryParameter("user_name",counsellor);


                Request request = new Request.Builder()
                        .url(url.build().toString())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        String errorMessage = "Error in getting ur user information";
                        alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                        alertDialog.setTitle("Error: ");
                        alertDialog.setMessage(errorMessage);
                        alertDialog.show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                        final String json = response.body().string();
                        counsellorProfile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                description = (TextView)findViewById(R.id.profileDesc);
                                name = (TextView)findViewById(R.id.profileName);
                                email = (TextView)findViewById(R.id.profileEmail);
                                phone = (TextView)findViewById(R.id.profilePhone);

                                try {
                                    JSONArray all = new JSONArray(json);
                                    for (int i=0; i<all.length(); i++)
                                    {
                                        JSONObject item=all.getJSONObject(i);

                                        String DESCRIPTION = item.getString("DESCRIPTION");
                                        String UNAME = item.getString("UNAME");
                                        String EMAIL = item.getString("EMAIL");
                                        String PHONE = item.getString("PHONE");

                                        description.setText(DESCRIPTION.replace("[","").replace("]",""));
                                        name.setText(UNAME);
                                        email.setText(EMAIL);
                                        phone.setText(PHONE);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }




    public void onReturn(View view){

        Intent  backToChat = new Intent(this,ChatWindow.class);
        backToChat.putExtra("counsellor",counsellor);
        backToChat.putExtra("username",myId);
        startActivity(backToChat);
        finish();

    }



}
