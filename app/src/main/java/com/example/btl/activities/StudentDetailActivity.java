package com.example.btl.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.ScoreAdapter;
import com.example.btl.models.Score;
import java.util.ArrayList;
import java.util.List;

public class StudentDetailActivity extends AppCompatActivity {

    private String studentId;
    private ScoreAdapter scoreAdapter;
    private List<Score> scoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        studentId = getIntent().getStringExtra("student_id");

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ view
        TextView tvInitial = findViewById(R.id.tv_detail_initial);
        TextView tvName    = findViewById(R.id.tv_detail_name);
        TextView tvId      = findViewById(R.id.tv_detail_student_id);
        TextView tvStatus  = findViewById(R.id.tv_detail_status);
        TextView tvDob     = findViewById(R.id.tv_detail_dob);
        TextView tvGender  = findViewById(R.id.tv_detail_gender);
        TextView tvEmail   = findViewById(R.id.tv_detail_email);
        TextView tvPhone   = findViewById(R.id.tv_detail_phone);
        TextView tvClass   = findViewById(R.id.tv_detail_class);
        TextView tvMajor   = findViewById(R.id.tv_detail_major);
        TextView tvYear    = findViewById(R.id.tv_detail_year);
        RecyclerView rv    = findViewById(R.id.rv_detail_scores);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        scoreAdapter = new ScoreAdapter(scoreList);
        rv.setAdapter(scoreAdapter);

        // TODO: Gọi API lấy thông tin sinh viên theo studentId
        // RetrofitClient.getApiService().getStudentById(studentId).enqueue(...)

        // ---- Dữ liệu mẫu ----
        tvInitial.setText("A");
        tvName.setText("Nguyễn Văn An");
        tvId.setText(studentId != null ? studentId : "A12345");
        tvStatus.setText("Đang học");
        tvDob.setText("01/01/2000");
        tvGender.setText("Nam");
        tvEmail.setText("a12345@thanglong.edu.vn");
        tvPhone.setText("0901234567");
        tvClass.setText("CNTT1");
        tvMajor.setText("Công nghệ thông tin");
        tvYear.setText("2022");
    }
}