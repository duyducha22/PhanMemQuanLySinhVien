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

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputLayout   tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_admin_password);
        etUsername  = findViewById(R.id.et_admin_username);
        etPassword  = findViewById(R.id.et_admin_password);

        MaterialButton btnLogin = findViewById(R.id.btn_admin_login);
        MaterialButton btnBack  = findViewById(R.id.btn_back_to_student);

        btnLogin.setOnClickListener(v -> validateAndLogin());
        btnBack.setOnClickListener(v -> finish());

        // Xóa lỗi khi đang nhập
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilUsername.setError(null);
            }
        });
    }

    private void validateAndLogin() {
        String user = etUsername.getText() != null
                ? etUsername.getText().toString().trim() : "";
        String pwd  = etPassword.getText()  != null
                ? etPassword.getText().toString().trim() : "";

        boolean valid = true;
        if (user.isEmpty()) { tilUsername.setError("Nhập tên đăng nhập"); valid = false; }
        if (pwd.isEmpty())  { tilPassword.setError("Nhập mật khẩu");       valid = false; }
        if (!valid) return;

        // Đóng gói dữ liệu gửi lên API
        java.util.Map<String, String> credentials = new java.util.HashMap<>();
        credentials.put("username", user);
        credentials.put("password", pwd);

        // Gọi API Đăng nhập
        com.example.btl.network.RetrofitClient.getApiService().login(credentials).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.btl.network.ApiResponse<com.example.btl.models.Student> apiResponse = response.body();

                    // Kiểm tra nếu đúng quyền Admin thì mở cổng
                    if (apiResponse.isSuccess() && "admin".equals(apiResponse.getRole())) {
                        android.widget.Toast.makeText(AdminLoginActivity.this, "Xin chào Admin!", android.widget.Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                        finish();
                    } else {
                        tilPassword.setError("Tài khoản không có quyền quản trị!");
                    }
                } else {
                    tilPassword.setError("Sai tên đăng nhập hoặc mật khẩu");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                android.widget.Toast.makeText(AdminLoginActivity.this, "Lỗi kết nối: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }
}