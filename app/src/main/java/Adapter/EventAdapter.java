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

    private List<Events> noticesList;
    private Context activity;
    private FirebaseFirestore firestore;


    public EventAdapter(Context mainActivity , List<Events> noticesList){
        this.noticesList = noticesList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.event_item , parent , false);

        firestore = FirebaseFirestore.getInstance();

        return new EventAdapter.MyViewHolder(view);
    }

    public void deleteTask(int position){
        Events notices = noticesList.get(position);
        //firestore.collection("Events").document(notices.EventId).delete();
        noticesList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        Events events = noticesList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Name" , events.getName());

    }
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Events events = noticesList.get(position);

        holder.event_name.setText(events.getName());

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return noticesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView event_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //event_number = itemView.findViewById(R.id.event_number);

        }
    }

}
