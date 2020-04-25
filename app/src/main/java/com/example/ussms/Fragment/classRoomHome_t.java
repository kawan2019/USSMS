package com.example.ussms.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ussms.Model.Users;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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



        Query query = fsdb.collection("Users");

        //Recycleroption


        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setLifecycleOwner(this)
                .setQuery(query, Users.class)
                .build();



        adapter = new FirestoreRecyclerAdapter<Users, classRoomHome_t.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classRoomHome_t.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                return new classRoomHome_t.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classRoomHome_t.UsersViewHolder holder, int i, @NonNull  Users u) {

                holder.mblogusername.setText(u.getUSERNAME());
                holder.mDescription.setText(u.getUID());



            }
        };

        mlectureList.setHasFixedSize(true);
        mlectureList.setLayoutManager(new LinearLayoutManager(getContext()));
        mlectureList.setAdapter(adapter);




        return view;
    }

    private class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView mblogusername;
        private  TextView mDescription;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mblogusername = itemView.findViewById(R.id.blog_user_name);
            mDescription = itemView.findViewById(R.id.blog_desc);



        }
    }
}
