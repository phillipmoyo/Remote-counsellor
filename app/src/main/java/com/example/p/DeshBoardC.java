package com.example.p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.p.helpers.LoginRegisterHelperC;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class DeshBoardC extends AppCompatActivity {

    Intent full;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desh_board_c);
        Toolbar toolbar = findViewById(R.id.dashToolBar);
        toolbar.setTitle("Check the counselling you'll be doing");
        setSupportActionBar(toolbar);

        full = getIntent();


    }


    public void addDescription(ArrayList<String> count,String counsellor){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder http = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/addDesc.php").newBuilder()
                .addQueryParameter("counsellor",counsellor)
                .addQueryParameter("description",count.toString());
        Request request = new Request.Builder().url(http.build().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(!response.isSuccessful()){
                        throw new IOException("ERROR "+response);
                    }
            }
        });

    }


    public void submit(View view) {
        LinearLayout layout = findViewById(R.id.layoutDashB);
        Intent intent = getIntent();
        ArrayList<String> typeOfProblem = new ArrayList<>();
        for(int i = 0;i < layout.getChildCount();++i){
            View checkedBox = layout.getChildAt(i);
            if(checkedBox instanceof CheckBox && ((CheckBox) checkedBox).isChecked()){
                String value = ((CheckBox) checkedBox).getText().toString();
                typeOfProblem.add(value);
            }
        }
        String name = intent.getStringExtra("counsellor");



        String fullName = typeOfProblem.toString();
        String Username = full.getStringExtra("USERNAME");
        String email = full.getStringExtra("EMAIL");
        String pass =full.getStringExtra("PASS");
        String phone = full.getStringExtra("PHONE");

        //please add counsellingType as a parameter in your execute method so we can insert it as the description of the counsellor

        String type = "Register";
        LoginRegisterHelperC loginRegisterHelperC = new LoginRegisterHelperC(this);
        if(typeOfProblem.size() == 0){
            Toast.makeText(this, "No box is checked", Toast.LENGTH_SHORT).show();
        }else {
            loginRegisterHelperC.execute(type, fullName, Username, email, pass, phone);
        }

    }
}
