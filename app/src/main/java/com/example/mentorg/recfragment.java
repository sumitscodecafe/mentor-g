package com.example.mentorg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class  recfragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    ParticipantAdapter itemAdapter;
    String showCourseMenu = "", courseTitle, menuTitle = "";
    int flag = 0;   //used for adapter listener(see bottom)
    EditText notice_text;

    public recfragment() {
        // Required empty public constructor
    }
    public recfragment(String showCourseMenu, String courseTitle) {
        this.showCourseMenu = showCourseMenu;
        this.courseTitle = courseTitle;
    }

    public recfragment(String menuTitle, String courseTitle, int flag){
        this.menuTitle = menuTitle;
        this.courseTitle = courseTitle;
        this.flag = flag;
    }

    public static recfragment newInstance(String param1, String param2) {
        recfragment fragment = new recfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courseMenuContents/");

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
        FirebaseRecyclerOptions<HomeMenuModel> menuOptions;
        FirebaseRecyclerOptions<ParticipantModel> itemList;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recfragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        //For setting editText & button to VISIBLE
        notice_text = view.findViewById(R.id.notice_text);
        Button button_plus = view.findViewById(R.id.btn_plus);
        String user = SharedPreference.readSharedSetting(getContext(), "user", "false");

        if(showCourseMenu.equals("Show Course Menu")){
            menuOptions =
                    new FirebaseRecyclerOptions.Builder<HomeMenuModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("courseMenu"), HomeMenuModel.class)
                            .build();
            homeAdapter = new HomeAdapter(menuOptions, courseTitle);
            recyclerView.setAdapter(homeAdapter);
            return view;
        }
        else if(menuTitle.equals("Notice")){
            if(user.equals("MENTOR")) {
                notice_text.setVisibility(View.VISIBLE);
                button_plus.setVisibility(View.VISIBLE);
                button_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Upload notice
                        String notice = notice_text.getText().toString();
                        DatabaseReference node = FirebaseDatabase.getInstance().getReference();
                        node.child("courseMenuContents/notice/"+courseTitle).push().child("fullname").setValue(notice);
                        notice_text.setText("");
                        Toast.makeText(getActivity(), "Notice added.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            itemList =
                    new FirebaseRecyclerOptions.Builder<ParticipantModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("courseMenuContents/notice/"+courseTitle), ParticipantModel.class)
                            .build();
            itemAdapter = new ParticipantAdapter(itemList);
            recyclerView.setAdapter(itemAdapter);
            return view;
        }
        else if(menuTitle.equals("Participants")){
            Query check_exist = FirebaseDatabase.getInstance().getReference().child("courseMenuContents/participants/"+courseTitle);
            check_exist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren())
                        Toast.makeText(getActivity(), "No record found.", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
            itemList =
                    new FirebaseRecyclerOptions.Builder<ParticipantModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("courseMenuContents/participants/"+courseTitle), ParticipantModel.class)
                            .build();
            itemAdapter = new ParticipantAdapter(itemList);
            recyclerView.setAdapter(itemAdapter);
            return view;
        }
        else if(menuTitle.equals("Study materials") || menuTitle.equals("Attendance") || menuTitle.equals("Grades")){
            if(user.equals("MENTOR")) {
                notice_text.setVisibility(View.VISIBLE);
                button_plus.setVisibility(View.VISIBLE);
                notice_text.setHint("Enter filename");
                button_plus.setText(R.string.upload_btn);

                button_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Upload file
                        selectPDF();
                    }
                });
            }
            //Message for no records found
            DatabaseReference node = FirebaseDatabase.getInstance().getReference();
            Query getUserCourseId = node.child("courseMenuContents").child(menuTitle).child(courseTitle).orderByChild("title");
            getUserCourseId.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren())
                        Toast.makeText(view.getContext(), "No records found.", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
            menuOptions =
                    new FirebaseRecyclerOptions.Builder<HomeMenuModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("courseMenuContents/"+menuTitle+"/"+courseTitle), HomeMenuModel.class)
                            .build();
            homeAdapter = new HomeAdapter(menuOptions, 1);
            recyclerView.setAdapter(homeAdapter);
            return view;
        }

        else if(user.equals("MENTOR")) {
            menuOptions =
                    new FirebaseRecyclerOptions.Builder<HomeMenuModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("homeMenu/mentorMenu"), HomeMenuModel.class)
                            .build();
        }
        else if(user.equals("MENTEE")){
            menuOptions =
                    new FirebaseRecyclerOptions.Builder<HomeMenuModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("homeMenu/menteeMenu"), HomeMenuModel.class)
                            .build();
        }
        else{
            menuOptions =
                    new FirebaseRecyclerOptions.Builder<HomeMenuModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("homeMenu/adminMenu"), HomeMenuModel.class)
                            .build();
        }

        homeAdapter = new HomeAdapter(menuOptions);
        recyclerView.setAdapter(homeAdapter);
        return view;
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999 && data != null && data.getData() != null){
            //String fileName = data.getDataString().substring(data.getDataString().lastIndexOf('/') + 1);
            uploadPDF(data.getData());
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("File upload");
        progressDialog.show();

        String fileName = notice_text.getText().toString();

        StorageReference reference = storageReference.child(System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Store data to real time DB
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                HomeMenuModel fileModel = new HomeMenuModel(fileName, uri.toString());
                databaseReference.child(menuTitle).child(courseTitle).push().setValue(fileModel);
                Toast.makeText(getContext(), "File uploaded!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                notice_text.setText("");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploading, please wait...");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(flag == 0)
            homeAdapter.startListening();
        else
            itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(flag == 0)
            homeAdapter.startListening();
        else
            itemAdapter.startListening();
    }
}