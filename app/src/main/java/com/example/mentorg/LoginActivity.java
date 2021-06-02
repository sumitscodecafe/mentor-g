package com.example.mentorg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username_id, password_id;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btn_register_id, btn_login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_id = findViewById(R.id.username);
        password_id = findViewById(R.id.password);
        btn_register_id = findViewById(R.id.btn_register);
        btn_login_id = findViewById(R.id.btn_login);
        radioGroup = findViewById(R.id.groupRadio);

        btn_login_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = username_id.getText().toString();
                String password = password_id.getText().toString();
                String user;
                int selectedRadio = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedRadio);

//                if(username.equals("") || password.equals("")){
//                    Toast.makeText(getApplicationContext(), "One or more fields are empty.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                //user authentication for same device and no cleared data: since deadline approaching (Actual using firebase)
//                String username_sharedPref = SharedPreference.readSharedSetting(getApplicationContext(), "username", "false");
//                String password_sharedPref = SharedPreference.readSharedSetting(getApplicationContext(), "password", "false");
//                if(!username_sharedPref.equals(username) || !password_sharedPref.equals(password)){
//                    Toast.makeText(getApplicationContext(), "Username or password incorrect!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if(selectedRadio == -1)
                    Toast.makeText(getApplicationContext(), "Select user option", Toast.LENGTH_LONG).show();
                else{
                    user = (String) radioButton.getText();
                    if(user.equals("Mentor"))
                        SharedPreference.saveSharedSetting(getApplicationContext(), "user", "MENTOR");
                    else if(user.equals("Mentee"))
                        SharedPreference.saveSharedSetting(getApplicationContext(), "user", "MENTEE");
                    else
                        SharedPreference.saveSharedSetting(getApplicationContext(), "user", "ADMIN");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });

        btn_register_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}