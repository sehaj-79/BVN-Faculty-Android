package com.aliferous.bvnfacultyandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView tv_welcome;
    ImageView im1,im2,imR;
    ConstraintLayout Splash, HomePage;
    CountDownTimer cdt_splash;
    String ID,name;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im1 = findViewById(R.id.image1);
        im2 = findViewById(R.id.image2);
        tv_welcome = findViewById(R.id.mainWelcome);
        imR = findViewById(R.id.randomImageView);
        Splash = findViewById(R.id.splashScreen);
        HomePage = findViewById(R.id.HomePage);
        db= FirebaseFirestore.getInstance();

        ID= getIntent().getStringExtra("ID");


        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);*/
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
                }
                else {}
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




    }
}
