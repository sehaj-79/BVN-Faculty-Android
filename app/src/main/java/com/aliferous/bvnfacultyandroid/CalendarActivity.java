package com.aliferous.bvnfacultyandroid;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Adapter.EventAdapter;
import Adapter.NoticeAdapter;
import Model.Events;
import Model.Notices;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView,eventRecyclerView;
    private LocalDate selectedDate;
    FloatingActionButton fab;
    ConstraintLayout AddEventPage;
    BlurView background_blur;
    Button AddEventBtn;
    String message;
    EditText AddEventET1,AddEventET2,AddEventET3,AddEventET4,AddEventET5,AddEventET6,AddEventET7;
    FirebaseFirestore db;
    String ID,Name;
    Spinner LocSpinner;

    private EventAdapter adapter;
    private FirebaseFirestore firestore;
    private Query query;
    private ListenerRegistration listenerRegistration;

    private List<Events> EventList;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        firestore = FirebaseFirestore.getInstance();

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
        LocSpinner = findViewById(R.id.locSpinner);

        eventRecyclerView = findViewById(R.id.EventRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
        EventList = new ArrayList<>();
        //Read Notices
        //readEvents();

        db= FirebaseFirestore.getInstance();
        ID= getIntent().getStringExtra("ID");


        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        blurBackground();

        List <String> locations = Arrays.asList("Foyer","Ground","Skating Rink","Conclave","M.P.Hall");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item,locations);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        LocSpinner.setAdapter(adapter);


        DocumentReference docRef = db.collection("IDs").document(""+ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Name = document.getString("Name");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



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

                //Set Name Value
                AddEventET7.setText(Name);
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
                String Audience = AddEventET5.getText().toString();
                String Guest = AddEventET6.getText().toString();
                String Organizer = AddEventET7.getText().toString();

                //Put to HashMap
                Map<String,Object> note = new HashMap<>();
                note.put("Event Name",EventName);
                note.put("Date",Date);
                note.put("Time",Time);
                note.put("Location",Location);
                note.put("Audience",Audience);
                note.put("Guest",Guest);
                note.put("Organizer",Organizer);


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

    private void readEvents() {

        EventList.clear();
        query = firestore.collection("Events");

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

                                            Events eventModel = document.toObject(Notices.class).withId(id);

                                            EventList.add(eventModel);
                                            adapter = new EventAdapter(CalendarActivity.this , EventList);
                                            eventRecyclerView.setAdapter(adapter);
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




