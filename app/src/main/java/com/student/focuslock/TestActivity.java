package com.student.focuslock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "TEST ACTIVITY WORKS!", Toast.LENGTH_LONG).show();
        finish();
    }
}
