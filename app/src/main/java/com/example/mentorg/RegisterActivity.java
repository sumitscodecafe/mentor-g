package com.example.mentorg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText username_id, password_id, cpassword_id, fullname_id;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btn_register_id, btn_login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname_id = findViewById(R.id.full_name);
        username_id = findViewById(R.id.username);
        password_id = findViewById(R.id.password);
        cpassword_id = findViewById(R.id.cpassword);
        btn_register_id = findViewById(R.id.btn_register);
        btn_login_id = findViewById(R.id.btn_login);
        radioGroup = findViewById(R.id.groupRadio);

        btn_register_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String fullname = fullname_id.getText().toString();
                String username = username_id.getText().toString();
                String password = password_id.getText().toString();
                String cpassword = cpassword_id.getText().toString();
                String user;
                int selectedRadio = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedRadio);

                //Store in Firebase DB
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference node = firebaseDatabase.getReference("user");
                UserInfoHolder userInfoHolder = new UserInfoHolder(fullname, username, password);

                if (selectedRadio == -1)
                    Toast.makeText(getApplicationContext(), "Select user option", Toast.LENGTH_LONG).show();
                else{
                    user = (String) radioButton.getText();
                    if(user.equals("Mentor")) {
                        SharedPreference.saveSharedSetting(getApplicationContext(), "user", "MENTOR");
                        SharedPreference.saveSharedSetting(getApplicationContext(), "fullname", fullname);
                        node.child("mentor").child(username).setValue(userInfoHolder);
                    }else {
                        SharedPreference.saveSharedSetting(getApplicationContext(), "user", "MENTEE");
                        SharedPreference.saveSharedSetting(getApplicationContext(), "fullname", fullname);
                        node.child("mentee").child(username).setValue(userInfoHolder);
                    }
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }


            }
        });

        btn_login_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}