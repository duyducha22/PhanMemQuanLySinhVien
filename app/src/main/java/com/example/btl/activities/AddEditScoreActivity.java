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
import com.example.btl.network.ApiResponse;
import com.example.btl.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditScoreActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE_ID = "edit_score_id";

    private boolean isEditMode = false;
    private String  editScoreId = null; // Đổi sang String cho khớp MongoDB

    private TextInputLayout   tilStudentId, tilSubjectCode, tilSubjectName;
    private TextInputLayout   tilCredits, tilSemester, tilMidterm, tilFinal;
    private TextInputEditText etStudentId, etSubjectCode, etSubjectName;
    private TextInputEditText etCredits, etSemester, etMidterm, etFinal;
    private TextView          tvStudentName, tvCalculatedAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_score);

        // Lấy ID điểm truyền sang dạng String
        editScoreId = getIntent().getStringExtra(EXTRA_SCORE_ID);
        isEditMode  = (editScoreId != null && !editScoreId.isEmpty());

        Toolbar toolbar = findViewById(R.id.toolbar_add_score);
        setSupportActionBar(toolbar);
        toolbar.setTitle(isEditMode ? "Chỉnh sửa điểm" : "Nhập điểm");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ View
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

        // (Chức năng tra cứu tên sinh viên sẽ hoàn thiện sau nếu cần)
        tilStudentId.setEndIconOnClickListener(v -> Toast.makeText(this, "Tra cứu sinh viên...", Toast.LENGTH_SHORT).show());

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

    private void loadScoreData(String scoreId) {
        // Tạm thời để trống, tí nữa anh em mình làm danh sách sẽ test cập nhật sau
    }

    private void validateAndSave() {
        String studentId  = etStudentId.getText()   != null ? etStudentId.getText().toString().trim().toUpperCase() : "";
        String subCode    = etSubjectCode.getText()  != null ? etSubjectCode.getText().toString().trim() : "";
        String subName    = etSubjectName.getText()  != null ? etSubjectName.getText().toString().trim() : "";
        String midStr     = etMidterm.getText()      != null ? etMidterm.getText().toString().trim() : "";
        String finalStr   = etFinal.getText()        != null ? etFinal.getText().toString().trim() : "";

        boolean valid = true;
        if (studentId.isEmpty())  { tilStudentId.setError("Nhập mã SV");          valid = false; } else tilStudentId.setError(null);
        if (subCode.isEmpty())    { tilSubjectCode.setError("Nhập mã môn");         valid = false; } else tilSubjectCode.setError(null);
        if (subName.isEmpty())    { tilSubjectName.setError("Nhập tên môn");        valid = false; } else tilSubjectName.setError(null);
        if (midStr.isEmpty())     { tilMidterm.setError("Nhập điểm giữa kỳ");       valid = false; } else tilMidterm.setError(null);
        if (finalStr.isEmpty())   { tilFinal.setError("Nhập điểm cuối kỳ");         valid = false; } else tilFinal.setError(null);
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

        if (isEditMode) {
            RetrofitClient.getApiService().updateScore(editScoreId, score).enqueue(new Callback<ApiResponse<Score>>() {
                @Override
                public void onResponse(Call<ApiResponse<Score>> call, Response<ApiResponse<Score>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(AddEditScoreActivity.this, "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else { Toast.makeText(AddEditScoreActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show(); }
                }
                @Override
                public void onFailure(Call<ApiResponse<Score>> call, Throwable t) {}
            });
        } else {
            RetrofitClient.getApiService().createScore(score).enqueue(new Callback<ApiResponse<Score>>() {
                @Override
                public void onResponse(Call<ApiResponse<Score>> call, Response<ApiResponse<Score>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(AddEditScoreActivity.this, "Đã lưu điểm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else { Toast.makeText(AddEditScoreActivity.this, "Lỗi khi lưu điểm", Toast.LENGTH_SHORT).show(); }
                }
                @Override
                public void onFailure(Call<ApiResponse<Score>> call, Throwable t) {}
            });
        }
    }
}