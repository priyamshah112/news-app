package com.example.newsaggregator;

import android.net.Uri;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class NewsAPI {
    private static MainActivity mainActivity;
    //News URL
    private static final String News_Source_URL ="https://newsapi.org/v2/sources?";
    private static final String APIKEY = "5cb977b21ab444b195fcca2d4de01676";
    private static HashMap<String, List<String>> menuDetailsWithCategory = new HashMap<>();

    private static RequestQueue queryQueue, queryQueue1;
    private static News newsArticles;
    private static final String News_Headline_URL = "https://newsapi.org/v2/top-headlines";
    private static List<ArticleDetailDataModel> detailsArticle;

    private static ArrayList<String> menuItems;
    private static HashSet<String> menuCategorySet = new HashSet<>();
    private static List<ArticleDataModel> sources;
    private static HashMap<String, String> colorHashMap = new HashMap<>();


    public static News getAPIData(MainActivity mainActivityIn){
        mainActivity = mainActivityIn;
        queryQueue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getURL(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsArticles = parseJson(response);
                        mainActivityIn.updateData(newsArticles);

                        menuItems = new ArrayList<>();
                        menuCategorySet.add("all");
                        sources = newsArticles.getSources();
                        for (ArticleDataModel source: sources){
                            menuCategorySet.add(source.getCategory());
                        }

                        mainActivityIn.updateMenuItem(menuCategorySet);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivityIn, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queryQueue.add(jsonObjectRequest);
        return newsArticles;
    }

    private static News parseJson(JSONObject response) {
        try{
            List<ArticleDataModel> sources = new ArrayList<>();
            // Status
            String status  = response.getString("status");

            if(response.getJSONArray("sources") != null){
                JSONArray sourcesArray = response.getJSONArray("sources");
                for (int i = 0; i < sourcesArray.length(); i++) {
                    JSONObject subSourceItem = sourcesArray.getJSONObject(i);
                    String id = subSourceItem.getString("id");
                    String name = subSourceItem.getString("name");
                    String category = subSourceItem.getString("category");
                    sources.add(new ArticleDataModel(id, name, category));
                }
            }
            newsArticles = new News(status, sources);
        } catch (Exception e){
            e.printStackTrace();
        }
        return newsArticles;
    }

    public static List<ArticleDataModel> updateDrawerLayoutData(String title) {
        List<ArticleDataModel> result = new ArrayList<>();
        for (int i =0 ; i < sources.size(); i++){
            if(sources.get(i).getCategory().equalsIgnoreCase(title)){
                result.add(sources.get(i));
            }
        }
        return result;
    }

    public static List<ArticleDetailDataModel> getArticles(String source, MainActivity mainActivityIn){
        mainActivity = mainActivityIn;
        queryQueue1 = Volley.newRequestQueue(mainActivity);
        Uri.Builder buildURL = Uri.parse(News_Headline_URL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", APIKEY);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, buildURL.build().toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(mainActivityIn, response.toString(), Toast.LENGTH_LONG).show();
                        detailsArticle = parseArticleDetails(response);
                        mainActivityIn.updateViewPager(detailsArticle);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivityIn, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queryQueue1.add(jsonObjectRequest);
        return detailsArticle;
    }

    private static String getURL() {
        Uri.Builder buildURL = Uri.parse(News_Source_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", APIKEY);
        return buildURL.build().toString();
    }

    private static List<ArticleDetailDataModel> parseArticleDetails(JSONObject response) {
        List<ArticleDetailDataModel> result = new ArrayList<>();
        try{
            JSONArray articlesJsonArray = response.getJSONArray("articles");
            for(int i = 0; i < articlesJsonArray.length(); i++){
                ArticleDetailDataModel articleDetailDataModel = new ArticleDetailDataModel();
                JSONObject jsonItem = articlesJsonArray.getJSONObject(i);
                String publishedAt = jsonItem.getString("publishedAt");
                articleDetailDataModel.setPublishedAt(publishedAt);
                String title = jsonItem.getString("title").toString();
                articleDetailDataModel.setTitle(title);
                String description = jsonItem.getString("description");
                articleDetailDataModel.setDescription(description);
                String urlToImage = jsonItem.getString("urlToImage").toString();
                articleDetailDataModel.setUrlToImage(urlToImage);
                String author = jsonItem.getString("author").toString();
                articleDetailDataModel.setAuthor(author);
                String url = jsonItem.getString("url");
                articleDetailDataModel.setUrl(url);
                result.add(articleDetailDataModel);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}

