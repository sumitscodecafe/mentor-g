package com.example.mentorg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //For no status bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).commit();

        //Future enhancements 1.0:
//        String fullname = SharedPreference.readSharedSetting(getApplicationContext(), "fullname", "false");
//        String user = SharedPreference.readSharedSetting(getApplicationContext(), "user", "false");
//
//        if(user.equals("MENTOR"))
//            welcomeAlert(fullname, "As a mentor you can create courses, manage them and add notice/materials" +
//                    " related to the courses under 'View course' option. We wish you a great teaching" +
//                    "experience with Mentor-G. Press 'OK' to continue.");
//        else if(user.equals("MENTEE"))
//            welcomeAlert(fullname, "As a mentee you can enroll into various courses provided by the Mentors" +
//                    " and keep track of your grades and attendance along with other aids that will make your learning" +
//                    " experience worth your while. We wish you a great learning experience with Mentor-G. Press 'OK' to continue.");
//        else
//            welcomeAlert(fullname, "Admins will receive requests from mentors for adding/removing courses and participants.");
    }

//    private void welcomeAlert(String fullname, String message) {
//        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
//        alertDialog.setTitle("Welcome "+fullname+"!");
//        alertDialog.setMessage(message);
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).commit();
//                        //dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
//    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // do something here
            SharedPreference.saveSharedSetting(getApplicationContext(), "login_flag", "false");
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}