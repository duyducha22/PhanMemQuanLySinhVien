package com.example.btl.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.btl.fragments.AdminScoresFragment;
import com.example.btl.fragments.AdminStudentsFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new AdminStudentsFragment() : new AdminScoresFragment();
    }

    @Override
    public int getItemCount() { return 2; }
}