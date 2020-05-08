package com.example.ussms.Fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Activity.NewPostClassRoom;
import com.example.ussms.Model.ClassFile;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoomHome_stu extends Fragment {

    private RecyclerView mlectureList;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private Button mdownload;

    public classRoomHome_stu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.f_class_room_home_stu, container, false);
        mlectureList = view.findViewById(R.id.lecture_list_stu);
        mAuth = FirebaseAuth.getInstance();


        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        String cn = pref.getString("CN","");
        if (cn != null)
            Toast.makeText(getContext(),cn,Toast.LENGTH_LONG).show();
        Query query = fsdb.collection("Users")
                .document(mAuth.getCurrentUser().getDisplayName())
                .collection("ClassRoom").document(cn).collection("FILE");

        //Recycleroption
        FirestoreRecyclerOptions<ClassFile> options = new FirestoreRecyclerOptions.Builder<ClassFile>()
                .setLifecycleOwner(this)
                .setQuery(query, ClassFile.class)
                .build();



        adapter = new FirestoreRecyclerAdapter<ClassFile, classRoomHome_stu.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classRoomHome_stu.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                mdownload = mview.findViewById(R.id.download_btn);

                return new classRoomHome_stu.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classRoomHome_stu.UsersViewHolder holder, int i, @NonNull final ClassFile u) {

                holder.mOwnerClass.setText(u.getFileOwner());


                String date = (DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString());
                holder.mDate.setText(date);

                holder.mDecription.setText(u.getFDescription());
                CircleImageView userImage = holder.mCircleImageView;
                Glide.with(getContext()).load(u.getPhotoUser()).into(userImage);


                mdownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url=u.getFile();
                        downloadFile(getActivity(),"Mobile", ".*",DIRECTORY_DOWNLOADS,url);


                    }
                });
//                holder.mdownload.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

            }
        };

        mlectureList.setHasFixedSize(true);
        mlectureList.setLayoutManager(new LinearLayoutManager(getContext()));
        mlectureList.setAdapter(adapter);

        return view;

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url ){


        DownloadManager downloadManager =(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalFilesDir(context,destinationDirectory, fileName + fileExtension);
        File mydownload = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/myFolder");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }

    private void showMessage(String m){
        Toast.makeText(getContext(),m,Toast.LENGTH_LONG).show();
    }


    private class UsersViewHolder extends RecyclerView.ViewHolder {


        private TextView mOwnerClass,mDate,mDecription;
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