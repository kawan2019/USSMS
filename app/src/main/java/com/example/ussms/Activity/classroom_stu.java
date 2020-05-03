package com.example.ussms.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Fragment.HomeFragment;
import com.example.ussms.Model.classUser;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class classroom_stu extends AppCompatActivity {

    private ImageButton mBack;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mClassList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_classroom_stu);

        mBack =findViewById(R.id.backhome);
        mAuth = FirebaseAuth.getInstance();
        mClassList=findViewById(R.id.class_list_s);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(classroom_stu.this, MainActivity.class));
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



        adapter = new FirestoreRecyclerAdapter<classUser, classroom_stu.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classroom_stu.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_subject_list_item,parent,false);

                return new classroom_stu.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classroom_stu.UsersViewHolder holder, int i, @NonNull final classUser u) {

                holder.mClassName.setText(u.getClassName());
                holder.mOwnerClass.setText(u.getClassOwner());
                CircleImageView userImage = holder.circleImageView;
                Glide.with(classroom_stu.this).load(u.getPhotoUser()).into(userImage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences.Editor editor = getSharedPreferences("Class", MODE_PRIVATE).edit();
                        editor.putString("CN", u.getClassName());
                        editor.putString("CD", u.getClassDepartment());
                        editor.putLong("CL", u.getClassLevel());
                        editor.apply();

                        Intent newPostIntent = new Intent(classroom_stu.this, ClassRoom_main.class);
                        startActivity(newPostIntent);
                    }
                });

            }
        };

        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(this));
        mClassList.setAdapter(adapter);


    }


    private void showMessage(String m){
        Toast.makeText(classroom_stu.this,m,Toast.LENGTH_LONG).show();
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