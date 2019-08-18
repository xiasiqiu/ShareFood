package com.xinyuan.xyorder.adapter.area;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.bean.CityBean;
import com.xinyuan.xyorder.common.bean.LoginInfo;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.LocationManager;
import com.xinyuan.xyorder.widget.MyGridView;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.permission.XPermission;

import org.greenrobot.eventbus.EventBus;

public class CityListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CityBean> mAllCityList;
    private List<CityBean> mHotCityList;
    public HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private String currentCity;//当前城市
    private TextView tvCurrentLocateCity;
    private TextView tvLocate;
    private final int VIEW_TYPE = 4;//view的类型个数

    public CityListAdapter(Context context, List<CityBean> allCityList,
                           List<CityBean> hotCityList, String currentCity) {
        this.mContext = context;
        this.mAllCityList = allCityList;
        this.mHotCityList = hotCityList;
        this.currentCity = currentCity;

        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[allCityList.size()];

        //这里的主要目的是将listview中要显示字母的条目保存下来，方便在滑动时获得位置，alphaIndexer在Acitivity有调用
        for (int i = 0; i < mAllCityList.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = getAlpha(mAllCityList.get(i).getPinyin());
            // 上一个汉语拼音首字母，如果不存在为" "
            String previewStr = (i - 1) >= 0 ? getAlpha(mAllCityList.get(i - 1).getPinyin()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(mAllCityList.get(i).getPinyin());
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
    }

    @Override
    public int getViewTypeCount() {

        return VIEW_TYPE;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 3 ? position : 3;
    }

    @Override
    public int getCount() {
        return mAllCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private LocationBean locationBean;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int viewType = getItemViewType(position);
        if (viewType == 0) {//view类型为0，也就是：当前定位城市的布局
            convertView = View.inflate(mContext, R.layout.item_location_city,
                    null);
            tvLocate = (TextView) convertView.findViewById(R.id.tv_locate);
            tvCurrentLocateCity = (TextView) convertView.findViewById(R.id.tv_current_locate_city);

            tvLocate.setText("当前定位城市");
            tvCurrentLocateCity.setVisibility(View.VISIBLE);
            tvCurrentLocateCity.setText(currentCity);
            if (!DataUtil.checkLocation(mContext)) {
                tvCurrentLocateCity.setText("点击重试");
                tvCurrentLocateCity.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initLocation();
                    }
                });
            } else {
                locationBean = DataUtil.getLocation(mContext);
                tvCurrentLocateCity.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        locationBean.setCurrentyCity(currentCity);
                        EventBus.getDefault().post(currentCity);
                        DataUtil.setLocation(mContext, locationBean);
                        ((Activity) mContext).onBackPressed();
                    }
                });
            }


        } else if (viewType == 1) {//热门城市
            convertView = View.inflate(mContext, R.layout.item_recent_visit_city, null);
            TextView tvRecentVisitCity = (TextView) convertView.findViewById(R.id.tv_recent_visit_city);
            tvRecentVisitCity.setText("热门城市");
            MyGridView gvRecentVisitCity = (MyGridView) convertView.findViewById(R.id.gv_recent_visit_city);
            gvRecentVisitCity.setAdapter(new HotCityAdapter(mContext, mHotCityList));
            gvRecentVisitCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    locationBean.setCurrentyCity(mHotCityList.get(i).getCityName());
                    DataUtil.setLocation(mContext, locationBean);
                    EventBus.getDefault().post(mHotCityList.get(i).getCityName());
                    ((Activity) mContext).onBackPressed();
                }
            });
        } else {//数据库中所有的城市的名字展示
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_city_list, null);
                viewHolder.tvAlpha = (TextView) convertView.findViewById(R.id.tv_alpha);
                viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city_name);
                viewHolder.llMain = (LinearLayout) convertView.findViewById(R.id.ll_main);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvCityName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationBean.setCurrentyCity(mAllCityList.get(position).getCityName());
                    DataUtil.setLocation(mContext, locationBean);
                    EventBus.getDefault().post(mAllCityList.get(position).getCityName());
                    ((Activity) mContext).onBackPressed();
                }
            });
            if (position >= 1) {
                viewHolder.tvCityName.setText(mAllCityList.get(position).getCityName());

                String currentStr = getAlpha(mAllCityList.get(position).getPinyin());
                String previewStr = (position - 1) >= 0 ? getAlpha(mAllCityList
                        .get(position - 1).getPinyin()) : " ";
                //如果当前的条目的城市名字的拼音的首字母和其前一条条目的城市的名字的拼音的首字母不相同，则将布局中的展示字母的TextView展示出来
                if (!previewStr.equals(currentStr)) {
                    viewHolder.tvAlpha.setVisibility(View.VISIBLE);
                    viewHolder.tvAlpha.setText(currentStr);
                } else {
                    viewHolder.tvAlpha.setVisibility(View.GONE);
                }
            }

        }

        return convertView;
    }


    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "热门";
        } else if (str.equals("2")) {
            return "全部";
        } else {
            return "#";
        }
    }

    class ViewHolder {
        TextView tvAlpha;
        TextView tvCityName;
        LinearLayout llMain;
    }

    private void initLocation() {
        XPermission.requestPermissions(mContext, 100, new String[]{Manifest.permission
                .ACCESS_FINE_LOCATION, Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                LocationManager.getInstance().start();
                LocationManager.getInstance().setListener(new LocationManager.LocationListener() {
                    @Override
                    public void onSuccessLocationListener(AMapLocation aMapLocation) {
                        if (aMapLocation != null) {
                            locationBean.setLatitude(aMapLocation.getLatitude());
                            locationBean.setLongitude(aMapLocation.getLongitude());
//                            locationBean.setCityId(aMapLocation.getCity());
                            locationBean.setCurrentyCity(aMapLocation.getCity());
                            DataUtil.setLocation(mContext, locationBean);
                            LocationManager.getInstance().stop();
                            notifyDataSetChanged();
                        }
                    }
                });

            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(mContext);
            }
        });

    }

}
