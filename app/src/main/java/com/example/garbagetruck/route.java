package com.example.garbagetruck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link route#newInstance} factory method to
 * create an instance of this fragment.
 */
public class route extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinnerCountry,spinnerDistrict,spinnerRoute;
    int spinnerCountryIndex,spinnerDistrictIndex,spinnerRouteIndex;

    String[] route,truck;
    String[] roueNumber = new String[]{"一","二","三","四","五","六","七","八","九"};
    String districtName,routeName,truckNum;
    ArrayList<RouteInformation> mDataSet = new ArrayList<>();
    String estimateURI = "https://api.kcg.gov.tw/api/service/get/14fe516d-ac62-4905-9325-70daae7616bd";
    String truckURL = "https://api.kcg.gov.tw/api/service/get/aaf4ce4b-4ca8-43de-bfaf-6dc97e89cac0";

    RecyclerView recyclerView;
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    RecyclerView.LayoutManager mLayoutManager;
    LayoutManagerType mCurrentLayoutManagerType;

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;


    public route() {
        // Required empty public constructor
    }

    private enum LayoutManagerType
    {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment route.
     */
    // TODO: Rename and change types and number of parameters
    public static route newInstance(String param1, String param2) {
        route fragment = new route();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerCountry = getView().findViewById(R.id.spinner);
        spinnerCountry.bringToFront();
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),R.array.country, android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter1);
        spinnerCountry.setSelection(12);
        spinnerDistrict = getView().findViewById(R.id.spinner2);
        spinnerDistrict.bringToFront();
        spinnerRoute = getView().findViewById(R.id.spinner3);
        spinnerRoute.bringToFront();
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.bringToFront();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter2;
                spinnerCountryIndex = i;
                switch (i){
                    case 0:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.keelungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 1:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.newTaipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 2:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taipeiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 3:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taoyuanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 4:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.hsinchuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 5:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.miaoliDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 6:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taichungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 7:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.changhuaDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 8:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.nantouDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 9:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.yunlinDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 10:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.chiayiDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 11:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.tainanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 12:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 13:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.pingtungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 14:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.taitungDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 15:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.hualienDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 16:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.yilanDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 17:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.penghuDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 18:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kinmenDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    case 19:
                        adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.lienchiangDistrict, android.R.layout.simple_spinner_dropdown_item);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + i);
                }
                spinnerDistrict.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerCountryIndex = 12;
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),R.array.kaohsiungDistrict, android.R.layout.simple_spinner_dropdown_item);
                spinnerDistrict.setAdapter(adapter2);
            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerDistrictIndex = i;
                ArrayAdapter<CharSequence> adapter3;
                if(spinnerCountryIndex == 12 && i == 0){
                    adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.route_kaohsiung_1, android.R.layout.simple_spinner_dropdown_item);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_1);
                }
                else if(spinnerCountryIndex == 12 && i == 1){
                    adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.route_kaohsiung_2, android.R.layout.simple_spinner_dropdown_item);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_2);

                }
                else {
                    adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.route1, android.R.layout.simple_spinner_dropdown_item);
                    truck = getResources().getStringArray(R.array.garbageTruck_kaohsiung_1);

                }
                if(spinnerCountryIndex == 12){
                    districtName = (String) spinnerDistrict.getSelectedItem();
                }
                spinnerRoute.setAdapter(adapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerDistrictIndex = 0;
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.route_kaohsiung_1, android.R.layout.simple_spinner_dropdown_item);
                spinnerRoute.setAdapter(adapter3);
            }
        });
        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), spinnerCountryIndex + spinnerDistrictIndex + "",Toast.LENGTH_SHORT).show();
                if(spinnerCountryIndex == 12){
//                    Toast.makeText(getContext(),"高雄",Toast.LENGTH_SHORT).show();
                    truckNum = truck[i];
                    mDataSet = new ArrayList<RouteInformation>();
                    try {
                        InputStream inputStream = getContext().getAssets().open("ksepb.csv");
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        CSVReader reader = new CSVReader(inputStreamReader);
//                        Toast.makeText(getContext(),"讀完CSV",Toast.LENGTH_SHORT).show();
                        String[] nextLine;

                        int stationNum = 0;
                        while((nextLine = reader.readNext()) != null){
                            if(nextLine[5].equals(districtName) && nextLine[0].equals(roueNumber[i])){
                                stationNum += 1;
                                RouteInformation info = new RouteInformation();
                                info.setRouteName("第"+nextLine[0]+"責任區");
                                info.setStationName(stationNum + "."+nextLine[4]);
                                info.setSettingTime(nextLine[3].split("~")[0].split("-")[0]);
                                info.setPackingDay("清運日：一、二、四、五、六");
                                info.setRecyclingDay("回收日：" + nextLine[2]);
                                mDataSet.add(info);
                                routeName = nextLine[0];
                            }
                        }
                        new DownloadTask().execute(estimateURI);
                        new DownloadTask2().execute(truckURL);
//                        initRecyclerView();
                    } catch (FileNotFoundException e) {
//                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(getContext(),mDataSet);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration());
    }
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType)
    {
        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null)
        {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType)
        {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getContext());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getContext());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = 1;
            outRect.right = 1;
            outRect.bottom = 20;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = 5;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return downloadUrl(estimateURI);
        }

        @Override
        protected void onPostExecute(String s) {

            try {
//                Toast.makeText(getContext(),"getJson",Toast.LENGTH_SHORT).show();
                JSONArray data = new JSONObject(s).getJSONArray("data");
                int stationNum = 0;
                for(int i=0;i<data.length();i++){
                    JSONObject siteObj = data.getJSONObject(i);
                    String siteInfo = siteObj.getString("area");
                    String siteInfo2 = siteObj.getString("area_name");
                    if(siteInfo.equals(districtName) && siteInfo2.equals(routeName)){
                        for(int j=stationNum;j<mDataSet.size();j++){
                            if(siteObj.getString("today_s").equals(mDataSet.get(j).getSettingTime()))
                            {
                                RouteInformation ri = mDataSet.get(j);
//                                ri.setSituation("|今日清運："+siteObj.getString("estime_s"));
                                ri.setEstimateTime(siteObj.getString("estime_s"));
                                if(siteObj.getString("estime_s").equals(null)){
                                    ri.setEstimateTime("本日已清運");
                                }
                                mDataSet.set(stationNum,ri);
                                stationNum = j;
                                break;
                            }
                        }

                    }



                }
//                initRecyclerView();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return downloadUrl(truckURL);
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray data = new JSONObject(s).getJSONArray("data");
//                Toast.makeText(getContext(),"After JSON",Toast.LENGTH_SHORT);
                for(int i=0;i<data.length();i++){
                    JSONObject siteObj = data.getJSONObject(i);
                    if(siteObj.getString("car").equals(truckNum)){
                        for(int j=0;j<mDataSet.size();j++){
                            RouteInformation ri = mDataSet.get(j);
                            ri.setSituationPosition(siteObj.getString("location"));
                        }
                    }



                }
                initRecyclerView();
            }
            catch (JSONException e) {
//                Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_LONG);
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
    private void updateConnectedFlags()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null)
        {
            NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnected())
            {
                wifiConnected = activeNetwork.isConnected();
                mobileConnected = activeNetwork.isConnected();
            }
            else
            {
                wifiConnected = false;
                mobileConnected = false;
            }
        }
        else
        {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    Object getSystemService(String name) {
        throw new RuntimeException("Stub!");
    }
}