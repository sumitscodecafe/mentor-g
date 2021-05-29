package com.example.mentorg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HomeAdapter extends FirebaseRecyclerAdapter<HomeMenuModel, HomeAdapter.myViewholder>{

    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull HomeMenuModel model) {
        holder.title.setText(model.getTitle());
        Glide.with(holder.img1.getContext()).load(model.getImgUrl()).into(holder.img1);

        //for moving into next fragment***************
        String titleText = (String) holder.title.getText();

        //MENTOR OPTIONS
        if(titleText.equals("Manage course")){
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new ManageCourseFragment()).addToBackStack(null).commit();
                }
            });
        }
        else {
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fullName = SharedPreference.readSharedSetting(v.getContext(), "fullname", "false");
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle(), fullName)).addToBackStack(null).commit();
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView title;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
