package com.xinyuan.xyorder.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.NoticeAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.mine.NoticeInfo;
import com.xinyuan.xyorder.common.bean.mine.NoticeListInfo;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.NormalDialog;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;
import com.youth.xframe.widget.loadingview.XLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>
 * Description：通知中心
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/16
 */
public class NoticeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.rv_msg)
    RecyclerView rv_msg;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.lodingView)
    XLoadingView lodingView;

    private NoticeAdapter noticeAdapter;
    private List<NoticeInfo> noticeInfoList = new ArrayList<>();
    private int pageId = 1;

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("通知中心");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }
        });

    }

    @Override
    public void initListener() {
        super.initListener();

    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        getNotice();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        noticeAdapter = new NoticeAdapter(R.layout.item_notice, noticeInfoList);
        noticeAdapter.setOnLoadMoreListener(this);
        rv_msg.setAdapter(noticeAdapter);
        rv_msg.setLayoutManager(layoutManager);

        noticeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                final NormalDialog calDialog = new NormalDialog(getActivity());
                calDialog.setMessage("确定要删除这条通知");
                calDialog.setEnterText("确定");
                calDialog.setCancleText("取消");
                calDialog.setClickListener(new NormalDialog.DialogClickListener() {
                    @Override
                    public void enterListener() {
                        deleteNotice(noticeAdapter.getItem(position).getNoticeId());
                    }

                    @Override
                    public void cancelListener() {
                    }
                });
                calDialog.show();
                return false;
            }
        });
    }

    @Override
    public void initData() {

    }

    public void getNotice() {
        OkGo.<HttpResponseData<NoticeListInfo>>get(Constants.API_NOTICE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .params("pageId", pageId)
                .params("pageSize", Constants.PAGE_SIZE)
                .execute(new JsonCallback<HttpResponseData<NoticeListInfo>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<NoticeListInfo>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            if (XEmptyUtils.isEmpty(response.body().getData().getList())) {
                                if (pageId == 1) {
                                    showState(3);
                                } else {
                                    noticeAdapter.loadMoreEnd();
                                }
                            } else {
                                if (pageId == 1) {
                                    noticeAdapter.setNewData(response.body().getData().getList());
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    noticeAdapter.notifyDataSetChanged();
                                    noticeAdapter.setEnableLoadMore(true);
                                    showState(1);
                                } else {
                                    noticeAdapter.addData(response.body().getData().getList());
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    noticeAdapter.notifyDataSetChanged();
                                    noticeAdapter.loadMoreComplete();
                                    showState(1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<NoticeListInfo>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    public void deleteNotice(long noticeId) {
        OkGo.<HttpResponseData<Void>>delete(Constants.API_NOTICE + "/" + noticeId)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<Void>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            XToast.info("删除成功");
                            pageId = 1;
                            getNotice();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        HttpUtil.handleError(getActivity(), response);
                        super.onError(response);

                    }
                });

    }

    @OnClick({R.id.iv_header_left})
    public void onClick(View v) {
        _mActivity.onBackPressed();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_notice;
    }


    @Override
    public void onRefresh() {
        noticeAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageId = 1;
                getNotice();
            }
        }, 500);
    }

    @Override
    public void onLoadMoreRequested() {
        pageId++;
        rv_msg.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNotice();
            }

        }, 500);
    }

    public void showState(int state) {
        switch (state) {
            case 0:
                lodingView.showLoading();
                break;
            case 1:
                lodingView.showContent();
                break;
            case 2:
                lodingView.showError();
                break;
            case 3:
                lodingView.showEmpty();
                break;

        }
    }
}
