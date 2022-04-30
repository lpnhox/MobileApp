package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
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

public class ListSong extends Fragment {
    FirebaseUser user;

    ListView listViewSong;
    ArrayList<Song> listSong;
    SongAdapter adapter;
    EditText searchView_song;
    ImageView searchSong_btn;

    JcPlayerView jcPlayerView;
    List<JcAudio> jcAudios;

    List<String> songIds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_list_song, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        searchSong_btn = getView().findViewById(R.id.searchSong_btn);
        searchView_song = getView().findViewById(R.id.searchView_song);
        listViewSong = getView().findViewById(R.id.listViewSong);
        listSong = new ArrayList<>();

        jcAudios=new ArrayList<>();
        jcPlayerView = getView().findViewById(R.id.jcplayersong);
        songIds = new ArrayList<>();

        user= FirebaseAuth.getInstance().getCurrentUser();

        displaySongs();

        searchSong_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView_song.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Vui lòng nhập tên bài hát!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent resultIntent = new Intent(getActivity(), SearchResult.class);
                    resultIntent.putExtra("enter",searchView_song.getText().toString());
                    startActivity(resultIntent);
                }

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
                        Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();

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
                adapter = new SongAdapter(getActivity().getApplicationContext(),listSong);
                jcPlayerView.initPlaylist(jcAudios, null);
                listViewSong.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();

            }
        });
    }

}