package com.example.btl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    // Regex: 1 chữ hoa + 5 chữ số (A12345)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^[A-Z]\\d{5}$");

    private TextInputLayout    tilStudentId, tilPassword;
    private TextInputEditText  etStudentId, etPassword;
    private MaterialButton     btnLogin, btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilStudentId  = findViewById(R.id.til_student_id);
        tilPassword   = findViewById(R.id.til_password);
        etStudentId   = findViewById(R.id.et_student_id);
        etPassword    = findViewById(R.id.et_password);
        btnLogin      = findViewById(R.id.btn_login);
        btnAdminLogin = findViewById(R.id.btn_admin_login);

        // Tự động viết hoa mã SV khi nhập
        etStudentId.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilStudentId.setError(null); // Xóa lỗi khi đang nhập
            }
        });

        btnLogin.setOnClickListener(v -> validateAndLogin());

        btnAdminLogin.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class))
        );
    }

    private void validateAndLogin() {
        String id  = etStudentId.getText() != null
                ? etStudentId.getText().toString().trim().toUpperCase() : "";
        String pwd = etPassword.getText()  != null
                ? etPassword.getText().toString().trim() : "";

        boolean valid = true;

        if (!STUDENT_ID_PATTERN.matcher(id).matches()) {
            tilStudentId.setError("Mã SV phải có dạng A12345 (1 chữ hoa + 5 chữ số)");
            valid = false;
        } else {
            tilStudentId.setError(null);
        }

        if (pwd.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            valid = false;
        } else {
            tilPassword.setError(null);
        }

        if (!valid) return;

        // TODO: Thay bằng gọi API thực
        // ApiService api = RetrofitClient.getApiService();
        // api.login(id, pwd).enqueue(...);

        // ---- Test giao diện (xóa khi có API) ----
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("student_id", id);
        startActivity(intent);
        finish();
    }
}