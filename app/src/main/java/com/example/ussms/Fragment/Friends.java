package com.example.ussms.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ussms.Abstracts.ItemClickListener;
import com.example.ussms.Model.Users;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friends extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    FirestoreRecyclerAdapter adapter;
    ArrayList<String> checked = new ArrayList<String>();
    FloatingActionButton fab;
    StringBuffer stringBuffer;
    String[] mStrings;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    public Friends() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_friends, container, false);

        recyclerView = view.findViewById(R.id.RC_friends);
        mFirestore = FirebaseFirestore.getInstance();
        fab = view.findViewById(R.id.floating_action_button);
        final Map<String,Object> msg =new HashMap<>();
        msg.put("title","hola");
        msg.put("msg","hhh Chone nsn");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuffer =new StringBuffer();
                mStrings = new String[checked.size()];
                int i =0;
                for (String s : checked){
                    mStrings[i] = s;
                    stringBuffer.append(s);
                    stringBuffer.append("\n");
                    Toast.makeText(getContext(),mStrings.length+"",Toast.LENGTH_LONG).show();
                i++;
                }
                if (checked.size() >0){

                }
                for (int j =0;mStrings.length>j;j++){
                    fsdb.collection("Users").document(mStrings[j]).collection("Message").document().set(msg);
                    Log.d("PLOPA",mStrings[j]);
//                    fsdb.collection("Users").document(mStrings[j]).collection("Messages").document().set(msg)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toasty.success(getContext(),mStrings.length+"",Toast.LENGTH_LONG,true).show();
//                            }else {
//                                Toasty.error(getContext(),task.getException().getMessage()+"",Toast.LENGTH_LONG,true).show();
//                            }
//                        }
//                    });
                }
            }
        });

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
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder,  int i, @NonNull  Users users) {

                holder.tv_username.setText(users.getUSERNAME());
                holder.tv_fullname.setText(users.getFULLNAME());
                holder.tv_uid.setText(users.getUID());
                CircleImageView user_image_view = holder.circleImageView;
                Glide.with(getContext()).load(users.getIMAGE()).into(user_image_view);
                final String a = users.getUSERNAME();
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        CheckBox ch = (CheckBox) v;
                        if (ch.isChecked()){
                            checked.add(a);
                        }else if(!ch.isChecked()){
                            checked.remove(a);
                        }
                    }
                });

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

    private class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_username,tv_fullname,tv_uid;
        private CircleImageView circleImageView;
        private CheckBox checkBox;
        ItemClickListener itemClickListener;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_fullname = itemView.findViewById(R.id.tv_fullname_rc_friends);
            tv_username = itemView.findViewById(R.id.tv_username_rc_friends);
            tv_uid = itemView.findViewById(R.id.tv_uid_rc_friends);
            circleImageView = itemView.findViewById(R.id.cig_rc_friends);
            checkBox = itemView.findViewById(R.id.ch_rc_friends);

            checkBox.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
