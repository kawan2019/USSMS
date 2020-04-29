package com.example.ussms.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ussms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class NewPostClassRoom extends AppCompatActivity {


    private ImageButton mselectImage;
    private EditText mpostDesc;
    private Button msubmit;

    private Uri mImageUri;
    private  static final int RESULT_LOAD_IMAGE =2;
    private StorageReference mStorage;
    private ProgressDialog mprogress;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_classroom_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mselectImage = findViewById(R.id.imageselect);
        mpostDesc = findViewById(R.id.desFild);
        msubmit = findViewById(R.id.submit);
        mprogress = new ProgressDialog(this);



        mselectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

            }
        });
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startPosting();

            }
        });
    }

    private void startPosting() {

        mprogress.setMessage("uploding .....");
        mprogress.show();

        final String desc = mpostDesc.getText().toString();

        if (!TextUtils.isEmpty(desc) && mImageUri != null) {

            final StorageReference filepath = mStorage.child("file/").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri url) {

                            SharedPreferences pref = getSharedPreferences("Class", Activity.MODE_PRIVATE);
                            String cn = pref.getString("CN","");
                            String cd = pref.getString("CD","");
                            Long cl = pref.getLong("CL",0);

                            String downloadUri = url.toString();

                            Map<String, Object> f = new HashMap<>();
                            f.put("FileOwner", mAuth.getCurrentUser().getDisplayName());
                            f.put("File", downloadUri);
                            f.put("CreateTime", FieldValue.serverTimestamp());
                            f.put("FDescription", desc);
                            f.put("PhotoUser", mAuth.getCurrentUser().getPhotoUrl() + toString());
                            f.put("FDepartment", cd);
                            f.put("FLevel", cl);

                            fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                    .collection("ClassRoom").document(cn).collection("FILE").document(cn).set(f);


                            mprogress.dismiss();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewPostClassRoom.this, "error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==RESULT_LOAD_IMAGE && resultCode ==RESULT_OK){

            mImageUri=data.getData();
            mselectImage.setImageURI(mImageUri);

        }

    }






}