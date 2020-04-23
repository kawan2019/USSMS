package com.example.ussms.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ussms.Model.Notification;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class classRoomNotification_t extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

    public classRoomNotification_t() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.f_class_room_notification_t, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.rc_notification);
        mFirestore = FirebaseFirestore.getInstance();

        final Query query = mFirestore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                .collection("Message").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Notification, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder,  int i, @NonNull final Notification notification) {
                holder.tv_username.setText(notification.getFrom());
                holder.tv_title.setText(notification.getTitle());
                holder.tv_message.setText(notification.getMessage());
                holder.tv_date.setText(notification.getTimestamp().toString());
                boolean status = notification.isStatus();
                CircleImageView cig_sender = holder.cig_sender;
                Glide.with(getContext()).load(notification.getPhoto()).into(cig_sender);
                CircleImageView cig_cat = holder.cig_cat;
                Glide.with(getContext()).load(notification.getCategory_photo()).into(cig_cat);
                final String documentId = getSnapshots().getSnapshot(i).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fsdb.collection("Users").document(mAuth.getCurrentUser()
                                .getDisplayName()).collection("Message").document(documentId).update("status",true);
                    }
                });
                if (status == true){
                    holder.linearLayout.setBackgroundColor(Color.WHITE);
                }
            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_rc_notification,parent,false);

                return new NotificationViewHolder(view);
            }

        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;}
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

    private class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username,tv_message,tv_date,tv_title;
        private CircleImageView cig_sender,cig_cat;
        private LinearLayout linearLayout;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_from_rc_notification);
            tv_title = itemView.findViewById(R.id.tv_title_rc_notification);
            tv_message = itemView.findViewById(R.id.tv_message_rc_notification);
            tv_date = itemView.findViewById(R.id.tv_date_rc_notification);
            cig_sender = itemView.findViewById(R.id.cig_user_rc_notification);
            cig_cat = itemView.findViewById(R.id.cig_cat_rc_notification);
            linearLayout = itemView.findViewById(R.id.la_rc_notification);

        }
    }
}
