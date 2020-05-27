package com.example.ussms.Fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
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
import com.example.ussms.Activity.ClassRoom_main_t;
import com.example.ussms.Activity.NewPostClassRoom;
import com.example.ussms.Model.ClassFile;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoomAcount_t extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 2;
    private RecyclerView mAssimentList;

    private EditText mdescription;
    private CircleImageView mImageProfile;
    private ImageButton mSelectAssiment;
    private Button mAddAssiment;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private FirestoreRecyclerAdapter adapter;
    private ImageButton mdownload;
    final static String TAG = "GWW";
    Map<String, Object> as;

    String Department;
    Long Lev;


    String cn, cd;
    long cl;
    int j = 0;
    String[] su = new String[50];
    private Uri mImageUri;
    String Id;


    public classRoomAcount_t() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f_class_room_acount_t, container, false);


        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        mAssimentList = view.findViewById(R.id.list_assement);
        mdescription = view.findViewById(R.id.dec_assiment);
        mImageProfile = view.findViewById(R.id.image_assiment);
        mSelectAssiment = view.findViewById(R.id.select_assiment);
        mAddAssiment = view.findViewById(R.id.add_assiment);


        SharedPreferences pref = getActivity().getSharedPreferences("Class", Activity.MODE_PRIVATE);
        cn = pref.getString("CN", "");
        cd = pref.getString("CD", "");
        cl = pref.getLong("CL", 0);

        SharedPreferences prefe = getActivity().getSharedPreferences("Account", Activity.MODE_PRIVATE);
         Department = prefe.getString("Department", "");
         Lev = prefe.getLong("Level",0 );

//        fsdb.collection("Users").whereEqualTo("DEPARTMENT", cd)
//                .whereEqualTo("LEVEL", cl)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("DOCC", document.getId());
//                                su[j] = document.getId() + "";
//                                j++;
//                            }
//                        } else {
//                            Log.d("DOCC", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });


        mSelectAssiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });
        mAddAssiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();



            }
        });




        Query query = fsdb.collection("Users")
                .document(mAuth.getCurrentUser().getDisplayName())
                .collection("ClassRoom").document(cn).collection("Assignment");

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


        adapter = new FirestoreRecyclerAdapter<ClassFile, classRoomAcount_t.UsersViewHolder>(options) {
            @NonNull
            @Override
            public classRoomAcount_t.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_post_classroom,parent,false);

                mdownload = mview.findViewById(R.id.download_btn);

                return new classRoomAcount_t.UsersViewHolder(mview);
            }

            @Override
            protected void onBindViewHolder(@NonNull final classRoomAcount_t.UsersViewHolder holder, int i, @NonNull final ClassFile u) {

                holder.mOwnerClass.setText(u.getFileOwner());

                fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                        .collection("ClassRoom").document(cn).collection("Assignment")
                        .document(u.getItem_id()).collection("assign")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        i++;
                                    }
                                    holder.tv_assin_count.setText(i+"");
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });






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

        mAssimentList.setHasFixedSize(true);
        mAssimentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAssimentList.setAdapter(adapter);


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

    private void startPosting() {

//        mprogress.setMessage("uploding .....");
//        mprogress.show();

        final String desc = mdescription.getText().toString();
        if (!TextUtils.isEmpty(desc) && mSelectAssiment != null) {


            final StorageReference filepath = mStorage.child("assiment/").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri url) {

                            String downloadUri = url.toString();

                            as = new HashMap<>();
                            as.put("FileOwner", mAuth.getCurrentUser().getDisplayName());
                            as.put("File", downloadUri);
                            as.put("CreateTime", FieldValue.serverTimestamp());
                            as.put("FDescription", desc);
                            as.put("PhotoUser", mAuth.getCurrentUser().getPhotoUrl() + toString());
                            as.put("FDepartment", cd);
                            as.put("FLevel", cl);



//                            fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
//                                    .collection("ClassRoom").document(cn).collection("Assignment").document().set(as);
//
//                            fsdb.collection("ClassRoom").document(cd).collection(cl+"")
//                                    .document(cn).collection("Assignment").document().set(as);

                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            CollectionReference productsRef = rootRef.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                    .collection("ClassRoom").document(cn).collection("Assignment");
                            CollectionReference newProductsRef = rootRef.collection("ClassRoom").document(cd).collection(cl+"")
                                    .document(cn).collection("Assignment");

                            WriteBatch batch = fsdb.batch();
                            batch.set(productsRef.document(desc), as);
                            batch.set(newProductsRef.document(desc), as);

                            batch.commit();


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "error : " + e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==RESULT_LOAD_IMAGE && resultCode ==RESULT_OK){

            mImageUri=data.getData();
            mSelectAssiment.setImageURI(mImageUri);

        }

    }

    private class UsersViewHolder extends RecyclerView.ViewHolder {


        private TextView mOwnerClass,mDate,mDecription,tv_assin_count;
        private CircleImageView mCircleImageView;



        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);


            mCircleImageView = itemView.findViewById(R.id.blog_user_image);
            mOwnerClass = itemView.findViewById(R.id.blog_user_name);
            mDecription = itemView.findViewById(R.id.blog_desc);
            tv_assin_count = itemView.findViewById(R.id.blog_like_count);
            mDate = itemView.findViewById(R.id.blog_date);


        }


    }
}
