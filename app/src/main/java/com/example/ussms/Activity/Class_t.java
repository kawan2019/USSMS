package com.example.ussms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Model.classUser;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tiper.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Class_t extends AppCompatActivity {

    private RecyclerView mClassList;
    private MaterialSpinner mLevelClass;
    private EditText mNameClass;
    private ImageView mImageTecher;
    private Button mClassBtn;
    ImageButton mback,mAddClass;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private FirestoreRecyclerAdapter adapter;
    private boolean validateLevSp = true;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String department = null;
    Integer[] Level = {1, 2, 3, 4};
    String[] su = new String[50];
    int level_;
    int j = 0;
    int e;
    public String a;
    String Nclass;
//    Fragment home = new classRoom_Main_t();

    public String getCln() {
        return cln;
    }

    public void setCln(String cln) {
        this.cln = cln;
    }

    private String cln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_class_t);

        mClassList = findViewById(R.id.class_list_s);
        mClassBtn = findViewById(R.id.class_btn);
        mNameClass = findViewById(R.id.edClass_name);
        mLevelClass = findViewById(R.id.level_class);
        mback=findViewById(R.id.backhome);
        mAddClass = findViewById(R.id.addClass);
        LinearLayout contentLayout = findViewById(R.id.contLayout);
        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
        mAuth = FirebaseAuth.getInstance();
        reload();

        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Class_t.this, MainActivity.class));
            }
        });
        mAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    float deg = mAddClass.getRotation() + 180F;
                    mAddClass.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                }
                else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    float deg = mAddClass.getRotation() - 180F;
                    mAddClass.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }

            }
        });


        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelClass.setAdapter(adp2);

        mLevelClass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                level_ = i + 1;
                validateLevSp = false;
                fsdb.collection("Users").whereEqualTo("DEPARTMENT",department)
                        .whereEqualTo("LEVEL", level_)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DOCC", document.getId());
                                        su[j]=document.getId()+"";
                                        j++;
                                    }
                                } else {
                                    Log.d("DOCC", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {
                validateLevSp = true;
            }
        });


        mClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Nclass = mNameClass.getText().toString();
                if (department == null){
                    showMessage("Please Check internet Connection.");
                    reload();
                }else if(validateLevSp){
                    showMessage("please Select Level.");
                }else if(TextUtils.isEmpty(Nclass)){
                    showMessage("Class name Empty.");
                }else {
                    Map<String, Object> hello = new HashMap<>();
                    hello.put("ClassOwner", mAuth.getCurrentUser().getDisplayName());
                    hello.put("ClassName", Nclass);
                    hello.put("CreateTime", FieldValue.serverTimestamp());
                    hello.put("ClassLevel", level_);
                    hello.put("PhotoUser",mAuth.getCurrentUser().getPhotoUrl()+toString());
                    hello.put("ClassDepartment",department);
                    hello.put("ClassMembersNumber",j);
                    fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                            .collection("ClassRoom").document(Nclass).set(hello);
                    for (int a = 0; a <= j-1; a++) {
                        e = a;
                        fsdb.collection("Users").document(su[a])
                                .collection("ClassRoom").document(Nclass).set(hello).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("send to "+su[e]);
                            }
                        });
                    }
                }
            }
        });
        Query query = fsdb.collection("Users")
                .document(mAuth.getCurrentUser().getDisplayName())
                .collection("ClassRoom");

        //Recycleroption


        FirestoreRecyclerOptions<classUser> options = new FirestoreRecyclerOptions.Builder<classUser>()
                .setLifecycleOwner(this)
                .setQuery(query, classUser.class)
                .build();



        adapter = new FirestoreRecyclerAdapter<classUser, Class_t.UsersViewHolder>(options) {
            @NonNull
            @Override
            public Class_t.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_subject_list_item,parent,false);

                return new UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final Class_t.UsersViewHolder holder, int i, @NonNull final classUser u) {

                holder.mClassName.setText(u.getClassName());
                holder.mOwnerClass.setText(u.getClassOwner());
                CircleImageView userImage = holder.circleImageView;
                Glide.with(Class_t.this).load(u.getPhotoUser()).into(userImage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences.Editor editor = getSharedPreferences("Class", MODE_PRIVATE).edit();
                        editor.putString("CN", u.getClassName());
                        editor.putString("CD", u.getClassDepartment());
                        editor.putLong("CL", u.getClassLevel());
                        editor.apply();

                        Intent newPostIntent = new Intent(Class_t.this, ClassRoom_main.class);
                        startActivity(newPostIntent);
                    }
                });

            }
        };

        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(this));
        mClassList.setAdapter(adapter);


    }

    private void reload() {
        fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            department = (String) document.get("DEPARTMENT");
                        }else {
                            Toast.makeText(Class_t.this,"Please Check internet Connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void showMessage(String m){
        Toast.makeText(Class_t.this,m,Toast.LENGTH_LONG).show();
    }

    private class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView mClassName;
        private TextView mOwnerClass;
        private CircleImageView circleImageView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.user_image);
            mClassName = itemView.findViewById(R.id.name_class);
            mOwnerClass = itemView.findViewById(R.id.name_techer);

        }


    }


}