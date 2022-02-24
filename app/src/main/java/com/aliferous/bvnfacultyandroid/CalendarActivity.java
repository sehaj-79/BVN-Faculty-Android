package com.aliferous.bvnfacultyandroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    FloatingActionButton fab;
    ConstraintLayout AddEventPage;
    BlurView background_blur;
    Button AddEventBtn;
    String message;
    EditText AddEventET1,AddEventET2,AddEventET3,AddEventET4,AddEventET5,AddEventET6,AddEventET7;
    FirebaseFirestore db;


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        fab = findViewById(R.id.fab);
        background_blur = findViewById(R.id.background_blur);
        AddEventPage = findViewById(R.id.addEvent);
        AddEventBtn = findViewById(R.id.AddEventButton);
        AddEventET1 = findViewById(R.id.AddEventET1);
        AddEventET2 = findViewById(R.id.AddEventET2);
        AddEventET3 = findViewById(R.id.AddEventET3);
        AddEventET4 = findViewById(R.id.AddEventET4);
        AddEventET5 = findViewById(R.id.AddEventET5);
        AddEventET6 = findViewById(R.id.AddEventET6);
        AddEventET7 = findViewById(R.id.AddEventET7);
        db= FirebaseFirestore.getInstance();


        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        blurBackground();


        calendarRecyclerView.setOnTouchListener(new OnSwipeTouchListener(CalendarActivity.this){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                previousMonthAction(null);
                Toast.makeText(getApplicationContext(),"Swipe Left",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextMonthAction(null);
                previousMonthAction(null);
                Toast.makeText(getApplicationContext(),"Swipe Right",Toast.LENGTH_LONG).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background_blur.setVisibility(View.VISIBLE);
                AddEventPage.setVisibility(View.VISIBLE);
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(background_blur, "alpha", 0f, 1f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(AddEventPage, "alpha", 0f, 1f);
                oa1.setDuration(600);
                oa2.setDuration(600);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.start();
                oa2.start();
            }
        });

        AddEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation Page 100% Alpha to 0% Alpha
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(background_blur, "alpha", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(AddEventPage, "alpha", 1f, 0f);
                oa1.setDuration(600);
                oa2.setDuration(600);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        background_blur.setVisibility(View.GONE);
                        AddEventPage.setVisibility(View.GONE);
                    }
                });
                oa1.start();
                oa2.start();

                //Get Strings
                String EventName = AddEventET1.getText().toString();
                String Date = AddEventET2.getText().toString();
                String Time = AddEventET3.getText().toString();
                String Location = AddEventET4.getText().toString();
                String Organizer = AddEventET5.getText().toString();
                String Guest = AddEventET6.getText().toString();
                String Audience = AddEventET7.getText().toString();

                //Put to HashMap
                Map<String,Object> note = new HashMap<>();
                note.put("Event Name",EventName);
                note.put("Date",Date);
                note.put("Time",Time);
                note.put("Location",Location);
                note.put("Organizer",Organizer);
                note.put("Guest",Guest);
                note.put("Audience",Audience);

                //Upload HashMap to Firestore
                db.collection("Events").document("2022").collection("Feb").document().set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Event Addded Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Unable to Add Event",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
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

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        message = "Selected Date " + 1 + " " + monthYearFromDate(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, message);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }
}




