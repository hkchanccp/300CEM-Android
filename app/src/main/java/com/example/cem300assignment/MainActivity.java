package com.example.cem300assignment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.cem300assignment.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static ViewPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weather App");
        }

        // TabLayout
        tabLayout = findViewById(R.id.tabs);

        // Set up ViewPager with adapter
        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new TabToday(), "TabToday");
        mPagerAdapter.addFragment(new TabFiveDays(), "TabFiveDays");
        mViewPager.setAdapter(mPagerAdapter);

        // Event listener
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.tab_text_1);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.tab_text_2);
                        break;
                    default:
                        getSupportActionBar().setTitle(R.string.tab_text_1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}