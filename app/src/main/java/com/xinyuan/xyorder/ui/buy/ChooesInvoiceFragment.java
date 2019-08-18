package com.xinyuan.xyorder.ui.buy;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.buy.InVoiceAdapter;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.buy.InVoiceBean;
import com.xinyuan.xyorder.common.bean.buy.MyInvoiceData;
import com.xinyuan.xyorder.common.even.BuyBusEven;
import com.xinyuan.xyorder.common.even.InvoiceEven;
import com.xinyuan.xyorder.mvp.contract.ChooesInvoiceView;
import com.xinyuan.xyorder.mvp.presenter.ChooesInvoicePresenter;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.RecycleViewDivider;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by f-x on 2017/11/2215:25
 */

public class ChooesInvoiceFragment extends BaseFragment<ChooesInvoicePresenter> implements ChooesInvoiceView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_inboice)
    RecyclerView rv_inboice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.tv_header_right)
    TextView tv_header_right;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rl_add_invoice)
    RelativeLayout rl_add_invoice;
    @BindView(R.id.loadingView)
    XLoadingView loadingView;

    public static ChooesInvoiceFragment newInstance() {
        ChooesInvoiceFragment chooesInvoiceFragment = new ChooesInvoiceFragment();
        return chooesInvoiceFragment;
    }

    @Override
    public void initListener() {
        super.initListener();
        rl_add_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(CreateInvoiceFragment.newInstance(null));
            }
        });
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("选择发票信息");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(1);
        rv_inboice.setLayoutManager(layoutManager);
    }

    private InVoiceAdapter inVoiceAdapter;
    public static boolean notUserRed = true;
    public static TextView tv_invoice_no;
    private int page = 1;


    @Override
    public void initData() {
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(1);
        rv_inboice.setLayoutManager(linearLayoutManager);
        mPresenter.getInvoiceList(page);

    }

    @Override
    public void showInvoiceList(MyInvoiceData data) {

        if (XEmptyUtils.isEmpty(inVoiceAdapter)) {
            this.inVoiceAdapter = new InVoiceAdapter(R.layout.fragment_invoice_item, data.getList());
            rv_inboice.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
            inVoiceAdapter.setAutoLoadMoreSize(3);
            this.rv_inboice.setAdapter(this.inVoiceAdapter);
            addTop();
            loadingView.showContent();
            page++;

        } else {
            if (XEmptyUtils.isEmpty(data.getList())) {
                if (page == 1) {
                    loadingView.showEmpty();
                } else {
                    inVoiceAdapter.loadMoreEnd();
                }
            } else {
                if (page == 1) {
                    inVoiceAdapter.setNewData(data.getList());
                    inVoiceAdapter.setEnableLoadMore(true);
                } else {
                    inVoiceAdapter.addData(data.getList());
                    inVoiceAdapter.loadMoreComplete();
                }
                loadingView.showContent();

            }
            page++;
        }


    }

    /**
     * 当页面显示时检查是否有通知
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    private void addTop() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_invoice_top, (ViewGroup) rv_inboice.getParent(), false);
        tv_invoice_no = view.findViewById(R.id.tv_invoice_no);
        tv_invoice_no.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (notUserRed) {
                    notUserRed = false;
                    tv_invoice_no.setActivated(false);
                    EventBus.getDefault().post(new BuyBusEven(BuyBusEven.NOCHOOESEINVOICE, ""));
                    _mActivity.onBackPressed();
                } else {
                    for (InVoiceBean inVoiceBean : inVoiceAdapter.getData()) {
                        if (inVoiceBean.isCheck()) {
                            inVoiceBean.setCheck(false);
                        }
                    }
                    inVoiceAdapter.notifyDataSetChanged();
                    notUserRed = true;
                    tv_invoice_no.setActivated(true);
                    EventBus.getDefault().post(new BuyBusEven(BuyBusEven.NOCHOOESEINVOICE, ""));
                    _mActivity.onBackPressed();


                }
            }
        });
        inVoiceAdapter.addHeaderView(view);
        loadingView.showContent();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void InvoiceEven(InvoiceEven invoiceEven) {
        if (invoiceEven.getFlag().equals(InvoiceEven.EDITE)) {
            InVoiceBean invoiceEven1 = (InVoiceBean) invoiceEven.getObj();
            start(CreateInvoiceFragment.newInstance(invoiceEven1));
        } else if (invoiceEven.getFlag().equals(InvoiceEven.FINISH)) {
            _mActivity.onBackPressed();
        } else if (invoiceEven.getFlag().equals(InvoiceEven.FLASH)) {
            loadingView.showLoading();
            page = 1;
            mPresenter.getInvoiceList(page);

        }


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

    @Override
    protected ChooesInvoicePresenter createPresenter() {
        return new ChooesInvoicePresenter(getActivity(), this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_invoice;
    }


    @Override
    public void onLoadMoreRequested() {
        rv_inboice.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getInvoiceList(page);

            }

        }, 500);
    }
}
