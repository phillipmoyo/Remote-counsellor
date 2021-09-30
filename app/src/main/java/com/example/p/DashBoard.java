package com.example.p;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DashBoard extends AppCompatActivity {

    private String userName, HEADER = "Type of counselling";
    RadioButton radioButton;
    RadioGroup group;
    Button submit;
    Intent strings;
    String name;
    Handler handler = new Handler();
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitle(HEADER);
        setSupportActionBar(toolbar);
        strings = getIntent();

        group = findViewById(R.id.buttonGroup);
        submit = findViewById(R.id.submitBtn);

        Intent getUserName = getIntent();
        userName = getUserName.getStringExtra("USERNAME");



    }
    private void upDateDB(String counsellor,int newNumber){

        if(counsellor.length() != 0){
            HttpUrl.Builder band = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s2185695/updateDataBase.php").newBuilder()
                    .addQueryParameter("counsellor",counsellor)
                    .addQueryParameter("newNumber",String.valueOf(newNumber));

            Request request = new Request.Builder().url(band.build().toString()).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(!response.isSuccessful()){
                        throw  new IOException();
                    }
                }
            });
        }


    }


    public void putID(String[] Ids){
       HttpUrl.Builder url = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/putId.php").newBuilder()
                .addQueryParameter("myId",Ids[0])
                .addQueryParameter("matched",Ids[1]);

        Request request = new Request.Builder().url(url.build().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });

    }


    public void AssignCounsellor(final String typeofproblem) {

        String url = "http://lamp.ms.wits.ac.za/home/s2185695/getCounsellor.php";
        HttpUrl.Builder http = HttpUrl.parse(url).newBuilder()
                .addQueryParameter("problem",typeofproblem);

        Request request = new Request.Builder().url(http.build().toString()).build();
        final String[] counsellor = new String[1];
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String Counsellor = response.body().string();
                    System.out.println(Counsellor);
                    if(Counsellor.length() == 5){
                        DashBoard.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alert = new AlertDialog.Builder(DashBoard.this).create();
                                alert.setTitle("Alert");
                                alert.setMessage("At this point in time\nWe regret to inform you that\n" +
                                        "The are no counsellors for your type of problem\nPlease try again later!!");

                                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            Intent back = new Intent(DashBoard.this,MainActivity.class);
                                            startActivity(back);
                                            finish();
                                    }
                                });

                                alert.show();
                            }
                        });
                    }else {

                        try {
                            // JSONArray jsonArray = new JSONArray(Counsellor);
                            JSONObject num = new JSONObject(Counsellor);
                            int numberOfAssigned = num.getInt("COUNT");

                            counsellor[0] = num.getString("UNAME");
                            name = counsellor[0];

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String type = "Register";
                                    String[] pair = new String[2];
                                    String Username = strings.getStringExtra("USERNAME");
                                    String email = strings.getStringExtra("EMAIL");
                                    String pass = strings.getStringExtra("PASS");
                                    String phone = strings.getStringExtra("PHONE");
                                    pair[0] = Username;
                                    pair[1] = name;
                                    putID(pair);
                                    LoginRegisterHelper loginRegisterHelper = new LoginRegisterHelper(DashBoard.this);

                                    loginRegisterHelper.execute(type, typeofproblem, Username, email, pass, phone, name);

                                }
                            });

                            upDateDB(counsellor[0], numberOfAssigned + 1);
                        } catch (JSONException J) {
                            J.printStackTrace();
                        }

                    }
                } else {
                    throw new IOException();
                }
            }
        });

    }

    public void doChecked(View view) {
        int btn_id = group.getCheckedRadioButtonId();
        radioButton = findViewById(btn_id);
        if (radioButton != null) {
            String fullName = radioButton.getText().toString();
           // Toast.makeText(this, , Toast.LENGTH_SHORT).show();
            AssignCounsellor(fullName);

        } else {
            Toast.makeText(this, "No button is checked", Toast.LENGTH_SHORT).show();
        }

    }
}
