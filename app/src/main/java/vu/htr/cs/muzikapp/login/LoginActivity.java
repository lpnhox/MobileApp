package vu.htr.cs.muzikapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vu.htr.cs.muzikapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_forgot_password,button_signup,button_login;
    TextInputEditText et_username;
    EditText et_password;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vu.htr.cs.muzikapp.R.layout.activity_login);
        button_signup= findViewById(R.id.button_signup);
        button_forgot_password = findViewById(R.id.button_forgot_password);
        button_login=findViewById(R.id.button_login);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);

        button_signup.setOnClickListener(this);
        button_forgot_password.setOnClickListener(this);
        button_login.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
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
        else if(view==button_login){
            String email = et_username.getText().toString().trim();
            String password= et_password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Nhập username/email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(getApplicationContext(), "Nhập Email hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Nhập password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if (password.length() < 6) {
                            et_password.setError(getString(R.string.minimum_password));
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                        }
                        return;
                    } else {
                        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }
}