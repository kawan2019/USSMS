package com.example.ussms.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ussms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class classRoomHome_t extends Fragment {

    public classRoomHome_t() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.f_class_room_home_t, container, false);

       return view;
    }
}
