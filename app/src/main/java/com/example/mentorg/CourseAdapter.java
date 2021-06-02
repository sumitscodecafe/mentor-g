package com.example.mentorg;

import android.content.DialogInterface;
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

public class CourseAdapter extends FirebaseRecyclerAdapter<CoursesModel, CourseAdapter.myViewholder>{

    String title_prevFragment;
    public CourseAdapter(@NonNull FirebaseRecyclerOptions<CoursesModel> options, String title_prevFragment) {
        super(options);
        this.title_prevFragment = title_prevFragment;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull CoursesModel model) {
        holder.name.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.mentor.setText(model.getMentor());

        //for moving into next fragment***************
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title_prevFragment.equals("Available course")){
                    String username = SharedPreference.readSharedSetting(view.getContext(), "username", "false");
                    String fullname = SharedPreference.readSharedSetting(view.getContext(), "fullname", "false");
                    //Store in Firebase DB
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference node = firebaseDatabase.getReference();
                    CoursesModel courseModel = new CoursesModel(model.getName(), model.getDescription(), model.getMentor());

                    String[] options = {"Enroll", "Un-enroll"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Select an option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(options[which].equals("Enroll")){
                                node.child("userCourse").child(username).push().setValue(courseModel);  //For personalised courses
                                String fullname = SharedPreference.readSharedSetting(view.getContext(), "fullname", "false");
                                node.child("courseMenuContents").child("participants").child(model.getName()).push().child("fullname").setValue(fullname);  //For courseParticipants
                                Toast.makeText(view.getContext(), "Enrolled successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Remove from userCourse
                                Query getUserCourseId = node.child("userCourse").child(username).orderByChild("name").equalTo(model.getName());
                                getUserCourseId.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                        int unEnrolledFlag = 0;
                                        for (DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                                            if(idSnapshot.hasChildren()) {
                                                idSnapshot.getRef().removeValue();
                                                unEnrolledFlag = 1;
                                                Toast.makeText(view.getContext(), "Course un-enrolled", Toast.LENGTH_SHORT).show();
                                            }
                                            if(unEnrolledFlag == 0)
                                                Toast.makeText(view.getContext(), "Course not enrolled!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                                //Remove from participants
                                Query getCourseName_participants = node.child("courseMenuContents").child("participants").child(model.getName()).orderByChild("fullname").equalTo(fullname);
                                getCourseName_participants.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                        int unEnrolledFlag = 0;
                                        for (DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                                            if(idSnapshot.hasChildren()) {
                                                idSnapshot.getRef().removeValue();
                                                unEnrolledFlag = 1;
                                                Toast.makeText(view.getContext(), "Course un-enrolled", Toast.LENGTH_SHORT).show();
                                            }
                                            if(unEnrolledFlag == 0)
                                                Toast.makeText(view.getContext(), "Course not enrolled!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                            }
                        }
                    });
                    builder.show();
                }
                else{   //For title_prevFragment = "My course" or "View course"
                    AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                    appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment("Show Course Menu", model.getName())).addToBackStack(null).commit();
                }
            }
        });
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
