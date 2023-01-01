package com.example.garbagetruck;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ExpandableListView expandableListView;

    private ArrayList<ArrayList<notifyPosition>> mItem = new ArrayList<ArrayList<notifyPosition>>();
    private ArrayList<String> mGroupName;

    private NotificationHelper mNotiHelper;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        Notification notification = null;
//        mNotiHelper = new NotificationHelper(getContext());
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("notify", Context.MODE_PRIVATE);
//        notification = mNotiHelper.getNotification2(sharedPreferences.getString("nickName",""));
//        if (notification != null)
//        {
//            mNotiHelper.notify(1100, notification);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        expandableListView = view.findViewById(R.id.expandableListView);
        notifyPosition notify = new notifyPosition();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("notify", Context.MODE_PRIVATE);
        notify.setCountry(sharedPreferences.getInt("country",12));
        notify.setDistric(sharedPreferences.getInt("distric",0));
        notify.setRoute(sharedPreferences.getInt("route",0));
        String[] a = sharedPreferences.getString("address","").split("區");
        a = a[a.length - 1].split("鄉");
        a = a[a.length - 1].split("鎮");
        a = a[a.length - 1].split("市");
        notify.setAddress(a[a.length-1]);
        notify.setNickName(sharedPreferences.getString("nickName",""));
        ArrayList<notifyPosition> n = new ArrayList<>();
        n.add(notify);
        mItem.add(n);
        String[] setting_name = getContext().getResources().getStringArray(R.array.setting_list);
        List<String> groupName = Arrays.asList(setting_name);
        List<String> settingGroup = new ArrayList<String>(groupName);
        mGroupName = (ArrayList<String>) settingGroup;
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getContext(),mGroupName,mItem);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });


    }
}