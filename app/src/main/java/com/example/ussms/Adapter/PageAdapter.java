package com.example.ussms.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ussms.Fragment.News.NewsAddPost;
import com.example.ussms.Fragment.News.NewsHome;
import com.example.ussms.Fragment.News.NewsNotification;
import com.example.ussms.Fragment.News.NewsProfile;
import com.example.ussms.Fragment.News.NewsSearch;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsHome();
            case 1:
                return new NewsSearch();
            case 2:
                return new NewsAddPost();
            case 3:
                return new NewsNotification();
            case 4:
                return new NewsProfile();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}