package com.example.btl.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.models.Student;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    private final List<Student>          data;
    private final OnStudentClickListener listener;

    public StudentAdapter(List<Student> data, OnStudentClickListener listener) {
        this.data     = data;
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Student s = data.get(pos);
        h.tvName.setText(s.getFullName());
        h.tvId.setText(s.getStudentId());
        h.tvClass.setText(s.getClassName());
        h.tvMajor.setText(s.getMajor());

        // Trích xuất chữ cái đầu tiên của tên để làm Avatar
        String fullName = s.getFullName();
        if (fullName != null && !fullName.trim().isEmpty()) {
            h.tvInitial.setText(String.valueOf(fullName.trim().charAt(0)).toUpperCase());
        } else {
            h.tvInitial.setText("S"); // Mặc định nếu không có tên
        }

        h.itemView.setOnClickListener(v -> listener.onStudentClick(s));
    }

    @Override public int getItemCount() { return data.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInitial, tvName, tvId, tvClass, tvMajor;
        ViewHolder(@NonNull View v) {
            super(v);
            tvInitial = v.findViewById(R.id.tv_item_initial);
            tvName    = v.findViewById(R.id.tv_item_name);
            tvId      = v.findViewById(R.id.tv_item_student_id);
            tvClass   = v.findViewById(R.id.tv_item_class);
            tvMajor   = v.findViewById(R.id.tv_item_major);
        }
    }
}