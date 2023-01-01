package com.example.garbagetruck;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager manager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        NotificationChannel channel = null;
        channel = new NotificationChannel("default","Notification", NotificationManager.IMPORTANCE_DEFAULT);
        getManager().createNotificationChannel(channel);
    }

    public Notification getNotification2(String nickName)
    {
        Intent intent = new Intent(this, MsgHandler.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // TO DO
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_NOTIFICATION_MSG",nickName);
        intent.putExtras(bundle);

        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TO DO

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification);
        SharedPreferences sharedPreferences = getSharedPreferences("notify",Context.MODE_PRIVATE);
        expandedView.setTextViewText(R.id.content_text,"垃圾車已抵達您指定的地點「"+sharedPreferences.getString("nickName","")+"」");
        // TO DO

//        Intent leftIntent = new Intent(this, NotificationIntentService.class);
//        // TO DO
//        leftIntent.setAction(Constants.EXTRA_REMOTE_LEFT_BUTTON);
//        leftIntent.putExtras(bundle);
//        PendingIntent pendingIntent1 = PendingIntent.getService(this,100,leftIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        expandedView.setOnClickPendingIntent(R.id.notification_collapsed_left_button,pendingIntent1);
//        Intent rightIntent = new Intent(this, NotificationIntentService.class);
//        // TO DO
//        rightIntent.setAction(Constants.EXTRA_REMOTE_RIGHT_BUTTON);
//        bundle.putString(Constants.EXTRA_NOTIFICATION_URI,strUri);
//        rightIntent.putExtras(bundle);
//        PendingIntent pendingIntent2 = PendingIntent.getService(this,200,rightIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        expandedView.setOnClickPendingIntent(R.id.notification_collapsed_right_button,pendingIntent2);
//        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
//        // TO DO

//        collapsedView.setTextViewText(R.id.timestamp,curTime);
//        Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.position);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(),"default")
                .setSmallIcon(R.drawable.garbagetruck)
                .setContentTitle("垃圾車快到囉!")
                .setContentText("垃圾車已抵達您指定的地點「"+sharedPreferences.getString("nickName","")+"」")
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingintent)
                .setAutoCancel(true)
                .build();
        return notification;
    }

    public void notify(int id, Notification notification)
    {
        getManager().notify(id, notification);
    }



    private NotificationManager getManager()
    {
        if (manager == null)
        {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
