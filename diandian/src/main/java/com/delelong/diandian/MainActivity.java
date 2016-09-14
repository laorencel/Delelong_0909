package com.delelong.diandian;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.RouteSearch;
import com.delelong.diandian.bean.Client;
import com.delelong.diandian.bean.Str;
import com.delelong.diandian.fragment.MenuFrag;
import com.delelong.diandian.fragment.TimeFrag;
import com.delelong.diandian.http.ClientLocationInfo;
import com.delelong.diandian.http.HttpUtils;
import com.delelong.diandian.listener.MyCameraChangeListener;
import com.delelong.diandian.listener.MyOrientationListener;
import com.delelong.diandian.listener.MyRouteSearchListener;
import com.delelong.diandian.pace.MyAMapLocation;
import com.delelong.diandian.utils.ToastUtil;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, LocationSource, AMapLocationListener {

    private static final String TAG = "BAIDUMAPFORTEST";


    HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_main);

        initScreen();
        isFirstIn = true;
        httpUtils = new HttpUtils(this);
        setUpMap(savedInstanceState);
        initView();
        setMyCameraChangeListenerListener();
        setMyRouteSearchListener();
    }
    Point leftTop,rightTop,leftBottom,rightBottom;
    private void initScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;

        leftTop = new Point(0,0);
        rightTop = new Point(0,screenWidth);
        leftBottom = new Point(screenHeight,0);
        rightBottom = new Point(screenHeight,screenWidth);
    }
    private void getVisibility(){
        LatLng leftTopLat = aMap.getProjection().fromScreenLocation(leftTop);
        LatLng rightTopLat =aMap.getProjection().fromScreenLocation(rightTop);
        LatLng leftBottomLat =aMap.getProjection().fromScreenLocation(leftBottom);
        LatLng rightBottomLat =aMap.getProjection().fromScreenLocation(rightBottom);
        Log.i(TAG, "leftTopLat: "+"latitude/"+leftTopLat.latitude+"longitude/"+leftTopLat.longitude);
        Log.i(TAG, "rightTopLat: "+"latitude/"+rightTopLat.latitude+"longitude/"+rightTopLat.longitude);
        Log.i(TAG, "leftBottomLat: "+"latitude/"+leftBottomLat.latitude+"longitude/"+leftBottomLat.longitude);
        Log.i(TAG, "rightBottomLat: "+"latitude/"+rightBottomLat.latitude+"longitude/"+rightBottomLat.longitude);
    }


    MenuFrag menuFrag;
    android.support.v7.app.ActionBar actionBar;
    View menuView;
    ImageButton head_actionbar, msg_actionbar;
    TextView city_actionbar;

    private void initActionBar() {
        actionBar = getSupportActionBar();
        if (Build.VERSION.SDK_INT >= 21) {
            //用于去除阴影
            actionBar.setElevation(0);
        }
        actionBar.setDisplayShowCustomEnabled(true);
        menuView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_action, null);
        actionBar.setCustomView(menuView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        head_actionbar = (ImageButton) menuView.findViewById(R.id.head_actionbar);
        msg_actionbar = (ImageButton) menuView.findViewById(R.id.msg_actionbar);
        city_actionbar = (TextView) menuView.findViewById(R.id.city_actionbar);
        head_actionbar.setOnClickListener(this);

    }

    private RouteSearch mRouteSearch;
    private MyRouteSearchListener myRouteSearchListener;
    private DrivePath mDrivePath;

    /**
     * 获取驾车路线信息
     */
    private void setMyRouteSearchListener() {
        mRouteSearch = new RouteSearch(context);
        myRouteSearchListener = new MyRouteSearchListener(aMap, context);
        mRouteSearch.setRouteSearchListener(myRouteSearchListener);
        myRouteSearchListener.getDrivePathListener(new MyRouteSearchListener.MyDrivePathListener() {
            @Override
            public void getDrivePath(DrivePath drivePath) {
                mDrivePath = drivePath;
            }
        });
        //根据路径获取里程数等
        if (mDrivePath != null) {
            //待实现
        }
    }

    private MyCameraChangeListener myCameraChangeListener;
    List<PoiItem> pois;
    LatLng centerOfMap;

    /**
     * 地图移动状态监听
     */
    private void setMyCameraChangeListenerListener() {
        myCameraChangeListener = new MyCameraChangeListener(myPosition, context);
        aMap.setOnCameraChangeListener(myCameraChangeListener);

        myCameraChangeListener.getGeoCodeResultListener(new MyCameraChangeListener.GeoCodeResulutListener() {
            @Override
            public void getReverseGeoCodeResult(RegeocodeResult regeocodeResult) {
                pois = regeocodeResult.getRegeocodeAddress().getPois();

                if (pois == null) {
                    myPosition.setText(regeocodeResult.getRegeocodeAddress().getCrossroads().get(0).getFirstRoadName() + "与" + regeocodeResult.getRegeocodeAddress().getCrossroads().get(0).getSecondRoadName() + "交口");
                } else {
                    mPositionPoiItem = pois.get(0);
                    myPosition.setText(mPositionPoiItem.getTitle());
                }
                //当前中心点所在城市
                city = regeocodeResult.getRegeocodeAddress().getCity();
                city_actionbar.setText(city);
            }

            @Override
            public void getLatlng(LatLng center) {
                centerOfMap = center;
            }
        });
    }

    private FragmentManager mFragManager;
    private TimeFrag mTimeFrag;

    public void enableClick() {
        myPosition.setEnabled(true);
        myDestination.setEnabled(true);
        timeToGo.setEnabled(true);
        route.setEnabled(true);
    }

    private void unEnableClick() {
        myPosition.setEnabled(false);
        myDestination.setEnabled(false);
        timeToGo.setEnabled(false);
        route.setEnabled(false);
    }

    private RelativeLayout rl_main;
    private TextView timeOfReach, positon;//司机到达时间、在哪上车
    private LinearLayout textInCenter;//屏幕中间布局

    private ImageButton myLocation;//我的位置按钮

    private ImageButton showTime, hideTime;//显示、隐藏时间显示
    private TextView myPosition, myDestination, timeToGo;//起点、终点、具体时间选择
    private LinearLayout lyOfTime, route;

    /**
     * 初始化
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
//        //创建乘客对象
        initClient();
        initOrder();

        mFragManager = getFragmentManager();

        //布局顺滑
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        rl_main.setLayoutTransition(new LayoutTransition());

        //隐藏拼车、确认模块
        rl_confirm = (RelativeLayout) findViewById(R.id.rl_confirm);
        ly_pinChe = (LinearLayout) findViewById(R.id.ly_pinChe);
        hidePinChe();

        timeOfReach = (TextView) findViewById(R.id.timeOfReach);
        positon = (TextView) findViewById(R.id.positon);


        //地址选择模块
        route = (LinearLayout) findViewById(R.id.route);
        lyOfTime = (LinearLayout) findViewById(R.id.lyOfTime);
        myPosition = (TextView) findViewById(R.id.myPosition);
        myDestination = (TextView) findViewById(R.id.myDestination);
        timeToGo = (TextView) findViewById(R.id.timeToGo);
        timeToGo.setOnClickListener(this);
        myPosition.setOnClickListener(this);
        myDestination.setOnClickListener(this);

        showTime = (ImageButton) findViewById(R.id.showTime);
        hideTime = (ImageButton) findViewById(R.id.hideTime);
        showTime.setOnClickListener(this);//显示打车预约时间
        hideTime.setOnClickListener(this);//隐藏打车预约时间

        myLocation = (ImageButton) findViewById(R.id.myLocation);
        myLocation.setOnClickListener(this);//定位到我的位置

    }

    private MapView mMapView = null;
    private AMap aMap = null;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private float mCurrentX;
    private MyOrientationListener myOrientationListener;

    /**
     * 初始化地图、定位
     *
     * @param savedInstanceState
     */
    private void setUpMap(Bundle savedInstanceState) {
        this.context = this;
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        //定义一个UiSettings对象
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);//是否允许显示缩放按钮。
        mUiSettings.setCompassEnabled(false);//指南针可用不可用。
        mUiSettings.setRotateGesturesEnabled(false);//是否允许通过手势来旋转。
        mUiSettings.setScaleControlsEnabled(false);//设置比例尺功能是否可用

        //改写箭头样式
