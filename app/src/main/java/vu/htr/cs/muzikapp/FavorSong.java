package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class FavorSong extends AppCompatActivity {

    private boolean checkPermission = false;
    List<String> songsNameList;
    List<String> songUrlList;
    List<String> songsArtistList;
    List<String> songsDurationList;
    List<String> thumbnail;
    ListAdapter adapter;
    ListView lvFavorMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faver_song);
        lvFavorMusic = findViewById(R.id.listFavorMusic);
        songsNameList = new ArrayList<>();
        songUrlList = new ArrayList<>();
        songsArtistList = new ArrayList<>();
        songsDurationList = new ArrayList<>();
        thumbnail = new ArrayList<>();
        retrieveSongs();
    }
    public void retrieveSongs(){
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Songs");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Song songObj = ds.getValue(Song.class);
                    songsNameList.add(songObj.getSongName());
                    songUrlList.add(songObj.getSongUrl());
                    songsArtistList.add(songObj.getSongArtist());
                    songsDurationList.add(songObj.getSongDuration());
                    thumbnail.add(songObj.getImageUrl());
                }

                adapter = new ListAdapter(getApplicationContext(),songsNameList,thumbnail,songsArtistList,songsDurationList);
                lvFavorMusic.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavorSong.this, "FAILED!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.uploadItem){
            if(validatePermissions()){
                Intent intent = new Intent(this,AddMusic.class);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validatePermissions(){

        Dexter.withActivity(FavorSong.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        checkPermission=true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        checkPermission=false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        return checkPermission;


    }
}