package vu.htr.cs.muzikapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button trans_to_favorbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trans_to_favorbtn= findViewById(R.id.trans_to_favorrbtn);
        trans_to_favorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favourIntent= new Intent(MainActivity.this, FavorSong.class);
                //favourIntent.putExtra("abc",123);
                startActivity(favourIntent);
            }
        });


    }

}