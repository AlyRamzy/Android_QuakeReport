package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static  String LOG_TAG=QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static List<EarthQuake> FetchEarthQuakeData(String requesturl){

        URL url= createUrl(requesturl);
        String JsonResponse=null;
        try{
             JsonResponse=makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<EarthQuake> earthQuakes= extractFeaturesFromJson(JsonResponse);
        return earthQuakes;

    }
    private  static URL createUrl(String stringurl)
    {
        URL url=null;
        try
        {
            url = new URL(stringurl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Proplem Building the URL", e);
        }
        return url;


    }
    private static String makeHttpRequest(URL url) throws IOException
    {
        String JSONResponse="";
        if(url==null)
            return JSONResponse;
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code : " + urlConnection.getResponseCode());
            }
        }
            catch(IOException e)
            {
                Log.e(LOG_TAG,"Proplem Retriving EarthQuake JSON Response ",e);
            }
            finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            if(inputStream!=null)
                inputStream.close();

        }
        return JSONResponse;



        }




    private static String readFromStream(InputStream inputStream)throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader= new BufferedReader(inputStreamReader);
            String Line=reader.readLine();
            while(Line!=null)
            {
                output.append(Line);
                Line=reader.readLine();
            }



        }
        return output.toString();
    }
    private static List<EarthQuake> extractFeaturesFromJson(String earhquakeJson){
        if(TextUtils.isEmpty(earhquakeJson))
            return null;
        List<EarthQuake> earthQuakes=new ArrayList<EarthQuake>();
        try
        {
            JSONObject baseJsonResponse = new JSONObject(earhquakeJson);
            JSONArray earthquakeArray= baseJsonResponse.getJSONArray("features");
            for(int i=0;i<earthquakeArray.length();i++)
            {
                JSONObject EarthQuake=earthquakeArray.getJSONObject(i);
                JSONObject current=EarthQuake.getJSONObject("properties");
                long date=current.getLong("time");
                String location= current.getString("place");
                double mag=current.getDouble("mag");
                String url=current.getString("url");
                earthQuakes.add(new EarthQuake(date,location,mag,url));
            }

        }
        catch (JSONException j)
        {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", j);

        }
        return earthQuakes;
    }


}