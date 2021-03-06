package com.example.ussms.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ussms.Activity.Class_t;
import com.example.ussms.Activity.News;
import com.example.ussms.Activity.classroom_stu;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private FlipperLayout fliper;
    Fragment friends = new Friends();
    public String department;
    public int level;
    FirebaseAuth mAuth;
    private AVLoadingIndicatorView avi;
    FirestoreRecyclerAdapter adapter;
    private TextView tv;
    ImageButton igb_activity_home;
    Handler handler = new Handler();
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
private String language;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.f_home, container, false);
        tv = root.findViewById(R.id.tv_noti_f_home);
        fliper = root.findViewById(R.id.fliper);
        igb_activity_home = root.findViewById(R.id.igb2);
        igb_activity_home.setOnClickListener(this);
        igb_activity_home.setVisibility(View.VISIBLE);
        root.findViewById(R.id.igb120).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        //this.handler.postDelayed(m_Runnable,1000);
        avi = root.findViewById(R.id.avi_igb_activity_home);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        language = preferences.getString("Type","");
        department = preferences.getString("Dep","");

        setLayout();




        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.igb120:
              // Toasty.success(getContext(),language,Toasty.LENGTH_LONG,true).show();
                if ("TCHN_1".equals(language)) {
                    startActivity(new Intent(getContext(), Class_t.class));
                }else{
                    startActivity(new Intent(getContext(), classroom_stu.class));
                }
                break;
            case R.id.igb2:
                igb_activity_home.setVisibility(View.GONE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                startActivity(new Intent(getContext(), News.class));
                break;
            default:

        }

    }

    private void setLayout() {
        final String url[]=new String[]{
                "https://uoh.edu.iq/sci/wp-content/uploads/2019/05/IMG_20190516_151628.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/11/DSC_0433.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/03/DSC_0291.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2020/03/nwe-1-1200x600.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2020/03/viber_image_2020-03-10_07-31-27-960x600.jpg",
                "https://uoh.edu.iq/ku/wp-content/uploads/2019/03/DSC_0410.jpg"
        };
        final String url1[]=new String[]{
                "https://uoh.edu.iq/sci/%d8%ae%d9%88%db%8e%d9%86%d8%af%da%a9%d8%a7%d8%b1%d8%a7%d9%86%db%8c-%d9%82%db%86%d9%86%d8%a7%d8%ba%db%8c-%d8%b3%db%8e%db%8c%db%95%d9%85%db%8c-%d8%a8%db%95%d8%b4%db%8c-%d8%b2%d8%a7%d9%86%d8%b3%d8%aa/",
                "https://uoh.edu.iq/ku/5990/",
                "https://uoh.edu.iq/ku/4305/",
                "https://uoh.edu.iq/ku/7027/",
                "https://uoh.edu.iq/ku/7020/",
                "https://uoh.edu.iq/ku/4328/"
        };
        final String dis[]=new String[]{
                "خوێندکارانی قۆناغی سێیەمی بەشی زانستی کۆمپیوتەر گەشتێكی زانستی ئەنجام دەدەن",
                "زانکۆی هەڵەبجە پێشوازی لەخوێندکارانی قۆناغی یەکەم دەکات",
                "په\u200Cیمانگایه\u200Cكی ئه\u200Cڵمانی به\u200Cهاوكاری زانكۆی هه\u200Cڵه\u200Cبجه\u200C توێژینه\u200Cوه\u200Cیه\u200Cك له\u200Cسه\u200Cر كیمیابارانكردنی شاره\u200Cكه\u200C ئه\u200Cنجامده\u200Cدات",
                "سەرۆكی زانكۆی هەڵەبجە بەیاوەری ژمارەیەك لە ئەندامانی ئەنجومەنی زانكۆی هەڵەبجە، سەردانی مەزاری شەهیدان دەكات",
                "زانكۆی هەڵەبجە بەمەبەستی كاری هاوبەشی زانستی زیاتر لەگەڵ زانكۆی گەرمیاندا پرۆتۆكۆلێكی لێك تێگەیشتنیان واژۆ كرد",
                "زانكۆی هه\u200Cڵه\u200Cبجه\u200C ماراسۆنی نێوده\u200Cوڵه\u200Cتی هه\u200Cڵه\u200Cبجه\u200Cی ئه\u200Cنجامدا"
        };
        for (int i=0;i<6;i++){
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
//    private final Runnable m_Runnable = new Runnable() {
//        public void run() {
//
//            fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).collection("Message")
//                    .whereEqualTo("status", false)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                int i = 0;
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    i++;
//                                }
//                                if (i == 0){
//                                    tv.setText("");
//                                }else {
//                                    tv.setBackground(getResources().getDrawable(R.drawable.bg_tv_notification_f_home));
//                                    tv.setText(""+i);
//                                }
//
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//            HomeFragment.this.handler.postDelayed(m_Runnable, 3000);
//        }
//
//    };
    @Override
    public void onPause() {
        super.onPause();
       // handler.removeCallbacks(m_Runnable);
    }
}
