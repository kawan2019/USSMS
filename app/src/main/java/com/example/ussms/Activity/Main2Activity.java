package com.example.ussms.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.example.ussms.Dialogs.Signup;
import com.example.ussms.Fragment.HomeFragmentN;
import com.example.ussms.R;
import com.example.ussms.Utils.AnimationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Main2Activity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfigurationn;
    private int mSelectedId;
    private final Handler mDrawerHandler = new Handler();
    private Toolbar toolbar;
    private AlertDialog alertDialogLogin,alertDialogResendEmail;
    private DrawerLayout drawer;
    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText edEmailLogin, edPasswordLogin;
    private String email, password, username;
    private Button btnLoginLogin,btnResendEmail;
    private AVLoadingIndicatorView avi;
    private String token;
    private static final String TOKEN = "TOKEN";
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    Signup cdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.a_main2);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this) ;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){mAuth.signOut();}
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
        View dialogView = inflater.inflate(R.layout.d_login, null);
        builder.setView(dialogView);
        edEmailLogin = dialogView.findViewById(R.id.edEmail_login);
        edPasswordLogin = dialogView.findViewById(R.id.edPassword_login);
        btnLoginLogin = dialogView.findViewById(R.id.btnLogin_login);
        avi = dialogView.findViewById(R.id.avi);
        avi.setIndicator("BallSpinFadeLoaderIndicator");

        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edEmailLogin.getText().toString().trim()+"@gmail.com";
                password = edPasswordLogin.getText().toString().trim();

                btnLoginLogin.setVisibility(View.INVISIBLE);
                avi.show();
                if (TextUtils.isEmpty(email)) {
                    AnimationUtil.shakeView(edEmailLogin, Main2Activity.this);
                    avi.hide();
                    btnLoginLogin.setVisibility(View.VISIBLE);
                }else if (TextUtils.isEmpty(password)){
                    AnimationUtil.shakeView(edEmailLogin, Main2Activity.this);
                    avi.hide();
                    btnLoginLogin.setVisibility(View.VISIBLE);
                }else {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    Toasty.success(getApplicationContext(),"Login Successfully",Toasty.LENGTH_LONG).show();
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                                        return;
                                                    }
                                                    token = task.getResult().getToken();
                                                    username = mAuth.getCurrentUser().getDisplayName();
                                                    Map<String, Object> userMap = new HashMap<>();
                                                    userMap.put(TOKEN, token);
                                                    Log.d(TAG,token);
                                                    fsdb.collection("Users").document(username).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG,"Success");
                                                            avi.hide();
                                                            startActivity(new Intent(Main2Activity.this,Splash.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG,"ERROR"+e.getMessage());
                                                        }
                                                    });
                                                }
                                            });

                                }else {
                                    alertDialogLogin.dismiss();
                                    View parentLayout = findViewById(android.R.id.content);
                                    Snackbar.make(parentLayout, "You need to confirm your account", Snackbar.LENGTH_LONG)
                                            .setAction("Resend Email", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    sendEmailVerification();
                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                            .show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),task.getException().getMessage()+"",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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
    public void toSignUp(final View view) {
        alertDialogLogin.dismiss();
        cdd=new Signup(Main2Activity.this);
        cdd.show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.register_dialog, null);
//        builder.setView(dialogView);
//
//        edUsernameRegister = dialogView.findViewById(R.id.edUsername_register);
//        edFullNameRegister = dialogView.findViewById(R.id.edFullname_register);
//        edEmailRegister = dialogView.findViewById(R.id.edEmail_register);
//        edPasswordRegister = dialogView.findViewById(R.id.edPassword_register);
//        btnRegisterRegister = dialogView.findViewById(R.id.btnRegister_register);
//        avir = dialogView.findViewById(R.id.avi_register);
//        spDepartmentRegister = dialogView.findViewById(R.id.spDepartment_register);
//        spLevelRegister = dialogView.findViewById(R.id.spLevel_register);
//        toggleButton = dialogView.findViewById(R.id.tgWho_register);
//        hoUsernameRegister = dialogView.findViewById(R.id.hoUsername_register);
//        hoEmailRegister = dialogView.findViewById(R.id.hoEmail_register);
//        dialogView.findViewById(R.id.STN_1).setOnClickListener(this);
//        dialogView.findViewById(R.id.TCHN_1).setOnClickListener(this);
//
//        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Level);
//        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spLevelRegister.setAdapter(adp2);
//        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this, R.array.departments_array
//                , android.R.layout.simple_list_item_1);
//        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spDepartmentRegister.setAdapter(adp3);
//
//        spDepartmentRegister.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
//                validateDepSp = false;
//                department = materialSpinner.getSelectedItem().toString();
//            }
//            @Override
//            public void onNothingSelected(MaterialSpinner materialSpinner) {
//                validateDepSp = true;
//            }
//        });
//        spLevelRegister.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
//                validateLevSp = false;
//                level_ = i+1;
//                Toast.makeText(getApplicationContext(),level_+"",Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(MaterialSpinner materialSpinner) {
//                validateLevSp = true;
//            }
//        });
//        avir.setIndicator("BallSpinFadeLoaderIndicator");
//        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                avir.show();
//                final boolean[] vu = {false};
//                btnRegisterRegister.setVisibility(View.INVISIBLE);
//                username = edUsernameRegister.getText().toString();
//                fullname = edFullNameRegister.getText().toString();
//                email = edEmailRegister.getText().toString().trim()+"@uoh.edu.iq";
//                password = edPasswordRegister.getText().toString();
//                String date = FieldValue.serverTimestamp().toString();
//                fsdb.collection("Users")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        if (document.getId().equalsIgnoreCase(username)){
//                                            hoUsernameRegister.setError("Already Token");
//                                            vu[0] = false;
//                                            break;
//                                        }else {
//                                            hoUsernameRegister.setError(null);
//                                            vu[0] = true;
//                                        }
//                                        Log.d(TAG, document.getId()+"");
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//                if ((TextUtils.isEmpty(username)) || (!username.matches("[a-zA-Z._]*")) || (username.length() < 5) || (vu[0])) {
//                    AnimationUtil.shakeView(edUsernameRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                } else if ((fullname.length() < 9) || (!fullname.matches("[a-zA-Z ]*"))) {
//                    AnimationUtil.shakeView(edFullNameRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                } else if (TextUtils.isEmpty(email)) {
//                    AnimationUtil.shakeView(edEmailRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                } else if ((TextUtils.isEmpty(password)) || (password.length() < 8)) {
//                    AnimationUtil.shakeView(edPasswordRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                }else if(validateDepSp){
//                    AnimationUtil.shakeView(spDepartmentRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                }else if(validateLevSp){
//                    AnimationUtil.shakeView(spLevelRegister, Main2Activity.this);
//                    avir.hide();
//                    btnRegisterRegister.setVisibility(View.VISIBLE);
//                }else {
//                    user_ = new HashMap<>();
//                    logs = new HashMap<>();
//
//                    user_.put(USERNAME,username);
//                    user_.put(EMAIL,email);
//                    user_.put(FULLNAME,fullname);
//                    user_.put(DEPARTMENT,department);
//                    user_.put(LEVEL,level_);
//                    logs.put(DID,getDeviceUniqueID(Main2Activity.this));
//                    logs.put(DATE_CREATION,date);
//                    logs.put(IMEI,getDeviceIMEI());
//                    user_.put(TYPE,who);
//                    user_.put(STATUS,false);
//                    Log.d(TAG, "createAccount:" + email);
//                    showProgressBar();
//                    sendInfoToDB(user_,logs);
//
//                    fsdb.collection("Users").document(username).set(user_).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                fsdb.collection("Users").document(username).collection("Logs").document("Creation_Log").set(logs).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            Log.d(TAG, "lodUserInformation:success");
//                                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                                    if (task.isSuccessful()){
//                                                        Log.d(TAG, "createUserWithEmail:success");
//                                                        user_.put(UID,mAuth.getUid());
//                                                        fsdb.collection("users").document(username).update(user_);
//                                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                                                .setDisplayName(username)
//                                                                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                                                                .build();
//                                                        mAuth.getCurrentUser().updateProfile(profileUpdates);
//                                                        alertDialogRegister.dismiss();
//                                                        sendEmailVerification();
//                                                        dialogAllSet();
//
//                                                    }else {
//                                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                                        if (task.getException().getMessage().equalsIgnoreCase("The email address is already in use by another account.")){
//                                                            hoEmailRegister.setError(task.getException().getMessage()+"");
//                                                        }else {
//                                                            hoEmailRegister.setError(null);
//                                                        }
//                                                        Toast.makeText(Main2Activity.this, task.getException().getMessage()+"",Toast.LENGTH_SHORT).show();
//                                                        hideProgressBar();
//                                                        btnRegisterRegister.setVisibility(View.VISIBLE);
//                                                    }
//                                                }
//                                            });
//                                        }else {
//                                            Log.w(TAG, "lodLogsInformation:failure", task.getException());
//                                            Toast.makeText(Main2Activity.this,task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
//                                            hideProgressBar();
//                                            btnRegisterRegister.setVisibility(View.VISIBLE);
//
//                                        }
//
//                                    }
//                                });
//                            }else{
//                                Log.w(TAG, "lodUserInformation:failure", task.getException());
//                                Toast.makeText(Main2Activity.this,task.getException().getMessage()+"", Toast.LENGTH_SHORT).show();
//                                hideProgressBar();
//                                btnRegisterRegister.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//
//
//
//
//
//                }
//            }
//        });
//        alertDialogRegister = builder.create();
//        alertDialogRegister.show();
    }
/*
    private void dialogResendEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.resendemail_dialog, null);
        builder.setView(dialogView);
        btnResendEmail = dialogView.findViewById(R.id.btnResend_resendEmail);
        btnResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
                alertDialogResendEmail.dismiss();
                alertDialogLogin.show();
            }
        });
        alertDialogResendEmail = builder.create();
        alertDialogResendEmail.show();
    }
 */
    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Main2Activity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(Main2Activity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void toLogin(View view) {
        alertDialogLogin.show();
        cdd.dismiss();
    }
    public void her(View view) {   }
    private void hideProgressBar() { if (avi!=null){
        avi.hide();
        btnLoginLogin.setVisibility(View.VISIBLE);
    }}
    private void showProgressBar() {if (avi!=null){
        avi.show();
        btnLoginLogin.setVisibility(View.INVISIBLE);
    }}

}
