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

public class HomeFragment extends Fragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private String studentId;

    public static HomeFragment newInstance(String studentId) {
        HomeFragment f = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        TextView tvName      = view.findViewById(R.id.tv_student_name);
        TextView tvIdBadge   = view.findViewById(R.id.tv_student_id_badge);
        TextView tvInitial   = view.findViewById(R.id.tv_avatar_initial);
        TextView tvClass     = view.findViewById(R.id.tv_class);
        TextView tvMajor     = view.findViewById(R.id.tv_major);
        TextView tvEmail     = view.findViewById(R.id.tv_email);
        TextView tvPhone     = view.findViewById(R.id.tv_phone);
        TextView tvGpa       = view.findViewById(R.id.tv_gpa);
        TextView tvCredits   = view.findViewById(R.id.tv_credits);
        RecyclerView rvScores = view.findViewById(R.id.rv_recent_scores);

        // Setup RecyclerView điểm gần đây
        rvScores.setLayoutManager(new LinearLayoutManager(getContext()));
        rvScores.setNestedScrollingEnabled(false);
        // Xử lý nút XEM TẤT CẢ ĐIỂM -> Chuyển sang Tab Điểm thi
        TextView tvViewAllScores = view.findViewById(R.id.tv_view_all_scores);
        if (tvViewAllScores != null) {
            tvViewAllScores.setOnClickListener(v -> {
                com.google.android.material.bottomnavigation.BottomNavigationView bnav = getActivity().findViewById(R.id.bottom_navigation);
                if (bnav != null) bnav.setSelectedItemId(R.id.nav_scores);
            });
        }

        // Hiển thị tạm thời trong lúc chờ API
        tvIdBadge.setText(studentId != null ? studentId : "Đang tải...");

        // GỌI API LẤY THÔNG TIN CHI TIẾT
        if (studentId != null && !studentId.isEmpty()) {
            com.example.btl.network.RetrofitClient.getApiService().getStudentById(studentId).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        com.example.btl.models.Student s = response.body().getData();

                        // Đổ dữ liệu thật lên giao diện
                        tvName.setText(s.getFullName() != null ? s.getFullName() : "Chưa cập nhật");
                        tvIdBadge.setText(s.getStudentId());
                        tvClass.setText(s.getClassName() != null ? s.getClassName() : "---");
                        tvMajor.setText(s.getMajor() != null ? s.getMajor() : "---");
                        tvEmail.setText(s.getEmail() != null ? s.getEmail() : "---");
                        tvPhone.setText(s.getPhone() != null ? s.getPhone() : "---");

                        // Xử lý avatar chữ cái đầu tiên
                        if (s.getFullName() != null && !s.getFullName().trim().isEmpty()) {
                            tvInitial.setText(String.valueOf(s.getFullName().trim().charAt(0)).toUpperCase());
                        } else {
                            tvInitial.setText("S");
                        }

                        // Mấy thông số này tạm thời fix cứng, mai mốt làm tính năng Điểm thi anh em mình gắn API sau
                        tvGpa.setText("3.45");
                        tvCredits.setText("87");
                        // Lấy 2 điểm gần nhất hiển thị lên RecyclerView
                        com.example.btl.network.RetrofitClient.getApiService().getScoresByStudent(studentId).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Score>>>() {
                            @Override
                            public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Score>>> call, retrofit2.Response<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Score>>> res) {
                                if (res.isSuccessful() && res.body() != null && res.body().isSuccess()) {
                                    java.util.List<com.example.btl.models.Score> allScores = res.body().getData();
                                    java.util.List<com.example.btl.models.Score> recentScores = new java.util.ArrayList<>();

                                    // Đảo ngược danh sách và lấy tối đa 2 điểm
                                    if (allScores != null) {
                                        java.util.Collections.reverse(allScores);
                                        for (int i = 0; i < Math.min(2, allScores.size()); i++) {
                                            recentScores.add(allScores.get(i));
                                        }
                                    }

                                    com.example.btl.adapters.ScoreAdapter adapter = new com.example.btl.adapters.ScoreAdapter(recentScores);
                                    rvScores.setAdapter(adapter);
                                }
                            }
                            @Override public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<java.util.List<com.example.btl.models.Score>>> call, Throwable t) {}
                        });

                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy dữ liệu sinh viên này!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}