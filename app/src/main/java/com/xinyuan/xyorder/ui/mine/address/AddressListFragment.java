package com.xinyuan.xyorder.ui.mine.address;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.AddressAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.bean.mine.AddressListInfo;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fx on 2017/10/15.
 */

public class AddressListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.sml_address_list)
    SwipeMenuListView sml_address_list;
    //    @BindView(R.id.swipeLayout)
//    SwipeRefreshPlus mSwipeRefreshPlus;
    @BindView(R.id.rl_add_address)
    RelativeLayout rl_add_address;
    @BindView(R.id.loadingView)
    XLoadingView loadingView;
    View noMoreView;
    private SwipeMenuCreator creator;
    private AddressAdapter addressAdapter;
    private List<AddressInfo> data = new ArrayList<>();
    private int pageId = 1;
    private boolean fromMe;

    public static AddressListFragment newInstance(boolean fromMe) {
        AddressListFragment fragment = new AddressListFragment();
        fragment.fromMe = fromMe;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("收货地址");
        setSwipeMenuCreator();
        sml_address_list.setMenuCreator(creator);
    }

    @Override
    public void initData() {
        getAddressList(pageId);
    }

    @Override
    public void initListener() {
        rl_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(CreateAddressFragment.newInstance(null));
                ScreenUtils.hideSoftInput(_mActivity);
            }
        });
        sml_address_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                deleteAddress(((AddressInfo) addressAdapter.getItem(position)).getContactId());
                return false;
            }
        });

        sml_address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!fromMe) {
                    EventBus.getDefault().postSticky(new BuyBusEven(BuyBusEven.CHOOESEUSER, ((AddressInfo) addressAdapter.getItem(i))));
                    _mActivity.onBackPressed();
                }
            }
        });
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }


    private void setSwipeMenuCreator() {
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                setSwipeDeleteButton(menu);
            }

            private void setSwipeDeleteButton(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.red)));
                // set item width
                deleteItem.setWidth(CommUtil.dip2px(getActivity(), 80));
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

    }

    /**
     * 显示收货地址
     *
     * @param body
     */
    private void showAddressList(AddressListInfo body) {
        if (XEmptyUtils.isEmpty(addressAdapter)) {
            if (XEmptyUtils.isEmpty(body)) {
                loadingView.showEmpty();
            } else {
                data.addAll(body.getList());
                addressAdapter = new AddressAdapter(getActivity(), data);
                sml_address_list.setAdapter(addressAdapter);
                loadingView.showContent();
            }
        } else {
            if (XEmptyUtils.isEmpty(body)) {
                if (pageId == 1) {
                    loadingView.showEmpty();
                }
            } else {
                if (pageId == 1) {
                    data.clear();
                    data.addAll(body.getList());
                    addressAdapter.notifyDataSetChanged();
//                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    data.addAll(body.getList());
                    addressAdapter.notifyDataSetChanged();
//                    mSwipeRefreshLayout.setRefreshing(false);
                }
                loadingView.showContent();
            }
        }

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
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showAddressList(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<AddressListInfo>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);

                    }
                });
    }

    /**
     * 刪除收貨地址
     *
     * @param contactId
     */
    public void deleteAddress(long contactId) {
        OkGo.<HttpResponseData<Void>>delete(Constants.API_ADD_USER + "/" + contactId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "删除中...") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            XToast.info("删除成功");
                            pageId = 1;
                            getAddressList(pageId);
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);

                    }
                });

    }


    @Subscribe
    public void onAddressEven(AddressPageEvent event) {
        if (event.getFlag().equals(AddressPageEvent.EDITE)) {
            int pos = (int) event.getObj();
            start(CreateAddressFragment.newInstance((AddressInfo) addressAdapter.getItem(pos)));
        } else if (event.getFlag().equals(AddressPageEvent.FALSH)) {
            onRefresh();

        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId = 1;
                getAddressList(pageId);
            }
        }, 500);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_address;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onStart() {
        registerEventBus(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
