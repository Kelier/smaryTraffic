package com.bus.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bus.form.R;
import com.bus.overlay.PoiOverlay;

import java.util.ArrayList;


/**
 * Created by John Yan on 2/12/2017.
 */

public class HomeMapNear extends AppCompatActivity  {

    private static boolean isPermissionRequested = false;
    //定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    PoiSearch mPoiSearch;
    LatLng center;

    public MyOrientationListener myOrientationListener;
    Float mLastX;


    MapView mMapView;
    BaiduMap mBaiduMap;
    ImageView back;

    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initManager();
        setContentView(R.layout.map_location_near);
        back= (ImageView) findViewById(R.id.back_before);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermission();
        //方向传感器监听
        initMyLoc();
        //POi相关

        mPoiSearch = PoiSearch.newInstance();//实例化一个poi
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);//设置监听

        //地图初始化
        mMapView= (MapView) findViewById(R.id.near_map);
       mBaiduMap = mMapView.getMap();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;


        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);


        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型

        mLocClient.setLocOption(option);
        mLocClient.start();

//        mBaiduMap
//                .setMyLocationConfigeration(new MyLocationConfiguration(
//                        mCurrentMode, true, mCurrentMarker));

    }



    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
//            Log.e("test1",String.valueOf(location.getLatitude()));
            if (location == null || mMapView == null) {

                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {

                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                center=ll;

                mPoiSearch.searchNearby((new PoiNearbySearchOption()).keyword("公交站").location(ll).radius(1000).pageNum(0));
/*城市的整个搜索
               mPoiSearch.searchInCity((new PoiCitySearchOption())
                      .city("天津")
                      .keyword("公交")
                       .pageNum(10));
*/

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(17.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


            }
        }



        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        public void onGetPoiResult(PoiResult result){
            if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                Toast.makeText(HomeMapNear.this, "未找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                //mBaiduMap.clear();
                PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result);
                overlay.addToMap();
                overlay.zoomToSpan();

                showNearbyArea(center,750);
                Log.e("poi",String.valueOf(result.getTotalPoiNum()));


                return;
            }
        }
        public void onGetPoiDetailResult(PoiDetailResult result){
            //获取Place详情页检索结果
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(HomeMapNear.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(HomeMapNear.this, "站点名称: "+result.getName() + " 换乘线路:" + result.getAddress(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };


    /**
     * 方向传感器监听
     * @author wangjian
     *
     */
    public static class MyOrientationListener implements SensorEventListener {
        //传感器管理者
        private SensorManager mSensorManager;
        //上下文
        private Context mContext;
        //传感器
        private Sensor mSensor;

        //方向传感器有三个坐标，现在只关注X
        private float mLastX;
        //构造函数
        public MyOrientationListener(Context context) {
            this.mContext = context;
        }
        //开始监听
        @SuppressWarnings("deprecation")
        public void start(){
            //获得传感器管理者
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            if(mSensorManager!=null){//是否支持
                //获得方向传感器
                mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            if(mSensor!=null){//如果手机有方向传感器，精度可以自己去设置，注册方向传感器
                Log.e("test","enter");
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
        //结束监听
        public void stop(){
            //取消注册的方向传感器
            mSensorManager.unregisterListener(this);
        }
        //传感器发生改变时
        @SuppressWarnings("deprecation")
        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断返回的传感器类型是不是方向传感器
            if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){

                //只获取x的值
                float x = event.values[SensorManager.DATA_X];
                //为了防止经常性的更新
                if(Math.abs(x-mLastX)>1.0){
                    if(onOrientationListener!=null){
                        onOrientationListener.onOrientationChanged(x);
                    }
                }
                mLastX = x;
            }
        }

        //当传感器精度发生改变，当前不用
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        private OnOrientationListener onOrientationListener;

        public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
            this.onOrientationListener = onOrientationListener;
        }

        //回掉方法
        public interface OnOrientationListener{
            void onOrientationChanged(float x);
        }

    }


    private void initMyLoc() {

        //方向传感器监听
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                //将获取的x轴方向赋值给全局变量
                mLastX = x;
            }
        });
    }

    private void initManager() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;


            Boolean phonegrant= checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;

            ArrayList<String> permissions = new ArrayList<>();
            if (phonegrant) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    /**
     * 对周边检索的范围进行绘制
     * @param center
     * @param radius
     */
    public void showNearbyArea( LatLng center, int radius) {


        OverlayOptions ooCircle = new CircleOptions().fillColor( 0x70ffffff )
                .center(center).stroke(new Stroke(5, 0x8000bfff ))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mPoiSearch.destroy();
        mMapView.onDestroy();
    }
}