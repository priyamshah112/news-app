package com.example.newsaggregator;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.R;
import com.example.newsaggregator.databinding.NewsArticleBinding;
public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private NewsArticleBinding binding;
    protected final TextView articleHeadline;
    protected final TextView articleDate;
    protected final TextView articleCount;
    protected final ImageView articleImage;
    protected final TextView articleAuthors;
    protected final TextView articleDesc;

    public ArticleViewHolder(NewsArticleBinding binding) {
        super(binding.getRoot());

        articleHeadline = binding.articleHeadline;
        articleDate = binding.articleDate;
        articleAuthors = binding.articleAuthors;
        articleDesc = binding.articleDesc;
        articleCount = binding.articleCount;
        articleImage = binding.articleImage;

        articleDesc.setMovementMethod(new ScrollingMovementMethod());
    }

    public TextView getArticleHeadline() {
        return articleHeadline;
    }

    public TextView getArticleDate() {
        return articleDate;
    }

    public TextView getArticleAuthors() {
        return articleAuthors;
    }

    public TextView getArticleDesc() {
        return articleDesc;
    }

    public TextView getArticleCount() {
        return articleCount;
    }

    public ImageView getArticleImage() {
        return articleImage;
    }
}

