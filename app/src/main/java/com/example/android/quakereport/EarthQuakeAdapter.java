package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {
    public EarthQuakeAdapter(@NonNull Context context,  @NonNull List<EarthQuake> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.listview_item,parent,false);

        }
        EarthQuake Current=getItem(position);
        TextView Mag =(TextView) view.findViewById(R.id.mag);
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        String mag=decimalFormat.format(Current.getMag());
        Mag.setText(mag);
        int color= getMagColor(Current.getMag());
        GradientDrawable gradientDrawable= (GradientDrawable) Mag.getBackground();
        gradientDrawable.setColor(color);

        TextView primLoc =(TextView) view.findViewById(R.id.primlocation);
        primLoc.setText(Current.getPrimaryLocation());
        TextView offLoc=(TextView) view.findViewById(R.id.offsetlocation);
        offLoc.setText(Current.getOffsetLocation());
        TextView Date =(TextView) view.findViewById(R.id.date);

        java.util.Date date=new Date(Current.getDate());
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd ,yyyy");
        String toDisplay= dateFormat.format(date);

        Date.setText(toDisplay);
        TextView hours=(TextView) view.findViewById(R.id.hours);
        SimpleDateFormat hm= new SimpleDateFormat("h:mm a");
        String hhmm= hm.format(date);
        hours.setText(hhmm);

        return view;
    }
    private int getMagColor(double mag)
    {
        int magnitude= (int) mag;
        int colorId;
        switch(magnitude){
            case 0:
            case 1:
                colorId=R.color.magnitude1;
                break;
            case 2:
                colorId=R.color.magnitude2;
                break;
            case 3:
                colorId=R.color.magnitude3;
                break;
            case 4:
                colorId=R.color.magnitude4;
                break;
            case 5:
                colorId=R.color.magnitude5;
                break;
            case 6:
                colorId=R.color.magnitude6;
                break;
            case 7:
                colorId=R.color.magnitude7;
                break;
            case 8:
                colorId=R.color.magnitude8;
                break;
            case 9:
                colorId=R.color.magnitude9;
                break;
            default:
                colorId=R.color.magnitude10plus;
                break;




        }
        return ContextCompat.getColor(getContext(),colorId);

    }
}
