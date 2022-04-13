package vu.htr.cs.muzikapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    List<String> songNames;
    List<String> songArtist;
    List<String> songDuration;
    List<String> thumbnails;
    Context context;

    public ListAdapter(Context context, List<String> songNames, List<String> thumbnails, List<String> songArtist, List<String> songDuration) {
        this.songNames = songNames;
        this.thumbnails = thumbnails;
        this.songArtist = songArtist;
        this.songDuration = songDuration;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songNames.size();
    }

    @Override
    public Object getItem(int i) {
        return songNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.song_favoritem,null);
            viewHolder= new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{ viewHolder=(ViewHolder) view.getTag(); }

        Transformation transformation = new RoundedTransformationBuilder().borderColor(Color.WHITE).borderWidth(2)
                .cornerRadiusDp(15).oval(false)
                .build();

        Picasso.get().load(thumbnails.get(i)).transform(transformation).into(viewHolder.thumbnail); // lam tron hinh anh cua picasso
        viewHolder.songName.setText(songNames.get(i));
        viewHolder.artistName.setText(songArtist.get(i));
        viewHolder.songDuration.setText(songDuration.get(i));
        return view;

    }
    private static class ViewHolder{
        TextView songName,artistName,songDuration;
        CardView cardView;
        ImageView thumbnail,currentlyPlaying;

        public ViewHolder(View view ){
            songName = view.findViewById(R.id.songName);
            thumbnail = view.findViewById(R.id.songThumbnail);
            artistName = view.findViewById(R.id.artistName);
            songDuration = view.findViewById(R.id.songDuration);
            cardView = view.findViewById(R.id.cardView);
            currentlyPlaying = view.findViewById(R.id.currentlyPlaying);
        }

    }
}
