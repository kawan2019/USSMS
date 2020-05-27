package com.example.ussms.Fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Model.ClassFile;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.bumptech.glide.util.Util.getSnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoomAcount_stu extends Fragment {


    private RecyclerView mAssimentList;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    String Department;
    Long Lev;
    private FirestoreRecyclerAdapter adapter;
    String cn, cd;
    long cl;
    private ImageButton mAssign;



    public classRoomAcount_stu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.f_class_room_acount_stu, container, false);

        mAuth = FirebaseAuth.getInstance();
        mAssimentList = view.findViewById(R.id.ass_list_stu);

        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        cn = pref.getString("CN", "");
        cd = pref.getString("CD", "");
        cl = pref.getLong("CL", 0);

        SharedPreferences prefe = getActivity().getSharedPreferences("Account", Activity.MODE_PRIVATE);
        Department = prefe.getString("Department", "");
        Lev = prefe.getLong("Level",0 );





        Query query = fsdb.collection("ClassRoom")
                .document(Department).collection(Lev+"").document(cn).collection("Assignment");


        //Recycleroption
        FirestoreRecyclerOptions<ClassFile> options = new FirestoreRecyclerOptions.Builder<ClassFile>()
                .setLifecycleOwner(this)
                .setQuery(query, new SnapshotParser<ClassFile>() {
                    @NonNull
                    @Override
                    public ClassFile parseSnapshot(@NonNull DocumentSnapshot snapshot) {

                        ClassFile classFile = snapshot.toObject(ClassFile.class);
                        String itemId = snapshot.getId();
                        classFile.setItem_id(itemId);

                        return classFile;
                    }
                })
                .build();


        adapter = new FirestoreRecyclerAdapter<ClassFile, classRoomAcount_stu.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classRoomAcount_stu.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                mAssign = mview.findViewById(R.id.comment);

                return new classRoomAcount_stu.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classRoomAcount_stu.UsersViewHolder holder, final int i, @NonNull final ClassFile u) {

                holder.mOwnerClass.setText(u.getFileOwner());



                String date = (DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString());
                holder.mDate.setText(date);

                holder.mDecription.setText(u.getFDescription());
                CircleImageView userImage = holder.mCircleImageView;
                Glide.with(getContext()).load(u.getPhotoUser()).into(userImage);


                mAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Itemid", Activity.MODE_PRIVATE).edit();
                        editor.putString("id", u.getItem_id());
                        editor.putString("teach", u.getFileOwner());

                        editor.apply();


                        classRoomAcount_stu nextFrag= new classRoomAcount_stu();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.id_assigment, new Assign(), "findThisFragment")
                                .addToBackStack(null)
                                .commit();

                    }
                });
            }
        };

        mAssimentList.setHasFixedSize(true);
        mAssimentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAssimentList.setAdapter(adapter);


        return view;
    }


    private class UsersViewHolder extends RecyclerView.ViewHolder {


        private TextView mOwnerClass, mDate, mDecription;
        private CircleImageView mCircleImageView;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);


            mCircleImageView = itemView.findViewById(R.id.blog_user_image);
            mOwnerClass = itemView.findViewById(R.id.blog_user_name);
            mDecription = itemView.findViewById(R.id.blog_desc);
            mDate = itemView.findViewById(R.id.blog_date);



        }
    }


}
