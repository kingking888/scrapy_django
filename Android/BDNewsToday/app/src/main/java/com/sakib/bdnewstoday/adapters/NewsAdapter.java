package com.sakib.bdnewstoday.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sakib.bdnewstoday.R;
import com.sakib.bdnewstoday.activities.NewsDetailsActivity;
import com.sakib.bdnewstoday.models.NewsList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsList> newsLists;
    private Context mContext;

    public NewsAdapter(List<NewsList> newsLists, Context mContext) {
        this.newsLists = newsLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v ;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_news_item,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        final NewsList newsList = newsLists.get(position);

        holder.tvNewsTitle.setText(newsList.getTitle());
        holder.tvNewsDesc.setText(newsList.getShort_description());

        String time = newsList.getDate();
        time = time.substring(0, time.length() - 6);
        Glide.with(mContext)
                .load(newsList.getImage())
                .thumbnail(0.25f)
                .into(holder.ivNews);

        Date displayDateTime =null;
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm aaa", Locale.getDefault());

        try {
            displayDateTime =formatter1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (displayDateTime != null) {
            holder.tvNewsTime.setText(targetFormat.format(displayDateTime));
        }

        final String finalDisplayDateTime = targetFormat.format(displayDateTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Toast.makeText(mContext,product_id,Toast.LENGTH_SHORT).show();
                Log.d("news_id", "onClick: " + newsList.getNews_id());

                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", newsList.getNews_id());
                intent.putExtra("title", newsList.getTitle());
                intent.putExtra("image", newsList.getImage());
                intent.putExtra("date", finalDisplayDateTime);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNewsTitle;
        private TextView tvNewsTime;
        private TextView tvNewsDesc;
        private ImageView ivNews;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNewsTitle = itemView.findViewById(R.id.tv_news_title);
            tvNewsTime = itemView.findViewById(R.id.tv_news_time);
            tvNewsDesc = itemView.findViewById(R.id.tv_news_desc);
            ivNews = itemView.findViewById(R.id.iv_news);


        }
    }

}
