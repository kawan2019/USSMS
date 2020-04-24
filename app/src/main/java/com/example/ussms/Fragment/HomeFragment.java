package com.example.ussms.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ussms.Activity.MainActivity;
import com.example.ussms.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import es.dmoral.toasty.Toasty;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private FlipperLayout fliper;
    Fragment friends = new Friends();
    FirebaseAuth mAuth;
    Fragment classroom = new ClassR_t();
    FirestoreRecyclerAdapter adapter;
    private TextView tv;
    Handler handler = new Handler();
    private FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
private String language;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.f_home, container, false);
        tv = root.findViewById(R.id.tv_noti_f_home);
        fliper = root.findViewById(R.id.fliper);
        root.findViewById(R.id.igb120).setOnClickListener(this);
        root.findViewById(R.id.igb2).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        this.handler.postDelayed(m_Runnable,1000);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);
        language = preferences.getString("Type","");
        setLayout();
        return root;
    }

    @Override
    public void onClick(View view) {
        MainActivity m = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.igb120:
               Toasty.success(getContext(),language,Toasty.LENGTH_LONG,true).show();
                m.g(classroom);
                break;
            case R.id.igb2:
                m.g(friends);
                break;
            default:

        }

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

    private class NotificationViewHolder extends RecyclerView.ViewHolder {
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    private final Runnable m_Runnable = new Runnable() {
        public void run() {

            fsdb.collection("Users").document(mAuth.getCurrentUser().getDisplayName()).collection("Message")
                    .whereEqualTo("status", false)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int i = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    i++;
                                }
                                if (i == 0){
                                    tv.setText("");
                                }else {
                                    tv.setBackground(getResources().getDrawable(R.drawable.bg_tv_notification_f_home));
                                    tv.setText(""+i);
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            HomeFragment.this.handler.postDelayed(m_Runnable, 3000);
        }

    };
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(m_Runnable);
    }
}
