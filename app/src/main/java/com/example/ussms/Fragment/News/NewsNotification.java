package com.example.ussms.Fragment.News;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ussms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsNotification extends Fragment {

    public NewsNotification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_news_notification, container, false);
    }
}
