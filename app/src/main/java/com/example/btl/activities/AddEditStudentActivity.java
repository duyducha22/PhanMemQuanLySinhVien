package com.example.btl.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.btl.R;
import com.example.btl.network.RetrofitClient;
import com.example.btl.network.ApiResponse;
import com.example.btl.models.Student;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditStudentActivity extends AppCompatActivity {

    public  static final String EXTRA_STUDENT_ID = "edit_student_id";
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^[A-Z]\\d{5}$");

    private boolean isEditMode = false;
    private String  editStudentId;

    private TextInputLayout tilId, tilName, tilDob, tilEmail, tilPhone, tilAddress;
    private TextInputLayout   tilClass, tilMajor, tilYear;
    private TextInputEditText etId, etName, etDob, etEmail, etPhone, etAddress;
    private TextInputEditText etClass, etMajor, etYear;
    private ChipGroup chipGender, chipStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        editStudentId = getIntent().getStringExtra(EXTRA_STUDENT_ID);
        isEditMode    = (editStudentId != null);

        Toolbar toolbar = findViewById(R.id.toolbar_add_student);
        setSupportActionBar(toolbar);
        toolbar.setTitle(isEditMode ? "Chỉnh sửa sinh viên" : "Thêm sinh viên");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ tất cả view
        tilId      = findViewById(R.id.til_form_student_id);
        tilName    = findViewById(R.id.til_form_name);
        tilDob     = findViewById(R.id.til_form_dob);
        tilEmail   = findViewById(R.id.til_form_email);
        tilPhone   = findViewById(R.id.til_form_phone);
        tilAddress = findViewById(R.id.til_form_address);
        tilClass   = findViewById(R.id.til_form_class);
        tilMajor   = findViewById(R.id.til_form_major);
        tilYear    = findViewById(R.id.til_form_year);

        etId      = findViewById(R.id.et_form_student_id);
        etName    = findViewById(R.id.et_form_name);
        etDob     = findViewById(R.id.et_form_dob);
        etEmail   = findViewById(R.id.et_form_email);
        etPhone   = findViewById(R.id.et_form_phone);
        etAddress = findViewById(R.id.et_form_address);
        etClass   = findViewById(R.id.et_form_class);
        etMajor   = findViewById(R.id.et_form_major);
        etYear    = findViewById(R.id.et_form_year);

        chipGender = findViewById(R.id.chip_group_gender);
        chipStatus = findViewById(R.id.chip_group_status);

        // Picker ngày sinh
        etDob.setOnClickListener(v -> showDatePicker());

        // Nếu đang ở chế độ sửa, tải dữ liệu lên form
        if (isEditMode) loadStudentData(editStudentId);

        MaterialButton btnSave   = findViewById(R.id.btn_save_student);
        MaterialButton btnCancel = findViewById(R.id.btn_cancel_student);

        btnSave.setOnClickListener(v -> validateAndSave());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        android.app.DatePickerDialog picker = new android.app.DatePickerDialog(this);
        picker.setOnDateSetListener((view, year, month, day) ->
                etDob.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
        );
        picker.show();
    }

    private void loadStudentData(String id) {
        // Gọi API Lấy thông tin chi tiết của sinh viên theo ID
        com.example.btl.network.RetrofitClient.getApiService().getStudentById(id).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    com.example.btl.models.Student s = response.body().getData();

                    // Điền dữ liệu thật vào các ô nhập liệu
                    etId.setText(s.getStudentId());
                    etId.setEnabled(false); // Đã là mã sinh viên thì khóa lại không cho sửa

                    etName.setText(s.getFullName());
                    etDob.setText(s.getDob());
                    etEmail.setText(s.getEmail());
                    etPhone.setText(s.getPhone());
                    etAddress.setText(s.getAddress());
                    etClass.setText(s.getClassName());
                    etMajor.setText(s.getMajor());
                    etYear.setText(String.valueOf(s.getEnrollmentYear()));

                    // Tick đúng giới tính
                    if ("Nữ".equalsIgnoreCase(s.getGender())) {
                        chipGender.check(R.id.chip_female);
                    }

                    // Tick đúng trạng thái học tập
                    if ("Tốt nghiệp".equalsIgnoreCase(s.getStatus())) {
                        chipStatus.check(R.id.chip_graduated);
                    } else if ("Đình chỉ".equalsIgnoreCase(s.getStatus())) {
                        chipStatus.check(R.id.chip_suspended);
                    }

                } else {
                    android.widget.Toast.makeText(AddEditStudentActivity.this, "Không lấy được dữ liệu sinh viên!", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                android.widget.Toast.makeText(AddEditStudentActivity.this, "Lỗi kết nối: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndSave() {
        String id      = etId.getText()      != null ? etId.getText().toString().trim().toUpperCase() : "";
        String name    = etName.getText()    != null ? etName.getText().toString().trim() : "";
        String dob     = etDob.getText()     != null ? etDob.getText().toString().trim() : "";
        String email   = etEmail.getText()   != null ? etEmail.getText().toString().trim() : "";
        String phone   = etPhone.getText()   != null ? etPhone.getText().toString().trim() : "";
        String cls     = etClass.getText()   != null ? etClass.getText().toString().trim() : "";
        String major   = etMajor.getText()   != null ? etMajor.getText().toString().trim() : "";
        String yearStr = etYear.getText()    != null ? etYear.getText().toString().trim() : "0";

        boolean valid = true;

        if (!isEditMode && !STUDENT_ID_PATTERN.matcher(id).matches()) {
            tilId.setError("Mã SV phải có dạng A12345");
            valid = false;
        } else tilId.setError(null);

        if (name.isEmpty())  { tilName.setError("Nhập họ và tên");  valid = false; }
        else tilName.setError(null);
        if (cls.isEmpty())   { tilClass.setError("Nhập lớp");        valid = false; }
        else tilClass.setError(null);
        if (major.isEmpty()) { tilMajor.setError("Nhập ngành học");  valid = false; }
        else tilMajor.setError(null);

        if (!valid) return;

        int year = 0;
        try { year = Integer.parseInt(yearStr); } catch (NumberFormatException ignored) {}

        // KHỞI TẠO BIẾN STUDENT Ở ĐÂY
        com.example.btl.models.Student student = new com.example.btl.models.Student();
        student.setStudentId(isEditMode ? editStudentId : id);
        student.setFullName(name);
        student.setDob(dob);
        student.setEmail(email);
        student.setPhone(phone);
        student.setAddress(etAddress.getText() != null ? etAddress.getText().toString().trim() : "");
        student.setClassName(cls);
        student.setMajor(major);
        student.setEnrollmentYear(year);

        int genderChipId = chipGender.getCheckedChipId();
        student.setGender(genderChipId == R.id.chip_female ? "Nữ" : "Nam");

        int statusChipId = chipStatus.getCheckedChipId();
        if      (statusChipId == R.id.chip_graduated)  student.setStatus("Tốt nghiệp");
        else if (statusChipId == R.id.chip_suspended)  student.setStatus("Đình chỉ");
        else                                            student.setStatus("Đang học");

        // GỌI API THÊM/SỬA SINH VIÊN
        if (isEditMode) {
            // CẬP NHẬT (PUT)
            com.example.btl.network.RetrofitClient.getApiService().updateStudent(editStudentId, student).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        android.widget.Toast.makeText(AddEditStudentActivity.this, "Cập nhật thành công!", android.widget.Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        android.widget.Toast.makeText(AddEditStudentActivity.this, "Lỗi cập nhật", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                    android.widget.Toast.makeText(AddEditStudentActivity.this, "Lỗi mạng: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // THÊM MỚI (POST)
            com.example.btl.network.RetrofitClient.getApiService().addStudent(student).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        android.widget.Toast.makeText(AddEditStudentActivity.this, "Đã thêm sinh viên lên MongoDB!", android.widget.Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        android.widget.Toast.makeText(AddEditStudentActivity.this, "Lỗi: Không thể thêm sinh viên", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                    android.widget.Toast.makeText(AddEditStudentActivity.this, "Lỗi mạng: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}