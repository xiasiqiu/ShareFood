package com.xinyuan.xyorder.ui.buy;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.BuyContactBean;
import com.xinyuan.xyorder.adapter.buy.ContactAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.AddressInfo;
import com.xinyuan.xyorder.common.bean.order.OrderContactBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.AddressPageEvent;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.ui.mine.address.CreateAddressFragment;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.ScreenUtils;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.xinyuan.xyorder.widget.RecyclerViewNoBugLinearLayoutManager;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XLoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by f-x on 2017/10/2716:00
 */

public class ChooesContactFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rv_contact)
    RecyclerView rv_contact;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.rl_add_address)
    RelativeLayout rl_add_address;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    private long shopId;                             //店铺ID
    private String orderType;                           //1-外卖  2-预定
    private ContactAdapter contactAdapter;
    private ContactAdapter notcontactAdapter;

    public static ChooesContactFragment newInstance(String orderType, long shopId) {
        ChooesContactFragment chooesContactFragment = new ChooesContactFragment();
        chooesContactFragment.orderType = orderType;
        chooesContactFragment.shopId = shopId;
        return chooesContactFragment;
    }


    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("选择收货地址");
        XLoadingDialog.with(getActivity())
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框

        rl_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(CreateAddressFragment.newInstance(null));
                ScreenUtils.hideSoftInput(_mActivity);
            }
        });
        CommUtil.setBack(_mActivity, iv_header_left);

    }

    @Override
    public void initData() {
        if (orderType.equals(Constants.ORDER_TAKEOUT)) {
            getContactStore(shopId);
        } else {
            getContactAll();
        }

    }

    private void showContact(BuyContactBean data) {
        if (XEmptyUtils.isEmpty(data.getCanNotUseContactList()) && XEmptyUtils.isEmpty(data.getCanUseContactList())) {
            tv_empty.setVisibility(View.VISIBLE);
            XLoadingDialog.with(getActivity()).dismiss();
            return;
        }

        if (!XEmptyUtils.isEmpty(data.getCanUseContactList())) {
            tv_empty.setVisibility(View.GONE);
            ll_empty.setVisibility(View.GONE);

            rv_contact.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_white));
            data.getCanUseContactList().get(0).setCheck(true);
            this.contactAdapter = new ContactAdapter(R.layout.item_contact, 1, data.getCanUseContactList());
            RecyclerViewNoBugLinearLayoutManager linearLayoutManager = new RecyclerViewNoBugLinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(1);
            rv_contact.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 2, getActivity().getResources().getColor(R.color.colorLine)));
            rv_contact.setLayoutManager(linearLayoutManager);
            this.rv_contact.setAdapter(this.contactAdapter);

            if (!XEmptyUtils.isEmpty(data.getCanNotUseContactList())) {
                this.notcontactAdapter = new ContactAdapter(R.layout.item_contact, 2, data.getCanNotUseContactList());
                View bootomView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contact_chooes_bottom, (ViewGroup) rv_contact.getParent(), false);
                RecyclerView rv_bootom = bootomView.findViewById(R.id.rv_not_contact);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                linearLayoutManager1.setOrientation(1);
                rv_bootom.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_gray));
                rv_bootom.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 2, getActivity().getResources().getColor(R.color.colorLine)));
                rv_bootom.setLayoutManager(linearLayoutManager1);
                rv_bootom.setAdapter(this.notcontactAdapter);
                contactAdapter.addFooterView(bootomView);
            }


        } else {
            tv_empty.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.VISIBLE);
            this.contactAdapter = new ContactAdapter(R.layout.item_contact, 2, data.getCanNotUseContactList());
            RecyclerViewNoBugLinearLayoutManager linearLayoutManager = new RecyclerViewNoBugLinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(1);
            rv_contact.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_gray));
            rv_contact.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
            rv_contact.setLayoutManager(linearLayoutManager);
            this.rv_contact.setAdapter(this.contactAdapter);


        }
        XLoadingDialog.with(getActivity()).dismiss();


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAddressEven(AddressPageEvent event) {
        if (event.getFlag().equals(AddressPageEvent.EDITE)) {
            int pos = (int) event.getObj();
            OrderContactBean orderContactBean = contactAdapter.getItem(pos);
            AddressInfo addressInfo = new AddressInfo(orderContactBean.getAddress(), orderContactBean.getContactName(), orderContactBean.getContactPhone(), orderContactBean.getGender(), orderContactBean.getLatitude(), orderContactBean.getLongitude(), orderContactBean.getContactId(), orderContactBean.getHouseNumber(), orderContactBean.getDetailAddress(), orderContactBean.getSimpleAddress());
            start(CreateAddressFragment.newInstance(addressInfo));
        } else if (event.getFlag().equals(AddressPageEvent.NOEDITE)) {
            int pos = (int) event.getObj();
            OrderContactBean orderContactBean = notcontactAdapter.getItem(pos);
            AddressInfo addressInfo = new AddressInfo(orderContactBean.getAddress(), orderContactBean.getContactName(), orderContactBean.getContactPhone(), orderContactBean.getGender(), orderContactBean.getLatitude(), orderContactBean.getLongitude(), orderContactBean.getContactId(), orderContactBean.getHouseNumber(), orderContactBean.getDetailAddress(), orderContactBean.getSimpleAddress());
            start(CreateAddressFragment.newInstance(addressInfo));
        } else if (event.getFlag().equals(AddressPageEvent.FALSH)) {
            if (orderType.equals(Constants.ORDER_TAKEOUT)) {
                getContactStore(shopId);
            } else {
                getContactAll();
            }
        } else if (event.getFlag().equals(AddressPageEvent.CHOOES)) {
            int pos = (int) event.getObj();
            OrderContactBean orderContactBean = contactAdapter.getItem(pos);
            AddressInfo addressInfo = new AddressInfo(orderContactBean.getAddress(), orderContactBean.getContactName(), orderContactBean.getContactPhone(), orderContactBean.getGender(), orderContactBean.getLatitude(), orderContactBean.getLongitude(), orderContactBean.getContactId(), orderContactBean.getHouseNumber(), orderContactBean.getDetailAddress(), orderContactBean.getSimpleAddress());

            EventBus.getDefault().post(new BuyBusEven(BuyBusEven.CHOOESEUSER, addressInfo));
            _mActivity.onBackPressed();
        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_contact_chooes;
    }

    public void getContactAll() {

        XLoadingDialog.with(getActivity())
                .setCanceled(false, true) //设置手动不可取消
                .setOrientation(XLoadingDialog.VERTICAL) //设置显示方式（水平或者垂直）
                .setBackgroundColor(getResources().getColor(R.color.bg_white))//设置对话框背景
                .setMessageColor(Color.BLACK)//设置消息字体颜色
                .show();//显示对话框

        OkGo.<HttpResponseData<BuyContactBean>>get(Constants.API_RES_CONTACT)
                .tag(this).headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<BuyContactBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<BuyContactBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showContact(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<BuyContactBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });
    }

    public void getContactStore(long shopId) {
        OkGo.<HttpResponseData<BuyContactBean>>get(Constants.API_SEND_CONTACT + shopId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<BuyContactBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<BuyContactBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showContact(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<BuyContactBean>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
