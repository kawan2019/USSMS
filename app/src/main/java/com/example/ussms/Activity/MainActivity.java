package com.example.ussms.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
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

import com.bumptech.glide.Glide;
import com.example.ussms.Fragment.Friends;
import com.example.ussms.Fragment.HomeFragment;
import com.example.ussms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private static final String TOKEN ="TOKEN" ;
    private int mSelectedId;
    private final Handler mDrawerHandler = new Handler();
    private Toolbar toolbar;
    DrawerLayout drawer;
    private ImageButton igb_classroom;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView tv_username_heder_nav,tv_email_heder_nav;
    CircleImageView cig_heder_nav;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.a_main);


        mAuth = FirebaseAuth.getInstance();









        Log.d("TOKENGG", FirebaseInstanceId.getInstance().getToken());
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
         drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        tv_email_heder_nav = hView.findViewById(R.id.email_header_nav);
        tv_username_heder_nav = hView.findViewById(R.id.username_header_nav);
        cig_heder_nav = hView.findViewById(R.id.cig_heder_nav);

        tv_email_heder_nav.setText(mAuth.getCurrentUser().getEmail()+"");
        tv_username_heder_nav.setText(mAuth.getCurrentUser().getDisplayName());
        Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(cig_heder_nav);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_logout, R.id.nav_ch,R.id.nav_friend,R.id.nav_reset,R.id.nav_share,R.id.nav_send,R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
            case R.id.nav_friend:
                setTitle(R.string.menu_language);
                navFragment = new Friends();
                break;
            case R.id.nav_setting:
                Toast.makeText(getApplicationContext(),"sed",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_ch:
                changeLanguage();
                break;
            case R.id.nav_logout:
                if (mAuth.getCurrentUser() != null){
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put(TOKEN, "");
                    fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(),Splash.class));
                            Toasty.success(getApplicationContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(), "An Error", Toast.LENGTH_LONG).show();
                        }
                    });

                }else {
                    startActivity(new Intent(this,Splash.class));
                }
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


    private void changeLanguage() {
        final String[] listItems = {"English","کوردی"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    public void g(Fragment navFragment){
        if (navFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            try {
                transaction.replace(R.id.nav_host_fragment, navFragment).commit();
            } catch (IllegalStateException ignored) {
            }
        }
    }
    public void loadLocale(){
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("Lang","");
        setLocale(language);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
