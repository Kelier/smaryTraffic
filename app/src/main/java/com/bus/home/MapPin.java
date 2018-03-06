package com.bus.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bus.form.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John Yan on 3/23/2017.
 */

public class MapPin extends AppCompatActivity {

    MapView mapView=null;
    private BaiduMap map;
    public LocationClient mLocationClient=null;
    public BDLocationListener myListener=new MyLocationListener();
    private double latitude;
    private double longitude;
    private GeoCoder mSearch;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private LatLng myLat;

    private TextView textPreview;
    private Button sumbit_loc;
    private ImageButton roundset;
    private ImageView back_before;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.pin_map);

        textPreview= (TextView) findViewById(R.id.address_loc);
        sumbit_loc= (Button) findViewById(R.id.picthed_add);
        roundset= (ImageButton) findViewById(R.id.roundset);
        back_before= (ImageView) findViewById(R.id.back_before);
        back_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        roundset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复位
//构建MarkerOption，用于在地图上添加Marker
                map.clear();
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.marker);
                OverlayOptions option = new MarkerOptions()
                        .position(myLat)
                        .icon(bitmap);
//在地图上添加Marker，并显示
                map.addOverlay(option);
//定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(myLat)
                        .zoom(18)
                        .build();
//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//改变地图状态
                map.setMapStatus(mMapStatusUpdate);
            }
        });
        sumbit_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回传位置
            }
        });

        mapView= (MapView) findViewById(R.id.choose_map);
        mCurrentMode=MyLocationConfiguration.LocationMode.NORMAL;

        getLocation();//得到位置
        getGeo();//得到地理编码
    }



    private void getGeo() {
        mSearch = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
            }


            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果
                System.out.println(result.getAddress());
                int size=result.getPoiList().size();
                if(size>0){
                    List <String> data=new ArrayList<>();

                    for (PoiInfo info:result.getPoiList()){
                        String name=info.name;
                        data.add(name);
                    }
                    System.out.println(data.get(0));
                    textPreview.setText(data.get(0));
                    }

            }
        };

        mSearch.setOnGetGeoCodeResultListener(listener);
    }

    private void getLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.stop();
        mSearch.destroy();
    }



    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null)
                return ;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            getMap();//绘制地图
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            textPreview.setText(location.getAddrStr());
            System.out.println(sb.toString());
            mLocationClient.stop();
        }
    }

    private void getMap() {

        //获取屏幕高度宽度
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;




        map = mapView.getMap();
        // 设置地图模式为交通地图
        mapView.showScaleControl(false);
        mapView.removeViewAt(1);

        map.setTrafficEnabled(true);
        // 设置启用内置的缩放控件
        mapView.showZoomControls(true);
        //获得百度地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
//      builder.targetScreen(new Point(width/2,height/2));
        //定位的坐标点
        LatLng latLng = new LatLng(latitude, longitude);
        myLat=latLng;

//      BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
//      OverlayOptions overlay = new MarkerOptions().position(latLng).icon(bitmap);
//      map.addOverlay(overlay);
        //设置地图中心为定位点
        builder.target(latLng);
        //设置缩放级别 18对应比例尺50米
        builder.zoom(18);
        MapStatus mapStatus = builder.build();
        MapStatusUpdate m = MapStatusUpdateFactory.newMapStatus(mapStatus);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
//在地图上添加Marker，并显示
        map.addOverlay(option);

        map.setMapStatus(m);
        map.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // TODO Auto-generated method stub
                LatLng target = map.getMapStatus().target;
                System.out.println(target.toString());
                //构建MarkerOption，用于在地图上添加Marker
                map.clear();
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.marker);
                OverlayOptions option = new MarkerOptions()
                        .position(target)
                        .icon(bitmap);
//在地图上添加Marker，并显示
                map.addOverlay(option);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(target));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

}
