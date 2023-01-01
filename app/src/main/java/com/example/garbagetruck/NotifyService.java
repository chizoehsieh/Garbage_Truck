package com.example.garbagetruck;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NotifyService extends Service {

    String truckURL = "https://api.kcg.gov.tw/api/service/get/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0",nickName;
    double doubleLat,doubleLong;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("notify", Context.MODE_PRIVATE);
        nickName = sharedPreferences.getString("nickName","");
        if(sharedPreferences.getString("address","").equals("")){
            stopSelf();
        }
        else{
            String address = sharedPreferences.getString("address","");
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(address,1);

                if(addressList != null){
                    doubleLat = addressList.get(0).getLatitude();
                    doubleLong = addressList.get(0).getLongitude();
                    new DownloadTask().execute(truckURL);

                }
            } catch (IOException e) {
                e.printStackTrace();
                doubleLong = 0;
                doubleLat = 0;
            }


            while(true){
                new DownloadTask().onPostExecute(truckURL);
                long endTime = System.currentTimeMillis() + 10*1000;
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return downloadUrl(truckURL);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String s) {

            try {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.garbagetruck);
                Bitmap b = bitmapDrawable.getBitmap();
                Bitmap smallIcon = Bitmap.createScaledBitmap(b,100,100,false);
//                Toast.makeText(getApplicationContext(),"getJson",Toast.LENGTH_SHORT).show();
//                LatLng truckPosition2 = new LatLng(22.642873,120.278835);
//                mMap.addMarker(new MarkerOptions().position(truckPosition2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("第一責任區"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(truckPosition2));
                JSONArray data = new JSONObject(s).getJSONArray("data");
//                Toast.makeText(getApplicationContext(),"After JSON",Toast.LENGTH_SHORT);
                Log.d("test","After JSON");
                for(int i=0;i<data.length();i++){
                    JSONObject siteObj = data.getJSONObject(i);
                    if(Double.parseDouble(siteObj.getString("y") )== doubleLat && Double.parseDouble(siteObj.getString("x")) == doubleLong){
                        Notification notification = null;
                        notification = new NotificationHelper(getApplicationContext()).getNotification2(nickName);
                    }




                }
            }
            catch (JSONException e) {
//                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG);
                e.printStackTrace();
            }
        }
    }

    private String downloadUrl(String urlString)
    {
        String strHTML = "";
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); // milliseconds
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();

            if(stream != null)
            {
                int leng = 0;
                byte[] Data = new byte[100];
                byte[] totalData = new byte[0];
                int totalLeg = 0;
                do
                {
                    leng = stream.read(Data);
                    if(leng > 0)
                    {
                        totalLeg += leng;
                        byte[] temp = new byte[totalLeg];
                        System.arraycopy(totalData, 0, temp, 0, totalData.length);
                        System.arraycopy(Data, 0, temp, totalData.length, leng);
                        totalData = temp;
                    }
                }
                while(leng > 0);
                // strReturn = new String(totalData,"UTF-8");
                strHTML = new String(totalData, "UTF-8");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
//            Log.e(TAG, e.toString());
        }
        return strHTML;
    }
}
