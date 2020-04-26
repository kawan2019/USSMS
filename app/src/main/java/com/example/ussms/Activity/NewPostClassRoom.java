package com.example.ussms.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ussms.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewPostClassRoom extends AppCompatActivity {



    private Button mselectImagee;

    private StorageReference mStotage;

    private static final int GALLERY_INTENT=2;

    private ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_classroom_post);

        mStotage= FirebaseStorage.getInstance().getReference();

        mselectImagee=findViewById(R.id.img);

        mprogressDialog=new ProgressDialog(this);

        mselectImagee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent=new Intent(Intent.ACTION_PICK);
//
//                intent.setType("image/*");
//
//                startActivityForResult(intent,GALLERY_INTENT);



                Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_INTENT);



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==GALLERY_INTENT && resultCode ==RESULT_OK){

            mprogressDialog.setMessage("UPlod..");
            mprogressDialog.show();

            Uri uri=data.getData();

            StorageReference filepath=mStotage.child("Photo").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(NewPostClassRoom.this,"Uplode Done ",Toast.LENGTH_LONG).show();

                    mprogressDialog.dismiss();
                }

            });
        }
    }



//            if (data.getClipData() != null){
//
//
//                Toast.makeText(NewPostClassRoom.this, "select multiolie file", Toast.LENGTH_SHORT).show();
//            }else if (data.getData() != null){
//
//
//                Toast.makeText(NewPostClassRoom.this, "Select single file", Toast.LENGTH_SHORT).show();
//            }



}
