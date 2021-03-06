package com.example.progettoapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.progettoapp.R
import com.example.progettoapp.YoutubeVideo
import com.example.progettoapp.adapter.VideoDetailsAdapter.VideoDetailsViewHolder
import com.example.progettoapp.model.Item
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class VideoDetailsAdapter(private val context: Context, private var videoDetailsList: List<Item>) : RecyclerView.Adapter<VideoDetailsViewHolder>() {
    fun setVideoDetailsList(videoDetailsList: List<Item>) {
        this.videoDetailsList = videoDetailsList
    }

    private var converted_date: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoDetailsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return VideoDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoDetailsViewHolder, position: Int) {
        // setUpDateTime();
        holder.publishedAt.text = videoDetailsList[position].id?.videoId
        holder.description.text = videoDetailsList[position].snippet?.description
        holder.title.text = (videoDetailsList[position].snippet?.title ?: Glide.with(context).load(videoDetailsList[position]
                .snippet
                ?.thumbnails
                ?.medium
                ?.url).into(holder.thumbnail)) as CharSequence?
        holder.itemView.setOnClickListener {
            val intent = Intent(context, YoutubeVideo::class.java)
            intent.putExtra("videoID", videoDetailsList[position].id?.videoId)
            context.startActivity(intent)
            Log.println(Log.INFO, "cliccato", "Video Id=" + (videoDetailsList[position].id?.videoId
                    ))
        }
    }

    private fun setUpDateTime(publishedAt: String): String? {
        try {
            val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSSX", Locale.getDefault())
            @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            val date = dateFormat.parse(publishedAt)
            converted_date = format.format(date)
        } catch (exception: ParseException) {
            Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
        }
        return converted_date
    }

    override fun getItemCount(): Int {
        return videoDetailsList.size
    }

    class VideoDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val publishedAt: TextView
        val title: TextView
        val description: TextView
        private val videoID: TextView? = null
        val thumbnail: CircleImageView

        init {
            publishedAt = itemView.findViewById(R.id.publishedAt)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            thumbnail = itemView.findViewById(R.id.thumbnail)
        }
    }
}