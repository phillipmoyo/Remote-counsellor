package com.example.p.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.p.MainActivity;
import com.example.p.OnBoardC;
import com.example.p.listOfUsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginRegisterHelperC extends AsyncTask<String,Void,String> {

    @SuppressLint("StaticFieldLeak")

    Context context;
    AlertDialog alertDialog;
    Activity activity;
    private ProgressDialog pDialog;
    String counsellor;
    public LoginRegisterHelperC(Activity activity1){
        this.activity = activity1;
        this.context = activity.getWindow().getContext();
    }

    @Override
    protected String doInBackground(String ... params) {
        String type = params[0];

        String login_url = "https://lamp.ms.wits.ac.za/home/s2185695/logC.php";
        String register_url = "https://lamp.ms.wits.ac.za/home/s2185695/regC.php";
        String forgotPassword_url = "https://lamp.ms.wits.ac.za/home/s2185695/forgotPassC.php";

        if(type.equals("Login")){
                try{
                    String user_name = params[1];
                    String password = params[2];
                    counsellor = user_name;
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_date =
                            URLEncoder.encode("user_name","UTF-8") +"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8") +"="+URLEncoder.encode(password,"UTF-8");

                    bufferedWriter.write(post_date);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    /////now we want to read the responce
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                    String result = "";
                    String line = "";//read line by line
                    while((line = bufferedReader.readLine())!=null)
                    {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        else if(type.equals("Register"))
        {
            try{
                String fullName = params[1];
                String user_name = params[2];
                String emailAddress = params[3];
                String password = params[4];
                String phoneNumber = params[5];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_date =
                                URLEncoder.encode("fname","UTF-8") +"="+URLEncoder.encode(fullName,"UTF-8") +"&"+
                                URLEncoder.encode("uname","UTF-8") +"="+URLEncoder.encode(user_name,"UTF-8") +"&"+
                                URLEncoder.encode("email","UTF-8") +"="+URLEncoder.encode(emailAddress,"UTF-8") +"&"+
                                URLEncoder.encode("pass","UTF-8") +"="+URLEncoder.encode(password,"UTF-8") +"&"+
                                URLEncoder.encode("phone","UTF-8") +"="+URLEncoder.encode(phoneNumber,"UTF-8");

                bufferedWriter.write(post_date);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result = "";
                String line ="";//read line by line
                while((line = bufferedReader.readLine())!=null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("ForgotPassword"))
        {
            try{

                String uname = params[1];
                String email = params[2];
                String phone = params[3];

                URL url = new URL(forgotPassword_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_date =
                        URLEncoder.encode("uname","UTF-8") +"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8") +"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8") +"="+URLEncoder.encode(phone,"UTF-8");

                bufferedWriter.write(post_date);
                bufferedWriter.flush();
                bufferedWriter.close();

                //now we want to read the response
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result = "";
                String line ="";//read line by line
                while((line = bufferedReader.readLine())!=null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Error:");

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                String errorMesaje = "Process cancelled";
                cancel(true);
            }
        });
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
        if(result != null && result.equalsIgnoreCase("Login Successful"))
        {
            Intent intent = new Intent(context, listOfUsers.class);
            intent.putExtra("counsellor",counsellor);
            context.startActivity(intent);

        }
        else if(result != null && result.equalsIgnoreCase("Insert Successful"))
        {
            Intent intent = new Intent(context, OnBoardC.class);
            intent.putExtra("counsellor",counsellor);
            context.startActivity(intent);


        }
        else if(result != null && result.equalsIgnoreCase("Exist"))
        {
            Intent intent = new Intent(context,listOfUsers.class);
            intent.putExtra("counsellor",counsellor);
            context.startActivity(intent);
        }else if(result == null) {
            String networkError = "There is no internet connection \nCheck your internet connection and try again";

            alertDialog.setMessage(networkError);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context, MainActivity.class);
                        context.startActivity(i);
                }
            });
            alertDialog.show();
        } else{
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}