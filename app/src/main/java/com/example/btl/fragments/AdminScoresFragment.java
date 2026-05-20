package com.example.btl.fragments;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.btl.R;
import com.example.btl.adapters.ScoreAdapter;
import com.example.btl.models.Score;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.*;

public class AdminScoresFragment extends Fragment {

    private ScoreAdapter  adapter;
    private List<Score>   scoreList = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etFilter = view.findViewById(R.id.et_filter_student_id);
        MaterialButton    btnFilter = view.findViewById(R.id.btn_filter_scores);
        RecyclerView      rv = view.findViewById(R.id.rv_admin_scores);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScoreAdapter(scoreList);
        rv.setAdapter(adapter);

        btnFilter.setOnClickListener(v -> {
            String id = etFilter.getText() != null
                    ? etFilter.getText().toString().trim().toUpperCase() : "";
            if (!id.isEmpty()) filterByStudent(id);
            else loadAllScores();
        });

        loadAllScores();
    }

    private void loadAllScores() {
        // TODO: Gọi API lấy tất cả điểm
        loadSampleData();
    }

    private void filterByStudent(String studentId) {
        // TODO: Gọi API lọc điểm theo mã SV
        // RetrofitClient.getApiService().getScoresByStudent(studentId).enqueue(...)
        loadSampleData();
    }

    private void loadSampleData() {
        Score s = new Score();
        s.setSubjectCode("CNTT101"); s.setSubjectName("Lập trình hướng đối tượng");
        s.setCredits(3); s.setMidtermScore(8.5); s.setFinalScore(9.0);
        s.setAverageScore(s.calcAverage()); s.setGrade(Score.calcGrade(s.getAverageScore()));
        s.setStudentId("A12345"); s.setSemester("HK1 2023-2024");
        scoreList.clear(); scoreList.add(s);
        adapter.notifyDataSetChanged();
    }
}