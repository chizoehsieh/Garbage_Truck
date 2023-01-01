package com.example.garbagetruck;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MsgHandler extends AppCompatActivity {
    public static String TAG = "HIPPO_DEBUG";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);


    }
}
