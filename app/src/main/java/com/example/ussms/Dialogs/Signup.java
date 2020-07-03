package com.example.ussms.Dialogs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.ussms.R;
import com.example.ussms.Utils.AnimationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tiper.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Signup extends AlertDialog implements android.view.View.OnClickListener {
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private boolean ve = false;
    private AlertDialog  alertDialogAllSet;
    private boolean validateDepSp = true;
    private boolean validateLevSp = true;
    private int level_;
    private String username,token,email, password, fullname, who = "STN_1", department;
    private EditText  edUsernameRegister, edFullNameRegister, edEmailRegister, edPasswordRegister;
    private Button  btnRegisterRegister, btnOkayAllSet;
    private MaterialSpinner spLevelRegister, spDepartmentRegister;
    private AVLoadingIndicatorView avir;
    private Integer[] Level = {1, 2, 3, 4};
    private final static String USERNAME = "USERNAME";
    private final static String DEPARTMENT = "DEPARTMENT";
    private final static String FULLNAME = "FULLNAME";
    private final static String EMAIL = "EMAIL";
    private final static String LEVEL = "LEVEL";
    private final static String IMAGE = "IMAGE";
    private final static String IMEI = "IMEI";
    private final static String DID = "DID";
    private final static String UID = "UID";
    private final static String DATE_CREATION = "DATE_CREATION";
    private final static String STATUS = "STATUS";
    private final static String TOKEN = "TOKEN";
    private final static String TYPE = "TYPE";
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

    public Signup(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_register);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) { mAuth.signOut(); }
        edUsernameRegister = findViewById(R.id.edUsername_register);;
        edFullNameRegister = findViewById(R.id.edFullname_register);
        edEmailRegister = findViewById(R.id.edEmail_register);
        edPasswordRegister = findViewById(R.id.edPassword_register);
        btnRegisterRegister = findViewById(R.id.btnRegister_register);
        avir = findViewById(R.id.avi_register);

        spDepartmentRegister = findViewById(R.id.spDepartment_register);
        spLevelRegister = findViewById(R.id.spLevel_register);

        findViewById(R.id.btnClose_register).setOnClickListener(this);
        findViewById(R.id.STN_1).setOnClickListener(this);
        findViewById(R.id.TCHN_1).setOnClickListener(this);

        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLevelRegister.setAdapter(adp2);

        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(getContext(), R.array.departments_array
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
                Toast.makeText(getContext(), level_ + "", Toast.LENGTH_LONG).show();
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
            }
        });
    }
    private void validateFilds() {
        username = edUsernameRegister.getText().toString();
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
                                Toasty.error(getContext(), "Username already exists", Toast.LENGTH_SHORT,true).show();
                                AnimationUtil.shakeView(edUsernameRegister, getContext());
                                Log.e("Error",".getMessage()");
                                hideProgressBar();
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
            AnimationUtil.shakeView(edUsernameRegister, getContext());
            hideProgressBar();
        }
        if ((!username.matches("[a-zA-Z._]*")) || (username.length() < 5)) {
            AnimationUtil.shakeView(edUsernameRegister, getContext());
            hideProgressBar();
        } else if ((fullname.length() < 9) || (!fullname.matches("[a-zA-Z ]*"))) {
            AnimationUtil.shakeView(edFullNameRegister, getContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(email) || (ve)) {
            AnimationUtil.shakeView(edEmailRegister, getContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if ((TextUtils.isEmpty(password)) || (password.length() < 8)) {
            AnimationUtil.shakeView(edPasswordRegister, getContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (validateDepSp) {
            AnimationUtil.shakeView(spDepartmentRegister, getContext());
            avir.hide();
            btnRegisterRegister.setVisibility(View.VISIBLE);
        } else if (validateLevSp) {
            AnimationUtil.shakeView(spLevelRegister, getContext());
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
                                                                        Toasty.success(getContext(), "Verification email sent", Toasty.LENGTH_SHORT, true).show();
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
                                                                dismiss();
                                                                dialogAllSet();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toasty.error(getContext(), "Error: " + e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                                                            }
                                                        });

                                                    }else {
                                                        Toasty.error(getContext(), "Error: " + task.getException().getMessage(), Toasty.LENGTH_SHORT, true).show();
                                                    }
                                                }

                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    task.getResult().getUser().delete();
                                }
                            });
                }else {
                    if (task.getException().getMessage().equalsIgnoreCase("The email address is already in use by another account.")){
                        Log.d(TAG,task.getException().getMessage()+"");
                        Toasty.error(getContext(),"This email already used.",Toasty.LENGTH_LONG,true).show();
                        hideProgressBar();
                    }else {
                        Toasty.error(getContext(),"Error, Please Check Network Connection.",Toasty.LENGTH_LONG,true).show();
                        hideProgressBar();
                    }

                }
            }
        });
    }
    private String getDate() {return FieldValue.serverTimestamp().toString();}
    private String getPhoneNumber() {
        String mPhoneNumber = "";
        TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(),"Please Allow",Toast.LENGTH_LONG).show();
        }else {mPhoneNumber = tMgr.getLine1Number();}
        return mPhoneNumber;
    }

    public String getDeviceUniqueID(){
        String device_unique_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(),"Please Allow ",Toast.LENGTH_LONG).show();
                return null;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }
    private void hideProgressBar() { if (avir!=null){
        btnRegisterRegister.setVisibility(View.VISIBLE);
        avir.hide();
    }}
    private void showProgressBar() {
        if (avir!=null){
            avir.show();
            btnRegisterRegister.setVisibility(View.INVISIBLE);
        }}
    private void dialogAllSet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.d_allset, null);
        builder.setView(dialogView);
        btnOkayAllSet = dialogView.findViewById(R.id.btnOkay_allset);
        btnOkayAllSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAllSet.dismiss();
            }
        });
        alertDialogAllSet = builder.create();
        alertDialogAllSet.show();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.STN_1){
            who = "STN_1";
            spLevelRegister.setVisibility(View.VISIBLE);
        }else if (v.getId() == R.id.TCHN_1){
            who = "TCHN_1";
            spLevelRegister.setVisibility(View.GONE);
        }else if (v.getId() == R.id.btnClose_register){
            dismiss();
        }
    }
}