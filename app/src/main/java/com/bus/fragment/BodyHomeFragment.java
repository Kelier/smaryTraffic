package com.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bus.adapter.HomeStationAdapter;
import com.bus.form.R;
import com.bus.home.HomeAlarm;
import com.bus.home.HomeMapNear;
import com.bus.home.HomeSearch;
import com.bus.model.stationGroomBean;
import com.bus.setting.SettingStar;
import com.bus.citylist.CityList;

import java.util.ArrayList;

import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;


public class BodyHomeFragment extends Fragment {
    ArrayList<stationGroomBean> data;
    HomeStationAdapter adapter;
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    private PtrClassicFrameLayout refresh;
    private TextView loc_city;
    private RelativeLayout my_star;
    private RelativeLayout my_map;
    private RelativeLayout my_tip;
    private RelativeLayout my_station;
    private RelativeLayout my_route;
    private android.widget.SearchView search;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.activity_body_home_fragment,null);
        setupView(rootview);
        swipeRefresh(rootview);
        transferOther();
        return rootview;
    }

    private void transferOther() {
        final Intent intent=new Intent();
        loc_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityList.class);
                startActivityForResult(intent, 1);
            }
        });

        my_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), HomeMapNear.class);
                startActivity(intent);
            }
        });
        my_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), SettingStar.class);
                startActivity(intent);
            }
        });
        my_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), HomeAlarm.class);
                startActivity(intent);
            }
        });

//        my_station.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(getActivity(), HomeStation.class);
//            }
//        });
//        my_route.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(getActivity(), HomeRoute.class);
//            }
//        });

        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText textView = (EditText ) search.findViewById(id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(),HomeSearch.class);
                startActivity(intent);
            }
        });


    }

    private void swipeRefresh(View rootview) {
        refresh= (PtrClassicFrameLayout) rootview.findViewById(R.id.refresh_layout);
        refresh.autoRefresh();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            switch (resultCode) {
                case 2:
                    loc_city.setText(data.getStringExtra("city"));
                    break;

                default:
                    break;
            }
    }

    private void setupView(View rootview) {
        loc_city= (TextView) rootview.findViewById(R.id.home_loc_city);
        my_star= (RelativeLayout) rootview.findViewById(R.id.home_star_rela);
        my_map= (RelativeLayout) rootview.findViewById(R.id.home_map_rela);
        my_tip= (RelativeLayout) rootview.findViewById(R.id.home_tip_rela);
        my_station= (RelativeLayout) rootview.findViewById(R.id.home_station_rela);
        my_route= (RelativeLayout) rootview.findViewById(R.id.home_route_rela);
        search= (android.widget.SearchView) rootview.findViewById(R.id.home_search_des);


        data=new ArrayList<>();
        data.add(new stationGroomBean("安河桥","100m","201","鼓楼",R.mipmap.bell_green,"3分"));
        data.add(new stationGroomBean("东直门","125m","235","滨海新区",R.mipmap.bell_yellow,"5分"));
        data.add(new stationGroomBean("王府井","420m","225","天塔",R.mipmap.bell_red,"10分"));
        data.add(new stationGroomBean("万达广场","890m","711","天安门",R.mipmap.bell_green,"21分"));
        adapter=new HomeStationAdapter(rootview.getContext(),data);
        // 布局管理器
//         线性布局
        layoutManager = new LinearLayoutManager(rootview.getContext()) ;
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        // 网格布局
//        manager = new GridLayoutManager(this , 3) ;
        // 瀑布布局
//        manager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL) ;
//        // 控件
        rv = (RecyclerView) rootview.findViewById(R.id.home_list_station);
        // 控件调用两个set方法
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);

    }
}
