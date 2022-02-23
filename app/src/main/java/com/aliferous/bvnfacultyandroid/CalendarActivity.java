package com.aliferous.bvnfacultyandroid;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    EditText AddEventET;

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
        AddEventET = findViewById(R.id.AddEventET1);

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
                String EventName = AddEventET.getText().toString();

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

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}




