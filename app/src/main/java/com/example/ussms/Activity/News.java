package com.example.ussms.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ussms.Fragment.News.NewsAddPost;
import com.example.ussms.Fragment.News.NewsHome;
import com.example.ussms.Fragment.News.NewsNotification;
import com.example.ussms.Fragment.News.NewsProfile;
import com.example.ussms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class News extends AppCompatActivity {
    private BottomNavigationView mainbottomNav;
    private NewsHome homeFragment;
    private NewsNotification notificationFragment;
    private NewsProfile accountFragment;
    private NewsAddPost addPostFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mainbottomNav = findViewById(R.id.bottom_navigation);

        homeFragment = new NewsHome();
        notificationFragment = new NewsNotification();
        accountFragment = new NewsProfile();
        addPostFragment = new NewsAddPost();

        initializeFragment();
        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                switch (item.getItemId()) {
                    case R.id.bottom_item_home_news:
                        replaceFragment(homeFragment, currentFragment);
                        return true;
                    case R.id.bottom_item_add_news:
                        replaceFragment(addPostFragment, currentFragment);
                        return true;
                    case R.id.bottom_item_notifi_news:
                        replaceFragment(notificationFragment, currentFragment);
                        return true;
                    case R.id.bottom_item_profile_news:
                        replaceFragment(accountFragment, currentFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    private void initializeFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_a_news, homeFragment);
        fragmentTransaction.add(R.id.frame_a_news, notificationFragment);
        fragmentTransaction.add(R.id.frame_a_news, accountFragment);
        fragmentTransaction.add(R.id.frame_a_news, addPostFragment);
        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(accountFragment);
        fragmentTransaction.hide(addPostFragment);
        fragmentTransaction.commit();
    }
    private void replaceFragment(Fragment fragment, Fragment currentFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == homeFragment){
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(addPostFragment);
        }
        if(fragment == accountFragment){
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(addPostFragment);
        }
        if(fragment == notificationFragment){
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(addPostFragment);
        }
        if(fragment == addPostFragment){
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}
