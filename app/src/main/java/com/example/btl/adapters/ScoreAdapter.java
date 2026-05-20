package com.example.btl.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.models.Score;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private final List<Score> data;

    public ScoreAdapter(List<Score> data) { this.data = data; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Score s = data.get(pos);
        h.tvCode.setText(s.getSubjectCode());
        h.tvName.setText(s.getSubjectName());
        h.tvCredits.setText(s.getCredits() + " tín chỉ");
        h.tvMidterm.setText(String.valueOf(s.getMidtermScore()));
        h.tvFinal.setText(String.valueOf(s.getFinalScore()));
        h.tvAvg.setText(String.valueOf(s.getAverageScore()));
        h.tvGrade.setText(s.getGrade() != null ? s.getGrade() : "—");

        // Đổi màu badge theo điểm
        int bgRes = "F".equals(s.getGrade())
                ? R.drawable.bg_grade_f : R.drawable.bg_grade_a;
        h.tvGrade.setBackgroundResource(bgRes);
    }

    @Override public int getItemCount() { return data.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvName, tvCredits, tvMidterm, tvFinal, tvAvg, tvGrade;
        ViewHolder(@NonNull View v) {
            super(v);
            tvCode    = v.findViewById(R.id.tv_subject_code);
            tvName    = v.findViewById(R.id.tv_subject_name);
            tvCredits = v.findViewById(R.id.tv_credits);
            tvMidterm = v.findViewById(R.id.tv_midterm_score);
            tvFinal   = v.findViewById(R.id.tv_final_score);
            tvAvg     = v.findViewById(R.id.tv_avg_score);
            tvGrade   = v.findViewById(R.id.tv_grade_badge);
        }
    }
}
