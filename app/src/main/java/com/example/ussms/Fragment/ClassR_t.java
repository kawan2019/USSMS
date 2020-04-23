package com.example.ussms.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Abstracts.ItemClickListener;
import com.example.ussms.Model.Users;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tiper.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassR_t extends Fragment {

    private RecyclerView mClassList;
    private Spinner mLevelClass;
    private  EditText mNameClass;

    private ImageView mImageTecher;
    private Button mClassBtn;

    private String username;

    private FirebaseFirestore mFirestore;
   private FirestoreRecyclerAdapter adapter;

    private boolean validateDepSp = true;
    private boolean validateLevSp = true;

    Integer[] Level = {1, 2, 3, 4};
    int level_;


    public ClassR_t() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.f_class_r_t, container, false);

        mFirestore =FirebaseFirestore.getInstance();

        mClassList = view.findViewById(R.id.class_list);
        mClassBtn = view.findViewById(R.id.class_btn);
        mNameClass = view.findViewById(R.id.edClass_name);
        mLevelClass = view.findViewById(R.id.level_class);


        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelClass.setAdapter(adp2);



        mLevelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> adapterView) {

                                                  }
                                              });


                //Qurry

                Query query = mFirestore.collection("Users");
        //Recycleroption
        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();


         adapter =  new FirestoreRecyclerAdapter<Users,UsersViewHolder> (options){
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View ClassView = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_subject_list_item,parent,false);

                return new UsersViewHolder(ClassView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ClassR_t.UsersViewHolder holder, int i, @NonNull  Users users) {

                holder.mNmae_t.setText(users.getUSERNAME());
                holder.mName_c.setText(users.getFULLNAME());



            }


            //viewHolder

    };

        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassList.setAdapter(adapter);

 return view;
}


    private class UsersViewHolder extends RecyclerView.ViewHolder{

        private TextView mNmae_t;
        private TextView mName_c;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mName_c = itemView.findViewById(R.id.name_class);
            mNmae_t = itemView.findViewById(R.id.name_techer);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
