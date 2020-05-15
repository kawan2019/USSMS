package com.example.ussms.Fragment.News;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ussms.Activity.MainActivity;
import com.example.ussms.Adapter.SliderAdapter;
import com.example.ussms.Model.Posts;
import com.example.ussms.Model.SliderItem;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsHome extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    private View view;
    ImageButton filterIcon;
    ArrayList<String> imageList = new ArrayList<String>();
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private SliderAdapter adapterr;
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    public NewsHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (View) inflater.inflate(R.layout.f_news_home, container, false);
        recyclerView = view.findViewById(R.id.rc_posts_news_home);
        Query query = fsdb.collection("Posts");

        FirestoreRecyclerOptions<Posts> options = new FirestoreRecyclerOptions.Builder<Posts>()
                .setQuery(query, Posts.class)
                .build();



        adapter = new FirestoreRecyclerAdapter<Posts, NewsHome.UsersViewHolder>(options) {
            @NonNull
            @Override
            public NewsHome.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_rc_posts_home,parent,false);

                return new NewsHome.UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NewsHome.UsersViewHolder holder, int i, @NonNull Posts posts) {
                imageList = (ArrayList<String>) posts.getImagesList();
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



        Context context = getContext();
        if(context != null){ }
        filterIcon = view.findViewById(R.id.filterIcon);
        view.findViewById(R.id.backIcon).setOnClickListener(this);
        LinearLayout contentLayout = view.findViewById(R.id.contentLayout);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilters();
            }
        });


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

    private class UsersViewHolder extends RecyclerView.ViewHolder{
        SliderView sliderView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderView = itemView.findViewById(R.id.imageSlider);

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
            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }
}
