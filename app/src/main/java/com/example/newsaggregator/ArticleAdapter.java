package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import com.example.newsaggregator.databinding.NewsArticleBinding;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private NewsArticleBinding binding;
    private final MainActivity mainActivity;
    private final List<ArticleDetailDataModel> articleDetailDataModelList;

    public ArticleAdapter(MainActivity mainActivity, List<ArticleDetailDataModel> articleDetailDataModelList) {
        this.mainActivity = mainActivity;
        this.articleDetailDataModelList = articleDetailDataModelList;
    }

    @Override
    public int getItemCount() {
        return articleDetailDataModelList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleDetailDataModel articleDetailDataModel = articleDetailDataModelList.get(position);

        if (articleDetailDataModel.getPublishedAt() != null) {
            String articleDate = getArticleDate(articleDetailDataModel.getPublishedAt());
            holder.articleDate.setText(articleDate);
        }

        if (articleDetailDataModel.getTitle() != null) {
            holder.articleHeadline.setText(articleDetailDataModel.getTitle());

            holder.articleHeadline.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleDetailDataModel.getUrl()));
                mainActivity.startActivity(intent);
            });
        }


        if (articleDetailDataModel.getUrl() != null) {
            Picasso picasso = Picasso.with(mainActivity);
            picasso.setLoggingEnabled(true);
            picasso.load(articleDetailDataModel.getUrlToImage())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.loading)
                    .into(holder.articleImage);

            holder.articleImage.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleDetailDataModel.getUrl()));
                mainActivity.startActivity(intent);
            });
        } else {
            holder.articleImage.setImageResource(R.drawable.noimage);
        }

        if (articleDetailDataModel.getAuthor() == null || articleDetailDataModel.getAuthor().equals("null")) {
            holder.articleAuthors.setVisibility(View.GONE);
        } else {
            holder.articleAuthors.setText(articleDetailDataModel.getAuthor());
        }

        if (articleDetailDataModel.getDescription() == null || articleDetailDataModel.getDescription().equals("null")) {
            holder.articleDesc.setVisibility(View.GONE);
        } else {
            holder.articleDesc.setText(articleDetailDataModel.getDescription());

            holder.articleDesc.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleDetailDataModel.getUrl()));
                mainActivity.startActivity(intent);
            });
        }

        holder.articleCount.setText(String.format("%d of %d", position + 1, articleDetailDataModelList.size()));
    }

//    @NonNull
//    @Override
//    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext())
////                .inflate(R.layout.news_article, parent, false);
////        return new ArticleViewHolder(view);
//        binding = NewsArticleBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//
//        // Find the views using the binding object
//        ArticleViewHolder viewHolder = new ArticleViewHolder(binding);
//    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsArticleBinding binding = NewsArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ArticleViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getArticleDate(String publishedAt) {
        String articleDate = "";

        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_INSTANT;
            TemporalAccessor accessor = timeFormatter.parse(publishedAt);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.from(accessor), ZoneId.systemDefault());
            articleDate = localDateTime.format(dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (articleDate.isEmpty()) {
            try {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                TemporalAccessor accessor = timeFormatter.parse(publishedAt);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.from(accessor), ZoneId.systemDefault());
                articleDate = localDateTime.format(dateTimeFormatter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return articleDate;
    }


}
