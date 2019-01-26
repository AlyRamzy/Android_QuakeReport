package com.example.android.quakereport;

/**
 * Created by Aly on 7/1/2018.
 */

public class EarthQuake {
    private long Date;
    private String primaryLocation;
    private String offsetLocation;
    private double Mag;
    private String Url;
   public EarthQuake(long date,String location,double mag,String url)
   {
       Date=date;
       String Location=location;
       Url=url;
       int index= Location.indexOf("of");
       if(index!=-1) {
           offsetLocation = Location.substring(0, index+2);
           primaryLocation = Location.substring(index + 2, Location.length());
       }
       else {
           offsetLocation="Near the";
           primaryLocation=Location;

       }
       Mag=mag;

   }
   public long getDate() {
       return Date;
   }
   public String getPrimaryLocation() {
       return primaryLocation;
   }

    public String getOffsetLocation() {
        return offsetLocation;
    }

    public double getMag() {
        return Mag;
    }

    public String getUrl() {
        return Url;
    }
}
