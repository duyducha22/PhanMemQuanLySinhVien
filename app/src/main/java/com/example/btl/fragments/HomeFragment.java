package com.example.btl.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.ScoreAdapter;

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

    @Nullable @Override
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

        // Hiển thị mã SV đang đăng nhập
        if (studentId != null) tvIdBadge.setText(studentId);

        // Setup RecyclerView điểm gần đây
        rvScores.setLayoutManager(new LinearLayoutManager(getContext()));
        rvScores.setNestedScrollingEnabled(false);

        // TODO: Gọi API lấy thông tin sinh viên
        // RetrofitClient.getApiService()
        //     .getStudentById(studentId)
        //     .enqueue(new Callback<Student>() {
        //         @Override public void onResponse(...) {
        //             Student s = response.body();
        //             tvName.setText(s.getFullName());
        //             tvInitial.setText(s.getInitial());
        //             tvClass.setText(s.getClassName());
        //             tvMajor.setText(s.getMajor());
        //             tvEmail.setText(s.getEmail());
        //             tvPhone.setText(s.getPhone());
        //         }
        //         @Override public void onFailure(...) { }
        //     });

        // ---- Dữ liệu mẫu test giao diện ----
        tvName.setText("Nguyễn Văn An");
        tvIdBadge.setText(studentId != null ? studentId : "A12345");
        tvInitial.setText("A");
        tvClass.setText("CNTT1");
        tvMajor.setText("Công nghệ thông tin");
        tvEmail.setText("a12345@thanglong.edu.vn");
        tvPhone.setText("0901234567");
        tvGpa.setText("3.45");
        tvCredits.setText("87");
    }
}