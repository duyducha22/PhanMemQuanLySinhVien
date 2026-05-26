package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl.R;
import com.example.btl.activities.LoginActivity;
import com.example.btl.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private String studentId;

    public static ProfileFragment newInstance(String studentId) {
        ProfileFragment f = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENT_ID, studentId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) studentId = getArguments().getString(ARG_STUDENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvInitial = view.findViewById(R.id.tv_profile_initial);
        TextView tvName    = view.findViewById(R.id.tv_profile_name);
        TextView tvId      = view.findViewById(R.id.tv_profile_id);
        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);

        tvId.setText(studentId != null ? studentId : "Đang tải...");

        // GỌI API LẤY THÔNG TIN CÁ NHÂN
        if (studentId != null) {
            RetrofitClient.getApiService().getStudentById(studentId).enqueue(new retrofit2.Callback<com.example.btl.network.ApiResponse<com.example.btl.models.Student>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, retrofit2.Response<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        com.example.btl.models.Student s = response.body().getData();
                        tvName.setText(s.getFullName());
                        tvId.setText(s.getStudentId());
                        if (s.getFullName() != null && !s.getFullName().isEmpty()) {
                            tvInitial.setText(String.valueOf(s.getFullName().trim().charAt(0)).toUpperCase());
                        }

                        setProfileRow(view, R.id.row_dob,     "Ngày sinh",    s.getDob());
                        setProfileRow(view, R.id.row_gender,   "Giới tính",    s.getGender());
                        setProfileRow(view, R.id.row_email,    "Email",        s.getEmail());
                        setProfileRow(view, R.id.row_phone,    "Điện thoại",   s.getPhone());
                        setProfileRow(view, R.id.row_address,  "Địa chỉ",      s.getAddress());
                        setProfileRow(view, R.id.row_class,    "Lớp",          s.getClassName());
                        setProfileRow(view, R.id.row_major,    "Ngành",        s.getMajor());
                        setProfileRow(view, R.id.row_year,     "Năm nhập học", String.valueOf(s.getEnrollmentYear()));
                    }
                }
                @Override public void onFailure(retrofit2.Call<com.example.btl.network.ApiResponse<com.example.btl.models.Student>> call, Throwable t) {}
            });
        }

        // Xử lý nút Đăng xuất
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void setProfileRow(View parent, int rowId, String label, String value) {
        View row = parent.findViewById(rowId);
        if (row == null) return;
        TextView tvLabel = row.findViewById(R.id.tv_label);
        TextView tvValue = row.findViewById(R.id.tv_value);
        if (tvLabel != null) tvLabel.setText(label);
        if (tvValue != null) tvValue.setText(value != null && !value.isEmpty() ? value : "---");
    }
}