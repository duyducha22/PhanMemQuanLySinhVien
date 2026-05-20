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
import com.example.btl.models.Score;
import java.util.ArrayList;
import java.util.List;

public class ScoresFragment extends Fragment {

    private static final String ARG_STUDENT_ID = "student_id";
    private String studentId;
    private ScoreAdapter adapter;
    private List<Score> scoreList = new ArrayList<>();

    public static ScoresFragment newInstance(String studentId) {
        ScoresFragment f = new ScoresFragment();
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
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvGpa      = view.findViewById(R.id.tv_gpa_display);
        TextView tvCredits  = view.findViewById(R.id.tv_total_credits);
        TextView tvSubjects = view.findViewById(R.id.tv_subject_count);
        RecyclerView rv     = view.findViewById(R.id.rv_scores);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScoreAdapter(scoreList);
        rv.setAdapter(adapter);

        loadScores(tvGpa, tvCredits, tvSubjects);
    }

    private void loadScores(TextView tvGpa, TextView tvCredits, TextView tvSubjects) {
        // TODO: Gọi API lấy điểm
        // RetrofitClient.getApiService()
        //     .getScoresByStudent(studentId)
        //     .enqueue(new Callback<List<Score>>() { ... });

        // ---- Dữ liệu mẫu ----
        Score s1 = new Score();
        s1.setSubjectCode("CNTT101"); s1.setSubjectName("Lập trình hướng đối tượng");
        s1.setCredits(3); s1.setMidtermScore(8.5); s1.setFinalScore(9.0);
        s1.setAverageScore(s1.calcAverage()); s1.setGrade(Score.calcGrade(s1.getAverageScore()));
        s1.setSemester("HK1 2023-2024");

        Score s2 = new Score();
        s2.setSubjectCode("CNTT102"); s2.setSubjectName("Cơ sở dữ liệu");
        s2.setCredits(3); s2.setMidtermScore(7.0); s2.setFinalScore(7.5);
        s2.setAverageScore(s2.calcAverage()); s2.setGrade(Score.calcGrade(s2.getAverageScore()));
        s2.setSemester("HK1 2023-2024");

        scoreList.clear();
        scoreList.add(s1); scoreList.add(s2);
        adapter.notifyDataSetChanged();

        tvGpa.setText("3.45");
        tvCredits.setText("87");
        tvSubjects.setText(String.valueOf(scoreList.size()));
    }
}