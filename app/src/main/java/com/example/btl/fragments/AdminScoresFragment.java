package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminScoresFragment extends Fragment {

    private ScoreAdapter  adapter;
    private List<Score>   scoreList = new ArrayList<>();
    private TextInputEditText etFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFilter = view.findViewById(R.id.et_filter_student_id);
        MaterialButton btnFilter = view.findViewById(R.id.btn_filter_scores);
        RecyclerView   rv = view.findViewById(R.id.rv_admin_scores);

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

    // Tự động làm mới danh sách khi vừa thêm điểm xong và quay lại
    @Override
    public void onResume() {
        super.onResume();
        String currentFilter = etFilter.getText() != null ? etFilter.getText().toString().trim() : "";
        if (currentFilter.isEmpty()) {
            loadAllScores();
        } else {
            filterByStudent(currentFilter);
        }
    }

    private void loadAllScores() {
        RetrofitClient.getApiService().getAllScores().enqueue(new Callback<ApiResponse<List<Score>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Score>>> call, Response<ApiResponse<List<Score>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    scoreList.clear();
                    if (response.body().getData() != null) {
                        scoreList.addAll(response.body().getData());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Score>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải điểm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByStudent(String studentId) {
        RetrofitClient.getApiService().getScoresByStudent(studentId).enqueue(new Callback<ApiResponse<List<Score>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Score>>> call, Response<ApiResponse<List<Score>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    scoreList.clear();
                    if (response.body().getData() != null) {
                        scoreList.addAll(response.body().getData());
                    }
                    adapter.notifyDataSetChanged();
                    if (scoreList.isEmpty()) {
                        Toast.makeText(getContext(), "Không tìm thấy điểm của SV này", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Score>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi lọc điểm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}