package com.example.mentorg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParticipantAdapter extends FirebaseRecyclerAdapter<ParticipantModel, ParticipantAdapter.myViewholder> {

    public ParticipantAdapter(@NonNull FirebaseRecyclerOptions<ParticipantModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ParticipantAdapter.myViewholder holder, int position, @NonNull ParticipantModel model) {
        holder.participant.setText(model.getFullname());
    }

    @NonNull
    @Override
    public ParticipantAdapter.myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item, parent, false);
        return new ParticipantAdapter.myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder{
        TextView participant;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            participant = itemView.findViewById(R.id.participant_fullname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
