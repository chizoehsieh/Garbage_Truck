package com.example.garbagetruck;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static Context mContext;
    private ArrayList<RouteInformation> mDataSet = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView estimateTime,stationName,routeName,settingTime,situation,packingDay,recyclingDay;

        public ViewHolder(View view)
        {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            estimateTime = view.findViewById(R.id.estimateTime);
            stationName = view.findViewById(R.id.stationName);
            routeName = view.findViewById(R.id.routeName);
            settingTime = view.findViewById(R.id.settingTime);
            situation = view.findViewById(R.id.situation);
            packingDay = view.findViewById(R.id.packingDay);
            recyclingDay = view.findViewById(R.id.recyclingDay);
        }

    }

    // Constructor
    public RecyclerViewAdapter(Context context, ArrayList<RouteInformation> mDataSet)
    {
        this.mContext = context;
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_custom_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        // 指派ViewHolder物件，重複使用，動態載入電影名稱(TextView)及圖片Resource ID(ImageView)
        // TO DO
        viewHolder.itemView.setTag("" + position);
        RouteInformation ri = mDataSet.get(position);
        viewHolder.estimateTime.setText(ri.getEstimateTime());
        viewHolder.stationName.setText(ri.getStationName());
        viewHolder.routeName.setText(ri.getRouteName());

//        viewHolder.situation.setText(ri.getSituation());
        viewHolder.settingTime.setText("表定"+ri.getSettingTime());
        viewHolder.packingDay.setText(ri.getPackingDay());
        viewHolder.recyclingDay.setText(ri.getRecyclingDay());
        if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4 ||Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1){
            viewHolder.situation.setText("|本日無清運");
            viewHolder.estimateTime.setText("本日無清運");
            viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#ADADAD"));
            viewHolder.estimateTime.setTextColor(Color.parseColor("#3C3C3C"));
        }
        else{
            if(ri.getEstimateTime() == null){

                viewHolder.situation.setText("|本日已清運");
                viewHolder.estimateTime.setText("本日已清運");
                viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#ADADAD"));
                viewHolder.estimateTime.setTextColor(Color.parseColor("#3C3C3C"));
            }
            else if(ri.getSituationPosition().equals("三民西及左營區隊停車場")){
                viewHolder.situation.setText("|尚未發車");
                viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#ADADAD"));
                viewHolder.estimateTime.setTextColor(Color.parseColor("#3C3C3C"));
            }
            else{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                viewHolder.situation.setText(ri.getSituationPosition());
                try {
//                    Date currentTime = Calendar.getInstance().getTime();
//                    Log.d("TimeTest","currentTime");
//                    String today = String.valueOf(currentTime.getDay());
//                    Log.d("today",today);
//                    String time = today+;
                    String current = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                    Date d = simpleDateFormat.parse(ri.getEstimateTime());
                    Date currentTime = simpleDateFormat.parse(current);
                    Log.d("TimeTest","parseTime");
//                    Long duration = Duration.between(currentTime.toInstant(), d.toInstant()).getSeconds();
//                    Log.d("duration",duration.toString());
                    Long difference = currentTime.getTime() - d.getTime();
                    int days,hours,min;
                    days = (int) (difference / (1000*60*60*24));
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    Log.d("difference", String.valueOf(currentTime.getTime()));
                    Log.d("difference",String.valueOf(d.getTime()));
                    Log.d("durationTest-min", String.valueOf(min));
                    Log.d("durationTest-hours",String.valueOf(hours));
                    Log.d("durationTest-days",String.valueOf(days));
                    if(days >= 0 && hours >= 0 && min > 0){
                        viewHolder.estimateTime.setText("本日已清運");
                        viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#ADADAD"));
                        viewHolder.estimateTime.setTextColor(Color.parseColor("#3C3C3C"));
                    }
                    else if(hours == 0 && min > -3){
                        viewHolder.estimateTime.setText("即將抵達");
                        viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#FF2D2D"));
                        viewHolder.estimateTime.setTextColor(Color.parseColor("#F0F0F0"));
                    }
                    else {
//                        viewHolder.situation.setText("尚未清運");
                        viewHolder.estimateTime.setBackgroundColor(Color.parseColor("#6C6C6C"));
                        viewHolder.estimateTime.setTextColor(Color.parseColor("#F0F0F0"));
                    }
                } catch (ParseException ex) {
                    Log.d("Timetest",ex.getMessage());
                }
            }
        }




    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }
}