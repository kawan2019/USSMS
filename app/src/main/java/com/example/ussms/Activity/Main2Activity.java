package com.example.ussms.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ussms.Fragment.HomeFragment;
import com.example.ussms.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private int mSelectedId;
    private final Handler mDrawerHandler = new Handler();
    private Toolbar toolbar;
    private AlertDialog alertDialogLogin;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main2);
         toolbar = findViewById(R.id.toolbar_n);
        setSupportActionBar(toolbar);
         drawer = findViewById(R.id.drawer_layout_n);
        NavigationView navigationView = findViewById(R.id.nav_view_n);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_n);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mSelectedId = item.getItemId();
                mDrawerHandler.removeCallbacksAndMessages(null);
                mDrawerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigate(mSelectedId);
                    }
                }, 250);
                drawer.closeDrawers();
                return true;
            }
        });
    }
    private void navigate(final int itemId) {
        Fragment navFragment = null;
        switch (itemId) {
            case R.id.nav_home:
                setTitle(R.string.menu_home);
                navFragment = new HomeFragment() ;
                break;
            case R.id.nav_login:
                loginDig();
                break;
            case R.id.nav_ch:
                changeLanguage();
                break;
            default:
                Toast.makeText(getApplicationContext(),"ops",Toast.LENGTH_LONG).show();
        }

        if (navFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            try {
                transaction.replace(R.id.nav_host_fragment, navFragment).commit();
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private void loginDig() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.email);
        alertDialogLogin = builder.create();
        alertDialogLogin.show();


    }

    private void changeLanguage() {
        final String[] listItems = {"English","کوردی"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("Change Language");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocale("en");
                    recreate();
                    finish();
                    startActivity(getIntent());
                }else {
                    setLocale("ku");
                    recreate();
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();

    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("Lang",lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("Lang","");
        setLocale(language);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_n);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void toSignUp(View view) {
        alertDialogLogin.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.register_dialog, null);
        builder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.email);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
