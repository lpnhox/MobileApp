package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vu.htr.cs.muzikapp.favourites.Favourite;

public class SearchResult extends AppCompatActivity {
    FirebaseUser user;

    ListView listView_result;
    ArrayList<Song> listRes;
    SongAdapter adapter;
    TextView textView_result;

    JcPlayerView jcPlayerView;
    List<JcAudio> jcAudios;

    List<String> songIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        listView_result = findViewById(R.id.listViewResult);
        textView_result = findViewById(R.id.textView_result);
        jcPlayerView = findViewById(R.id.jcplayerres);
        listRes = new ArrayList<>();
        jcAudios=new ArrayList<>();
        songIds = new ArrayList<>();

        String enter = getIntent().getStringExtra("enter");
        textView_result.setText(enter);

        user= FirebaseAuth.getInstance().getCurrentUser();

        displayRes(textView_result.getText().toString());

        listView_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jcPlayerView.playAudio(jcAudios.get(i));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification(); // default icon - Goi trinh phat thong bao tai activity nay
                adapter.notifyDataSetChanged();
            }
        });
        listView_result.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                        Toast.makeText(SearchResult.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchResult.this, "FAILED!", Toast.LENGTH_SHORT).show();

                    }
                });
                return false;
            }
        });

    }
    public void displayRes(String enter){
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Songs");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Song songObj = ds.getValue(Song.class);
                    if (songObj.getSongName().toLowerCase().contains(enter.toLowerCase())){
                        listRes.add(new Song(songObj.getSongName(),songObj.getSongUrl(),songObj.getImageUrl(),songObj.getSongArtist(),songObj.getSongDuration()));
                        jcAudios.add(JcAudio.createFromURL(songObj.getSongName(), songObj.getSongUrl()));
                    }
                }
                if (listRes.size() > 0){
                    adapter = new SongAdapter(getApplicationContext(),listRes);
                    jcPlayerView.initPlaylist(jcAudios, null);
                    listView_result.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchResult.this, "Không tìm thấy kết quả!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchResult.this, "FAILED!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}