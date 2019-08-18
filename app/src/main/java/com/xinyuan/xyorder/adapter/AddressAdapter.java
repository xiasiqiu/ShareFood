package com.xinyuan.xyorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.youth.xframe.utils.XEmptyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class AddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressInfo> datas;

    public AddressAdapter(Context mContext, List<AddressInfo> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_address, null, false);
            viewHolder = new ViewHolder();
            viewHolder.initView(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_tel.setText(datas.get(i).getContactPhone());
        viewHolder.tv_sex.setText(datas.get(i).getGender().equals("MALE") ? "男" : "女");
        if (XEmptyUtils.isEmpty(datas.get(i).getHouseNumber())) {
            viewHolder.tv_address.setText(datas.get(i).getAddress());
        } else {
            viewHolder.tv_address.setText(datas.get(i).getAddress() + " " + datas.get(i).getHouseNumber());
        }
        if(!XEmptyUtils.isEmpty(datas.get(i).getContactName())){
            viewHolder.tv_user_name.setText(datas.get(i).getContactName());

        }
        viewHolder.iv_address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AddressPageEvent(AddressPageEvent.EDITE, i));
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tv_address;
        TextView tv_user_name;
        TextView tv_sex;
        TextView tv_tel;
        ImageView iv_address_edit;

        private void initView(View v) {
            tv_address = v.findViewById(R.id.tv_address);
            tv_user_name = v.findViewById(R.id.tv_user_name);
            tv_sex = v.findViewById(R.id.tv_sex);
            tv_tel = v.findViewById(R.id.tv_tel);
            iv_address_edit = v.findViewById(R.id.iv_address_edit);
        }
    }
}
