package com.example.ussms.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
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
import com.example.ussms.Model.classUser;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tiper.MaterialSpinner;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassR_t extends Fragment {

    private RecyclerView mClassList;
    private Spinner mLevelClass;
    private EditText mNameClass;

    private ImageView mImageTecher;
    private Button mClassBtn;

    private FirestoreRecyclerAdapter adapter;

    private boolean validateDepSp = true;
    private boolean validateLevSp = true;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    Integer[] Level = {1, 2, 3, 4};

    public ClassR_t() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.f_class_r_t, container, false);
        mClassList = view.findViewById(R.id.class_list);
        mClassBtn = view.findViewById(R.id.class_btn);
        mNameClass = view.findViewById(R.id.edClass_name);
        mLevelClass = view.findViewById(R.id.level_class);

        mAuth = FirebaseAuth.getInstance();


        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelClass.setAdapter(adp2);

        mLevelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int level_ = i + 1;

                fsdb.collection("Users").whereEqualTo("LEVEL", level_)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DOCC", document.getId());

                                    }
                                } else {
                                    Log.d("DOCC", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                mClassBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String Nclass = mNameClass.getText().toString();
                        int levl = mLevelClass.getSelectedItemPosition();


                        if (!TextUtils.isEmpty(Nclass)) {


                            Map<String, Object> c = new HashMap<>();
                            c.put("CLASSROOM", Nclass);
                            c.put("LEVEL", levl);

                            fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).collection("Classroom").document().set(c);


                            mNameClass.setText("");


                        }

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Qurry

        Query query = fsdb.collection("Users").document().collection("Classroom");

        //Recycleroption
        FirestoreRecyclerOptions<classUser> options = new FirestoreRecyclerOptions.Builder<classUser>()
                .setQuery(query, classUser.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<classUser, ClassR_t.UsersViewHolder>(options) {
            @NonNull
            @Override
            public ClassR_t.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_subject_list_item, parent, false);

                return new ClassR_t.UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ClassR_t.UsersViewHolder holder, int i, @NonNull classUser classUser) {

                holder.mNaeClass.setText(classUser.getCLASSROOM());

            }
        };

        mClassList.setHasFixedSize(true);
        mClassList.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassList.setAdapter(adapter);

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

    private class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView mNaeClass;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameClass = itemView.findViewById(R.id.name_class);

        }
    }

}