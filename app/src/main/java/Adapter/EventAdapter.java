package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.bvnfacultyandroid.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Model.Events;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Events> eventsList;
    private Context activity;
    private FirebaseFirestore firestore;


    public EventAdapter(Context mainActivity , List<Events> eventsList){
        this.eventsList = eventsList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.event_item , parent , false);

        firestore = FirebaseFirestore.getInstance();

        return new EventAdapter.MyViewHolder(view);
    }



    public Context getContext(){
        return activity;
    }


    public void editTask(int position){
        Events events = eventsList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Name" , events.getEventName());

    }
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Events events = eventsList.get(position);

        holder.event_name.setText(events.getEventName());

    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView event_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            event_name = itemView.findViewById(R.id.event_name);

        }
    }

}
