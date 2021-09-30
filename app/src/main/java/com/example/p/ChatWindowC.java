package com.example.p;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatWindowC extends AppCompatActivity {

    HttpUrl.Builder url;
    LinearLayout layout;
    String matchedUser,me;
    Handler handler  = new Handler();
    UserMessageLog messageLog;
    Cursor cursor;
    String[] ids = new String[2];
    OkHttpClient client = new OkHttpClient();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window);
        messageLog = new UserMessageLog(ChatWindowC.this,"Messages");
        layout =(LinearLayout)findViewById(R.id.chatLinearLayout);
        Toolbar toolbar = findViewById(R.id.chatToolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);
        Intent getValues = getIntent();
        matchedUser = getValues.getStringExtra("username");
        me = getValues.getStringExtra("counsellor");
        ids[0]=me;
        ids[1]=matchedUser;


        if (isConnected()) {
            Thread handleRequest = new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest();
                }
            });
            handleRequest.start();
        }else{
            doOffline();
        }


    }

    public void doRequest() {
        Log.i("doRequest : "," internet request is being processed");

        url = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/receivedMessageCounsellor.php").newBuilder()
                .addQueryParameter("counsellor", me)
                .addQueryParameter("user", matchedUser);

        if(layout.getChildCount() == 0)
            ChatWindowC.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doOffline();
                }
            });


        final Request request = new Request.Builder().url(url.build().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String responseData = response.body().string();
                    System.out.println(responseData+" ------> this is the data ");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONArray all = new JSONArray(responseData);
                                if (all.length() != 0) {
                                    processJSON(responseData);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });

        if (isConnected()) {
            try {
                Thread.sleep(10000);
                doRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }



    public boolean isConnected(){
        try{
            ConnectivityManager connect = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = connect.getActiveNetworkInfo();

            return network != null && network.isConnected();

        }catch(NullPointerException nullP){
            nullP.printStackTrace();
            return false;
        }

    }


    public void doOffline(){

        String messageR = matchedUser.concat(" : ");

        LinearLayout layout = findViewById(R.id.chatLinearLayout);
        if(messageLog.getSize() != 0){
            Cursor cursor = messageLog.doQuery("SELECT message,status FROM USERMESSAGELOG WHERE myId = ? AND id = ?",ids);
            while(cursor.moveToNext()){
               TextView text = new TextView(this);
                String messageU = "You : ";


                String status = cursor.getString(cursor.getColumnIndex("status"));
                final String message = cursor.getString(cursor.getColumnIndex("message"));
                if(status.equalsIgnoreCase("sent")){

                    text.setText(messageU.concat(message));
                }else{
                    text.setText(messageR.concat(message));
                }
                layout.addView(text);
            }
            cursor.close();
        }
    }

    public void deleteMessage(String message){

        url = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/delMessageUSERS.php").newBuilder()
                .addQueryParameter("message",message);
        Request request = new Request.Builder().url(url.build().toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException();
                }

            }
        });

    }

    public void doSendMessage(View view){

        TextView sentText = new TextView(this);
        EditText message = findViewById(R.id.typeHere);
        String you = "You : ".concat(message.getText().toString());
        sentText.setText(you);
        String[] vals = new String[4];
        vals[0] = me;
        vals[1] = matchedUser;
        vals[2] = message.getText().toString();
        vals[3] = "sent";

        if(vals[2].isEmpty()){
            Toast.makeText(this, "the text field is empty", Toast.LENGTH_SHORT).show();
        }else {

            messageLog.doUpdate("INSERT INTO USERMESSAGELOG(myId,id,message,status) VALUES(?,?,?,?)", vals);
            url = HttpUrl.parse("http://lamp.ms.wits.ac.za/home/s1862081/sendMessageCounsellor.php").newBuilder()
                    .addQueryParameter("message", message.getText().toString())
                    .addQueryParameter("user", matchedUser)
                    .addQueryParameter("counsellor", me);


            Request request = new Request.Builder().url(url.build().toString()).build();
            layout.addView(sentText);
            message.setText("");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException();
                    }
                }
            });
        }


    }


    public void processJSON(String json){
        //as you've probably noticed the is a bug in this code still working on it

        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                String message = item.getString("MESSAGE");
                TextView view = new TextView(ChatWindowC.this);
                String[] vals = new String[4];
                vals[0]  = me;
                vals[1] = matchedUser;
                vals[2] = message;
                vals[3] = "received";
               view.setText(matchedUser.concat(" : "+message));

                messageLog.doUpdate("INSERT INTO USERMESSAGELOG(myId,id,message,status) VALUES(?,?,?,?)",vals);


                layout.addView(view);
                deleteMessage(message);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
