package com.softaholik.bdnewstoday.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.activities.NewsDetailsActivity;
import com.softaholik.bdnewstoday.activities.NewsSourceActivity;
import com.softaholik.bdnewstoday.models.News;
import com.softaholik.bdnewstoday.models.NewsList;
import com.softaholik.bdnewstoday.models.NewsViewModel;
import com.softaholik.bdnewstoday.models.db.NewsDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsList> newsLists;
    private Context mContext;
    private static String TAG = NewsAdapter.class.getSimpleName();
    private OnItemClickListener listener;


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
        final NewsDatabase newsDatabase = NewsDatabase.getInstance(mContext);
        final NewsList newsList = newsLists.get(position);
        holder.tvNewsTitle.setText(newsList.getTitle());
        holder.tvNewsDesc.setText(String.format("%s.......", newsList.getShort_description()));

        String time = newsList.getDate();
        time = time.substring(0, time.length() - 6);

        Glide.with(mContext)
                .load(newsList.getImage())
                .thumbnail(0.25f)
                .into(holder.ivNews);

        Date displayDateTime =null;
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("long", "onLongClick: pressed");
                Toast.makeText(mContext,"News has been saved"+newsList.getNews_id(),Toast.LENGTH_SHORT).show();

                return true;
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

    public interface OnItemClickListener {
        void onSaveClick(News news);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
