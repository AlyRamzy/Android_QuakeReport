/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private EarthQuakeAdapter mAdapter;
    private static final int ID=1;
    private TextView EmptyTextView;
    private ProgressBar progressBar;
    private LoaderManager loaderManager;
    private SwipeRefreshLayout refreshLayout;
    private NetworkInfo networkInfo;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        EmptyTextView=(TextView) findViewById(R.id.Empty_Text);
        progressBar=(ProgressBar)findViewById(R.id.Progress_Bar);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);
        final ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE) ;
        networkInfo= connectivityManager.getActiveNetworkInfo();




         loaderManager = getLoaderManager();
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthQuake>());
        earthquakeListView.setEmptyView(EmptyTextView);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkInfo= connectivityManager.getActiveNetworkInfo();
                if((mAdapter==null||mAdapter.isEmpty())&&networkInfo!=null&&networkInfo.isConnected()) {
                    loaderManager.destroyLoader(ID);
                }
                loaderManager.initLoader(ID,null,EarthquakeActivity.this);
                refreshLayout.setRefreshing(false);
            }
        });


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake current = mAdapter.getItem(position);

                Uri earthQuakUri = Uri.parse(current.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, earthQuakUri);
                startActivity(webIntent);

            }
        });

        if(networkInfo!=null&&networkInfo.isConnected()) {
            loaderManager.initLoader(ID, null, this);
        }
        else{
            progressBar.setVisibility(View.GONE);
            EmptyTextView.setText(R.string.no_Internet);
        }

    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"TEST CREATE LOADER");
        return new EarthquakeLoader(this,USGS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        Log.i(LOG_TAG,"TEST onLoadFinish");
        if(networkInfo!=null&&networkInfo.isConnected()) {
            EmptyTextView.setText(R.string.no_earthquake);
        }
        else {
            EmptyTextView.setText(R.string.no_Internet);
        }
        progressBar.setVisibility(View.GONE);
        if(data!=null&&!data.isEmpty())
        {
            mAdapter.clear();
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {

        mAdapter.clear();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
