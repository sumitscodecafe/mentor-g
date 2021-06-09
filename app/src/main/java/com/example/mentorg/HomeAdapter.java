package com.example.mentorg;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import static androidx.core.content.ContextCompat.startActivity;

public class HomeAdapter extends FirebaseRecyclerAdapter<HomeMenuModel, HomeAdapter.myViewholder>{

    String courseTitle;
    int flag = 0;
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options) {
        super(options);
    }
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options, int flag) {
        super(options);
        this.flag = flag;
    }
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<HomeMenuModel> options, String courseTitle) {
        super(options);
        this.courseTitle = courseTitle;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull HomeMenuModel model) {
        holder.title.setText(model.getTitle());
        //Glide.with(holder.img1.getContext()).load(model.getImgUrl()).into(holder.img1);

        //for moving into next fragment***************
        String titleText = (String) holder.title.getText();

        // TODO: Conditions for Course Menu options
        //User home menu OPTIONS
        if ("Manage course".equals(titleText)) {
            holder.img1.setImageResource(R.drawable.manage_course);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new ManageCourseFragment()).addToBackStack(null).commit();
                }
            });
        } else if (flag == 1) {
            holder.img1.setImageResource(R.drawable.download_icon);
            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("application/pdf");
                    intent.setData(Uri.parse(model.getImgUrl()));
                    startActivity(v.getContext(), intent, null);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("application/pdf");
                    intent.setData(Uri.parse(model.getImgUrl()));
                    startActivity(v.getContext(), intent, null);
                }
            });
        }
        else if ("View course".equals(titleText) || "My courses".equals(titleText) || "Available course".equals(titleText)) {
            holder.img1.setImageResource(R.drawable.courses);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new CoursesFragment(model.getTitle())).addToBackStack(null).commit();
                }
            });

        }else if ("Profile".equals(titleText)) {   //TODO
            holder.img1.setImageResource(R.drawable.profile);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Under construction.", Toast.LENGTH_LONG).show();
                }
            });
        }else if ("Manage users".equals(titleText)) { //TODO
            holder.img1.setImageResource(R.drawable.manage_user);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Under construction.", Toast.LENGTH_LONG).show();
                }
            });
        }else if ("Manage course request".equals(titleText)){  //TODO: last option: Manage course request(Course add/remove request by mentors should arrive here, then admin will take action?)
            holder.img1.setImageResource(R.drawable.manage_course);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Under construction.", Toast.LENGTH_LONG).show();
                }
            });
        }
        //FOR  CourseMenuOptions:
        else if ("Participants".equals(titleText)) {
            holder.img1.setImageResource(R.drawable.participants);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment(model.getTitle(), courseTitle, 1)).addToBackStack(null).commit();
                }
            });
        }
        else if("Notice".equals(titleText)){
            holder.img1.setImageResource(R.drawable.notice);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment(model.getTitle(), courseTitle, 1)).addToBackStack(null).commit();
                }
            });
        }
        else if ("Study materials".equals(titleText) || "Attendance".equals(titleText) || "Grades".equals(titleText)) {
            if("Study materials".equals(titleText))
                holder.img1.setImageResource(R.drawable.study_materials);
            else if("Attendance".equals(titleText))
                holder.img1.setImageResource(R.drawable.attendance);
            else
                holder.img1.setImageResource(R.drawable.grade);

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment(model.getTitle(), courseTitle, 0)).addToBackStack(null).commit();
                }
            });
        }
        else{
            holder.img1.setImageResource(R.drawable.download_icon);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Invalid option.", Toast.LENGTH_LONG).show();}
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
