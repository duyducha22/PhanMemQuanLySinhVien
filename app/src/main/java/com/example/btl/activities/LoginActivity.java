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

        // Đóng gói dữ liệu gửi lên API
        java.util.Map<String, String> credentials = new java.util.HashMap<>();
        credentials.put("username", id);
        credentials.put("password", pwd);

        // Gọi API Đăng nhập
        com.example.btl.network.RetrofitClient.getApiService().login(credentials).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.btl.network.ApiResponse<com.example.btl.models.Student> apiResponse = response.body();

                    // Kiểm tra nếu đúng là sinh viên thì cho vào
                    if (apiResponse.isSuccess() && "student".equals(apiResponse.getRole())) {
                        android.widget.Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", android.widget.Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("student_id", id);
                        startActivity(intent);
                        finish();
                    } else {
                        tilPassword.setError("Tài khoản này không phải sinh viên!");
                    }
                } else {
                    tilPassword.setError("Sai mã sinh viên hoặc mật khẩu");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                android.widget.Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }
}