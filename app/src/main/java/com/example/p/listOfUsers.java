package com.example.p;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p.helpers.ListViewHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class listOfUsers extends AppCompatActivity {

    String myId;
    ListView list;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_to_users);
        Toolbar toolbar = findViewById(R.id.listLayout);
        toolbar.setTitle("Matched users");
        setSupportActionBar(toolbar);


        Intent count = getIntent();
        myId = count.getStringExtra("counsellor");
        makeList(myId);
        
        list = findViewById(R.id.ListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout lay = (LinearLayout) view;
                int size = lay.getChildCount();

                if(size != 0 ){
                    LinearLayout out =  (LinearLayout) lay.getChildAt(1);
                    goToChat(((TextView)out.getChildAt(0)).getText().toString());
                }

            }
        });

    }


    public void goToChat(String user){
            Intent intent = new Intent(this,ChatWindowC.class);
            intent.putExtra("username",user);
            intent.putExtra("counsellor",myId);
            startActivity(intent);
    }

    public  void makeList(String name){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder http = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/getIdForC.php").newBuilder()
                .addQueryParameter("counsellor",name);
        Request request = new Request.Builder().url(http.build().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                   final String data = response.body().string();
                   final String[] UandD = data.split("<br>");
                    System.out.println("this are the two arrays --------> "+ UandD[0]+" , "+UandD[1]);
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           processJSON(UandD[0],UandD[1]);
                       }
                   });

                }else{
                    throw new IOException("Error "+response);
                }
            }
        });

    }

    public void processJSON(String username,String description){
        ArrayList<String> users = new ArrayList<>();
        ArrayList<String> Description = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(username);
            JSONArray desc = new JSONArray(description);
            if(array.length() == 0){
                Toast.makeText(this, "No users assigned yet", Toast.LENGTH_SHORT).show();
            }else{
                 for(int i = 0;i < array.length();++i) {
                    JSONObject object = array.getJSONObject(i);
                    JSONObject des = desc.getJSONObject(i);

                    String user = object.getString("MyId");
                    String descrip = des.getString("FNAME");
                    users.add(user);
                    Description.add(descrip);
                    System.out.println("this is it----------> "+ user);
                 }
                ListViewHelper adapter = new ListViewHelper(this,users,Description);
                list.setAdapter(adapter);
            }
        }catch (JSONException J){
            J.printStackTrace();
        }



    }


}
