package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.example.btl.R;
import com.example.btl.activities.LoginActivity;
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
        if (getArguments() != null)
            studentId = getArguments().getString(ARG_STUDENT_ID);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvInitial = view.findViewById(R.id.tv_profile_initial);
        TextView tvName    = view.findViewById(R.id.tv_profile_name);
        TextView tvId      = view.findViewById(R.id.tv_profile_id);
        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);

        // TODO: Gọi API lấy thông tin & điền vào các row
        // Tạm thời dùng dữ liệu mẫu
        tvInitial.setText("A");
        tvName.setText("Nguyễn Văn An");
        tvId.setText(studentId != null ? studentId : "A12345");

        // Điền các hàng thông tin (item_profile_row)
        setProfileRow(view, R.id.row_dob,     "Ngày sinh",    "01/01/2000");
        setProfileRow(view, R.id.row_gender,   "Giới tính",    "Nam");
        setProfileRow(view, R.id.row_email,    "Email",        "a12345@thanglong.edu.vn");
        setProfileRow(view, R.id.row_phone,    "Điện thoại",   "0901234567");
        setProfileRow(view, R.id.row_address,  "Địa chỉ",      "Hà Nội");
        setProfileRow(view, R.id.row_class,    "Lớp",          "CNTT1");
        setProfileRow(view, R.id.row_major,    "Ngành",        "Công nghệ thông tin");
        setProfileRow(view, R.id.row_year,     "Năm nhập học", "2022");

        btnLogout.setOnClickListener(v -> {
            // Xóa session / SharedPreferences nếu cần
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    // Hàm tiện ích điền label + value vào item_profile_row
    private void setProfileRow(View parent, int rowId, String label, String value) {
        View row = parent.findViewById(rowId);
        if (row == null) return;
        TextView tvLabel = row.findViewById(R.id.tv_label);
        TextView tvValue = row.findViewById(R.id.tv_value);
        if (tvLabel != null) tvLabel.setText(label);
        if (tvValue != null) tvValue.setText(value);
    }
}