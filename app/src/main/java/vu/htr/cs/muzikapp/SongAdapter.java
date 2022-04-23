package vu.htr.cs.muzikapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> listSong;
    private ArrayList<Song> listSongOld;
    Context context;

    public SongAdapter(Context context, ArrayList<Song> listSong) {
        this.listSong = listSong;
        this.listSongOld = listSong;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i).getSongName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final SongAdapter.ViewHolder viewHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.song_list_item,null);
            viewHolder= new SongAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{ viewHolder=(SongAdapter.ViewHolder) view.getTag(); }

        Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidth(2)
                .cornerRadiusDp(15).oval(false)
                .build();

        Picasso.get().load(listSong.get(i).getImageUrl()).transform(transformation).into(viewHolder.songThumbnail_list); // lam tron hinh anh cua picasso
        viewHolder.songName_list.setText(listSong.get(i).getSongName());
        viewHolder.artistName_list.setText(listSong.get(i).getSongArtist());
        viewHolder.songDuration_list.setText(listSong.get(i).getSongDuration());
        return view;

    }

    private static class ViewHolder{
        ImageView songThumbnail_list;
        CardView cardView;
        TextView songName_list, artistName_list, songDuration_list;

        public ViewHolder(View view ){
            songName_list = view.findViewById(R.id.songName_list);
            songThumbnail_list = view.findViewById(R.id.songThumbnail_list);
            artistName_list = view.findViewById(R.id.artistName_list);
            songDuration_list = view.findViewById(R.id.songDuration_list);
            cardView = view.findViewById(R.id.cardView_list);
        }
    }
}
