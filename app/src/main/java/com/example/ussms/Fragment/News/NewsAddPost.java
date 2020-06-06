package com.example.ussms.Fragment.News;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.example.ussms.Adapter.PickedImagesAdapter;
import com.example.ussms.Adapter.SliderAdapter;
import com.example.ussms.Model.AddPost;
import com.example.ussms.Model.SliderItem;
import com.example.ussms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class NewsAddPost extends Fragment implements BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate, BSImagePicker.OnSelectImageCancelledListener{
    private static final int READ_PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;
    Button btnPost;
    ImageButton igbPickImage;
    //RecyclerView recyclerView;
    List<AddPost> imagesList;
    List<String> savedImagesUri;
    PickedImagesAdapter adapter;
    //CoreHelper coreHelper;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    EditText ed_des;
    ProgressDialog progressDialog;
    String des;
    private SliderAdapter adapterr;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    int counter;
    private String department;
    private FragmentActivity myContext;
    private SliderView sliderView;
    TextView tv_on,tv_od;
    CircleImageView cig_o;
    public NewsAddPost() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_news_add_post, container, false);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        savedImagesUri = new ArrayList<>();

        cig_o = (CircleImageView) view.findViewById(R.id.cig_post_owner_i_addPost);
        tv_on = view.findViewById(R.id.tv_post_owner_name_i_addPost);
        Glide.with(getContext()).load(mAuth.getCurrentUser().getPhotoUrl()).fitCenter().into(cig_o);
        tv_on.setText(mAuth.getCurrentUser().getDisplayName());
        sliderView = view.findViewById(R.id.imageSlider_addPost);
        adapterr = new SliderAdapter(getContext());

        igbPickImage = view.findViewById(R.id.igb_add_post_news_add);
        btnPost = view.findViewById(R.id.btn_post_addPost);
        ed_des = view.findViewById(R.id.ed_dis_addPost);
        imagesList = new ArrayList<>();

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


        igbPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissionAndPickImage();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages(view);
            }
        });
        return view;
    }

    private void uploadImages(View view) {
        des = ed_des.getText().toString();
        if (imagesList.size() != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Uploaded 0/"+imagesList.size());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final StorageReference storageReference = storage.getReference();
            for (int i = 0; i < imagesList.size(); i++) {
                final int finalI = i;
                storageReference.child("Users").child(mAuth.getCurrentUser().getDisplayName()).child("Posts").child(imagesList.get(i).getImageName()).putFile(imagesList.get(i).getImageURI()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference.child("Users").child(mAuth.getCurrentUser().getDisplayName()).child("Posts").child(imagesList.get(finalI).getImageName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    counter++;
                                    progressDialog.setMessage("Uploaded "+counter+"/"+imagesList.size());
                                    if (task.isSuccessful()){
                                        savedImagesUri.add(task.getResult().toString());
                                    }else{
                                        storageReference.child("Users").child(mAuth.getCurrentUser().getDisplayName()).child("Posts").child(imagesList.get(finalI).getImageName()).delete();
                                        Toast.makeText(getContext(), "Couldn't save "+imagesList.get(finalI).getImageName(), Toast.LENGTH_SHORT).show();
                                    }
                                    if (counter == imagesList.size()){
                                        saveImageDataToFirestore(progressDialog);
                                    }
                                }
                            });
                        }else{
                            progressDialog.setMessage("Uploaded "+counter+"/"+imagesList.size());
                            counter++;
                            Toast.makeText(getContext(), "Couldn't upload "+imagesList.get(finalI).getImageName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            createSnackBar(view, "Please add some images first.", "", null, Snackbar.LENGTH_SHORT);
        }
    }

    private void saveImageDataToFirestore(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Saving uploaded images...");
        Map<String, Object> dataMap = new HashMap<>();
        for (int i=0; i<savedImagesUri.size(); i++){}
        dataMap.put("Image", savedImagesUri);
        dataMap.put("PostOwnerName", mAuth.getCurrentUser().getDisplayName());
        dataMap.put("Date", FieldValue.serverTimestamp());
        dataMap.put("Department", department);
        dataMap.put("PostType", "image");
        dataMap.put("Privacy", "Public");
        dataMap.put("PostOwnerImage", mAuth.getCurrentUser().getPhotoUrl()+"");
        dataMap.put("Description", des);
        fsdb.collection("Posts").document().set(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                createAlert("Success", "Images uploaded and saved successfully!", "OK", "", null, null, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                createAlert("Error", "Images uploaded but we couldn't save them to database.", "OK", "", null, null, null);
                Log.e("MainActivity:SaveData", e.getMessage());
            }
        });
    }

    private void verifyPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
            }
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.ussms.Fragment.News")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(6)
                .build();
        pickerDialog.show(getChildFragmentManager(), "picker");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            imagesList.add(new AddPost(getFileNameFromUri(uri), uri));
                            adapter.notifyDataSetChanged();

                        }

                    } else {
                        Uri uri = data.getData();
                        imagesList.add(new AddPost(getFileNameFromUri(uri), uri));
                        adapter.notifyDataSetChanged();

                        if (imagesList.size()>4){
                            igbPickImage.setVisibility(View.INVISIBLE);
                        }


                    }
                }
        }
    }
    public void createSnackBar(View view, String message, String actionText, View.OnClickListener actionClickListener, int time){
        Snackbar.make(view, message, time).setAction(actionText, actionClickListener).show();
    }
    public void createAlert(String alertTitle, String alertMessage, String positiveButtonText,
                            String negativeButtonText, DialogInterface.OnClickListener positiveButtonListener,
                            DialogInterface.OnClickListener negativeButtonListener,
                            DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton(positiveButtonText, positiveButtonListener)
                .setNegativeButton(negativeButtonText, negativeButtonListener)
                .setOnDismissListener(dismissListener)
                .create().show();
    }
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        showDialog();
        for (int i = 0; i<uriList.size();i++){

            List<SliderItem> sliderItemList = new ArrayList<>();
            for (int j = 0; j < uriList.size(); j++) {
                SliderItem sliderItem = new SliderItem();

                sliderItem.setImageUrl(String.valueOf(uriList.get(j)));
                sliderItemList.add(sliderItem);
            }
            adapterr.renewItems(sliderItemList);

            sliderView.setSliderAdapter(adapterr);

            imagesList.add(new AddPost(getFileNameFromUri(uriList.get(i)), uriList.get(i)));
        }
        dismissDialog();
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {

    }
    private void showDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private  void dismissDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

}