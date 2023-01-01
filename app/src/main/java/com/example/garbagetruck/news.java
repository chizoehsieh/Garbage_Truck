package com.example.garbagetruck;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link news#newInstance} factory method to
 * create an instance of this fragment.
 */
public class news extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public news() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment news.
     */
    // TODO: Rename and change types and number of parameters
    public static news newInstance(String param1, String param2) {
        news fragment = new news();
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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Banner banner = view.findViewById(R.id.banner);
        ArrayList<Integer> images = new ArrayList<Integer>();
        images.add(R.drawable.recycler_1);
        images.add(R.drawable.recycler_2);
        images.add(R.drawable.recycler_3);
        images.add(R.drawable.recycler_4);
        images.add(R.drawable.recycler_5);
        banner.setImageLoader(new GlideIamgeLoader());
        banner.setImages(images);
        banner.setDelayTime(3000);
        banner.start();

    }

    public class GlideIamgeLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView){
            Glide.with(context).load(path).into(imageView);
        }
    }
}