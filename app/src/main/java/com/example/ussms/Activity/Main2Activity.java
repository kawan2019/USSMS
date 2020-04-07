package com.example.ussms.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ussms.Fragment.HomeFragmentN;
import com.example.ussms.R;
import com.example.ussms.Utils.AnimationUtil;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiper.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfigurationn;
    private int mSelectedId;
    private final Handler mDrawerHandler = new Handler();
    private Toolbar toolbar;
    private AlertDialog alertDialogLogin, alertDialogRegister;
    private DrawerLayout drawer;
    private boolean validateDepSp = true;
    private boolean validateLevSp = true;
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edEmailLogin, edPasswordLogin, edUsernameRegister, edFullNameRegister, edEmailRegister, edPasswordRegister;
    private String email, emailL, paswwordL, password, fullname, username,department,who;
    private int level_;
    private Button btnLoginLogin, btnRegisterRegister;
    private MaterialButtonToggleGroup toggleButton;
    MaterialSpinner spLevelRegister, spDepartmentRegister;
    private ImageButton btnCloseRegister, btnCloseLogin;
    private AVLoadingIndicatorView avi, avir;
    Integer [] Level = {1, 2, 3, 4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main2);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        
        toolbar = findViewById(R.id.toolbar_n);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout_n);
        NavigationView navigationView = findViewById(R.id.nav_view_n);
        mAppBarConfigurationn = new AppBarConfiguration.Builder(R.id.nav_home).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_n);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfigurationn);
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
            case R.id.nav_home_n:
                setTitle(R.string.menu_home);
                navFragment = new HomeFragmentN();
                break;
            case R.id.nav_login_n:
                loginDig();
                break;
            case R.id.nav_ch_n:
                changeLanguage();
                break;
            default:
                Toast.makeText(getApplicationContext(), "ops", Toast.LENGTH_LONG).show();
        }
        if (navFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            try {
                transaction.replace(R.id.nav_host_fragment_n, navFragment).commit();
            } catch (IllegalStateException ignored) {
            }
        }
    }
    private void loginDig() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        builder.setView(dialogView);
        edEmailLogin = (EditText) dialogView.findViewById(R.id.edEmail_login);
        edPasswordLogin = (EditText) dialogView.findViewById(R.id.edPassword_login);
        btnLoginLogin = (Button) dialogView.findViewById(R.id.btnLogin_login);
        avi = (AVLoadingIndicatorView) dialogView.findViewById(R.id.avi);
        avi.setIndicator("BallSpinFadeLoaderIndicator");
        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginLogin.setVisibility(View.INVISIBLE);
                avi.show();
            }
        });

        alertDialogLogin = builder.create();
        alertDialogLogin.show();
    }
    private void changeLanguage() {
        final String[] listItems = {"English", "کوردی"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("Change Language");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setLocale("en");
                    recreate();
                    finish();
                    startActivity(getIntent());
                } else {
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
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Lang", lang);
        editor.apply();
    }
    public void loadLocale() {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("Lang", "");
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
        return NavigationUI.navigateUp(navController, mAppBarConfigurationn)
                || super.onSupportNavigateUp();
    }

    public void toSignUp(View view) {
        alertDialogLogin.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.register_dialog, null);
        builder.setView(dialogView);

        edUsernameRegister = (EditText) dialogView.findViewById(R.id.edUsername_register);
        edFullNameRegister = (EditText) dialogView.findViewById(R.id.edFullname_register);
        edEmailRegister = (EditText) dialogView.findViewById(R.id.edEmail_register);
        edPasswordRegister = (EditText) dialogView.findViewById(R.id.edPassword_register);
        btnRegisterRegister = (Button) dialogView.findViewById(R.id.btnRegister_register);
        avir = (AVLoadingIndicatorView) dialogView.findViewById(R.id.avi_register);
        spDepartmentRegister = (MaterialSpinner) dialogView.findViewById(R.id.spDepartment_register);
        spLevelRegister = (MaterialSpinner) dialogView.findViewById(R.id.spLevel_register);
        toggleButton = (MaterialButtonToggleGroup) dialogView.findViewById(R.id.tgWho_register);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevelRegister.setAdapter(adp2);
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this, R.array.departments_array
                , android.R.layout.simple_list_item_1);
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartmentRegister.setAdapter(adp3);

        toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (checkedId == R.id.STN_1){
                    who = "STN_1";
                }else {
                    who = "TCHN_1";
                }
            }
        });

        spDepartmentRegister.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                validateDepSp = false;
                department = materialSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {
                validateDepSp = true;
            }
        });
        spLevelRegister.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                validateLevSp = false;
                level_ = i+1;
                Toast.makeText(getApplicationContext(),level_+"",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {
                validateLevSp = true;
            }
        });

        avir.setIndicator("BallSpinFadeLoaderIndicator");
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        Toast.makeText(getApplicationContext(), getDeviceIMEI()+"  "+getDeviceUniqueID(this), Toast.LENGTH_LONG).show();
        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avir.show();
                btnRegisterRegister.setVisibility(View.INVISIBLE);
                username = edUsernameRegister.getText().toString();
                fullname = edFullNameRegister.getText().toString();
                email = edEmailRegister.getText().toString();
                password = edPasswordRegister.getText().toString();
                if ((TextUtils.isEmpty(username)) || (!username.matches("[a-zA-Z._]*")) || (username.length() < 5)) {
                    AnimationUtil.shakeView(edUsernameRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                } else if ((fullname.length() < 9) || (!fullname.matches("[a-zA-Z ]*"))) {
                    AnimationUtil.shakeView(edFullNameRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(email)) {
                    AnimationUtil.shakeView(edEmailRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                } else if ((TextUtils.isEmpty(password)) || (password.length() < 8)) {
                    AnimationUtil.shakeView(edPasswordRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                }else if(validateDepSp){
                    AnimationUtil.shakeView(spDepartmentRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                }else if(validateLevSp){
                    AnimationUtil.shakeView(spLevelRegister, Main2Activity.this);
                    avir.hide();
                    btnRegisterRegister.setVisibility(View.VISIBLE);
                }else {


                }
            }
        });
        alertDialogRegister = builder.create();
        alertDialogRegister.show();
    }
    public String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Please Allow ",Toast.LENGTH_LONG).show();
                return null;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }
    public void toLogin(View view) {
        alertDialogRegister.dismiss();
        alertDialogLogin.show();
    }
    public void her(View view) {   }
    public void closeDL(View view) {alertDialogLogin.dismiss();}
    public void closeDR(View view) {alertDialogRegister.dismiss();}
}
