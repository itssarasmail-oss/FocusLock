package com.student.focuslock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText("التطبيق يعمل! ✅");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(30);
        tv.setBackgroundColor(Color.BLACK);
        tv.setGravity(17);
        
        setContentView(tv);
    }
}
