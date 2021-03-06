package com.example.progettoapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.progettoapp.YoutubeVideo;
import com.example.progettoapp.model.Item;
import com.example.progettoapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoDetailsAdapter extends RecyclerView.Adapter<VideoDetailsAdapter.VideoDetailsViewHolder> {
    private  Context context;
    private  List<Item> videoDetailsList;
    public void setVideoDetailsList(List<Item> videoDetailsList) {
        this.videoDetailsList = videoDetailsList;
    }


    private  String converted_date;

    public VideoDetailsAdapter(Context context, List<Item> videoDetailsList) {
        this.context = context;
        this.videoDetailsList= videoDetailsList;
    }

    @NonNull
    @Override
    public VideoDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        return new VideoDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDetailsViewHolder holder, int position){
        // setUpDateTime();
        holder.publishedAt.setText(videoDetailsList.get(position).getId().getVideoId());
        holder.description.setText(videoDetailsList.get(position).getSnippet().getDescription());
        holder.title.setText(videoDetailsList.get(position).getSnippet().getTitle());

        Glide.with(context).load(videoDetailsList
                .get(position)
                .getSnippet()
                .getThumbnails()
                .getMedium()
                .getUrl()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, YoutubeVideo.class);
                intent.putExtra("videoID", videoDetailsList.get(position).getId().getVideoId());
                context.startActivity(intent);
                Log.println(Log.INFO,"cliccato","Video Id="+videoDetailsList.get(position).getId().getVideoId());
            }
        });
    }

    private String setUpDateTime(String publishedAt) {

        try{
            DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSSX", Locale.getDefault());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Date date= dateFormat.parse(publishedAt);
            converted_date = format.format(date);
        }catch (ParseException exception){
            Toast.makeText(context,exception.getMessage(),Toast.LENGTH_LONG).show();
        }
        return  converted_date;
    }

    @Override
    public int getItemCount() {
        return videoDetailsList.size();
    }

    public static class VideoDetailsViewHolder extends RecyclerView.ViewHolder{
        private final TextView publishedAt;
        private final TextView title;
        private final TextView description;
        private TextView videoID;
        private final CircleImageView thumbnail;

        public VideoDetailsViewHolder(View itemView){
            super(itemView);
            publishedAt = itemView.findViewById(R.id.publishedAt);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

}