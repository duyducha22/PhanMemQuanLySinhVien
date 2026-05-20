package com.example.btl.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.btl.R;
import com.example.btl.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String currentStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentStudentId = getIntent().getStringExtra("student_id");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bnav = findViewById(R.id.bottom_navigation);

        // Mặc định hiển thị HomeFragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(currentStudentId));
            bnav.setSelectedItemId(R.id.nav_home);
        }

        bnav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if      (id == R.id.nav_home)    fragment = HomeFragment.newInstance(currentStudentId);
            else if (id == R.id.nav_search)  fragment = new SearchFragment();
            else if (id == R.id.nav_scores)  fragment = ScoresFragment.newInstance(currentStudentId);
            else if (id == R.id.nav_profile) fragment = ProfileFragment.newInstance(currentStudentId);

            if (fragment != null) { loadFragment(fragment); return true; }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}