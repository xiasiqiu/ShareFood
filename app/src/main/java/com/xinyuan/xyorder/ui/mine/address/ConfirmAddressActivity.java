package com.xinyuan.xyorder.ui.mine.address;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.LocationManager;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XLoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;


public class ConfirmAddressActivity extends BaseActivity implements AMap.OnPOIClickListener,
        AMap.OnMarkerClickListener, GeocodeSearch.OnGeocodeSearchListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.amapView)
    MapView amapView;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_detail_address)
    TextView tv_detail_address;
    @BindView(R.id.tv_confirm_address)
    TextView tv_confirm_address;
    //    @BindView(R.id.iv_current_postion)
//    ImageView iv_current_postion;
    private AMap aMap;
    private AMapLocation mLocation;
    private Marker myLocMaker;
    boolean isFirstLoc = true; // 是否首次定位
    private Double lat;
    private Double lng;
    private String address;
    private MarkerOptions moptions;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private AddressInfo info;

    public static ConfirmAddressActivity newInstance() {
        ConfirmAddressActivity fragment = new ConfirmAddressActivity();
        return fragment;
    }

    @Override
    public void initView() {
        SystemBarHelper.immersiveStatusBar(this, 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(this, toolbar);
        tv_header_center.setText("确认收货地址");
        info = (AddressInfo) getIntent().getSerializableExtra("info");
        XPermission.requestPermissions(this, 100, new String[]{Manifest.permission
                .ACCESS_FINE_LOCATION}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                initSetting(savedInstanceState);
            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(ConfirmAddressActivity.this);
            }
        });
    }

    private void initSetting(Bundle saveInstanceState) {

        XLoadingDialog.with(this)
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框
        amapView.onCreate(saveInstanceState);// 此方法必须重写
        aMap = amapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-50);//隐藏logo
        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种

        if (XEmptyUtils.isEmpty(info)) {

            lat = DataUtil.getLocation(ConfirmAddressActivity.this).getLatitude();
            lng = DataUtil.getLocation(ConfirmAddressActivity.this).getLatitude();
            initLocation();
        } else {
            lat = info.getLatitude();
            lng = info.getLongitude();
            tv_address.setText(info.getSimpleAddress());
            tv_detail_address.setText(info.getDetailAddress());
            XLoadingDialog.with(ConfirmAddressActivity.this).dismiss();
        }
        if (!XEmptyUtils.isEmpty(lat) && !XEmptyUtils.isEmpty(lng)) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 15));//地图移
            if (myLocMaker == null) {
                addLoMarker(Double.valueOf(lat), Double.valueOf(lng));
            } else {
                LatLng ll = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                myLocMaker.setPosition(ll);
            }

        }
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。

        aMap.setOnPOIClickListener(this);
        aMap.setOnMarkerClickListener(this);
//        initLocation();
        initMapListener();
    }

    private void initLocation() {
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mLocationChangedListener = onLocationChangedListener;
            }

            @Override
            public void deactivate() {
                mLocationChangedListener = null;
            }
        });

        LocationManager.getInstance().setListener(new LocationManager.LocationListener() {
            @Override
            public void onSuccessLocationListener(AMapLocation aMapLocation) {
                // map view 销毁后不在处理新接收的位置
                if (aMapLocation == null) {
                    return;
                }
                if (mLocationChangedListener != null) {
                    mLocationChangedListener.onLocationChanged(aMapLocation);
                }
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                mLocation = aMapLocation;
                LatLng ll = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if (isFirstLoc) {
                    isFirstLoc = false;
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
                    if (myLocMaker == null) {
                        addLoMarker(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    } else {
                        myLocMaker.setPosition(ll);
                    }
                }
//                LocationManager.getInstance().stop();
                XLoadingDialog.with(ConfirmAddressActivity.this).dismiss();
            }
        });
        LocationManager.getInstance().start();

    }


    private void initMapListener() {

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                /*oldCenterPoint = cameraPosition.target;*/
            }

            @Override
            public void onCameraChangeFinish(final CameraPosition cameraPosition) {
                double lats = cameraPosition.target.latitude;
                double lngs = cameraPosition.target.longitude;
                lat = lats;
                lng = lngs;
                myLocMaker.setPosition(new LatLng(lats, lngs));
                myLocMaker.showInfoWindow();

                regeocodeSearch(cameraPosition.target.latitude, cameraPosition.target.longitude, 3000);
            }
        });

    }

    private void regeocodeSearch(double lat, double lng, float radius) {
        LatLonPoint point = new LatLonPoint(lat, lng);
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, radius, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (1000 == rCode) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            tv_address.setText(regeocodeAddress.getFormatAddress());
            tv_detail_address.setText(regeocodeAddress.getStreetNumber().getStreet() + regeocodeAddress.getStreetNumber().getNumber());
//            address = regeocodeAddress.getFormatAddress();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    /**
     * 添加当前定位marker
     */
    private void addLoMarker(double lat, double lng) {
//        if (lat == 0 || lng=0) {
//            return;
//        }
        Bitmap bMap = BitmapFactory.decodeResource(getResources(),
                R.drawable.mylocation);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        moptions = new MarkerOptions();
//        moptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rect_gray)));
        moptions.icon(des);
        moptions.anchor(0.5f, 0.5f);
        moptions.position(new LatLng(lat, lng));
        moptions.title("送到这里");
        myLocMaker = aMap.addMarker(moptions);
        myLocMaker.showInfoWindow();
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_confirm_address, R.id.iv_header_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_address:
                ScreenUtils.hideSoftInput(ConfirmAddressActivity.this);
                address = tv_address.getText().toString() + " " + tv_detail_address.getText().toString();
                AddressInfo info = new AddressInfo(address, lat, lng, tv_address.getText().toString(), tv_detail_address.getText().toString());
                EventBus.getDefault().postSticky(new AddressPageEvent(AddressPageEvent.CHOOSELOCATION, info));
                finish();
                break;
            case R.id.iv_header_left:
                ScreenUtils.hideSoftInput(ConfirmAddressActivity.this);
                finish();
                break;
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_confirm_address;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPOIClick(Poi poi) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        amapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        amapView.setVisibility(View.INVISIBLE);
        amapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        amapView.setVisibility(View.VISIBLE);
        amapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (amapView != null) {
            amapView.onDestroy();
        }
        if (null != LocationManager.getInstance()) {
            LocationManager.getInstance().stop();//销毁定位客户端，同时销毁本地定位服务。
        }
    }
}
