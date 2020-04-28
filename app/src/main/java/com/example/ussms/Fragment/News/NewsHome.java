package com.example.ussms.Fragment.News;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.ussms.R;
import com.eyebrows.video.EyebrowsVideoView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsHome extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    ImageButton filterIcon;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;

    public NewsHome() {
    }

    EyebrowsVideoView videoView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        coordinatorLayout = (CoordinatorLayout)inflater.inflate(R.layout.f_news_home, container, false);

        Context context = getContext();

        if(context != null){
        }

        filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);
        LinearLayout contentLayout = coordinatorLayout.findViewById(R.id.contentLayout);

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


        return coordinatorLayout;
    }

    private void toggleFilters(){
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
