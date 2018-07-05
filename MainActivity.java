package com.guoxw.clocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClocksView clocksView=findViewById(R.id.clocksView);
        clocksView.setNowTime(8,30,23);
    }
}
