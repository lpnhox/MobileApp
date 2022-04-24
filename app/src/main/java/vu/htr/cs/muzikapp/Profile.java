package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vu.htr.cs.muzikapp.login.LoginActivity;
import vu.htr.cs.muzikapp.login.User;

public class Profile extends AppCompatActivity {
    TextView displayname,tv_username,tv_email,tv_phone;
    String uid;
    Button btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        displayname=findViewById(R.id.displayname);
        tv_username=findViewById(R.id.tv_username);
        tv_email=findViewById(R.id.tv_email);
        tv_phone=findViewById(R.id.tv_phone);
        btn_logout=findViewById(R.id.btn_logout);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //uri hinh
        if(user!=null){
            String email=user.getEmail();
            tv_email.setText(email);
            uid=user.getUid();
            getInfo();
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    tv_email.setText("Please sign in");
                    displayname.setText("User");
                    tv_username.setText("User");
                    tv_phone.setText("*********");
                    Intent reload=new Intent(Profile.this, LoginActivity.class);
                    finish();
                    startActivity(reload);
                }
            });

        }else
        {
            Toast.makeText(Profile.this, "Không thể truy vấn!", Toast.LENGTH_SHORT).show();
        }




    }
    private void getInfo(){
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(uid.equals(ds.getKey())){
                        Toast.makeText(Profile.this,"Vo duoc roi",Toast.LENGTH_SHORT).show();
                        User curUser= ds.getValue(User.class);
                        displayname.setText(curUser.getUserName());
                        tv_username.setText(curUser.getUserName());
                        tv_phone.setText(curUser.getPhone());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "FAILED!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}