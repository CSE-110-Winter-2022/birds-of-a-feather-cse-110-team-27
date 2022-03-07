package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsers;

import java.util.ArrayList;
import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder> {
    private List<SessionWithUsers> sessions; //
    private int userId;

    public SessionViewAdapter(List<SessionWithUsers> sessions, int userId){
        super();
        this.sessions = sessions;
        this.userId = userId;
    }

    @NonNull
    @Override
    public SessionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.session_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewAdapter.ViewHolder holder, int position){
        holder.setSession(sessions.get(position));
        holder.setUserId(userId);
    }


    @Override
    public int getItemCount(){
        return this.sessions.size();
    }

    public void removeSession(int position){
        this.sessions.remove(position);
        this.notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private final TextView sessionTextview;
        private SessionWithUsers session;
        private int userId;

        ViewHolder(View itemView){
            super(itemView);
            this.sessionTextview = itemView.findViewById(R.id.session_row_text);
            itemView.setOnClickListener(this);
        }

        public void setSession(SessionWithUsers session){
            this.session = session;
            this.sessionTextview.setText(session.getSessionName());
        }
        public void setUserId(int userId) {
           this.userId = userId;
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, FindNearbyActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("session_id", session.getSession().getId());
            context.startActivity(intent);
        }
    }
}
