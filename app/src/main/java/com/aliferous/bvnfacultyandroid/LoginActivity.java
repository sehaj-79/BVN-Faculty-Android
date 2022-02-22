package com.aliferous.bvnfacultyandroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {


    Button submitID,submitOTP;
    EditText enterID;
    TextView textView;
    VideoView loading1;
    EditText e1,e2,e3,e4,e5,e6,et;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ConstraintLayout Page1, Page2;
    String codeSent,codeEntered;
    int OTP;


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs",MODE_PRIVATE);
        String enteredID = sharedPreferences.getString("StoredID","");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("ID",enteredID);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        et = findViewById(R.id.et);
        e1 = findViewById(R.id.et1);
        e2 = findViewById(R.id.et2);
        e3 = findViewById(R.id.et3);
        e4 = findViewById(R.id.et4);
        e5 = findViewById(R.id.et5);
        e6 = findViewById(R.id.et6);

        requestPermissions();

        new OTPReceiver().setEditText_otp(et);

        TextWatcher mTextEditorWatcher1 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1)
                    e2.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcher2 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1)
                    e3.requestFocus();
                else if (count==0)
                    e1.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcher3 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1)
                    e4.requestFocus();
                else if (count==0)
                    e2.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcher4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1)
                    e5.requestFocus();
                else if (count==0)
                    e3.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcher5 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==1)
                    e6.requestFocus();
                else if (count==0)
                    e4.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcher6 = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count==0)
                    e5.requestFocus();
            }

            public void afterTextChanged(Editable s) {
            }
        };

        TextWatcher mTextEditorWatcherT = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OTP = Integer.parseInt(s.toString());

                int o1 = OTP/100000;
                int o2 = OTP/10000;
                o2 = o2 % 10;
                int o3 = OTP/1000;
                o3 = o3 % 10;
                int o4 = OTP/100;
                o4 = o4 % 10;
                int o5 = OTP/10;
                o5 = o5 % 10;
                int o6 = OTP%10;
                e1.setText(""+o1);
                e2.setText(""+o2);
                e3.setText(""+o3);
                e4.setText(""+o4);
                e5.setText(""+o5);
                e6.setText(""+o6);

            }

            public void afterTextChanged(Editable s) {
            }
        };


        e1.addTextChangedListener(mTextEditorWatcher1);
        e2.addTextChangedListener(mTextEditorWatcher2);
        e3.addTextChangedListener(mTextEditorWatcher3);
        e4.addTextChangedListener(mTextEditorWatcher4);
        e5.addTextChangedListener(mTextEditorWatcher5);
        e6.addTextChangedListener(mTextEditorWatcher6);
        et.addTextChangedListener(mTextEditorWatcherT);


        submitID = findViewById(R.id.SubmitID);
        enterID = findViewById(R.id.EnterID);
        submitOTP = findViewById(R.id.SubmitOTP);
        //enterOTP = findViewById(R.id.EnterOTP);
        Page1 = findViewById(R.id.page1);
        Page2 = findViewById(R.id.page2);
        textView = findViewById(R.id.LoginPageText);
        loading1 = findViewById(R.id.loading1);
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();




        submitID.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //show loading
                String videopath= "android.resource://com.aliferous.bvnfaculty/"+R.raw.loading;
                Uri uri = Uri.parse(videopath);
                loading1.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);
                loading1.setVideoURI(uri);
                loading1.setZOrderOnTop(true);
                loading1.start();
                loading1.setVisibility(View.VISIBLE);
                loading1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        loading1.start();
                    }
                });


                //background tasks while loading sign appears
                String ID = enterID.getText().toString();
                DocumentReference dr =  db.collection("IDs").document(ID);
                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String contact = document.get("Contact").toString().trim();
                                contact="+91"+contact;
                                textView.setText("Enter the OTP sent to "+ contact +" below : ");
                                sendVerificationCode(contact);

                            } else {
                                Toast.makeText(getApplicationContext(),"Incorrect ID\nContact MIS Department",Toast.LENGTH_SHORT).show();
                                loading1.stopPlayback();
                                loading1.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Task Not Successful"+task.getException(),Toast.LENGTH_SHORT).show();
                            loading1.stopPlayback();
                            loading1.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i1 = Integer.parseInt(e1.getText().toString().trim());
                int i2 = Integer.parseInt(e2.getText().toString().trim());
                int i3 = Integer.parseInt(e3.getText().toString().trim());
                int i4 = Integer.parseInt(e4.getText().toString().trim());
                int i5 = Integer.parseInt(e5.getText().toString().trim());
                int i6 = Integer.parseInt(e6.getText().toString().trim());
                codeEntered = String.valueOf((i1*100000) + (i2*10000) + (i3*1000) + (i4*100) + (i5*10) + i6);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeEntered);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{
                    Manifest.permission.RECEIVE_SMS
            },100);
        }
    }

    void sendVerificationCode(String phno){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //SAVING ID IN STORAGE
                            SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs",MODE_WORLD_READABLE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("StoredID",enterID.getText().toString());
                            editor.commit();


                            //CORRECT OTP AND LOGIN
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            String enteredID = enterID.getText().toString();
                            intent.putExtra("ID",enteredID);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            //Next Page Animation
            Page2.setVisibility(View.VISIBLE);
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(Page1, "translationX", 0f, -1200f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(Page2, "translationX", 1200f, 0f);
            oa1.setDuration(350);
            oa2.setDuration(350);
            oa1.setInterpolator(new AccelerateDecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Page1.setVisibility(View.GONE);
                }
            });
            oa1.start();
            oa2.start();
            //Next Page Animation Over

            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            // Toast.makeText(getApplicationContext(),"Code Sent",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };
}

