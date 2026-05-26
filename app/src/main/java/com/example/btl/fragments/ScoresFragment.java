package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.ScoreAdapter;
import com.example.btl.models.Score;
import com.example.btl.network.ApiResponse;
import com.example.btl.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoresFragment extends Fragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private String studentId;
    private ScoreAdapter adapter;
    private List<Score> scoreList = new ArrayList<>();

    private TextView tvGpa, tvCredits, tvSubjects;

    public static ScoresFragment newInstance(String studentId) {
        ScoresFragment f = new ScoresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENT_ID, studentId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            studentId = getArguments().getString(ARG_STUDENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGpa      = view.findViewById(R.id.tv_gpa_display);
        tvCredits  = view.findViewById(R.id.tv_total_credits);
        tvSubjects = view.findViewById(R.id.tv_subject_count);
        RecyclerView rv = view.findViewById(R.id.rv_scores);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScoreAdapter(scoreList);
        rv.setAdapter(adapter);

        loadScores();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadScores();
    }

    private void loadScores() {
        if (studentId == null || studentId.isEmpty()) return;

        RetrofitClient.getApiService().getScoresByStudent(studentId).enqueue(new Callback<ApiResponse<List<Score>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Score>>> call, Response<ApiResponse<List<Score>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    scoreList.clear();
                    if (response.body().getData() != null) {
                        scoreList.addAll(response.body().getData());
                    }
                    adapter.notifyDataSetChanged();
                    calculateStatistics(); // Tự động tính GPA và Tín chỉ
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Score>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateStatistics() {
        int totalCredits = 0;
        double totalPoint4 = 0;

        for (Score s : scoreList) {
            int c = s.getCredits();
            totalCredits += c;

            // Quy đổi điểm chữ sang hệ 4
            double p4 = 0;
            if ("A".equals(s.getGrade())) p4 = 4.0;
            else if ("B".equals(s.getGrade())) p4 = 3.0;
            else if ("C".equals(s.getGrade())) p4 = 2.0;
            else if ("D".equals(s.getGrade())) p4 = 1.0;

            totalPoint4 += (p4 * c);
        }

        tvSubjects.setText(String.valueOf(scoreList.size()));
        tvCredits.setText(String.valueOf(totalCredits));

        if (totalCredits > 0) {
            double gpa = Math.round((totalPoint4 / totalCredits) * 100.0) / 100.0;
            tvGpa.setText(String.valueOf(gpa));
        } else {
            tvGpa.setText("0.0");
        }
    }
}