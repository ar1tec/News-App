package com.example.mansi.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String NEWS_BASE_URL = "https://content.guardianapis.com/search?";
    private NewsAdapter adapter;
    private ProgressBar loading_spinner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading_spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        textView = (TextView) findViewById(R.id.error_msg);

        if (!isInternetConnected()) {
            loading_spinner.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.no_internet_connection));
            return;
        }

        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News clickedNews = adapter.getItem(position);

                Uri uri = Uri.parse(clickedNews.getWebUrl());
                Intent openWebIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openWebIntent);
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelectedId = item.getItemId();
        if (menuItemSelectedId == R.id.settings_icon) {
            Intent openSettingsActivityIntent = new Intent(this, settings_activity.class);
            startActivity(openSettingsActivityIntent);
        }
        return true;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPreferences.getString(getString(R.string.list_preference_key), getString(R.string.pref_sort_default_value));
        String querySearch = sharedPreferences.getString(getString(R.string.edit_preference_key), getString(R.string.pref_edit_default_value));

        Uri baseUri = Uri.parse(NEWS_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("q", querySearch);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(getBaseContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        loading_spinner.setVisibility(View.GONE);
        if (newses != null && !newses.isEmpty()) {
            adapter.clear();
            adapter.addAll(newses);
        } else {
            textView.setVisibility(View.VISIBLE);

            textView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter = new NewsAdapter(this, new ArrayList<News>());
    }
}
