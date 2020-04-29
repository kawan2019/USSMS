package com.example.ussms.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Activity.MainActivity;
import com.example.ussms.Model.ClassFile;
import com.example.ussms.Model.Users;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoomHome_t extends Fragment {

    private RecyclerView mlectureList;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    public classRoomHome_t() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.f_class_room_home_t, container, false);

        mlectureList = view.findViewById(R.id.lecture_list);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        String cn = pref.getString("CN","");



        Query query = fsdb.collection("Users")
                .document(mAuth.getCurrentUser().getDisplayName())
                .collection("ClassRoom").document(cn).collection("FILE");

        //Recycleroption


        FirestoreRecyclerOptions<ClassFile> options = new FirestoreRecyclerOptions.Builder<ClassFile>()
                .setLifecycleOwner(this)
                .setQuery(query, ClassFile.class)
                .build();



        adapter = new FirestoreRecyclerAdapter<ClassFile, classRoomHome_t.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classRoomHome_t.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                return new classRoomHome_t.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classRoomHome_t.UsersViewHolder holder, int i, @NonNull final ClassFile u) {

                holder.mClassName.setText(u.getFileOwner());
                CircleImageView userImage = holder.circleImageView;
                Glide.with(getContext()).load(u.getPhotoUser()).into(userImage);



            }
        };

        mlectureList.setHasFixedSize(true);
        mlectureList.setLayoutManager(new LinearLayoutManager(getContext()));
        mlectureList.setAdapter(adapter);

        return view;

    }



    private void showMessage(String m){
        Toast.makeText(getContext(),m,Toast.LENGTH_LONG).show();
    }


    private class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView mClassName;
        private TextView mOwnerClass;
        private CircleImageView circleImageView;



        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);


            circleImageView = itemView.findViewById(R.id.blog_user_image);
            mClassName = itemView.findViewById(R.id.blog_user_name);


        }


    }



}