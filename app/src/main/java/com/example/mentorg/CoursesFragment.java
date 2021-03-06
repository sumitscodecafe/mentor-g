package com.example.mentorg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CoursesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String title_previousFragment;
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;

    public CoursesFragment() {
        // Required empty public constructor
    }
    public CoursesFragment(String title_previousFragment) {
        this.title_previousFragment = title_previousFragment;
    }

    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
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
        FirebaseRecyclerOptions<CoursesModel> menuOptions;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recfragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
        String username = SharedPreference.readSharedSetting(getActivity(), "username", "false");

        if(title_previousFragment.equals("Available course")) {
            Query check_exist = FirebaseDatabase.getInstance().getReference().child("course");
            check_exist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren())
                        Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
            menuOptions = new FirebaseRecyclerOptions.Builder<CoursesModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("course"), CoursesModel.class)
                    .build();
        }else {
            Query check_exist = FirebaseDatabase.getInstance().getReference().child("userCourse").child(username);
            check_exist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren())
                        Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
            menuOptions = new FirebaseRecyclerOptions.Builder<CoursesModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("userCourse").child(username), CoursesModel.class)
                    .build();
        }
        courseAdapter = new CourseAdapter(menuOptions, title_previousFragment);
        recyclerView.setAdapter(courseAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        courseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        courseAdapter.stopListening();
    }
}