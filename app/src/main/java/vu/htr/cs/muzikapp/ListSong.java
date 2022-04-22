package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FilterReader;
import java.util.ArrayList;
import java.util.List;

import vu.htr.cs.muzikapp.favourites.Favourite;

public class ListSong extends AppCompatActivity {
    FirebaseUser user;

    ListView listViewSong;
    ArrayList<Song> listSong;
    SongAdapter adapter;
    SearchView searchView_song;
    List<String> songsNameList;
    List<String> songUrlList;
    List<String> songsArtistList;
    List<String> songsDurationList;
    List<String> thumbnail;

    JcPlayerView jcPlayerView;
    List<JcAudio> jcAudios;

    List<String> songIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        listViewSong = findViewById(R.id.listViewSong);
        listSong = new ArrayList<>();

        songsNameList = new ArrayList<>();
        songUrlList = new ArrayList<>();
        songsArtistList = new ArrayList<>();
        songsDurationList = new ArrayList<>();
        thumbnail = new ArrayList<>();

        jcAudios=new ArrayList<>();
        jcPlayerView = findViewById(R.id.jcplayersong);
        songIds = new ArrayList<>();

        user= FirebaseAuth.getInstance().getCurrentUser();

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

        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jcPlayerView.playAudio(jcAudios.get(i));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification(); // default icon - Goi trinh phat thong bao tai activity nay
                adapter.notifyDataSetChanged();
            }
        });
        listViewSong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference favorDbref= FirebaseDatabase.getInstance().getReference("Favourites");
                DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Songs");
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            songIds.add(ds.getKey());
                        }
                        String id = favorDbref.push().getKey();
                        Favourite fv=new Favourite(user.getEmail(), songIds.get(i));
                        favorDbref.child(id).setValue(fv);
                        Toast.makeText(ListSong.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListSong.this, "FAILED!", Toast.LENGTH_SHORT).show();

                    }
                });
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
                    jcAudios.add(JcAudio.createFromURL(songObj.getSongName(), songObj.getSongUrl()));
                }

                adapter = new SongAdapter(getApplicationContext(),listSong);
                jcPlayerView.initPlaylist(jcAudios, null);
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