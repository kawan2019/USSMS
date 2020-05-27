package com.example.ussms.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ussms.Model.ClassFile;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Assign extends Fragment {

    private RecyclerView mAssignList;


    private static final int RESULT_LOAD_IMAGE = 2;
    private EditText mDescription;
    private ImageButton mAssign;
    private Button mAddAssign;
    private TextView mUsername;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirestoreRecyclerAdapter adapter;

    private CircleImageView mImageProfile;

    private Uri mImageUri;

    private ProgressDialog mprogress;
    String cn, cd;
    long cl;
    String itemId;
    String teachear;


    public Assign() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.f_assign, container, false);


        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

     mAddAssign =view.findViewById(R.id.add_assiment);
     mAssign =view.findViewById(R.id.select_assiment);
     mDescription=view.findViewById(R.id.dec_assiment);
     mImageProfile=view.findViewById(R.id.image_assiment);
        mAssignList =view.findViewById(R.id.asing_list);

        mprogress = new ProgressDialog(getContext());


        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        cn = pref.getString("CN", "");
        cd = pref.getString("CD", "");
        cl = pref.getLong("CL", 0);


        SharedPreferences prefrenc = getActivity().getSharedPreferences("Itemid", Activity.MODE_PRIVATE);
         itemId = prefrenc.getString("id","");
         teachear= prefrenc.getString("teach","");

        Log.d("EE",teachear);

        Toast.makeText(getContext(), "owner"+teachear, Toast.LENGTH_LONG).show();





        mAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        mAddAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                startPosting();

            }
        });


        Query query =  fsdb.collection("ClassRoom").document(cd).collection(cl+"")
                .document(cn).collection("Assignment")
                .document(itemId).collection("assign");


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


        adapter = new FirestoreRecyclerAdapter<ClassFile, Assign.UsersViewHolder>(options) {
            @NonNull
            @Override
            public Assign.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                return new Assign.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final Assign.UsersViewHolder holder, final int i, @NonNull final ClassFile u) {

                holder.mOwnerClass.setText(u.getFileOwner());



                String date = (DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString());
                holder.mDate.setText(date);

                holder.mDecription.setText(u.getFDescription());
                CircleImageView userImage = holder.mCircleImageView;
                Glide.with(getContext()).load(u.getPhotoUser()).into(userImage);

            }
        };

        mAssignList.setHasFixedSize(true);
        mAssignList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAssignList.setAdapter(adapter);


        return view;
    }


    private void startPosting() {

        mprogress.setMessage("uploding .....");
        mprogress.show();

        final String desc = mDescription.getText().toString();
        if (!TextUtils.isEmpty(desc) && mAssign != null) {


            final StorageReference filepath = mStorage.child("assign/").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Map<String, Object> asign = new HashMap<>();
                            asign.put("FileOwner", mAuth.getCurrentUser().getDisplayName());
                            asign.put("CreateTime", FieldValue.serverTimestamp());
                            asign.put("FDescription", desc);
                            asign.put("PhotoUser", mAuth.getCurrentUser().getPhotoUrl() + toString());
                            asign.put("FDepartment", cd);
                            asign.put("FLevel", cl);

//
//                            for (int a = 0; a <= j - 1; a++) {
//                                fsdb.collection("Users").document(su[a])
//                                        .collection("ClassRoom").document(cn).collection("Assignment").document().set(as)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toasty.success(getContext(), "Success Upload", Toasty.LENGTH_LONG).show();
//                                            }
//                                        });
//                            }



                            fsdb.collection("Users").document(teachear)
                                    .collection("ClassRoom").document(cn).collection("Assignment")
                                    .document(itemId).collection("assign").document(mAuth.getCurrentUser().getDisplayName()).set(asign);

                            fsdb.collection("ClassRoom").document(cd).collection(cl+"")
                                    .document(cn).collection("Assignment")
                                    .document(itemId).collection("assign").document(mAuth.getCurrentUser().getDisplayName()).set(asign);

                            mprogress.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();

                }
            });
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==RESULT_LOAD_IMAGE && resultCode ==RESULT_OK){

            mImageUri=data.getData();
            mAssign.setImageURI(mImageUri);

        }

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
