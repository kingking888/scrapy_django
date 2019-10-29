package com.softaholik.bdnewstoday.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.models.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.NewsHolder> {
    private List<News> news = new ArrayList<>();
    private Context mContext;

    public SavedNewsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_news_item, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = this.news.get(position);
        holder.tvNewsTitle.setText(news.getTitle());
        holder.tvNewsDesc.setText(news.getDescription());

        String time = news.getDate();
        time = time.substring(0, time.length() - 6);

        Glide.with(mContext)
                .load(news.getImage())
                .thumbnail(0.25f)
                .into(holder.ivNews);

        Date displayDateTime =null;
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());

        try {
            displayDateTime =formatter1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (displayDateTime != null) {
            holder.tvNewsTime.setText(targetFormat.format(displayDateTime));
        }

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        private TextView tvNewsTitle;
        private TextView tvNewsTime;
        private TextView tvNewsDesc;
        private ImageView ivNews;

        public NewsHolder(View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_news_title);
            tvNewsTime = itemView.findViewById(R.id.tv_news_time);
            tvNewsDesc = itemView.findViewById(R.id.tv_news_desc);
            ivNews = itemView.findViewById(R.id.iv_news);

        }
    }
}
