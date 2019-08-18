package com.xinyuan.xyorder.adapter.area;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.common.bean.CityBean;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

public class SearchResultAdapter extends BaseAdapter {

    private List<CityBean> mSearchList;
    private Context mContext;
    private LayoutInflater mInflater;

    public SearchResultAdapter(Context context, List<CityBean> searchList) {
        this.mSearchList = searchList;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mSearchList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_search_list, null);
            viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCityName.setText(mSearchList.get(position).getCityName());
        viewHolder.tvCityName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ScreenUtils.hideSoftInput((Activity) mContext);
                LocationBean locationBean;
                if (!DataUtil.checkLocation(mContext)) {
                    locationBean = new LocationBean();
                } else {
                    locationBean = DataUtil.getLocation(mContext);
                }

                locationBean.setCurrentyCity(mSearchList.get(position).getCityName());
                DataUtil.setLocation(mContext, locationBean);
                EventBus.getDefault().post(mSearchList.get(position).getCityName());
                ((Activity) mContext).onBackPressed();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvCityName;
    }

}
