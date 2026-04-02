package com.student.focuslock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ========== التصميم الأسود الزجاجي ==========
        
        // LinearLayout الرئيسي
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 100, 40, 40);
        
        // خلفية سوداء مع تأثير زجاجي
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#CC000000")); // أسود شفاف
        background.setCornerRadius(0);
        mainLayout.setBackground(background);
        mainLayout.setBackgroundColor(Color.parseColor("#0A0A0F")); // أسود عميق
        
        // عنوان التطبيق
        TextView title = new TextView(this);
        title.setText("🔮 FOCUS LOCK");
        title.setTextSize(34);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.parseColor("#E0E0E0")); // أبيض ناعم
        title.setGravity(1);
        title.setPadding(0, 0, 0, 60);
        // إضافة تأثير الظل
        title.setShadowLayer(8, 0, 0, Color.parseColor("#66000000"));
        mainLayout.addView(title);
        
        // ========== حقل البريد الإلكتروني (زجاجي) ==========
        etEmail = new EditText(this);
        etEmail.setHint("البريد الإلكتروني");
        etEmail.setTextColor(Color.parseColor("#F0F0F0"));
        etEmail.setHintTextColor(Color.parseColor("#80FFFFFF"));
        
        // خلفية زجاجية للحقل
        GradientDrawable emailBg = new GradientDrawable();
        emailBg.setColor(Color.parseColor("#30FFFFFF")); // أبيض شفاف
        emailBg.setCornerRadius(25);
        emailBg.setStroke(1, Color.parseColor("#60FFFFFF"));
        etEmail.setBackground(emailBg);
        
        etEmail.setPadding(30, 20, 30, 20);
        mainLayout.addView(etEmail);
        
        // ========== حقل كلمة المرور (زجاجي) ==========
        etPassword = new EditText(this);
        etPassword.setHint("كلمة المرور");
        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setTextColor(Color.parseColor("#F0F0F0"));
        etPassword.setHintTextColor(Color.parseColor("#80FFFFFF"));
        
        // خلفية زجاجية للحقل
        GradientDrawable passwordBg = new GradientDrawable();
        passwordBg.setColor(Color.parseColor("#30FFFFFF"));
        passwordBg.setCornerRadius(25);
        passwordBg.setStroke(1, Color.parseColor("#60FFFFFF"));
        etPassword.setBackground(passwordBg);
        
        etPassword.setPadding(30, 20, 30, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 20;
        etPassword.setLayoutParams(params);
        mainLayout.addView(etPassword);
        
        // ========== زر تسجيل الدخول (زجاجي) ==========
        btnLogin = new Button(this);
        btnLogin.setText("تسجيل الدخول");
        btnLogin.setTextColor(Color.parseColor("#FFFFFF"));
        btnLogin.setTextSize(16);
        btnLogin.setTypeface(null, Typeface.BOLD);
        
        // خلفية زجاجية للزر
        GradientDrawable loginBg = new GradientDrawable();
        loginBg.setColor(Color.parseColor("#40FFFFFF")); // أبيض شفاف
        loginBg.setCornerRadius(30);
        loginBg.setStroke(1, Color.parseColor("#A0FFFFFF"));
        btnLogin.setBackground(loginBg);
        
        btnLogin.setPadding(30, 18, 30, 18);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 40;
        btnLogin.setLayoutParams(params);
        mainLayout.addView(btnLogin);
        
        // ========== زر إنشاء حساب (شفاف) ==========
        btnRegister = new Button(this);
        btnRegister.setText("إنشاء حساب جديد");
        btnRegister.setTextColor(Color.parseColor("#B0FFFFFF"));
        btnRegister.setTextSize(14);
        
        GradientDrawable registerBg = new GradientDrawable();
        registerBg.setColor(Color.TRANSPARENT);
        registerBg.setCornerRadius(30);
        registerBg.setStroke(1, Color.parseColor("#60FFFFFF"));
        btnRegister.setBackground(registerBg);
        
        btnRegister.setPadding(30, 18, 30, 18);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 15;
        btnRegister.setLayoutParams(params);
        mainLayout.addView(btnRegister);
        
        // ========== TextView للحالة (نيلي) ==========
        tvStatus = new TextView(this);
        tvStatus.setText("");
        tvStatus.setTextColor(Color.parseColor("#4D9FFF")); // أزرق نيلي فاتح
        tvStatus.setGravity(1);
        tvStatus.setPadding(0, 25, 0, 0);
        tvStatus.setTextSize(14);
        mainLayout.addView(tvStatus);
        
        setContentView(mainLayout);
        
        // ========== إعداد الأزرار ==========
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                tvStatus.setText("✧ البريد الإلكتروني وكلمة المرور مطلوبان ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
            } else {
                tvStatus.setText("✧ جاري تسجيل الدخول... ✧");
                tvStatus.setTextColor(Color.parseColor("#4D9FFF"));
                Toast.makeText(this, "✨ مرحباً " + email, Toast.LENGTH_SHORT).show();
            }
        });
        
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                tvStatus.setText("✧ البريد الإلكتروني وكلمة المرور مطلوبان ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
            } else if (password.length() < 6) {
                tvStatus.setText("✧ كلمة المرور يجب أن تكون 6 خانات على الأقل ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
            } else {
                tvStatus.setText("✧ جاري إنشاء الحساب... ✧");
                tvStatus.setTextColor(Color.parseColor("#4D9FFF"));
                Toast.makeText(this, "🎉 تم إنشاء الحساب بنجاح!", Toast.LENGTH_SHORT).show();
            }
        });
    }
            }
