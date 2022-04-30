package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Profile extends Fragment {
    TextView displayname,tv_username,tv_email,tv_phone;
    String uid;
    Button btn_logout,btn_change_pass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_profile, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        displayname=getView().findViewById(R.id.displayname);
        tv_username=getView().findViewById(R.id.tv_username);
        tv_email=getView().findViewById(R.id.tv_email);
        tv_phone=getView().findViewById(R.id.tv_phone);
        btn_logout=getView().findViewById(R.id.btn_logout);
        btn_change_pass=getView().findViewById(R.id.btn_change_pass);

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
                    Intent reload=new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();
                    startActivity(reload);
                }
            });
            btn_change_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ChangePassword.class));
                }
            });


        }else
        {
            Toast.makeText(getActivity(), "Không thể truy vấn!", Toast.LENGTH_SHORT).show();
        }




    }
    private void getInfo(){
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(uid.equals(ds.getKey())){
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
                Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}