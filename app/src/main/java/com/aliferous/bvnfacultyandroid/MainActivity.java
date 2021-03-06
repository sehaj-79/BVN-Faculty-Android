package com.aliferous.bvnfacultyandroid;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.NoticeAdapter;
import Model.Notices;
import Model.User;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {



    TextView tv_welcome,tv_department,tv_noticeAdd;
    ImageView im1,im2,imR;
    EditText noticeTitle, noticeDesc;
    Button addNoticeBtn;
    ConstraintLayout Splash, HomePage,AddNoticePage;
    CountDownTimer cdt_splash;
    BlurView background_blur;
    String ID,name,department;
    FirebaseFirestore db;
    int notice_count=0,MasterAccess;

    private NoticeAdapter adapter;
    RecyclerView notice_recycler;
    private List<Notices> noticesList;

    private FirebaseFirestore firestore;
    private Query query;
    private ListenerRegistration listenerRegistration;

    ImageView nav_home,nav_calender,nav_todo,nav_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();


        im1 = findViewById(R.id.image1);
        im2 = findViewById(R.id.image2);
        tv_welcome = findViewById(R.id.mainWelcome);
        tv_department = findViewById(R.id.mainDepartment);
        tv_noticeAdd = findViewById(R.id.mainNoticeAdd);
        imR = findViewById(R.id.randomImageView);
        Splash = findViewById(R.id.splashScreen);
        HomePage = findViewById(R.id.HomePage);
        db= FirebaseFirestore.getInstance();

        noticeTitle = findViewById(R.id.AddNoticeTitle);
        noticeDesc = findViewById(R.id.AddNoticeDesc);
        addNoticeBtn = findViewById(R.id.AddNoticeButton);
        AddNoticePage = findViewById(R.id.addNotice);
        background_blur = findViewById(R.id.main_background_blur);


        nav_home = findViewById(R.id.main_nav_home);
        nav_calender = findViewById(R.id.main_nav_calender);
        nav_todo = findViewById(R.id.main_nav_todo);
        nav_profile = findViewById(R.id.main_nav_profile);

        ID= getIntent().getStringExtra("ID");
        blurBackground();


        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });

        DocumentReference dr =  db.collection("IDs").document(ID);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    name = document.get("Name").toString().trim();
                    tv_welcome.setText("Welcome "+name);
                    department = document.get("Department").toString().trim();
                    tv_department.setText("Department of "+department);
                    MasterAccess = Integer.parseInt(document.get("MasterAccess").toString());
                }
                else {}
            }
        });

        if (MasterAccess == 0)
        {
            tv_noticeAdd.setVisibility(View.VISIBLE);
        }

        tv_noticeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNotice();
            }
        });



        cdt_splash = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(im1, "alpha", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(im2, "alpha", 1f, 0f);
                final ObjectAnimator oa3 = ObjectAnimator.ofFloat(HomePage, "alpha", 0f, 1f);
                final ObjectAnimator oaR = ObjectAnimator.ofFloat(imR, "alpha", 1f, 0f);
                oa1.setDuration(350);
                oa2.setDuration(350);
                oaR.setDuration(150);
                oa3.setDuration(350);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa3.setInterpolator(new AccelerateDecelerateInterpolator());
                oaR.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa2.start();
                    }
                });
                oa2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Splash.setVisibility(View.GONE);
                        HomePage.setVisibility(View.VISIBLE);
                        oa3.start();
                    }
                });
                oa1.start();
                oaR.start();
            }
        }.start();

        notice_recycler = findViewById(R.id.main_notices_recycler);
        notice_recycler.setHasFixedSize(true);
        notice_recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        noticesList = new ArrayList<>();
        //Read Notices
        readNotices();


        //NavBar

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);*/
            }
        });

        nav_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });

        nav_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this ,TodoActivity.class);
                startActivity(intent);
            }
        });

        nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void AddNotice() {

        background_blur.setVisibility(View.VISIBLE);
        AddNoticePage.setVisibility(View.VISIBLE);
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(background_blur, "alpha", 0f, 1f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(AddNoticePage, "alpha", 0f, 1f);
        oa1.setDuration(600);
        oa2.setDuration(600);
        oa1.setInterpolator(new AccelerateDecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.start();
        oa2.start();

        addNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation Page 100% Alpha to 0% Alpha
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(background_blur, "alpha", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(AddNoticePage, "alpha", 1f, 0f);
                oa1.setDuration(600);
                oa2.setDuration(600);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        background_blur.setVisibility(View.GONE);
                        AddNoticePage.setVisibility(View.GONE);
                    }
                });
                oa1.start();
                oa2.start();

                //Get Strings
                String NoticeTitle = noticeTitle.getText().toString();
                String NoticeDesc = noticeDesc.getText().toString();
                int NoticeStatus = 0;


                //Put to HashMap
                Map<String,Object> notice = new HashMap<>();
                notice.put("Title",NoticeTitle);
                notice.put("Desc",NoticeDesc);
                notice.put("Status",NoticeStatus);


                //Upload HashMap to Firestore
                db.collection("Notices").document().set(notice).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Notice Addded Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Unable to Add Notice",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    private void readNotices() {

        noticesList.clear();
        query = firestore.collection("Notices");

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                firestore.collection("Notices")
                        .whereEqualTo("Status", 0)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
                                            String id = document.getId();
                                            notice_count++;
                                            Notices NoticeModel = document.toObject(Notices.class).withId(id);
                                            NoticeModel.setCount(notice_count);
                                            noticesList.add(NoticeModel);
                                            adapter = new NoticeAdapter(MainActivity.this , noticesList, MasterAccess);
                                            notice_recycler.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

                listenerRegistration.remove();

            }
        });

    }

    private void blurBackground() {

        float radius = 2f;

        background_blur.clearFocus();

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        background_blur.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

}
