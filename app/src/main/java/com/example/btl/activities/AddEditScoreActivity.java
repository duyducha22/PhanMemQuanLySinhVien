package com.example.btl.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.btl.R;
import com.example.btl.models.Score;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditScoreActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE_ID = "edit_score_id";

    private boolean isEditMode = false;
    private int     editScoreId = -1;

    private TextInputLayout   tilStudentId, tilSubjectCode, tilSubjectName;
    private TextInputLayout   tilCredits, tilSemester, tilMidterm, tilFinal;
    private TextInputEditText etStudentId, etSubjectCode, etSubjectName;
    private TextInputEditText etCredits, etSemester, etMidterm, etFinal;
    private TextView          tvStudentName, tvCalculatedAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_score);

        editScoreId = getIntent().getIntExtra(EXTRA_SCORE_ID, -1);
        isEditMode  = (editScoreId != -1);

        Toolbar toolbar = findViewById(R.id.toolbar_add_score);
        setSupportActionBar(toolbar);
        toolbar.setTitle(isEditMode ? "Chỉnh sửa điểm" : "Nhập điểm");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ
        tilStudentId   = findViewById(R.id.til_score_student_id);
        tilSubjectCode = findViewById(R.id.til_subject_code);
        tilSubjectName = findViewById(R.id.til_subject_name);
        tilCredits     = findViewById(R.id.til_credits);
        tilSemester    = findViewById(R.id.til_semester);
        tilMidterm     = findViewById(R.id.til_midterm);
        tilFinal       = findViewById(R.id.til_final);

        etStudentId   = findViewById(R.id.et_score_student_id);
        etSubjectCode = findViewById(R.id.et_subject_code);
        etSubjectName = findViewById(R.id.et_subject_name);
        etCredits     = findViewById(R.id.et_credits);
        etSemester    = findViewById(R.id.et_semester);
        etMidterm     = findViewById(R.id.et_midterm);
        etFinal       = findViewById(R.id.et_final);

        tvStudentName    = findViewById(R.id.tv_score_student_name);
        tvCalculatedAvg  = findViewById(R.id.tv_calculated_avg);

        // Tự động tính điểm TB khi nhập
        TextWatcher scoreWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                recalcAverage();
            }
        };
        etMidterm.addTextChangedListener(scoreWatcher);
        etFinal.addTextChangedListener(scoreWatcher);

        // Tìm sinh viên khi nhập mã
        tilStudentId.setEndIconOnClickListener(v -> lookupStudent());

        if (isEditMode) loadScoreData(editScoreId);

        MaterialButton btnSave   = findViewById(R.id.btn_save_score);
        MaterialButton btnCancel = findViewById(R.id.btn_cancel_score);
        btnSave.setOnClickListener(v -> validateAndSave());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void recalcAverage() {
        try {
            double mid   = Double.parseDouble(etMidterm.getText().toString());
            double fin   = Double.parseDouble(etFinal.getText().toString());
            double avg   = Math.round((mid * 0.4 + fin * 0.6) * 10.0) / 10.0;
            String grade = Score.calcGrade(avg);
            tvCalculatedAvg.setText(avg + " (" + grade + ")");
        } catch (NumberFormatException e) {
            tvCalculatedAvg.setText("—");
        }
    }

    private void lookupStudent() {
        String id = etStudentId.getText() != null
                ? etStudentId.getText().toString().trim().toUpperCase() : "";
        if (id.isEmpty()) return;

        // TODO: Gọi API kiểm tra sinh viên tồn tại
        // RetrofitClient.getApiService().getStudentById(id).enqueue(...)

        // ---- Mẫu test ----
        tvStudentName.setText("Nguyễn Văn An — CNTT1");
        tvStudentName.setVisibility(android.view.View.VISIBLE);
    }

    private void loadScoreData(int scoreId) {
        // TODO: Gọi API lấy điểm theo scoreId
        etStudentId.setText("A12345");
        etSubjectCode.setText("CNTT101");
        etSubjectName.setText("Lập trình hướng đối tượng");
        etCredits.setText("3");
        etSemester.setText("HK1 2023-2024");
        etMidterm.setText("8.5");
        etFinal.setText("9.0");
    }

    private void validateAndSave() {
        String studentId  = etStudentId.getText()   != null ? etStudentId.getText().toString().trim().toUpperCase() : "";
        String subCode    = etSubjectCode.getText()  != null ? etSubjectCode.getText().toString().trim() : "";
        String subName    = etSubjectName.getText()  != null ? etSubjectName.getText().toString().trim() : "";
        String midStr     = etMidterm.getText()      != null ? etMidterm.getText().toString().trim() : "";
        String finalStr   = etFinal.getText()        != null ? etFinal.getText().toString().trim() : "";

        boolean valid = true;
        if (studentId.isEmpty())  { tilStudentId.setError("Nhập mã SV");          valid = false; }
        else tilStudentId.setError(null);
        if (subCode.isEmpty())    { tilSubjectCode.setError("Nhập mã môn");         valid = false; }
        else tilSubjectCode.setError(null);
        if (subName.isEmpty())    { tilSubjectName.setError("Nhập tên môn");        valid = false; }
        else tilSubjectName.setError(null);
        if (midStr.isEmpty())     { tilMidterm.setError("Nhập điểm giữa kỳ");       valid = false; }
        else tilMidterm.setError(null);
        if (finalStr.isEmpty())   { tilFinal.setError("Nhập điểm cuối kỳ");         valid = false; }
        else tilFinal.setError(null);
        if (!valid) return;

        double mid = Double.parseDouble(midStr);
        double fin = Double.parseDouble(finalStr);
        if (mid < 0 || mid > 10) { tilMidterm.setError("Điểm phải từ 0-10"); return; }
        if (fin < 0 || fin > 10) { tilFinal.setError("Điểm phải từ 0-10");   return; }

        Score score = new Score();
        score.setStudentId(studentId);
        score.setSubjectCode(subCode);
        score.setSubjectName(subName);
        score.setMidtermScore(mid);
        score.setFinalScore(fin);
        score.setAverageScore(score.calcAverage());
        score.setGrade(Score.calcGrade(score.getAverageScore()));
        score.setSemester(etSemester.getText() != null ? etSemester.getText().toString().trim() : "");
        try { score.setCredits(Integer.parseInt(etCredits.getText().toString().trim())); }
        catch (NumberFormatException e) { score.setCredits(0); }

        // TODO: Gọi API
        // if (isEditMode) RetrofitClient.getApiService().updateScore(editScoreId, score).enqueue(...)
        // else            RetrofitClient.getApiService().createScore(score).enqueue(...)

        Toast.makeText(this, "Đã lưu điểm thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}