package com.example.btl.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.StudentAdapter;
import com.example.btl.models.Student;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText      etSearch;
    private ImageView     ivClear;
    private RecyclerView  rvResults;
    private LinearLayout  layoutEmpty;
    private ProgressBar   progressBar;
    private TextView      tvResultCount;
    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch      = view.findViewById(R.id.et_search);
        ivClear       = view.findViewById(R.id.iv_clear_search);
        rvResults     = view.findViewById(R.id.rv_search_results);
        layoutEmpty   = view.findViewById(R.id.layout_empty);
        progressBar   = view.findViewById(R.id.progress_search);
        tvResultCount = view.findViewById(R.id.tv_result_count);

        // Setup RecyclerView
        adapter = new StudentAdapter(studentList, student -> {
            // Mở StudentDetailActivity khi click vào 1 sinh viên
            android.content.Intent intent = new android.content.Intent(
                    getContext(),
                    com.example.btl.activities.StudentDetailActivity.class
            );
            intent.putExtra("student_id", student.getStudentId());
            startActivity(intent);
        });
        rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvResults.setAdapter(adapter);

        // Nút xóa tìm kiếm
        ivClear.setOnClickListener(v -> etSearch.setText(""));

        // Lắng nghe nhập liệu
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                ivClear.setVisibility(keyword.isEmpty() ? View.GONE : View.VISIBLE);
                if (keyword.length() >= 2) performSearch(keyword);
                else if (keyword.isEmpty()) clearResults();
            }
        });

        // Tìm khi bấm Enter bàn phím
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch(etSearch.getText().toString().trim());
            return true;
        });
    }

    private void performSearch(String keyword) {
        progressBar.setVisibility(View.VISIBLE);
        rvResults.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);

        // Gọi API tìm kiếm đã viết bên Node.js
        com.example.btl.network.RetrofitClient.getApiService().searchStudents(keyword).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Student>>>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Student>>> call, retrofit2.Response<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Student>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    showResults(response.body().getData());
                } else {
                    showResults(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Student>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResults(List<Student> results) {
        if (results == null || results.isEmpty()) {
            rvResults.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            tvResultCount.setVisibility(View.GONE);
        } else {
            rvResults.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            tvResultCount.setVisibility(View.VISIBLE);
            tvResultCount.setText("Tìm thấy " + results.size() + " sinh viên");
            studentList.clear();
            studentList.addAll(results);
            adapter.notifyDataSetChanged();
        }
    }

    private void clearResults() {
        studentList.clear();
        adapter.notifyDataSetChanged();
        rvResults.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        tvResultCount.setVisibility(View.GONE);
    }
}