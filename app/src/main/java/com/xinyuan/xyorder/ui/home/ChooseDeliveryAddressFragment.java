package com.xinyuan.xyorder.ui.home;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.DeliveryAddressAdapter;
import com.xinyuan.xyorder.adapter.area.NearByPositionAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.home.LocationBean;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.bean.mine.AddressListInfo;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AroundStoreEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.LocationManager;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.utils.permission.XPermission;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/14
 */
public class ChooseDeliveryAddressFragment extends BaseFragment implements PoiSearch.OnPoiSearchListener {

    @BindView(R.id.rv_choose_address)
    RecyclerView rv_choose_address;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lodingView)
    XLoadingView lodingView;
    @BindView(R.id.et_search)
    EditTextWithDel etSearch;
    @BindView(R.id.rv_search_history)
    RecyclerView rv_search_history;
    RecyclerView rv_nearby;
    RelativeLayout rl_current_city;
    TextView tv_current_locate_city;
    DeliveryAddressAdapter deliveryAddressAdapter;
    List<AddressInfo> datas = new ArrayList<>();
    double lat, lng;
    String city;
    PoiSearch poiSearch;
    NearByPositionAdapter nearByPositionAdapter, nearByPositionAdapter1;
    List<PoiItem> searchCityList = new ArrayList<>();
    List<PoiItem> nearByCityList = new ArrayList<>();
    ProgressBar pb_progress;
    ImageView iv_relocation;
    TextView tv_relocation;
    TextView tv_delivery_address;
    View v_delivery_address;

    public static ChooseDeliveryAddressFragment newInstance() {
        ChooseDeliveryAddressFragment fragment = new ChooseDeliveryAddressFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("选择收货地址");
        CommUtil.setBack(_mActivity, iv_header_left);
        rv_search_history.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        LocationManager.getInstance().start();
        deliveryAddressAdapter = new DeliveryAddressAdapter(R.layout.item_delivery_address, datas);
        rv_choose_address.setAdapter(deliveryAddressAdapter);
        rv_choose_address.setLayoutManager(new LinearLayoutManager(getActivity()));

        nearByPositionAdapter1 = new NearByPositionAdapter(R.layout.item_nearby_position, searchCityList);
        rv_search_history.setAdapter(nearByPositionAdapter1);
        rv_search_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(!XEmptyUtils.isEmpty(DataUtil.getToken(getActivity()))) {
            getAddressList(1);
        }else{

        }
        addBottom();
        addTop();
    }

    private void search() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() == null || "".equals(s.toString())) {
                    rv_choose_address.setVisibility(View.VISIBLE);
                    rv_search_history.setVisibility(View.GONE);
                } else {
                    searchCityList.clear();
                    rv_choose_address.setVisibility(View.GONE);
                    rv_search_history.setVisibility(View.VISIBLE);
                    getResultPositionList(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addTop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_location_city, (ViewGroup) rv_choose_address.getParent(), false);
        deliveryAddressAdapter.addHeaderView(view);
        rl_current_city=view.findViewById(R.id.rl_current_city);
        tv_current_locate_city=view.findViewById(R.id.tv_current_locate_city);
        pb_progress=view.findViewById(R.id.pb_progress);
        tv_relocation=view.findViewById(R.id.tv_relocation);
        iv_relocation=view.findViewById(R.id.iv_relocation);
        v_delivery_address=view.findViewById(R.id.v_delivery_address);
        tv_delivery_address=view.findViewById(R.id.tv_delivery_address);
        initLocation();
        if(XEmptyUtils.isEmpty(DataUtil.getLocation(getActivity()))||XEmptyUtils.isEmpty(DataUtil.getLocation(getActivity()).getCurrentyCity())) {
            tv_current_locate_city.setText("未获取到位置");
        }else{
            tv_current_locate_city.setText(DataUtil.getLocation(getActivity()).getCurrentyCity());
        }

        tv_relocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb_progress.setVisibility(View.VISIBLE);
                iv_relocation.setVisibility(View.GONE);
                initLocation();
            }
        });
        tv_current_locate_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(DataUtil.getLocation(getActivity()).getCurrentyCity());
                EventBus.getDefault().postSticky(new AroundStoreEven(AroundStoreEven.AREAR_SEARCH));

                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void initListener() {
        deliveryAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressInfo addressInfo = (AddressInfo) adapter.getData().get(position);
                LocationBean locationBean = DataUtil.getLocation(getActivity());
                locationBean.setLatitude(addressInfo.getLatitude());
                locationBean.setLongitude(addressInfo.getLongitude());
                DataUtil.setLocation(getActivity(), locationBean);

                if(!XEmptyUtils.isEmpty(((AddressInfo) adapter.getData().get(position)).getSimpleAddress())){
                    EventBus.getDefault().post(((AddressInfo) adapter.getData().get(position)).getSimpleAddress());
                }else {
                    EventBus.getDefault().post(((AddressInfo) adapter.getData().get(position)).getAddress());
                }
                EventBus.getDefault().postSticky(new AroundStoreEven(AroundStoreEven.AREAR_SEARCH));
                _mActivity.onBackPressed();

            }
        });
        nearByPositionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {//附近地址item
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiItem poiItem = (PoiItem) adapter.getData().get(position);
                LocationBean locationBean = DataUtil.getLocation(getActivity());
                locationBean.setLatitude(poiItem.getLatLonPoint().getLatitude());
                locationBean.setLongitude(poiItem.getLatLonPoint().getLongitude());
                DataUtil.setLocation(getActivity(), locationBean);

                EventBus.getDefault().post(poiItem.getTitle());
                EventBus.getDefault().postSticky(new AroundStoreEven(AroundStoreEven.AREAR_SEARCH));
                _mActivity.onBackPressed();
            }
        });

        nearByPositionAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {//搜索地址item
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PoiItem poiItem = (PoiItem) adapter.getData().get(position);
                LocationBean locationBean = DataUtil.getLocation(getActivity());
                locationBean.setLatitude(poiItem.getLatLonPoint().getLatitude());
                locationBean.setLongitude(poiItem.getLatLonPoint().getLongitude());
                DataUtil.setLocation(getActivity(), locationBean);

                EventBus.getDefault().post(poiItem.getTitle());
                EventBus.getDefault().postSticky(new AroundStoreEven(AroundStoreEven.AREAR_SEARCH));
                _mActivity.onBackPressed();
            }
        });
        search();
    }

    private void addBottom() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_delivery_address_bottom, (ViewGroup) rv_choose_address.getParent(), false);
        deliveryAddressAdapter.addFooterView(view);
        rv_nearby = view.findViewById(R.id.rv_nearby);
        PoiSearch.Query query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页

        nearByPositionAdapter = new NearByPositionAdapter(R.layout.item_nearby_position, nearByCityList);
        rv_nearby.setAdapter(nearByPositionAdapter);
        rv_nearby.setLayoutManager(new LinearLayoutManager(getActivity()));

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);

    }

    /**
     * 获取用户收货地址列表
     *
     * @param pageId
     */
    public void getAddressList(final int pageId) {
        OkGo.<HttpResponseData<AddressListInfo>>get(Constants.API_ADD_USER)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .params("pageId", pageId)
                .params("pageSize", Constants.PAGE_SIZE)
                .execute(new JsonCallback<HttpResponseData<AddressListInfo>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<AddressListInfo>> response) {
//                        lodingView.showContent();
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showAddress(response.body().getData().getList());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<AddressListInfo>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    private void getResultPositionList(String key) {
        PoiSearch.Query query = new PoiSearch.Query(key, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页
        query.setCityLimit(true);
        PoiSearch poiSearch1 = new PoiSearch(getActivity(), query);

        poiSearch1.searchPOIAsyn();// 异步搜索

        poiSearch1.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if(poiResult.getPois()==null||poiResult.getPois().size()==0){
                    XToast.info("没有搜索结果");
                    return;
                }
                searchCityList.addAll(poiResult.getPois());
                nearByPositionAdapter1.notifyDataSetChanged();
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });


    }

    private void showAddress(List<AddressInfo> addressInfos){
        if(addressInfos!=null&&addressInfos.size()>0) {
            v_delivery_address.setVisibility(View.VISIBLE);
            tv_delivery_address.setVisibility(View.VISIBLE);
            datas.addAll(addressInfos);
            deliveryAddressAdapter.notifyDataSetChanged();
        }

    }

    private void initLocation() {
        XPermission.requestPermissions(getActivity(), 100, new String[]{Manifest.permission
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
                            pb_progress.setVisibility(View.GONE);
                            iv_relocation.setVisibility(View.VISIBLE);
                            LocationManager.getInstance().stop();

                            LocationBean locationBean = DataUtil.getLocation(getActivity());
                            locationBean.setLatitude(aMapLocation.getLatitude());
                            locationBean.setLongitude(aMapLocation.getLongitude());
                            locationBean.setCurrentyCity(aMapLocation.getAoiName());

                            String s = aMapLocation.getAdCode();
                            s = s.substring(0, 4) + "00";
//                            locationBean.setCityId(s);
                            DataUtil.setLocation(getActivity(), locationBean);

                            tv_current_locate_city.setText(aMapLocation.getAoiName());
                            city = aMapLocation.getCity();
                            lat = aMapLocation.getLatitude();
                            lng = aMapLocation.getLongitude();
//
                            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat,
                                    lng), 5000));
                            poiSearch.searchPOIAsyn();// 异步搜索
                        }
                    }
                });

            }

            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(getActivity());
            }
        });
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_choose_address;
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        lodingView.showContent();
        if(!XEmptyUtils.isEmpty(poiResult.getPois())) {
            nearByCityList.clear();
            nearByCityList.addAll(poiResult.getPois());
            nearByPositionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
