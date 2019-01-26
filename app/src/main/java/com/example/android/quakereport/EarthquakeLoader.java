package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {
    private static String LOG_TAG=EarthquakeLoader.class.getName();
    private String mURL;

    public EarthquakeLoader(Context context,String url){
        super(context);
        mURL=url;

    }

    @Override
    public List<EarthQuake> loadInBackground() {
        Log.i(LOG_TAG,"TEST Load in Background");
        if(mURL==null)
            return null;
        return QueryUtils.FetchEarthQuakeData(mURL);
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"TEST onStartLoading  ");
        forceLoad();
    }
}
