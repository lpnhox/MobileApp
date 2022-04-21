package vu.htr.cs.muzikapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import vu.htr.cs.muzikapp.R;
public class ForgotPassword extends AppCompatActivity {
    TextInputEditText et_email;
    Button button_send;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        et_email=findViewById(R.id.et_email);
        button_send= findViewById(R.id.button_send);

        auth = FirebaseAuth.getInstance();
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPassword.this, "Nhập địa chỉ email của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgotPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Chúng tôi đã gửi email khôi phục cho bạn", Toast.LENGTH_SHORT).show();
                            et_email.setText("");
                        } else {
                            Toast.makeText(ForgotPassword.this, "Email chưa được đăng ký!", Toast.LENGTH_SHORT).show();
                            et_email.setText("");
                        }
                    }
                });


            }
        });
    }


}