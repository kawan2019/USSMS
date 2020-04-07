package com.example.ussms.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ussms.R;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class HomeFragment extends Fragment {
    private FlipperLayout fliper;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fliper = root.findViewById(R.id.fliper);


        setLayout();
        return root;
    }



    private void setLayout() {
        final String url[]=new String[]{
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/03/سشسش.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/03/DSC_0291.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/03/DSC_0410.jpg"
        };
        final String url1[]=new String[]{
                "https://uoh.edu.iq/ku/4362/",
                "https://uoh.edu.iq/ku/4305/",
                "https://uoh.edu.iq/ku/4328/"
        };
        final String dis[]=new String[]{
                "زانكۆی هه\u200Cڵه\u200Cبجه\u200C و په\u200Cیمانگای فێڵسبێرگه\u200Cی ئه\u200Cڵمانی توێژینه\u200Cوه\u200Cیه\u200Cك ئه\u200Cنجام ده\u200Cده\u200Cن",
                "په\u200Cیمانگایه\u200Cكی ئه\u200Cڵمانی به\u200Cهاوكاری زانكۆی هه\u200Cڵه\u200Cبجه\u200C توێژینه\u200Cوه\u200Cیه\u200Cك له\u200Cسه\u200Cر كیمیابارانكردنی شاره\u200Cكه\u200C ئه\u200Cنجامده\u200Cدات",
                "زانكۆی هه\u200Cڵه\u200Cبجه\u200C ماراسۆنی نێوده\u200Cوڵه\u200Cتی هه\u200Cڵه\u200Cبجه\u200Cی ئه\u200Cنجامدا"
        };
        for (int i=0;i<3;i++){
            FlipperView view= new FlipperView(getContext());
            view.setImageUrl(url[i]);
            view.setDescription(dis[i]);
           // fliper.showInnerPagerIndicator();
            /*
            view.setDescriptionBackgroundColor(Color.RED);
            view.setDescriptionBackgroundAlpha(0.5f);
            view.setDescriptionTextColor(Color.WHITE);
            view.setImageScaleType(ScaleType.CENTER_INSIDE);
            */
            fliper.addFlipperView(view);
            view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                @Override
                public void onFlipperClick(FlipperView flipperView) {

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url1[fliper.getCurrentPagePosition()]));
                    startActivity(i);
                }
            });
        }

    }
}
