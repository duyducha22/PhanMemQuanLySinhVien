package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.activities.AddEditStudentActivity;
import com.example.btl.adapters.StudentAdapter;
import com.example.btl.models.Student;
import com.example.btl.network.ApiResponse;
import com.example.btl.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStudentsFragment extends Fragment {

    private StudentAdapter adapter;
    private List<Student>  studentList = new ArrayList<>();
    private TextView       tvTotal;

    @Nullable
    @Override
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

        // Cài đặt Adapter và bắt sự kiện khi bấm vào 1 sinh viên trong danh sách
        adapter = new StudentAdapter(studentList, student -> {
            Intent intent = new Intent(getContext(), AddEditStudentActivity.class);
            intent.putExtra(AddEditStudentActivity.EXTRA_STUDENT_ID, student.getStudentId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        // Tính năng tìm kiếm (Sẽ hoàn thiện sau)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString().trim());
            }
        });

        // Kích hoạt hàm lấy dữ liệu từ server khi vừa mở màn hình
        loadStudents();
    }

    // Mỗi lần quay lại màn hình này (ví dụ vừa thêm SV xong), danh sách sẽ tự động làm mới
    @Override
    public void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        // Gọi API lấy toàn bộ danh sách sinh viên
        RetrofitClient.getApiService().getAllStudents().enqueue(new Callback<ApiResponse<List<Student>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Student>>> call, Response<ApiResponse<List<Student>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Xóa dữ liệu cũ, nhét dữ liệu mới từ Server vào
                    studentList.clear();
                    studentList.addAll(response.body().getData());

                    // Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại giao diện
                    adapter.notifyDataSetChanged();

                    // Cập nhật tổng số lượng
                    tvTotal.setText("Tổng: " + studentList.size() + " sinh viên");
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu sinh viên!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Student>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối máy chủ: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterStudents(String keyword) {
        // Tạm thời giữ nguyên hiển thị keyword, tính năng lọc mình làm sau
        tvTotal.setText("Lọc: " + keyword);
    }
}