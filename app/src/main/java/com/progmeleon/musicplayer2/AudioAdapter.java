package com.progmeleon.musicplayer2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    List<AudioModel> audios = new ArrayList<>();
    private OnItemClickListener listener;

    public AudioAdapter(List<AudioModel> audios, OnItemClickListener listener) {
        this.audios = audios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_layout, parent, false);
        return new AudioViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioModel audioModel = audios.get(position);
        holder.title.setText(audioModel.getTitle());
        holder.path.setText(audioModel.getPath());
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder{
        TextView title, path;
        public AudioViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            path = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            });
        }

    }
}
