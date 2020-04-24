package com.example.ussms.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ussms.Model.classUser;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

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

    private boolean validateLevSp = true;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String department =null;
    Integer[] Level = {1, 2, 3, 4};
     String [] su = new String[50];
    int level_;
    int j =0;
    int e;

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
        reload();

        ArrayAdapter<Integer> adp2 = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, Level);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelClass.setAdapter(adp2);

        mLevelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                level_ = i + 1;
                validateLevSp = false;
                fsdb.collection("Users").whereEqualTo("DEPARTMENT",department)
                        .whereEqualTo("LEVEL", level_)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DOCC", document.getId());
                                         su[j]=document.getId()+"";
                                         j++;
                                    }
                                } else {
                                    Log.d("DOCC", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                validateLevSp = true;
            }
        });

        mClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Nclass = mNameClass.getText().toString();
                if (department == null){
                    showMessage("Please Check internet Connection.");
                    reload();
                }else if(validateLevSp){
                    showMessage("please Select Level.");
                }else if(TextUtils.isEmpty(Nclass)){
                    showMessage("Class name Empty.");
                }else {
                    Map<String, Object> hello = new HashMap<>();
                    hello.put("ClassOwner", mAuth.getCurrentUser().getDisplayName());
                    hello.put("ClassName", Nclass);
                    hello.put("CreateTime", FieldValue.serverTimestamp());
                    hello.put("ClassLevel", level_);
                    hello.put("ClassDepartment",department);
                    hello.put("ClassMembersNumber",j);
                    fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                            .collection("ClassRoom").document(Nclass).set(hello);
                    for (int a = 0; a <= j-1; a++) {
                        e = a;
                        fsdb.collection("Users").document(su[a])
                                .collection("ClassRoom").document(Nclass).set(hello).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("send to "+su[e]);
                            }
                        });
                    }
                }
            }
        });
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

    private void reload() {
        fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            department = (String) document.get("DEPARTMENT");
                        }else {
                            Toast.makeText(getContext(),"Please Check internet Connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    private void showMessage(String m){
        Toast.makeText(getContext(),m,Toast.LENGTH_LONG).show();
    }

    private class UsersViewHolder extends RecyclerView.ViewHolder {

        private TextView mNaeClass;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameClass = itemView.findViewById(R.id.name_class);

        }
    }

}