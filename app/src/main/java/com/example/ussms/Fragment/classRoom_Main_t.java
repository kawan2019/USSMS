package com.example.ussms.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ussms.Activity.NewPostClassRoom;
import com.example.ussms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoom_Main_t extends Fragment {

    private BottomNavigationView mainbottomNav;
    private FragmentActivity myContext;
    private classRoomHome_t homeFragment;
    private classRoomNotification_t notificationFragment;
    private classRoomAcount_t accountFragment;
    private ClassR_t class_r;
    private FloatingActionButton addPostBtn;

    public String getCl_n() {
        return cl_n;
    }

    public void setCl_n(String cl_n) {
        this.cl_n = cl_n;
    }

    private String cl_n;


    public classRoom_Main_t() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.f_class_room__main_t, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        String cn = pref.getString("CN","");

        Toast.makeText(getContext(),cn,Toast.LENGTH_LONG).show();

        mainbottomNav = view.findViewById(R.id.mainBottomNav);
        homeFragment = new classRoomHome_t();
        class_r = new ClassR_t();
        notificationFragment = new classRoomNotification_t();
        accountFragment = new classRoomAcount_t();
        addPostBtn = view.findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(getContext(), NewPostClassRoom.class);
                startActivity(newPostIntent);
            }
        });
        initializeFragment();
        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment currentFragment = myContext.getSupportFragmentManager().findFragmentById(R.id.main_container);
                switch (item.getItemId()) {
                    case R.id.bottom_item_home_cr:
                        replaceFragment(homeFragment, currentFragment);
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
        return view;
    }
    public void displayReceivedData(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
    private void initializeFragment(){
        FragmentTransaction fragmentTransaction = myContext.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, notificationFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);
        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(accountFragment);
        fragmentTransaction.commit();
    }
    private void replaceFragment(Fragment fragment, Fragment currentFragment){
        FragmentTransaction fragmentTransaction = myContext.getSupportFragmentManager().beginTransaction();
        if(fragment == homeFragment){
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
        }
        if(fragment == accountFragment){
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);
        }
        if(fragment == notificationFragment){
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext = (FragmentActivity) activity;
    }

}
