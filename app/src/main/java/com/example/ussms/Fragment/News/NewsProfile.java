package com.example.ussms.Fragment.News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.ussms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsProfile extends Fragment {

    public NewsProfile() {
    }

    VideoView videoView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.f_news_profile, container, false);
        videoView = (VideoView) view.findViewById(R.id.videoView);



   return view; }
}
