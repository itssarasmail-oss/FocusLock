package com.student.focuslock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        // التحقق إذا كان المستخدم مسجل دخوله بالفعل
        if (mAuth.getCurrentUser() != null) {
            goToFocusActivity();
        }

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            tvStatus.setText("املأ البريد الإلكتروني وكلمة المرور");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                goToFocusActivity();
            } else {
                tvStatus.setText("فشل تسجيل الدخول: " + task.getException().getMessage());
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            tvStatus.setText("املأ البريد الإلكتروني وكلمة المرور");
            return;
        }
        if (password.length() < 6) {
            tvStatus.setText("كلمة المرور يجب أن تكون 6 خانات على الأقل");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                // حفظ بيانات المستخدم في Realtime Database
                String userId = mAuth.getCurrentUser().getUid();
                User user = new User(email);
                mDatabase.child(userId).setValue(user);
                tvStatus.setText("تم إنشاء الحساب بنجاح!");
                goToFocusActivity();
            } else {
                tvStatus.setText("فشل إنشاء الحساب: " + task.getException().getMessage());
            }
        });
    }

    private void goToFocusActivity() {
        startActivity(new Intent(MainActivity.this, FocusActivity.class));
        finish();
    }

    // كلاس بسيط للمستخدم
    public static class User {
        public String email;
        public User() {}
        public User(String email) { this.email = email; }
    }
}
