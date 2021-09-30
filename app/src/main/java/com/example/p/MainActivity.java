package com.example.p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button user,counsellor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.U);
        counsellor = findViewById(R.id.C);
    }
    public void Go(View v)
    {
        Intent intent = null;
        if(v.getId() == R.id.U)
        {
            intent = new Intent(getApplicationContext(),Login.class);
        }
        if (v.getId() == R.id.C)
        {
            intent = new Intent(getApplicationContext(),LoginC.class);
        }
        startActivity(intent);
    }
}
