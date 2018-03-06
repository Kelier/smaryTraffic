package com.bus.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bus.adapter.PinStationAdapter;
import com.bus.form.R;
import com.bus.listenner.RecyclerItemClickListener;
import com.bus.model.stationBean;

import java.util.ArrayList;


/**
 * Created by John Yan on 3/23/2017.
 */

public class HomePinFirst extends AppCompatActivity implements
        OnGetPoiSearchResultListener,OnGetSuggestionResultListener {
    ImageView back;

    RelativeLayout nav_my;
    RelativeLayout nav_go;
    EditText destination;
    RelativeLayout push;
    PoiSearch mPoiSearch=null;
    SuggestionSearch mSuggestSearch=null;

    ArrayList<stationBean> vertis;//存放搜索推荐列表
    PinStationAdapter adapter;
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.station_pin);
        //初始化poi

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mSuggestSearch= SuggestionSearch.newInstance();
        mSuggestSearch.setOnGetSuggestionResultListener(this);


        initUI();

        if(getIntent().getStringExtra("startPointcode").equalsIgnoreCase("1")){
            Log.e("test","携带位置标志1");
            nav_my.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //broadcast广播回传选定的地址
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nav_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePinFirst.this,MapPin.class));

            }
        });
        destination.addTextChangedListener(new TextWatcher() {
            //监听文本，如果改变，渲染底层布局，推送站点
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("test","1"+String.valueOf(s));

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(String.valueOf(s).length()>0){
                    push.setBackgroundResource(R.color.home_listbc);
                    Log.e("test","2"+String.valueOf(s));
                    String current=String.valueOf(s);
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city("天津")
                            .keyword(current)
                            .pageNum(10));
                    mSuggestSearch
                            .requestSuggestion((new SuggestionSearchOption())
                                    .keyword(current).city("天津"));
                }else rv.removeAllViewsInLayout();




            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("test","3"+String.valueOf(s));


            }
        });



    }
    private void initUI() {
        back= (ImageView) findViewById(R.id.back_before);
        nav_my= (RelativeLayout) findViewById(R.id.nav_my);
        nav_go= (RelativeLayout) findViewById(R.id.nav_go);
        destination= (EditText) findViewById(R.id.search_destination);
        push=(RelativeLayout)findViewById(R.id.Push_Tip);
        destination.setGravity(Gravity.LEFT);
        destination.setHint("输入地点");
        rv = (RecyclerView) findViewById(R.id.station_line);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult result) {
        //获取POI检索结果

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.e("poi","fuck");
            return;
        }
        //遍历所有POI
        for (SuggestionResult.SuggestionInfo info:result.getAllSuggestions())
            Log.e("poi",info.city+","+info.district+","+info.key);
        vertis=new ArrayList<>();
        for (SuggestionResult.SuggestionInfo info:result.getAllSuggestions()) {
            if(info.city.length()>=1||info.district.length()>=1)
                vertis.add(new stationBean(R.drawable.route_go,info.city+info.district,info.key));
            addLayer();
        }
    }

    private void addLayer() {

        adapter=new PinStationAdapter(this,vertis);


        // 布局管理器
//         线性布局
        layoutManager = new LinearLayoutManager(this) ;
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        // 网格布局
//        manager = new GridLayoutManager(this , 3) ;
        // 瀑布布局
//        manager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL) ;
//        // 控件

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e("touch", "onClick position : " + position);
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {

//                        startActivity(new Intent(this,HomeNavLine.class));
                    }
                }));

// 控件调用两个set方法
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);

    }


}
