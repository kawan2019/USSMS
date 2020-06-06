package com.example.ussms.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.ussms.Adapter.PageAdapter;
import com.example.ussms.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

public class News extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    BadgeDrawable badge;
    PageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_news);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_home_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.ic_search_blac_24dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.baseline_add_circle_outline_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.baseline_notification_important_black_18dp)));
        tabLayout.addTab(tabLayout.newTab().setIcon(getResources().getDrawable(R.drawable.baseline_person_black_18dp)));
        badge = tabLayout.getTabAt(3).getOrCreateBadge();
        badge.setVisible(true);
        badge.setNumber(99);


        pageAdapter = new PageAdapter(getSupportFragmentManager(),5);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

