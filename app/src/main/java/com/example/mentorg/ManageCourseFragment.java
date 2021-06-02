package com.example.mentorg;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ManageCourseFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText courseName, courseDescription;
    Button btn_submit;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public ManageCourseFragment() {
        // Required empty public constructor
    }

    public static ManageCourseFragment newInstance(String param1, String param2) {
        ManageCourseFragment fragment = new ManageCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_course, container, false);
        btn_submit = view.findViewById(R.id.btn_submit);
        radioGroup = view.findViewById(R.id.groupRadio);
        courseName = view.findViewById(R.id.courseName);
        courseDescription = view.findViewById(R.id.courseDescription);

        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int selectedRadio = radioGroup.getCheckedRadioButtonId();
                String action;
                radioButton = view.findViewById(selectedRadio);
                String name = courseName.getText().toString();
                String description = courseDescription.getText().toString();
                String mentorName = SharedPreference.readSharedSetting(getActivity(), "fullname", "false");

                if(name.equals("") || mentorName.equals("")){
                    Toast.makeText(getActivity(), "Course name can't be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Store in Firebase DB
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference node = firebaseDatabase.getReference();
                CoursesModel courseModel = new CoursesModel(name, description, mentorName);

                if (selectedRadio == -1)
                    Toast.makeText(getActivity(), "Select action", Toast.LENGTH_SHORT).show();
                else{
                    action = (String) radioButton.getText();
                    String username = SharedPreference.readSharedSetting(getActivity(), "username", "false");
                    if(action.equals("Add course")){
                        if(description.equals("")){
                            Toast.makeText(getActivity(), "Description can't be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        node.child("course").push().setValue(courseModel);  //For general all courses
                        node.child("userCourse").child(username).push().setValue(courseModel);  //For personalised courses
                        Toast.makeText(getActivity(), "Course added", Toast.LENGTH_LONG).show();
                    }else{
                        //For general course removal
                        Query getCourseId = node.child("course").orderByChild("name").equalTo(name);
                        getCourseId.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                int unEnrolledFlag = 0;
                                for (DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                                    if(idSnapshot.hasChildren()) {
                                        idSnapshot.getRef().removeValue();
                                        unEnrolledFlag = 1;
                                        Toast.makeText(getActivity(), "Course removed", Toast.LENGTH_SHORT).show();
                                    }
                                    if(unEnrolledFlag == 0)
                                        Toast.makeText(getActivity(), "Course not found!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                        //For personal course removal
                        Query getUserCourseId = node.child("userCourse").child(username).orderByChild("name").equalTo(name);
                        getCourseId.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot idSnapshot: dataSnapshot.getChildren()) {
                                    if(idSnapshot.hasChildren()) {
                                        idSnapshot.getRef().removeValue();
                                        Toast.makeText(getActivity(), "Course removed", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Course not found!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                    courseName.setText("");
                    courseDescription.setText("");
                    radioGroup.clearCheck();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}