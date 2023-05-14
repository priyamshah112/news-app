package com.example.newsaggregator;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.newsaggregator.databinding.ActivityMainBinding;
import com.example.newsaggregator.databinding.DrawerItemsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    News news;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerToggle;
    List<ArticleDataModel> articleDataModelList;
    List<ArticleDataModel> tempList;
    private ArrayAdapter<ArticleDataModel> arrayAdapter;
    private ViewPager2 viewPager;
    TextView textView;
    private Menu menu;
    private static List<ArticleDetailDataModel> articleDetails = new ArrayList<>();
    ArticleAdapter articlesAdapter;
    HashMap<String, String> color ;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(Html.fromHtml("<small><font color=\"white\">" + getString(R.string.app_name)+"</small>"));

        initializeViews();
        setupNavigationDrawer();

        if (isNetworkAvailable()) {
            getNewsData();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void setupNavigationDrawer() {
        listView.setOnItemClickListener((parent, view, position, id) -> categorySelected(position));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer_text, R.string.close_drawer_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void getNewsData() {
        news = NewsAPI.getAPIData(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getTitle().equals("all")){
            updateDrawerItems(articleDataModelList, item.getTitle().toString());
        }
        else if(item.getTitle() != null){
            List<ArticleDataModel> updateDrawerItems =  NewsAPI.updateDrawerLayoutData(item.getTitle().toString());
            updateDrawerItems(updateDrawerItems, item.getTitle().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    private void categorySelected(int position) {
        //setTitle(tempList.get(position).getName());
        getSupportActionBar().setTitle(Html.fromHtml("<small><font color=\"white\">" +tempList.get(position).getName()+"</small>"));
        articleDetails = NewsAPI.getArticles(tempList.get(position).getId(), this);
        viewPager.setCurrentItem(position);

        drawerLayout.setBackgroundResource(0);
        viewPager.setBackground(null);
        drawerLayout.closeDrawer(listView);

    }

    private void updateDrawerItems(List<ArticleDataModel> updateDrawerItems, String title) {
        tempList = updateDrawerItems;
        arrayAdapter = new ArrayAdapter<ArticleDataModel>(this, R.layout.drawer_items, updateDrawerItems){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ArticleDataModel newsSource = updateDrawerItems.get(position);
                DrawerItemsBinding listItemBinding = DrawerItemsBinding.inflate(getLayoutInflater(), parent, false);
                textView = listItemBinding.textView;
                textView.setText(newsSource.getName());
                Log.d(TAG, "getView: title " + title + updateDrawerItems.size());
                if(title.equals("all")){
                    Log.d(TAG, "getView: In here");
                    //setTitle("News Gateway ("+updateDrawerItems.size()+")");
                    textView.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                    getSupportActionBar().setTitle(Html.fromHtml("<small><font color=\"white\">" +"News Gateway ("+updateDrawerItems.size()+")" +"</small>"));
                } else {
                    Log.d(TAG, "getView: else");
                    Log.d(TAG, "getView: "+ updateDrawerItems.size() + updateDrawerItems.get(0).getCategory());
                    getSupportActionBar().setTitle(Html.fromHtml("<small><font color=\"white\">" +updateDrawerItems.get(0).getCategory()+" ("+updateDrawerItems.size()+")" +"</small>"));
                    //setTitle(updateDrawerItems.get(0).getCategory()+" ("+updateDrawerItems.size()+")");
                }
                textView.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                return listItemBinding.getRoot();
            }
        };
        listView.setAdapter(arrayAdapter);
    }



    private boolean isNetworkAvailable() {
        if(NewsHelper.isNetworkConnected(this)){
            return true;
        }
        return false;
    }

    private void initializeViews() {
        viewPager = binding.viewPager;
        ArticleAdapter articlesAdapter = new ArticleAdapter(this, articleDetails);
        drawerLayout = binding.drawerLayout;
        listView = binding.leftDrawer;
    }


    public void updateData(News news) {
        this.news = news;
        articleDataModelList = news.getSources();
        tempList = articleDataModelList;

        arrayAdapter = new ArrayAdapter<ArticleDataModel>(this, android.R.layout.simple_list_item_1, this.articleDataModelList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ArticleDataModel newsSource = articleDataModelList.get(position);
                DrawerItemsBinding listItemBinding = DrawerItemsBinding.inflate(getLayoutInflater(), parent, false);
                textView = listItemBinding.textView;
                textView.setText(newsSource.getName());
                //setTitle("News Gateway ("+ articleDataModelList.size()+")");
                getSupportActionBar().setTitle(Html.fromHtml("<small><font color=\"white\">" +"News Gateway ("+articleDataModelList.size()+")" +"</small>"));
                textView.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                return listItemBinding.getRoot();
            }
        };
        listView.setAdapter(arrayAdapter);
    }

    public void updateMenuItem(HashSet<String> menuCategorySet) {
        // Updating Menu items
        color = NewsHelper.textColor(menuCategorySet);
        for (String menu: menuCategorySet) {
            if(!menu.equals("all")){
                SpannableString s = new SpannableString(menu);
                s.setSpan(new ForegroundColorSpan(Color.parseColor(color.get(menu))), 0, s.length(), 0);
                this.menu.add(s);
            } else {
                this.menu.add(menu);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }

    public void updateViewPager(List<ArticleDetailDataModel> articleDetails) {
        articlesAdapter = new ArticleAdapter(this, articleDetails);
        viewPager.setAdapter(articlesAdapter);
    }
}
