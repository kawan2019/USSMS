package com.example.ussms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ussms.Fragment.classRoomAcount_stu;
import com.example.ussms.Fragment.classRoomAcount_t;
import com.example.ussms.Fragment.classRoomHome_stu;
import com.example.ussms.Fragment.classRoomHome_t;
import com.example.ussms.Fragment.classRoomNotification_stu;
import com.example.ussms.Fragment.classRoomNotification_t;
import com.example.ussms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClassRoom_main_stu extends AppCompatActivity {

    private BottomNavigationView mainbottomNav;
    private FragmentActivity myContext;
    private classRoomHome_stu homeFragment_stu;
    private classRoomNotification_stu notificationFragment;
    private classRoomAcount_stu accountFragment;
    private Class_t class_r;
    private ImageButton mBack;

    public String getCl_n() {
        return cl_n;
    }

    public void setCl_n(String cl_n) {
        this.cl_n = cl_n;
    }

    private String cl_n;
    private String language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_class_room_main_stu);


        SharedPreferences pref = getSharedPreferences("Class", Activity.MODE_PRIVATE);
        String cn = pref.getString("CN","");


        Toast.makeText(this,cn,Toast.LENGTH_LONG).show();

        mBack = findViewById(R.id.backmain);

        mainbottomNav = findViewById(R.id.mainBottomNav);
        homeFragment_stu = new classRoomHome_stu();
//        class_r = new ClassR_t();
        notificationFragment = new classRoomNotification_stu();
        accountFragment = new classRoomAcount_stu();


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassRoom_main_stu.this,classroom_stu.class));
                 finish();
            }
        });
        initializeFragment();
        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                switch (item.getItemId()) {
                    case R.id.bottom_item_home_cr:

                        replaceFragment(homeFragment_stu, currentFragment);

                        return true;
                    case R.id.bottom_item_profile_cr:
                        replaceFragment(accountFragment, currentFragment);
                        return true;
                    case R.id.bottom_item_notifi_cr:
                        replaceFragment(notificationFragment, currentFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
    public void displayReceivedData(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    private void initializeFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, homeFragment_stu);

        fragmentTransaction.add(R.id.main_container, notificationFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);
        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(accountFragment);
        fragmentTransaction.commit();
    }
    private void replaceFragment(Fragment fragment, Fragment currentFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();



        if(fragment == homeFragment_stu){
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);

        }
        if(fragment == accountFragment){
            fragmentTransaction.hide(homeFragment_stu);
            fragmentTransaction.hide(notificationFragment);
        }
        if(fragment == notificationFragment){
            fragmentTransaction.hide(homeFragment_stu);
            fragmentTransaction.hide(accountFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }


}


