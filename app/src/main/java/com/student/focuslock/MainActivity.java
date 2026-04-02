package com.student.focuslock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ========== إنشاء واجهة جميلة بالكود ==========
        
        // LinearLayout الرئيسي
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 100, 40, 40);
        mainLayout.setBackgroundColor(Color.parseColor("#0F172A")); // لون داكن أنيق
        
        // عنوان التطبيق
        TextView title = new TextView(this);
        title.setText("🔒 Focus Lock");
        title.setTextSize(32);
        title.setTextColor(Color.parseColor("#F59E0B")); // ذهبي
        title.setGravity(1);
        title.setPadding(0, 0, 0, 40);
        mainLayout.addView(title);
        
        // حقل البريد الإلكتروني
        etEmail = new EditText(this);
        etEmail.setHint("البريد الإلكتروني");
        etEmail.setTextColor(Color.WHITE);
        etEmail.setHintTextColor(Color.parseColor("#94A3B8"));
        etEmail.setBackgroundColor(Color.parseColor("#1E293B"));
        etEmail.setPadding(30, 20, 30, 20);
        mainLayout.addView(etEmail);
        
        // حقل كلمة المرور
        etPassword = new EditText(this);
        etPassword.setHint("كلمة المرور");
        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setTextColor(Color.WHITE);
        etPassword.setHintTextColor(Color.parseColor("#94A3B8"));
        etPassword.setBackgroundColor(Color.parseColor("#1E293B"));
        etPassword.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 20;
        etPassword.setLayoutParams(params);
        mainLayout.addView(etPassword);
        
        // زر تسجيل الدخول
        btnLogin = new Button(this);
        btnLogin.setText("تسجيل الدخول");
        btnLogin.setTextColor(Color.WHITE);
        btnLogin.setBackgroundColor(Color.parseColor("#F59E0B"));
        btnLogin.setPadding(30, 20, 30, 20);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 30;
        btnLogin.setLayoutParams(params);
        mainLayout.addView(btnLogin);
        
        // زر إنشاء حساب
        btnRegister = new Button(this);
        btnRegister.setText("إنشاء حساب جديد");
        btnRegister.setTextColor(Color.parseColor("#F59E0B"));
        btnRegister.setBackgroundColor(Color.TRANSPARENT);
        btnRegister.setPadding(30, 20, 30, 20);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 10;
        btnRegister.setLayoutParams(params);
        mainLayout.addView(btnRegister);
        
        // TextView للحالة
        tvStatus = new TextView(this);
        tvStatus.setText("");
        tvStatus.setTextColor(Color.parseColor("#EF4444"));
        tvStatus.setGravity(1);
        tvStatus.setPadding(0, 20, 0, 0);
        mainLayout.addView(tvStatus);
        
        setContentView(mainLayout);
        
        // ========== إعداد الأزرار ==========
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                tvStatus.setText("❌ البريد الإلكتروني وكلمة المرور مطلوبان");
            } else {
                tvStatus.setText("✅ جاري تسجيل الدخول...");
                Toast.makeText(this, "تم تسجيل الدخول: " + email, Toast.LENGTH_SHORT).show();
            }
        });
        
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                tvStatus.setText("❌ البريد الإلكتروني وكلمة المرور مطلوبان");
            } else if (password.length() < 6) {
                tvStatus.setText("❌ كلمة المرور يجب أن تكون 6 خانات على الأقل");
            } else {
                tvStatus.setText("✅ جاري إنشاء الحساب...");
                Toast.makeText(this, "تم إنشاء الحساب: " + email, Toast.LENGTH_SHORT).show();
            }
        });
    }
            }
