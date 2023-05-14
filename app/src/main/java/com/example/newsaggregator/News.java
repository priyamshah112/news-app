package com.example.newsaggregator;

import java.util.List;

public class News {
    private List<ArticleDataModel> sources;
    private String status;

    public News(String status, List<ArticleDataModel> sources) {
        this.status = status;
        this.sources = sources;
    }

    public List<ArticleDataModel> getSources() {
        return sources;
    }

    public void setSources(List<ArticleDataModel> sources) {
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

