package com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.btec.fpt.campus_expense_manager.R;

public class AnalyticsFragment extends Fragment {
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_reports, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new AnalyticsPagerAdapter(getChildFragmentManager()));
        return view;
    }

    private class AnalyticsPagerAdapter extends FragmentPagerAdapter {
        public AnalyticsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DataVisualizationFragment();
                case 1:
                    return new ReportFragment();
                default:
                    return new DataVisualizationFragment();
            }
        }

        @Override
        public int getCount() {
            return 2; // Number of fragments
        }
    }
}
