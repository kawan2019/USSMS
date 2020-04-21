package com.example.ussms.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ussms.Model.Users;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friends extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    FirestoreRecyclerAdapter adapter;
    public Friends() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_friends, container, false);

        recyclerView = view.findViewById(R.id.RC_friends);
        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("Users");

        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Users, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_rc_friends,parent,false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int i, @NonNull Users users) {

                holder.tv_username.setText(users.getUSERNAME());
                holder.tv_fullname.setText(users.getFULLNAME());
                holder.tv_uid.setText(users.getUID());
                CircleImageView user_image_view = holder.circleImageView;
                Glide.with(getContext()).load(users.getIMAGE()).into(user_image_view);

            }
        };

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);
    return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private class UsersViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_username,tv_fullname,tv_uid;
        private CircleImageView circleImageView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_fullname = itemView.findViewById(R.id.tv_fullname_rc_friends);
            tv_username = itemView.findViewById(R.id.tv_username_rc_friends);
            tv_uid = itemView.findViewById(R.id.tv_uid_rc_friends);
            circleImageView = itemView.findViewById(R.id.cig_rc_friends);

        }
    }
}
