package com.example.ussms.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tiper.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Main2Activity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfigurationn;
    private int mSelectedId;
    private final Handler mDrawerHandler = new Handler();
    private Toolbar toolbar;
    private AlertDialog alertDialogLogin, alertDialogRegister, alertDialogAllSet,alertDialogResendEmail;
    private DrawerLayout drawer;
    private boolean validateDepSp = true;
    private boolean validateLevSp = true;
    @ServerTimestamp Date time;
    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText edEmailLogin, edPasswordLogin, edUsernameRegister, edFullNameRegister, edEmailRegister, edPasswordRegister;
    TextInputLayout hoUsernameRegister,hoEmailRegister;
    private String email, emailL, paswwordL, password, fullname, username,department;
    private String who = "STN_1";

    private int level_;
    private Button btnLoginLogin, btnRegisterRegister,btnStudentRegister,btnTeacherRegester,btnOkayAllSet,btnResendEmail;
    private MaterialButtonToggleGroup toggleButton;
    MaterialSpinner spLevelRegister, spDepartmentRegister;
    private ImageButton btnCloseRegister, btnCloseLogin;
     Map<String,Object> user_,logs;
    View dialogView;
    private AVLoadingIndicatorView avi, avir;
    Integer [] Level = {1, 2, 3, 4};
    final static String USERNAME = "USERNAME";
    final static String DEPARTMENT = "DEPARTMENT";
    final static String FULLNAME = "FULLNAME";
    final static String EMAIL= "EMAIL";
    final static String LEVEL= "LEVEL";
    final static String IMEI = "IMEI";
    final static String DID = "DID";
    private final static String IMAGE = "IMAGE";
    private boolean ve = false;
    final static String UID = "UID";
    final static String DATE_CREATION = "DATE_CREATION";
    final static String STATUS = "STATUS";
    final static String TYPE = "TYPE";
    private static final String TAG = "EmailPassword";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private FirebaseAuth mAuth;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private CollectionReference cr = fsdb.collection("Users");
    private String TOKEN="TOKEN";
    private String token;

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

        dialogView.findViewById(R.id.btnClose_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogLogin.dismiss();
            }
        });
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
                    AnimationUtil.shakeView(edPasswordLogin, Main2Activity.this);
                    avi.hide();
                    btnLoginLogin.setVisibility(View.VISIBLE);
                }else {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    fsdb.collection("Users")
                                            .document(mAuth.getCurrentUser().getDisplayName())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(!documentSnapshot.exists()){
                                                        avi.hide();
                                                        btnLoginLogin.setVisibility(View.VISIBLE);
                                                        Toasty.error(getApplicationContext(), "An Error", Toast.LENGTH_LONG).show();
                                                    }else{

                                                        hideProgressBar();
                                                        String type = (String) documentSnapshot.get("TYPE");
                                                        String department = (String) documentSnapshot.get("DEPARTMENT");
                                                        long Level = (long) documentSnapshot.get("LEVEL");

                                                        SharedPreferences.Editor editor = getSharedPreferences("Account",MODE_PRIVATE).edit();
                                                        editor.putString("Type",type);
                                                        editor.putString("Department",department);
                                                        editor.putLong("Level",Level);
                                                        editor.apply();

                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            Log.w(TAG, "getInstanceId failed", task.getException());
                                                                            avi.hide();
                                                                            btnLoginLogin.setVisibility(View.VISIBLE);
                                                                            return;
                                                                        }
                                                                        String token = task.getResult().getToken();
                                                                        Map<String, Object> userMap = new HashMap<>();
                                                                        Log.d(TAG,token);
                                                                        userMap.put(TOKEN, token);
                                                                        fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                startActivity(new Intent(Main2Activity.this,Splash.class));
                                                                                Toasty.success(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toasty.error(getApplicationContext(), "An Error", Toast.LENGTH_LONG).show();
                                                                                avi.hide();
                                                                                btnLoginLogin.setVisibility(View.VISIBLE);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Error",e.getMessage());
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
                                Toasty.warning(getApplicationContext(), "Sorry "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG,task.getException().getMessage());
                                avi.hide();
                                btnLoginLogin.setVisibility(View.VISIBLE);
                            }
                            avi.hide();
                            btnLoginLogin.setVisibility(View.VISIBLE);}
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
        //Signup cdd=new Signup(Main2Activity.this);

       // cdd.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.d_register, null);
        builder.setView(dialogView);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) { mAuth.signOut(); }
        edUsernameRegister = dialogView.findViewById(R.id.edUsername_register);;
        edFullNameRegister = dialogView.findViewById(R.id.edFullname_register);
        edEmailRegister = dialogView.findViewById(R.id.edEmail_register);
        edPasswordRegister = dialogView.findViewById(R.id.edPassword_register);
        btnRegisterRegister = dialogView.findViewById(R.id.btnRegister_register);
        avir = dialogView.findViewById(R.id.avi_register);

        spDepartmentRegister = dialogView.findViewById(R.id.spDepartment_register);
        spLevelRegister = dialogView.findViewById(R.id.spLevel_register);

        dialogView.findViewById(R.id.btnClose_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogRegister.dismiss();
            }
        });
        dialogView.findViewById(R.id.STN_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                who = "STN_1";
                validateLevSp = true;
                spLevelRegister.setVisibility(View.VISIBLE);
            }
        });
        dialogView.findViewById(R.id.TCHN_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                who = "TCHN_1";
                validateLevSp = false;
                spLevelRegister.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevelRegister.setAdapter(adp2);

        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this, R.array.departments_array
                , android.R.layout.simple_list_item_1);
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartmentRegister.setAdapter(adp3);

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
                level_ = i + 1;
                Toast.makeText(getApplicationContext(), level_ + "", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {
                validateLevSp = true;
            }
        });
        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                validateFilds();
                btnRegisterRegister.setVisibility(View.GONE);
            }
        });



        alertDialogRegister = builder.create();
        alertDialogRegister.show();
    }


    private void validateFilds() {
        username =  edUsernameRegister.getText().toString();
        fullname = edFullNameRegister.getText().toString();
        email = edEmailRegister.getText().toString().trim() + "@gmail.com";
        password = edPasswordRegister.getText().toString();
        if (!TextUtils.isEmpty(username)){
            fsdb.collection("Users")
                    .document(username)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(!documentSnapshot.exists()){
                                registerUser();
                            }else{
                                Toasty.error(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT,true).show();
                                AnimationUtil.shakeView(edUsernameRegister, getApplicationContext());
                                Log.e("Error",".getMessage()");
                                avir.hide();
                                btnRegisterRegister.setVisibility(View.VISIBLE);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error",e.getMessage());
                            btnRegisterRegister.setVisibility(View.VISIBLE);
                        }
                    });
        }else {
            AnimationUtil.shakeView(edUsernameRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        }
        if ((!username.matches("[a-zA-Z._]*")) || (username.length() < 5)) {
            AnimationUtil.shakeView(edUsernameRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if ((fullname.length() < 9) || (!fullname.matches("[a-zA-Z ]*"))) {
            AnimationUtil.shakeView(edFullNameRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(email) || (ve)) {
            AnimationUtil.shakeView(edEmailRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if ((TextUtils.isEmpty(password)) || (password.length() < 8)) {
            AnimationUtil.shakeView(edPasswordRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (validateDepSp) {
            AnimationUtil.shakeView(spDepartmentRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (validateLevSp) {
            AnimationUtil.shakeView(spLevelRegister, getApplicationContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        }
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> usernameMap = new HashMap<String, Object>();
                    usernameMap.put(USERNAME, username);
                    fsdb.collection("Users")
                            .document(username)
                            .set(usernameMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    task.getResult()
                                            .getUser()
                                            .sendEmailVerification()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    final String userUid = task.getResult().getUser().getUid();
                                                    if (task.isSuccessful()) {
                                                        Map<String, Object> userMap = new HashMap<>();
                                                        userMap.put(UID, userUid);
                                                        userMap.put(USERNAME, username);
                                                        userMap.put(FULLNAME, fullname);
                                                        userMap.put(IMAGE, "https://firebasestorage.googleapis.com/v0/b/ussms-1c788.appspot.com/o/Defaults%2FPngItem_2145309.png?alt=media&token=e47460f0-12aa-4f95-819a-5ee70d602ebd");
                                                        userMap.put(EMAIL, email);
                                                        userMap.put(DEPARTMENT, department);
                                                        userMap.put(LEVEL, level_);
                                                        userMap.put(TYPE, who);
                                                        userMap.put(TOKEN, token);
                                                        userMap.put(STATUS, false);

                                                        fsdb.collection("Users").document(username).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toasty.success(getApplicationContext(), "Verification email sent", Toasty.LENGTH_SHORT, true).show();
                                                                    }
                                                                });

                                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                        .setDisplayName(username)
                                                                        .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/ussms-1c788.appspot.com/o/Defaults%2FPngItem_2145309.png?alt=media&token=e47460f0-12aa-4f95-819a-5ee70d602ebd"))
                                                                        .build();
                                                                mAuth.getCurrentUser().updateProfile(profileUpdates);

                                                                FirebaseInstanceId.getInstance().getInstanceId()
                                                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                                if (!task.isSuccessful()) {
                                                                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                                                                    return;
                                                                                }
                                                                                String token = task.getResult().getToken();
                                                                                Map<String, Object> userMap = new HashMap<>();
                                                                                Log.d(TAG,token);
                                                                                userMap.put(TOKEN, token);
                                                                                fsdb.collection("Users").document(username).update(userMap);
                                                                            }
                                                                        });
                                                                final Map<String, Object> logs = new HashMap();
                                                                logs.put(DATE_CREATION, getDate());
                                                                logs.put(DID, getDeviceUniqueID());
                                                                logs.put(IMEI, getDeviceIMEI());
                                                                logs.put(PHONE_NUMBER, getPhoneNumber());
                                                                fsdb.collection("Users").document(username).collection("Logs").document("Creation").set(logs);
                                                                alertDialogRegister.dismiss();
                                                                mAuth.signOut();
                                                                dialogAllSet();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toasty.error(getApplicationContext(), "Error: " + e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                                                                avir.hide();
                                                                btnRegisterRegister.setVisibility(View.VISIBLE);
                                                            }
                                                        });

                                                    }else {
                                                        Toasty.error(getApplicationContext(), "Error: " + task.getException().getMessage(), Toasty.LENGTH_SHORT, true).show();
                                                        avir.hide();
                                                        btnRegisterRegister.setVisibility(View.VISIBLE);
                                                    }

                                                }

                                            });
                                    avir.hide();
                                    btnRegisterRegister.setVisibility(View.VISIBLE);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    task.getResult().getUser().delete();
                                    avir.hide();
                                    btnRegisterRegister.setVisibility(View.VISIBLE);
                                }
                            });
                }else {
                    if (task.getException().getMessage().equalsIgnoreCase("The email address is already in use by another account.")){
                        Log.d(TAG,task.getException().getMessage()+"");
                        Toasty.error(getApplicationContext(),"This email already used.",Toasty.LENGTH_LONG,true).show();
                        avir.hide();
                        btnRegisterRegister.setVisibility(View.VISIBLE);
                    }else {
                        Toasty.error(getApplicationContext(),"Error, Please Check Network Connection.",Toasty.LENGTH_LONG,true).show();
                        avir.hide();
                        btnRegisterRegister.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }
    private String getDate() {return FieldValue.serverTimestamp().toString();}
    private String getPhoneNumber() {
        String mPhoneNumber = "";
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please Allow",Toast.LENGTH_LONG).show();
        }else {mPhoneNumber = tMgr.getLine1Number();}
        return mPhoneNumber;
    }
    public String getDeviceUniqueID(){
        String device_unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Please Allow ",Toast.LENGTH_LONG).show();
                return null;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }
    private void dialogAllSet(){
        alertDialogRegister.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogViewe = inflater.inflate(R.layout.d_allset, null);
        builder.setView(dialogViewe);
        btnOkayAllSet = dialogViewe.findViewById(R.id.btnOkay_allset);
        btnOkayAllSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAllSet.dismiss();
            }
        });
        alertDialogAllSet = builder.create();
        alertDialogAllSet.show();
    }

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
        alertDialogRegister.dismiss();
        alertDialogLogin.show();
    }
    public void her(View view) {   }
    private void hideProgressBar() { if (avir!=null){
        avi.hide();
        btnLoginLogin.setVisibility(View.VISIBLE);
    }}
    private void showProgressBar() {if (avir!=null){
        avir.show();
        btnLoginLogin.setVisibility(View.INVISIBLE);
    }}

}
