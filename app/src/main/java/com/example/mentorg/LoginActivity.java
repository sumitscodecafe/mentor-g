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

        String login_flag = SharedPreference.readSharedSetting(getApplicationContext(), "login_flag", "false");
        String reg_flag = SharedPreference.readSharedSetting(getApplicationContext(), "reg_flag", "false");
        String username_sharedPref = SharedPreference.readSharedSetting(getApplicationContext(), "username", "false");
        String password_sharedPref = SharedPreference.readSharedSetting(getApplicationContext(), "password", "false");

        if(login_flag.equals("true")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        if(reg_flag.equals("true")) {
            username_id.setText(username_sharedPref);
            password_id.setText(password_sharedPref);
        }
        if(reg_flag.equals("false")) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        }

        btn_login_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = username_id.getText().toString();
                String password = password_id.getText().toString();
                String user = SharedPreference.readSharedSetting(getApplicationContext(), "user", "false");;
                int selectedRadio = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedRadio);
                String user_type = radioButton.getText().toString();

                if(reg_flag.equals("false")) {
                    Toast.makeText(getApplicationContext(), "Please register first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedRadio == -1) {
                    Toast.makeText(getApplicationContext(), "Select user option", Toast.LENGTH_LONG).show();
                    return;
                }
                if(username.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "One or more fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //user authentication for same device and no cleared data: since deadline approaching (Actual using firebase)
                SharedPreference.saveSharedSetting(getApplicationContext(), "login_flag", "true");
                if(!username_sharedPref.equals(username) || !password_sharedPref.equals(password)){
                    Toast.makeText(getApplicationContext(), "Username or password incorrect!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(user_type.equalsIgnoreCase("Admin")) {
                    Toast.makeText(getApplicationContext(), "Under construction!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!user_type.equalsIgnoreCase(user)){
                    Toast.makeText(getApplicationContext(), "Incorrect user type!", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
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