//        myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked1));
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
//        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        //设置方向监听
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setmOnOritationListener(new MyOrientationListener.OnOritationListener() {
            @Override
            public void onOritationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    private boolean isFirstIn = true;
    private LatLng startLat,endLat;
    public MyAMapLocation myAMapLocation;
    AMapLocation mAMapLocation;
    /**
     * 定位
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                aMap.setMyLocationRotateAngle(mCurrentX);

                //判断是否上传位置
                if (mAMapLocation!=null){
                    upDateLocation();
                }

                mAMapLocation = aMapLocation;
                myAMapLocation = new MyAMapLocation(mAMapLocation.getCountry(), mAMapLocation.getProvince(),
                        mAMapLocation.getCity(), mAMapLocation.getDistrict(), mAMapLocation.getAddress(), mAMapLocation.getAdCode());
                city = mAMapLocation.getCity();

                //首次进入定位到我的位置
                if (isFirstIn) {
                    centerToMyLocation(aMap, mLocationClient, myOrientationListener, mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
                    myPosition.setText(aMapLocation.getPoiName());
                    startLat = new LatLng(mAMapLocation.getLatitude(),mAMapLocation.getLongitude());
                    isFirstIn = false;
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(MainActivity.this, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 上传会员位置(判断与原位置相差10米)
     */
    private void upDateLocation() {
        endLat = new LatLng(mAMapLocation.getLatitude(),mAMapLocation.getLongitude());
        float distance = AMapUtils.calculateLineDistance(startLat,endLat);
        if (distance>10){
            startLat = new LatLng(endLat.latitude,endLat.longitude);
            //上传位置
            ClientLocationInfo locationInfo = new ClientLocationInfo(mAMapLocation.getLongitude()+"",
                    mAMapLocation.getLatitude()+"",mAMapLocation.getSpeed()+"",mCurrentX+"");
            List<String> list = httpUtils.upDateLocation(Str.URL_UPDATELOCATION,locationInfo);
            Log.i(TAG, "upDateLocation: ");
        }
    }


    private Context context;
    Client client;

    /**
     * 初始化客户信息
     */
    private void initClient() {
        client = new Client();
//        client.setPhone(getIntent().getStringExtra("phone"));
    }

    private String orderedTime;
    private String orderedMode;

    String city;

    private void initOrder() {
        orderedTime = "现在";
        orderedMode = "拼车";
    }

    boolean isLogining = true;

    /**
     * 设置登陆状态
     *
     * @param isLogining
     */
    public void setLogining(boolean isLogining) {
        this.isLogining = isLogining;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_actionbar:
                //调出侧边栏
                toLogin();
                if (menuFrag == null) {
                    menuFrag = new MenuFrag();
                    mFragManager.beginTransaction().add(R.id.rl_menuFrag, menuFrag, "menuFrag").addToBackStack(null).show(menuFrag).commit();
                } else {
                    //退回栈后fragment重新添加
                    mFragManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.rl_menuFrag, menuFrag, "menuFrag").addToBackStack(null).show(menuFrag).commit();
                }
                isForeground = false;
                actionBar.hide();
                unEnableClick();//本层按钮不可用，免误操作
                break;
            case R.id.myLocation:
                //定位到我的位置
                centerToMyLocation(aMap, mLocationClient, myOrientationListener, mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
                getVisibility();
                break;
            case R.id.showTime:
                lyOfTime.setVisibility(View.VISIBLE);

//                lyOfTime.setAnimation(AnimationUtils.loadAnimation(this, R.anim.item_time_show));
                break;
            case R.id.hideTime:
                lyOfTime.setVisibility(View.GONE);
                break;
            case R.id.timeToGo:
                //（如果没加载）加载时间选择器
                if (mTimeFrag == null) {
                    mTimeFrag = new TimeFrag();
                    mFragManager.beginTransaction().add(R.id.frag_time_picker, mTimeFrag, "TimeFrag").addToBackStack(null).show(mTimeFrag).commit();
                } else {
                    mFragManager.beginTransaction().show(mTimeFrag).commit();
                }
                mTimeFrag.getTimePickerListener(new TimeFrag.TimePickerListener() {
                    @Override
                    public void getTime(String time) {
                        orderedTime = time;
                        timeToGo.setText(orderedTime);
                    }
                });
                break;
            case R.id.myDestination:
                intentActivityForResult(this, ChoosePosition.class, "choose", "myDestination", city, Str.REQUESTPOSITIONCODE);
                break;
            case R.id.myPosition:
                intentActivityForResult(this, ChoosePosition.class, "choose", "myPosition", city, Str.REQUESTDESTINATIONCODE);
                break;
            case R.id.tv_pinChe:
                tv_pinChe.setTextColor(getResources().getColor(R.color.colorPinChe));
                tv_buPinChe.setTextColor(Color.BLACK);
                orderedMode = "拼车";
                break;
            case R.id.tv_buPinChe:
                tv_pinChe.setTextColor(Color.BLACK);
                tv_buPinChe.setTextColor(getResources().getColor(R.color.colorPinChe));
                orderedMode = "不拼车";
                break;
            case R.id.tv_confirm:
                if (mPositionPoiItem == null || mDestinationPoiItem == null) {
                    ToastUtil.show(context, "请先设置起始点");
                    return;
                }
                toLogin();

                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                        mPositionPoiItem.getLatLonPoint(), mDestinationPoiItem.getLatLonPoint());
                // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//                List<LatLonPoint> passedByPoints = new ArrayList<>();
//                passedByPoints.add(new LatLonPoint(mPositionPoiItem.getLatLonPoint().getLatitude(), mPositionPoiItem.getLatLonPoint().getLongitude()));
                RouteSearch.DriveRouteQuery routeQuery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
                mRouteSearch.calculateDriveRouteAsyn(routeQuery);// 异步路径规划驾车模式查询

                hidePinChe();
                break;
        }
    }

    /**
     * 判断不在登陆状态，先登陆
     * 需修改成dialog形式
     */
    private void toLogin() {
        if (!isLogining) {
            Toast.makeText(MainActivity.this, "请先登陆", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        ;
    }

    PoiItem mPositionPoiItem;//起点poi
    PoiItem mDestinationPoiItem;//终点poi

    //获取选取的位置信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value = data.getStringExtra("key");
        if (value.equals("noChoice")) {
            return;
        }
        Bundle bundle = data.getBundleExtra("bundle");
        switch (resultCode) {
            case Str.REQUESTPOSITIONCODE:
                if (value.equals("myPosition")) {
                    mPositionPoiItem = bundle.getParcelable("PoiInfo");
                    Log.i(TAG, "onActivityResult: " + mPositionPoiItem.getTitle());
                    myPosition.setText(mPositionPoiItem.getTitle());
//                    client.setStartLatitude(mPositionPoiItem.getLatLonPoint().getLatitude());
//                    client.setStartLongitude(mPositionPoiItem.getLatLonPoint().getLongitude());
                }
                break;
            case Str.REQUESTDESTINATIONCODE:
                if (value.equals("myDestination")) {
                    mDestinationPoiItem = bundle.getParcelable("PoiInfo");
                    myDestination.setText(mDestinationPoiItem.getTitle());
                    //添加终点标记
                    addDestinationMarker(mDestinationPoiItem);
                    //终点坐标
                    double desLatitude = mDestinationPoiItem.getLatLonPoint().getLatitude();
                    double desLongitude = mDestinationPoiItem.getLatLonPoint().getLongitude();
//                    centerToMyLocation(aMap, mLocationClient, myOrientationListener, desLatitude, desLongitude);

//                    client.setEndLatitude(desLatitude);
//                    client.setEndLongitude(desLongitude);

                    //设置拼车按钮可见
                    if (tv_pinChe == null) {
                        initConfirmButtonView();
                    }
                    showPinChe();
                }
                break;
        }
    }

    BitmapDescriptor mDestinationMarker;

    /**
     * 添加目的地marker
     *
     * @param item MyCloudPoiInfo
     */
    private void addDestinationMarker(PoiItem item) {
        mDestinationMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_destination);
        aMap.clear();

        LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());

        MarkerOptions options = new MarkerOptions().position(latLng).icon(mDestinationMarker).zIndex(5);
        Marker marker = (Marker) aMap.addMarker(options);
    }

    LinearLayout ly_pinChe;//显示隐藏
    RelativeLayout rl_confirm;//显示隐藏
    TextView tv_pinChe, tv_buPinChe, tv_coupon, tv_confirm;

    private void hidePinChe(){
        ly_pinChe.setVisibility(View.GONE);
        rl_confirm.setVisibility(View.GONE);
    }
    private void showPinChe(){
        ly_pinChe.setVisibility(View.VISIBLE);
        rl_confirm.setVisibility(View.VISIBLE);
    }
    /**
     * 拼车及确认按钮
     */
    private void initConfirmButtonView() {
        tv_pinChe = (TextView) findViewById(R.id.tv_pinChe);
        tv_buPinChe = (TextView) findViewById(R.id.tv_buPinChe);
        tv_coupon = (TextView) findViewById(R.id.tv_coupon);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        //设置初始文本
        tv_pinChe.setText(Html.fromHtml("拼车<br/><big>约30元</big>"));
        tv_pinChe.setTextColor(getResources().getColor(R.color.colorPinChe));
        tv_buPinChe.setText(Html.fromHtml("不拼车<br/><big>约35元</big>"));
        tv_coupon.setText(Html.fromHtml("优惠券已抵扣 <font color='#Fe8a03'>5.0</font> 元"));
        tv_confirm.setText(Html.fromHtml("确认拼车<small><small>(您最多可带1人)</small></small>"));

        tv_pinChe.setOnClickListener(this);
        tv_buPinChe.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    MyLocationStyle myLocationStyle;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    if (mLocationClient == null) {
        mLocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        mLocationOption.setInterval(5000);
        mLocationClient.setLocationOption(mLocationOption);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        mLocationClient.startLocation();
    }
}

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    public static boolean isForeground = false;

    @Override
    protected void onStart() {
        super.onStart();
        //首次进入开启定位，以后返回界面可以由centerToMyLocation重新开启定位
        if (isFirstIn) {
            if (aMap != null) {
                aMap.setMyLocationEnabled(true);
                mLocationClient.startLocation();
                if (!mLocationClient.isStarted()) {
                    mLocationClient.startLocation();
                    //开启方向传感器
                    myOrientationListener.start();
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        aMap.setMyLocationEnabled(false);
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
        //关闭方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        myCameraChangeListener = null;
        myOrientationListener = null;
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
        deactivate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        isForeground = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
//        mLocationClient.startLocation();
        aMap.setMyLocationEnabled(true);
//        aMap.setMyLocationStyle(myLocationStyle);
    }

    private boolean isTwice = false;

    /**
     * 2次返回键退出程序
     *
     * @param keyCode keyCode
     * @param event   event
     * @return true or false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isForeground) {
                //处于可见状态
                if (isTwice) {
                    isTwice = !isTwice;
                    return super.onKeyDown(keyCode, event);
                } else {
                    isTwice = !isTwice;
                    if (ly_pinChe != null) {
                        hidePinChe();
                    }

                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isTwice = false;
                        }
                    }, 3000);
                    return false;
                }
            } else {
                if (!actionBar.isShowing()) {
                    //从fragment退回，显示actionbar
                    actionBar.show();
                }
                if (!myPosition.isEnabled()) {
                    enableClick();
                }
                isForeground = !isForeground;
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
