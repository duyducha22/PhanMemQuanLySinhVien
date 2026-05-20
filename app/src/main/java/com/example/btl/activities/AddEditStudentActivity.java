package com.example.btl.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.btl.R;
import com.example.btl.models.Student;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;

public class AddEditStudentActivity extends AppCompatActivity {

    public  static final String EXTRA_STUDENT_ID = "edit_student_id";
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^[A-Z]\\d{5}$");

    private boolean isEditMode = false;
    private String  editStudentId;

    private TextInputLayout   tilId, tilName, tilDob, tilEmail, tilPhone, tilAddress;
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
        // TODO: Gọi API lấy thông tin sinh viên theo id
        // RetrofitClient.getApiService().getStudentById(id).enqueue(...)
        // Sau đó điền vào các EditText

        // ---- Dữ liệu mẫu để test ----
        etId.setText(id);
        etId.setEnabled(false); // Không cho sửa mã SV khi edit
        etName.setText("Nguyễn Văn An");
        etDob.setText("01/01/2000");
        etEmail.setText("a12345@thanglong.edu.vn");
        etPhone.setText("0901234567");
        etAddress.setText("Hà Nội");
        etClass.setText("CNTT1");
        etMajor.setText("Công nghệ thông tin");
        etYear.setText("2022");
    }

    private void validateAndSave() {
        String id      = etId.getText()      != null ? etId.getText().toString().trim().toUpperCase() : "";
        String name    = etName.getText()    != null ? etName.getText().toString().trim() : "";
        String dob     = etDob.getText()     != null ? etDob.getText().toString().trim() : "";
        String email   = etEmail.getText()   != null ? etEmail.getText().toString().trim() : "";
        String phone   = etPhone.getText()   != null ? etPhone.getText().toString().trim() : "";
        String cls     = etClass.getText()   != null ? etClass.getText().toString().trim() : "";
        String major   = etMajor.getText()   != null ? etMajor.getText().toString().trim() : "";
        String year    = etYear.getText()    != null ? etYear.getText().toString().trim() : "";

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

        // Tạo object Student
        Student student = new Student();
        student.setStudentId(isEditMode ? editStudentId : id);
        student.setFullName(name);
        student.setDateOfBirth(dob);
        student.setEmail(email);
        student.setPhone(phone);
        student.setAddress(etAddress.getText() != null ? etAddress.getText().toString().trim() : "");
        student.setClassName(cls);
        student.setMajor(major);
        student.setAcademicYear(year);

        // Giới tính từ chip
        int genderChipId = chipGender.getCheckedChipId();
        student.setGender(genderChipId == R.id.chip_female ? "Nữ" : "Nam");

        // Trạng thái từ chip
        int statusChipId = chipStatus.getCheckedChipId();
        if      (statusChipId == R.id.chip_graduated)  student.setStatus("graduated");
        else if (statusChipId == R.id.chip_suspended)  student.setStatus("suspended");
        else                                            student.setStatus("active");

        // TODO: Gọi API
        // if (isEditMode) RetrofitClient.getApiService().updateStudent(editStudentId, student).enqueue(...)
        // else            RetrofitClient.getApiService().createStudent(student).enqueue(...)

        Toast.makeText(this, isEditMode ? "Đã cập nhật!" : "Đã thêm sinh viên!", Toast.LENGTH_SHORT).show();
        finish();
    }
}