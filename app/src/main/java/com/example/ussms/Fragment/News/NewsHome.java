package com.example.ussms.Fragment.News;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ussms.Activity.MainActivity;
import com.example.ussms.Adapter.SliderAdapter;
import com.example.ussms.Model.Posts;
import com.example.ussms.Model.SliderItem;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsHome extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    private View view;
    ImageButton filterIcon;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    ArrayList<String> imageList = new ArrayList<String>();
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private SliderAdapter adapterr;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    LinearLayout relativeLayout;
    FirebaseAuth mAuth;
    boolean liked = false;
    private String filterDep;

    public NewsHome() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (View) inflater.inflate(R.layout.f_news_home, container, false);
        recyclerView = view.findViewById(R.id.rc_posts_news_home);
        Query query = fsdb.collection("Posts").orderBy("Date");
        mAuth = FirebaseAuth.getInstance();
        view.findViewById(R.id.backIcon).setOnClickListener(this);
         relativeLayout = view.findViewById(R.id.Relative_f_home);

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setLifecycleOwner(this)
                .setQuery(query,new SnapshotParser<Posts>() {
                    @NonNull
                    @Override
                    public Posts parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Posts products = snapshot.toObject(Posts.class);
                        String itemId = snapshot.getId();
                        products.setId(itemId);
                        return products;
                    }
                }).build();


        adapter = new FirestoreRecyclerAdapter<Posts, NewsHome.UsersViewHolder>(options) {
            @NonNull
            @Override
            public NewsHome.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_rc_posts_home,parent,false);

                return new NewsHome.UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NewsHome.UsersViewHolder holder, int i, @NonNull final Posts posts) {

                isLiked(posts.getId(),holder.igb_like);

                imageList = (ArrayList<String>) posts.getImagesList();
                holder.tv_postOwnerName.setText(posts.getPostOwnerName());
                holder.tv_postOwnerDepartment.setText(posts.getDepartment());
                holder.tv_postDescreiption.setText(posts.getDescription());
                holder.tv_postOwnerNameDes.setText(posts.getPostOwnerName());

                holder.sliderView.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                            @Override
                            public void onSingleClick(View view) {

                            }

                            @Override
                            public void onDoubleClick(View view) {
                                like(posts.getId(),holder.igb_like);
                            }
                        }));

                holder.igb_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.igb_like.getTag()=="Liked"){
                            unlike(posts.getId());
                            holder.igb_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_heartsease));
                            //Toast.makeText(getContext(),holder.getAdapterPosition()+"",Toast.LENGTH_SHORT).show();

                        }else {
                            like(posts.getId(),holder.igb_like);
                        }
                    }
                });

                CircleImageView user_image_view = holder.cig_postOwner;
                Glide.with(getContext()).load(posts.getPostOwnerImage()).into(user_image_view);


                List<SliderItem> sliderItemList = new ArrayList<>();
                for (int j = 0; j < imageList.size(); j++) {
                    SliderItem sliderItem = new SliderItem();
                    sliderItem.setImageUrl(imageList.get(j));
                    sliderItemList.add(sliderItem);
                }
                adapterr.renewItems(sliderItemList);

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx >= 0) {
                    View v = recyclerView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset >= 1 ) {
                        relativeLayout.animate().translationY(0).setDuration(200);


                    }else {
                        if (recyclerView.getVisibility() == View.VISIBLE){
                            relativeLayout.animate().translationY(-200).setDuration(0);
                        }

                    }
                }
            }
        });
        return view;
    }

    private void isLiked(String id, final ImageButton igb_like) {
        fsdb.collection("Posts")
                .document(id).collection("Like")
                .document(mAuth.getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            igb_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        }else{
                            igb_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_heartsease));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error",e.getMessage());
                    }
                });
    }

    private void unlike(String id) {
        fsdb.collection("Posts").document(id).collection("Like")
                .document(mAuth.getCurrentUser().getDisplayName()).delete();

    }

    private void like(String id,final ImageButton igb_like) {

        Map map = new HashMap();
        map.put("Username",mAuth.getCurrentUser().getDisplayName());
        map.put("Time", FieldValue.serverTimestamp());
        map.put("UserPhoto",mAuth.getCurrentUser().getPhotoUrl().toString());
        fsdb.collection("Posts").document(id).collection("Like")
                .document(mAuth.getCurrentUser().getDisplayName()).set(map, SetOptions.merge());
        igb_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        igb_like.setTag("Liked");

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

    private class UsersViewHolder extends RecyclerView.ViewHolder{
        SliderView sliderView;
        CircleImageView cig_postOwner;
        TextView tv_postOwnerName,tv_postOwnerDepartment,tv_postOwnerNameDes,tv_postDescreiption;
        ImageButton igb_like;
        RelativeLayout Relative ;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderView = itemView.findViewById(R.id.imageSlider);
            cig_postOwner = itemView.findViewById(R.id.cig_post_owner_i_rc_post);
            tv_postOwnerName = itemView.findViewById(R.id.tv_post_owner_name_i_rc_post);
            tv_postOwnerNameDes = itemView.findViewById(R.id.tv_post_owner_name_i_Rc_post);
            tv_postDescreiption = itemView.findViewById(R.id.tv_des_Rc_post);
            tv_postOwnerDepartment = itemView.findViewById(R.id.tv_post_owner_department_i_rc_post);
            Relative = itemView.findViewById(R.id.Relative_l_rd_post);

            igb_like = itemView.findViewById(R.id.igb_like_Rc_post);

            adapterr = new SliderAdapter(getContext());
            sliderView.setSliderAdapter(adapterr);

            sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(false);
        }
    }

    private void toggleFilters(){
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            float deg = filterIcon.getRotation() + 180F;
            filterIcon.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        }
        else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            float deg = filterIcon.getRotation() - 180F;
            filterIcon.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.backIcon){
            showDialog();
            startActivity(new Intent(getContext(), MainActivity.class));
            dismissDialog();

        }
    }
    private void showDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private  void dismissDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {

            }
        }
    }
}
