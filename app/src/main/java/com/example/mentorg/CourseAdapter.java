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

public class CourseAdapter extends FirebaseRecyclerAdapter<CoursesModel, CourseAdapter.myViewholder>{

    public CourseAdapter(@NonNull FirebaseRecyclerOptions<CoursesModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull CoursesModel model) {
        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.mentor.setText(model.getMentor());

        //for moving into next fragment***************
        // GOTO COURSE MENU ############## CREATE MODEL AND ADAPTER FOR COURSE MENU, GOTO recfragment(pass here: name of course n For menu & operations, see eg. homeAdapter->courseAdapter): GIVE CONDITION IN HomeAdapter
//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
//                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new ManageCourseFragment()).addToBackStack(null).commit();
//            }
//        });
//        holder.description.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
//                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new ManageCourseFragment()).addToBackStack(null).commit();
//            }
//        });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new myViewholder(view);
    }

    public class myViewholder extends RecyclerView.ViewHolder{
        TextView name, description, mentor;

        public myViewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            mentor = itemView.findViewById(R.id.mentor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
