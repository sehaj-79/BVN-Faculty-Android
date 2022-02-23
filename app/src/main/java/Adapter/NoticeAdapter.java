package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliferous.bvnfacultyandroid.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Model.Notices;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private List<Notices> noticesList;
    private Context activity;
    private FirebaseFirestore firestore;

    public NoticeAdapter(Context mainActivity , List<Notices> noticesList){
        this.noticesList = noticesList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.notice_item , parent , false);

        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        Notices notices = noticesList.get(position);
        firestore.collection("Notices").document(notices.NoticeId).delete();
        noticesList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        Notices notices = noticesList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("Title" , notices.getTitle());
        bundle.putString("Desc" , notices.getDesc());
        bundle.putString("Id" , notices.NoticeId);

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Notices notices = noticesList.get(position);

        if(notices.getStatus()==0){

            holder.notice_title.setText(notices.getTitle());
            holder.notice_desc.setText(notices.getDesc());


        }


    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return noticesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView notice_number,notice_title,notice_desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notice_number = itemView.findViewById(R.id.notice_number);
            notice_title = itemView.findViewById(R.id.notice_title);
            notice_desc = itemView.findViewById(R.id.notice_details);

        }
    }
}