package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import vu.htr.cs.muzikapp.favourites.Favourite;

public class FavorSong extends Fragment {

    private boolean checkPermission = false;
    FirebaseUser user;

    ListView lvFavorMusic;
    ListAdapter adapter;

    List<String> songsNameList;
    List<String> songUrlList;
    List<String> songsArtistList;
    List<String> songsDurationList;
    List<String> thumbnail;

    JcPlayerView jcPlayerView;
    List<JcAudio> jcAudios;
    List<String> userFvSID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_faver_song, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        user= FirebaseAuth.getInstance().getCurrentUser();

        lvFavorMusic = getView().findViewById(R.id.listFavorMusic);
        songsNameList = new ArrayList<>();
        songUrlList = new ArrayList<>();
        songsArtistList = new ArrayList<>();
        songsDurationList = new ArrayList<>();
        thumbnail = new ArrayList<>();

        userFvSID= new ArrayList<>();
        jcAudios=new ArrayList<>();
        jcPlayerView = getView().findViewById(R.id.jcplayerfavor);

        getUserFavoriteSongs();
        lvFavorMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jcPlayerView.playAudio(jcAudios.get(i));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification(); // default icon - Goi trinh phat thong bao tai activity nay
                adapter.notifyDataSetChanged();
            }
        });
        lvFavorMusic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference favorDbref= FirebaseDatabase.getInstance().getReference("Favourites");
                favorDbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Favourite fvObj= ds.getValue(Favourite.class);
                            Boolean myresult= fvObj.getUserEmail().equalsIgnoreCase(user.getEmail());
                            if(myresult){
                                userFvSID.add(fvObj.getSongId());
                            }
                        }

                        //logic cua minh o day
                       Query itemRemove = favorDbref.orderByChild("songId").
                                equalTo(userFvSID.get(i));
                        itemRemove.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Không thể xóa!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent reload=new Intent(getActivity(),FavorSong.class);
                getActivity().finish();
                startActivity(reload);
                return false;
            }
        });
    }
    public void getUserFavoriteSongs(){
        DatabaseReference favorDbref= FirebaseDatabase.getInstance().getReference("Favourites");
//        Favourite myFa=new Favourite(user.getEmail(),"-N0CO1GwbPvqC4rDANbG");
//        favorDbref.child(favorDbref.push().getKey()).setValue(myFa);
        favorDbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Favourite fvObj= ds.getValue(Favourite.class);
                    Boolean myresult= fvObj.getUserEmail().equalsIgnoreCase(user.getEmail());
                    if(myresult){
                        userFvSID.add(fvObj.getSongId());
                    }
                }
                retrieveSongs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.uploadItem){
            if(validatePermissions()){
                Intent intent = new Intent(getActivity(),AddMusic.class);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validatePermissions(){

        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    private void retrieveSongs(){
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("Songs");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(userFvSID.contains(ds.getKey())){
                        Song songObj = ds.getValue(Song.class);
                        songsNameList.add(songObj.getSongName());
                        songUrlList.add(songObj.getSongUrl());
                        songsArtistList.add(songObj.getSongArtist());
                        songsDurationList.add(songObj.getSongDuration());
                        thumbnail.add(songObj.getImageUrl());

                        jcAudios.add(JcAudio.createFromURL(songObj.getSongName(), songObj.getSongUrl()));
                    }
                }

                adapter = new ListAdapter(getActivity().getApplicationContext(),songsNameList,thumbnail,songsArtistList,songsDurationList);
                jcPlayerView.initPlaylist(jcAudios, null);
                lvFavorMusic.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "FAILED!", Toast.LENGTH_SHORT).show();

            }
        });
    }

}