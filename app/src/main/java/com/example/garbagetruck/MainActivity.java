package com.example.garbagetruck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;  //自動import的toolbar是有衝突的，要換成import這個檔案才能使用
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    ViewPager2 viewPager;
    route routeFragment;
    home homeFragment;
    news newsFragment;
    SettingFragment settingFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,NotifyService.class);
        stopService(intent);
//        Toast.makeText(this,"stopService",Toast.LENGTH_SHORT).show();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        routeFragment = new route();
        homeFragment = new home();
        newsFragment = new news();
        settingFragment = new SettingFragment();
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView.setSelectedItemId(R.id.home);

        ArrayList<Fragment> fragmentList =  new ArrayList<>();
        fragmentList.add(routeFragment);
        fragmentList.add(homeFragment);
        fragmentList.add(newsFragment);
        fragmentList.add(settingFragment);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment = fragmentList.get(position);
                return fragment;
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        viewPager.setOffscreenPageLimit(1);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onPagerSelected(position);
            }
        });
        viewPager.setCurrentItem(1);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.route:
                        viewPager.setCurrentItem(0);
//                        Toast.makeText(MainActivity.this,"點擊路線",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home:
                        viewPager.setCurrentItem(1);
//                        Toast.makeText(MainActivity.this,"點擊首頁",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.news:
                        viewPager.setCurrentItem(2);
//                        Toast.makeText(MainActivity.this,"點擊最新消息",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });





    }

    private void onPagerSelected(int position) {
        switch (position){
            case 0:
                bottomNavigationView.setSelectedItemId(R.id.route);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(R.id.home);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id.news);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(0);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(MainActivity.this,"開啟設定",Toast.LENGTH_SHORT).show();
        viewPager.setCurrentItem(3);
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.route:
//                Toast.makeText(MainActivity.this,"點擊路線",Toast.LENGTH_SHORT).show();
//                routeFragment = new route();
//                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,routeFragment,"fragment").commit();
//
//                return true;
//            case R.id.home:
//                Toast.makeText(MainActivity.this,"點擊首頁",Toast.LENGTH_SHORT).show();
//                homeFragment = new home();
//                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,homeFragment,"fragment").commit();
//
//                return true;
//            case R.id.news:
//                Toast.makeText(MainActivity.this,"點擊最新消息",Toast.LENGTH_SHORT).show();
//                newsFragment = new news();
//                fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,newsFragment,"fragment").commit();
//
//                return true;
//        }
//        return false;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this,NotifyService.class);
        startService(intent);
//        Toast.makeText(this,"startService",Toast.LENGTH_SHORT).show();
    }
}