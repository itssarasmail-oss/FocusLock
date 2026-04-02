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
import android.text.TextUtils;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvStatus, tvFooter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // تهيئة Firebase
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
            }
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        } catch (Exception e) {
            Toast.makeText(this, "Firebase error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        // التصميم
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 100, 40, 40);
        mainLayout.setBackgroundColor(Color.parseColor("#050510"));
        
        TextView title = new TextView(this);
        title.setText("🔮 FOCUS LOCK");
        title.setTextSize(34);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.WHITE);
        title.setGravity(1);
        title.setPadding(0, 0, 0, 50);
        mainLayout.addView(title);
        
        // حقل البريد
        etEmail = new EditText(this);
        etEmail.setHint("البريد الإلكتروني");
        etEmail.setTextColor(Color.WHITE);
        etEmail.setHintTextColor(Color.parseColor("#AAFFFFFF"));
        GradientDrawable emailBg = new GradientDrawable();
        emailBg.setColor(Color.parseColor("#30FFFFFF"));
        emailBg.setCornerRadius(30);
        emailBg.setStroke(1, Color.parseColor("#80FFFFFF"));
        etEmail.setBackground(emailBg);
        etEmail.setPadding(35, 22, 35, 22);
        mainLayout.addView(etEmail);
        
        // حقل كلمة المرور
        etPassword = new EditText(this);
        etPassword.setHint("كلمة المرور");
        etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setTextColor(Color.WHITE);
        etPassword.setHintTextColor(Color.parseColor("#AAFFFFFF"));
        GradientDrawable passwordBg = new GradientDrawable();
        passwordBg.setColor(Color.parseColor("#30FFFFFF"));
        passwordBg.setCornerRadius(30);
        passwordBg.setStroke(1, Color.parseColor("#80FFFFFF"));
        etPassword.setBackground(passwordBg);
        etPassword.setPadding(35, 22, 35, 22);
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
        btnLogin.setTextSize(16);
        btnLogin.setTypeface(null, Typeface.BOLD);
        GradientDrawable loginBg = new GradientDrawable();
        loginBg.setColor(Color.parseColor("#3D5AFE"));
        loginBg.setCornerRadius(35);
        btnLogin.setBackground(loginBg);
        btnLogin.setPadding(30, 18, 30, 18);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 40;
        btnLogin.setLayoutParams(params);
        mainLayout.addView(btnLogin);
        
        // زر إنشاء حساب
        btnRegister = new Button(this);
        btnRegister.setText("إنشاء حساب جديد");
        btnRegister.setTextColor(Color.parseColor("#CCFFFFFF"));
        btnRegister.setTextSize(14);
        GradientDrawable registerBg = new GradientDrawable();
        registerBg.setColor(Color.TRANSPARENT);
        registerBg.setCornerRadius(35);
        registerBg.setStroke(1, Color.parseColor("#80FFFFFF"));
        btnRegister.setBackground(registerBg);
        btnRegister.setPadding(30, 18, 30, 18);
        params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 15;
        btnRegister.setLayoutParams(params);
        mainLayout.addView(btnRegister);
        
        // حالة التطبيق
        tvStatus = new TextView(this);
        tvStatus.setText("");
        tvStatus.setTextColor(Color.parseColor("#3D5AFE"));
        tvStatus.setGravity(1);
        tvStatus.setPadding(0, 25, 0, 0);
        tvStatus.setTextSize(14);
        mainLayout.addView(tvStatus);
        
        // Footer
        tvFooter = new TextView(this);
        tvFooter.setText("Developed by 1383");
        tvFooter.setTextColor(Color.parseColor("#80FFFFFF"));
        tvFooter.setTextSize(12);
        tvFooter.setGravity(1);
        tvFooter.setPadding(0, 40, 0, 0);
        tvFooter.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://1383portfolio.vercel.app")));
        });
        mainLayout.addView(tvFooter);
        
        setContentView(mainLayout);
        
        // الأزرار
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                tvStatus.setText("✧ البريد وكلمة المرور مطلوبان ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                return;
            }
            
            tvStatus.setText("✧ جاري تسجيل الدخول... ✧");
            tvStatus.setTextColor(Color.parseColor("#3D5AFE"));
            
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        tvStatus.setText("✅ تم تسجيل الدخول بنجاح!");
                        tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                        Toast.makeText(MainActivity.this, "✨ مرحباً بك ✨", Toast.LENGTH_LONG).show();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "خطأ";
                        tvStatus.setText("❌ " + error);
                        tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                    }
                });
        });
        
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                tvStatus.setText("✧ البريد وكلمة المرور مطلوبان ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                return;
            }
            if (password.length() < 6) {
                tvStatus.setText("✧ كلمة المرور 6 خانات على الأقل ✧");
                tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                return;
            }
            
            tvStatus.setText("✧ جاري إنشاء الحساب... ✧");
            tvStatus.setTextColor(Color.parseColor("#3D5AFE"));
            
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            mDatabase.child(user.getUid()).setValue(new User(email, System.currentTimeMillis()));
                        }
                        tvStatus.setText("✅ تم إنشاء الحساب بنجاح!");
                        tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                        Toast.makeText(MainActivity.this, "🎉 تم إنشاء حسابك!", Toast.LENGTH_LONG).show();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "خطأ";
                        tvStatus.setText("❌ " + error);
                        tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                    }
                });
        });
    }
    
    public static class User {
        public String email;
        public long createdAt;
        public User() {}
        public User(String email, long createdAt) {
            this.email = email;
            this.createdAt = createdAt;
        }
    }
                                }
