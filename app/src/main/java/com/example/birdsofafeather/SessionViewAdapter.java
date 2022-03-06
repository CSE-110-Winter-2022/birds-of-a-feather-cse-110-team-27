package com.example.birdsofafeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsers;

import java.util.List;

public class SessionViewAdapter extends RecyclerView.Adapter<SessionViewAdapter.ViewHolder> {
    private List<SessionWithUsers> sessions; //

    public SessionViewAdapter(List<SessionWithUsers> sessions){
        super();
        this.sessions = sessions;
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
    }

    @Override
    public int getItemCount(){
        return this.sessions.size();
    }

    public void removeSession(int position){
        this.sessions.remove(position);
        this.notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView sessionTextview;
        private SessionWithUsers session;

        ViewHolder(View itemView){
            super(itemView);
            this.sessionTextview = itemView.findViewById(R.id.session_row_text);
        }

        public void setSession(SessionWithUsers session){
            this.session = session;
            this.sessionTextview.setText(session.getSessionName());
        }
    }
}
