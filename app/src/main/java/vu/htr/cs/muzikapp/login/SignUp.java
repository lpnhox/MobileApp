package vu.htr.cs.muzikapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;


import java.util.regex.Pattern;

import vu.htr.cs.muzikapp.R;

public class SignUp extends AppCompatActivity {
    TextInputEditText et_phone,et_username,et_email;
    EditText et_password,et_confirm_password;
    Button button_signup_2;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    private void SetNull(){
        et_phone.setText("");
        et_username.setText("");
        et_email.setText("");
        et_password.setText("");
        et_confirm_password.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        //lay firebase auth instance
        auth=FirebaseAuth.getInstance();
        et_phone=findViewById(R.id.et_phone);
        et_username=findViewById(R.id.et_username);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_confirm_password=findViewById(R.id.et_confirm_password);
        button_signup_2=findViewById(R.id.button_signup_2);
        button_signup_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_email.getText().toString().trim();
                String username=et_username.getText().toString().trim();
                String phone=et_phone.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                String confirm_password = et_confirm_password.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Nhập Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "Nhập Email hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Nhập password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password tối thiểu 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirm_password)){
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user= new User(username,email,phone);
                                    FirebaseDatabase.getInstance().getReference("Users").
                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                            setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(SignUp.this, "Đăng ký thành công." ,
                                                        Toast.LENGTH_SHORT).show();
                                                SetNull();

                                                return;
                                            }
                                            else{
                                                Toast.makeText(SignUp.this, "Đăng ký thất bại." ,
                                                        Toast.LENGTH_SHORT).show();
                                                SetNull();
                                                return;
                                            }

                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }
                        });
            }
        });
    }
}