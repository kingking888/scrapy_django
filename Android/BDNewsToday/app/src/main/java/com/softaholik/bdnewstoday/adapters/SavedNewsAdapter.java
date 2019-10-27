package com.softaholik.bdnewstoday.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.models.News;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.NewsHolder> {
    private List<News> news = new ArrayList<>();

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
