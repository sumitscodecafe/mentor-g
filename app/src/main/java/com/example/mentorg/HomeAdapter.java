package com.example.mentorg;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends FirebaseRecyclerAdapter<HomeMenuModel, HomeAdapter.myViewholder>{

    String courseTitle;
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options) {
        super(options);
    }
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options, String courseTitle) {
        super(options);
        this.courseTitle = courseTitle;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull HomeMenuModel model) {
        holder.title.setText(model.getTitle());
        Glide.with(holder.img1.getContext()).load(model.getImgUrl()).into(holder.img1);

        //for moving into next fragment***************
        String titleText = (String) holder.title.getText();

        // TODO: Conditions for Course Menu options
        //User home menu OPTIONS
        if ("Manage course".equals(titleText)) {
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new ManageCourseFragment()).addToBackStack(null).commit();
                }
            });
        } else if ("View course".equals(titleText) || "My courses".equals(titleText) || "Available course".equals(titleText)) {
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
                }
            });

        }else if ("Profile".equals(titleText)) {   //TODO
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
                }
            });
        }else if ("Manage users".equals(titleText)) { //TODO
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
                }
            });
        }else if ("Manage course request".equals(titleText)){  //TODO: last option: Manage course request(Course add/remove request by mentors should arrive here, then admin will take action?)
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
                }
            });
        }
        //FOR  CourseMenuOptions:
        else if ("Participants".equals(titleText)) {
            //Store in Firebase DB
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference node = firebaseDatabase.getReference();

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment(model.getTitle(), courseTitle, 1)).addToBackStack(null).commit();
                }
            });
        }
        else if ("Notice".equals(titleText)) {
            //Store in Firebase DB
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference node = firebaseDatabase.getReference();

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment(model.getTitle(), courseTitle, 1)).addToBackStack(null).commit();
                }
            });
        }
        else{   //TODO
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
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
