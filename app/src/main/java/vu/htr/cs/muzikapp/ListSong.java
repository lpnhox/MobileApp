package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSong extends AppCompatActivity {
    ListView listViewSong;
    ArrayList<Song> listSong;
    SongAdapter adapter;
    SearchView searchView_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        listViewSong = findViewById(R.id.listViewSong);
        listSong = new ArrayList<>();
        displaySongs();

        searchView_song = (SearchView) findViewById(R.id.searchView_song);
        searchView_song.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    public void displaySongs(){
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Songs");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Song songObj = ds.getValue(Song.class);
                    listSong.add(new Song(songObj.getSongName(),songObj.getSongUrl(),songObj.getImageUrl(),songObj.getSongArtist(),songObj.getSongDuration()));
                }

                adapter = new SongAdapter(getApplicationContext(),listSong);
                listViewSong.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListSong.this, "FAILED!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}