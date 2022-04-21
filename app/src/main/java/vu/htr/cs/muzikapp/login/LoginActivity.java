package vu.htr.cs.muzikapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import vu.htr.cs.muzikapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_forgot_password,button_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vu.htr.cs.muzikapp.R.layout.activity_login);
        button_signup= findViewById(R.id.button_signup);
        button_forgot_password = findViewById(R.id.button_forgot_password);
        button_signup.setOnClickListener(this);
        button_forgot_password.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view==button_signup){
            startActivity(new Intent(LoginActivity.this,SignUp.class));

        }
        else if(view==button_forgot_password){
            Intent forgotIntent = new Intent(LoginActivity.this,ForgotPassword.class);
            startActivity(forgotIntent);

        }
    }
}