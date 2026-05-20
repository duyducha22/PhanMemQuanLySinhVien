package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.btl.R;
import com.example.btl.activities.AddEditStudentActivity;
import com.example.btl.adapters.StudentAdapter;
import com.example.btl.models.Student;
import java.util.*;

public class AdminStudentsFragment extends Fragment {

    private StudentAdapter adapter;
    private List<Student>  studentList = new ArrayList<>();
    private TextView       tvTotal;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotal = view.findViewById(R.id.tv_total_students);
        RecyclerView rv = view.findViewById(R.id.rv_admin_students);
        EditText etSearch = view.findViewById(R.id.et_admin_search);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // Dùng StudentAdapter với listener cho admin (edit/delete)
        adapter = new StudentAdapter(studentList, student -> {
            Intent intent = new Intent(getContext(), AddEditStudentActivity.class);
            intent.putExtra(AddEditStudentActivity.EXTRA_STUDENT_ID, student.getStudentId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        // Tìm kiếm realtime
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString().trim());
            }
        });

        loadStudents();
    }

    private void loadStudents() {
        // TODO: Gọi API
        // RetrofitClient.getApiService().getAllStudents().enqueue(...)

        // ---- Dữ liệu mẫu ----
        Student s1 = new Student();
        s1.setStudentId("A12345"); s1.setFullName("Nguyễn Văn An");
        s1.setClassName("CNTT1"); s1.setMajor("Công nghệ thông tin"); s1.setStatus("active");

        Student s2 = new Student();
        s2.setStudentId("B67890"); s2.setFullName("Trần Thị Bình");
        s2.setClassName("KT2"); s2.setMajor("Kế toán"); s2.setStatus("active");

        studentList.clear();
        studentList.add(s1); studentList.add(s2);
        adapter.notifyDataSetChanged();
        tvTotal.setText("Tổng: " + studentList.size() + " sinh viên");
    }

    private void filterStudents(String keyword) {
        // TODO: Lọc danh sách hoặc gọi API search
        tvTotal.setText("Lọc: " + keyword);
    }
}