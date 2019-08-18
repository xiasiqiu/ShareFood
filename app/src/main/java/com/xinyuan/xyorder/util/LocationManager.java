package com.xinyuan.xyorder.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.youth.xframe.widget.XToast;

/**
 * Just do you things!
 * Title.
 * 高德地图
 * Created by Y-bin on 16/10/31.
 */
public class LocationManager {

    private Context context;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    AMapLocationClientOption mOption;

    //成都经纬度
    public static final double LAT = 30.65748007;
    public static final double LNG = 104.06576693;

    private LocationListener mListener;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;

    private static final int STROKE_COLOR = Color.argb(180, 123, 104, 238);
    private static final int FILL_COLOR = Color.argb(30, 132, 112, 255);

    final String error = "请到http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/查看错误码说明.";

    private static class LocationManagerHolder {
        private static final LocationManager INSTANCE = new LocationManager();
    }

    private LocationManager() {
    }

    public static final LocationManager getInstance() {
        return LocationManager.LocationManagerHolder.INSTANCE;
    }

    /**
     * 设置默认的定位参数
     *
     * @return
     */
    public AMapLocationClient getAMapLocationClient(Context context) {
        this.context = context;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);

        }
        if (mOption == null) {
            mOption = new AMapLocationClientOption();
//            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
//            mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//            mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
//            mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
//            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//            mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//            mOption.setWifiScan(true);//设置是否强制刷新WIFI，默认为true，强制刷新。
        }

        mLocationClient.setLocationOption(mOption);
        return mLocationClient;
    }

    /**
     * 设置地图变量
     *
     * @param aMap
     */
    public void setAmap(AMap aMap) {
        /**
         * 设置地图定位监听
         */
        aMap.setLocationSource(new LocationSource() {
            /**
             * 激活定位
             */
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mLocationChangedListener = onLocationChangedListener;
            }

            /**
             * 停止定位
             */
            @Override
            public void deactivate() {
                mLocationChangedListener = null;
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(104.072258, 30.663422), 13));//默认地图移到成都市,缩放级别3-19
    }

    /**
     * 自定义定位点
     *
     * @param aMap
     * @param icon 定位点图标
     */
    public void etupLocationStyle(AMap aMap, int icon) {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(icon));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(3);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.anchor(0.5f, 0.5f);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e("AmapError", "location success: "+aMapLocation.getAddress());
                    if (mListener != null) {
                        mListener.onSuccessLocationListener(aMapLocation);
                    }
                    if (mLocationChangedListener != null) {
                        mLocationChangedListener.onLocationChanged(aMapLocation);
                    }
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    String msg = aMapLocation.getErrorInfo().replace(error,"");
                    XToast.error(msg);
                    stop();
                }
            }else{
                Log.e("AmapError", "location Error: aMapLocation null");
            }
        }
    };

    public void start() {
                    /*if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            ToastUtils.ToastShort(context,"定位权限未被允许,请去权限管理设置");
                            return;
                        }
                    }*/
        if (mLocationClient != null) {
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.startLocation();
        }

    }

    public void stop() {
        if (mLocationListener != null) {
            mLocationClient.unRegisterLocationListener(mLocationListener);
        }
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    public void destroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    /**
     * 定位回调监听
     */
    public interface LocationListener {
        void onSuccessLocationListener(AMapLocation aMapLocation);
    }

    public void setListener(LocationListener mListener) {
        this.mListener = mListener;
    }

    /*public void unregisterListener(){
        if(mLocationListener!=null){
            mLocationClient.unRegisterLocationListener(mLocationListener);
        }
    }*/

}
