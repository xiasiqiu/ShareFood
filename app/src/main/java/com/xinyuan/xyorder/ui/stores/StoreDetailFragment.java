package com.xinyuan.xyorder.ui.stores;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.storedetail.StoreActAdapter;
import com.xinyuan.xyorder.adapter.storedetail.StoreInfoImgAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.store.store.StoreDetail;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.widget.GoToNailPopupWindow;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.permission.XPermission;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/18 20:20
 * 商品详情页
 */
public class StoreDetailFragment extends BaseFragment {


    @BindView(R.id.rv_store_acy)
    RecyclerView rv_store_acy;
    RecyclerView rv_store_info;
    TextView tv_store_address;
    TextView tv_store_time;
    View line;
    TextView tv_empty_store_pic;

    private StoreInfoImgAdapter storeInfoImgAdapter;
    private StoreActAdapter storeActAdapter;
    private List<String> listdata;
    private StoreDetail storeDetail;
    private GoToNailPopupWindow goToNailPopupWindow;

    public static StoreDetailFragment newInstance(StoreDetail storeDetail) {
        StoreDetailFragment fragment = new StoreDetailFragment();
        fragment.storeDetail = storeDetail;
        return fragment;
    }

    @Override
    public void initView(View rootView) {

    }


    @Override
    public void initData() {
        if (!XEmptyUtils.isEmpty(storeDetail)) {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_store_acy.setLayoutManager(manager);
            storeActAdapter = new StoreActAdapter(R.layout.item_store_activity, storeDetail.getShopActive());
            rv_store_acy.setAdapter(storeActAdapter);
            addTop();
        }
    }

    private void addTop() {
        View topView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_store_detail_top, (ViewGroup) rv_store_acy.getParent(), false);
        tv_store_address = topView.findViewById(R.id.tv_store_address);
        tv_store_time = topView.findViewById(R.id.tv_store_time);
        tv_empty_store_pic = topView.findViewById(R.id.tv_empty_store_pic);
        line = topView.findViewById(R.id.line);
        tv_store_address.setText(storeDetail.getAddress());
        tv_store_time.setText("经营时间：" + storeDetail.getBusBeginTime() + "-" + storeDetail.getBusEndTime());
        rv_store_info = topView.findViewById(R.id.rv_store_info);
        ImageView iv_store_phone = topView.findViewById(R.id.iv_store_phone);

        if (!XEmptyUtils.isEmpty(storeDetail.getCarousel())) {
            tv_empty_store_pic.setVisibility(View.GONE);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_store_info.setLayoutManager(manager);
            storeInfoImgAdapter = new StoreInfoImgAdapter(R.layout.item_image, storeDetail.getCarousel());
            rv_store_info.setAdapter(storeInfoImgAdapter);

        } else {
            tv_empty_store_pic.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
        }

        final LatLng latLng = new LatLng(storeDetail.getLatitude().doubleValue(), storeDetail.getLongitude().doubleValue());
        final String address = storeDetail.getAddress();
        tv_store_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNailPopupWindow = new GoToNailPopupWindow(_mActivity);
                goToNailPopupWindow.showPopupWindow(view);
                goToNailPopupWindow.setOnNailListener(new GoToNailPopupWindow.onNailListener() {

                    @Override
                    public void onGaodeListener() {
                        mapToNail(address, latLng, false);
                    }

                    @Override
                    public void onBaiduListener() {
                        // TODO Auto-generated method stub
                        mapToNail(address, latLng, true);
                    }
                });
            }
        });
        iv_store_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XPermission.requestPermissions(getActivity(), 100, new String[]{Manifest.permission
                        .CALL_PHONE}, new XPermission.OnPermissionListener() {
                    //权限申请成功时调用
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + storeDetail.getPhoneNum());
                        intent.setData(data);
                        startActivity(intent);
                    }

                    //权限被用户禁止时调用
                    @Override
                    public void onPermissionDenied() {
                        //给出友好提示，并且提示启动当前应用设置页面打开权限
                        XPermission.showTipsDialog(getActivity());
                    }
                });
            }
        });

        storeActAdapter.addHeaderView(topView);
        rv_store_acy.scrollToPosition(0);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_store_detail;
    }

    public void mapToNail(String destAddr, LatLng destll, Boolean isBaidu) {
        if (isBaidu) {
            try {
                String baiduStr = "intent://map/direction?" + "destination=latlng:" + destll.latitude + ","
                        + destll.longitude + "|name:" + destAddr + "&mode=driving#Intent;"
                        + "scheme=bdapp;package=com.baidu.BaiduMap;end";
                Intent intent = Intent.getIntent(baiduStr);
                if (CommUtil.isInstallByPackageName("com.baidu.BaiduMap")) {
                    startActivity(intent); // 启动调用
                } else {
                    Toast.makeText(_mActivity, "未安装百度地图,请先安装！", Toast.LENGTH_SHORT).show();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String gaodeStr = "androidamap://navi?sourceApplication=" + "共享点餐" + "&lat=" + destll.latitude + "&lon="
                        + destll.longitude + "&dev=1&style=2" + "&pkg=com.autonavi.minimap";
                Intent intent = Intent.getIntent(gaodeStr);
                if (CommUtil.isInstallByPackageName("com.autonavi.minimap")) {
                    startActivity(intent);
                } else {
                    Toast.makeText(_mActivity, "未安装高德地图,请先安装！", Toast.LENGTH_SHORT).show();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
