package com.student.focuslock;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;
import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FocusActivity extends AppCompatActivity {

    private TextView timerText, motivationText;
    private Button btnStart, btnStop, btnSettings;
    private WebView webView;
    private LinearLayout appsContainer;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60 * 60 * 1000; // ساعة افتراضية
    private boolean isRunning = false;
    private DatabaseReference mDatabase;
    private String userId;
    private long startTime;
    private Map<String, Long> appUsageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        // تهيئة Firebase
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Sessions").child(userId);

        // تهيئة العناصر
        timerText = findViewById(R.id.timerText);
        motivationText = findViewById(R.id.motivationText);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnSettings = findViewById(R.id.btnSettings);
        webView = findViewById(R.id.webView);
        appsContainer = findViewById(R.id.appsContainer);

        setupWebView();
        setupAllowedApps();
        setupMotivationMessages();

        btnStart.setOnClickListener(v -> startSession());
        btnStop.setOnClickListener(v -> stopSession());
        btnSettings.setOnClickListener(v -> showSettingsDialog());

        // منع الخروج من التطبيق بدون كلمة سر (حماية)
        blockBackButton();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        
        // تحميل المواقع المسموحة فقط
        webView.loadUrl("https://fullmark.online");
    }

    private void setupAllowedApps() {
        // قائمة التطبيقات المسموحة (يمكن إضافتها من الاعدادات)
        String[] allowedPackages = {"com.android.chrome", "com.google.android.youtube", "com.duolingo"};
        String[] appNames = {"Chrome", "YouTube", "Duolingo"};

        for (int i = 0; i < appNames.length; i++) {
            Button appButton = new Button(this);
            appButton.setText(appNames[i]);
            appButton.setBackgroundResource(R.drawable.glass_button);
            appButton.setTextColor(Color.WHITE);
            int finalI = i;
            appButton.setOnClickListener(v -> openApp(allowedPackages[finalI]));
            appsContainer.addView(appButton);
        }
    }

    private void openApp(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
            // تسجيل الاستخدام
            if (isRunning) {
                appUsageMap.put(packageName, System.currentTimeMillis());
            }
        } else {
            Toast.makeText(this, "التطبيق غير مثبت", Toast.LENGTH_SHORT).show();
        }
    }

    private void startSession() {
        if (timeLeftInMillis <= 0) return;
        isRunning = true;
        startTime = System.currentTimeMillis();
        
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
                // كل 10 دقائق (600000 مللي)
                if (millisUntilFinished % 600000 < 1000) {
                    sendMotivationMessage();
                }
            }

            @Override
            public void onFinish() {
                isRunning = false;
                timerText.setText("انتهت الجلسة! أحسنت!");
                saveSessionData();
                lockDevice();
            }
        }.start();
        
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    private void stopSession() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        saveSessionData();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        Toast.makeText(this, "تم حفظ الجلسة", Toast.LENGTH_SHORT).show();
    }

    private void saveSessionData() {
        long duration = (System.currentTimeMillis() - startTime) / 1000; // بالثواني
        
        Map<String, Object> session = new HashMap<>();
        session.put("duration", duration);
        session.put("timestamp", System.currentTimeMillis());
        session.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        session.put("appsUsed", appUsageMap.keySet().toString());
        
        mDatabase.push().setValue(session);
    }

    private void updateTimerText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void sendMotivationMessage() {
        String[] messages = {
            "🌟 أنت رائع! استمر في التركيز!",
            "⚠️ تذكر: النجاح لا يأتي بسهولة! ركز جيداً!",
            "🔥 قوي إرادتك! أنت قادر على الإنجاز!",
            "💪 لا تفتح التطبيقات الأخرى! ركز على هدفك!",
            "📚 ثواني وأنت تحقق حلمك! استمر!"
        };
        Random random = new Random();
        String msg = messages[random.nextInt(messages.length)];
        motivationText.setText(msg);
        
        // اهتزاز تحذيري
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) v.vibrate(500);
    }

    private void lockDevice() {
        // منع استخدام التطبيقات الأخرى (تطبيق الحظر)
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(this, AdminReceiver.class);
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Toast.makeText(this, "الرجاء تفعيل صلاحيات المسؤول", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivity(intent);
        }
    }

    private void setupMotivationMessages() {
        // سيتم إرسالها أثناء الجلسة
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        EditText etTime = dialogView.findViewById(R.id.etTime);
        etTime.setText(String.valueOf(timeLeftInMillis / 60000));
        
        builder.setView(dialogView)
                .setTitle("الإعدادات")
                .setPositiveButton("حفظ", (dialog, which) -> {
                    int minutes = Integer.parseInt(etTime.getText().toString());
                    timeLeftInMillis = minutes * 60 * 1000;
                    updateTimerText();
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void blockBackButton() {
        // منع الخروج من التطبيق
    }

    @Override
    public void onBackPressed() {
        // تعطيل زر الرجوع نهائياً أثناء الجلسة
        if (isRunning) {
            Toast.makeText(this, "لا يمكن الخروج أثناء جلسة المذاكرة!", Toast.LENGTH_LONG).show();
        } else {
            super.onBackPressed();
        }
    }

    // كلاس لاستقبال إدارة الجهاز (AdminReceiver)
    public static class AdminReceiver extends DeviceAdminReceiver {
        @Override
        public void onEnabled(Context context, Intent intent) {
            Toast.makeText(context, "تم تفعيل حماية الجهاز", Toast.LENGTH_SHORT).show();
        }
    }
}
