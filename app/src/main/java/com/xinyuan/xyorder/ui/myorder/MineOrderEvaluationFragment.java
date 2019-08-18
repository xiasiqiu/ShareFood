package com.xinyuan.xyorder.ui.myorder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.order.MineOrderEvaluationAdapter;
import com.xinyuan.xyorder.adapter.order.StoreAppraiseImageAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.bean.store.store.StoreEvaluateBean;
import com.xinyuan.xyorder.common.callback.JsonCallback;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.util.BigPhoto.BigPhotoActivity;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.youth.xframe.utils.XEmptyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/11/30
 */
public class MineOrderEvaluationFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    TextView tv_store_name;
    CircleImageView customer_image;
    RecyclerView rv_good_pic;
    private FlowLayout fl_seller_service_tag;   //商家评论标签
    private FlowLayout fl_delivery_service_tag; //配送评论标签

    ImageView iv_delivery_service_eva_level;
    ImageView iv_shop_service_eva_level;
    TextView tv_shop_eva;
    TextView tv_delivery_eva;

    @BindView(R.id.rv_evaluation)
    RecyclerView rv_evaluation;
    OrderBean orderBean;
    StoreAppraiseImageAdapter storeAppraiseImageAdapter;

    private MineOrderEvaluationAdapter mineOrderEvaluationAdapter;

    public static MineOrderEvaluationFragment newInstance(OrderBean orderBean) {
        MineOrderEvaluationFragment fragment = new MineOrderEvaluationFragment();
        fragment.orderBean = orderBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
//        SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
//        SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        tv_header_center.setText("我的评价");
        CommUtil.setBack(_mActivity, iv_header_left);
    }

    @Override
    public void initData() {
        getEveluationDetail();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine_order_evaluation;
    }

    private void addTop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_evaluation_top, (ViewGroup) rv_evaluation.getParent(), false);
        mineOrderEvaluationAdapter.addHeaderView(view);
        tv_store_name = view.findViewById(R.id.tv_store_name);
        customer_image = view.findViewById(R.id.customer_image);
        tv_store_name.setText(orderBean.getShopName());
        GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/shopLogo/" + orderBean.getShopId() + ".png" + Constants.IMG_STORE, customer_image);
    }

    private void addBottom(StoreEvaluateBean storeEvaluateBean) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_mine_evaluation_bottom, (ViewGroup) rv_evaluation.getParent(), false);
        mineOrderEvaluationAdapter.addFooterView(view);
        rv_good_pic = view.findViewById(R.id.rv_good_pic);
        fl_delivery_service_tag = view.findViewById(R.id.fl_delivery_service_tag);
        fl_seller_service_tag = view.findViewById(R.id.fl_seller_service_tag);
        iv_delivery_service_eva_level = view.findViewById(R.id.iv_delivery_service_eva_level);
        iv_shop_service_eva_level = view.findViewById(R.id.iv_shop_service_eva_level);
        tv_delivery_eva = view.findViewById(R.id.tv_delivery_eva);
        tv_shop_eva = view.findViewById(R.id.tv_shop_eva);

        if (!XEmptyUtils.isEmpty(storeEvaluateBean.getDeliveryAppraise())) {
            if (!XEmptyUtils.isEmpty(storeEvaluateBean.getDeliveryAppraise().getAppraiseContent())) {
                tv_delivery_eva.setText(storeEvaluateBean.getDeliveryAppraise().getAppraiseContent());
            } else {
                tv_delivery_eva.setVisibility(View.GONE);
            }
            iv_delivery_service_eva_level.setImageResource((Integer) DataUtil.getEvaStar(getActivity(), storeEvaluateBean.getDeliveryAppraise().getAppraiseLevel()));
            if (!XEmptyUtils.isEmpty(storeEvaluateBean.getDeliveryAppraise().getTagDeliveryAppraise())) {
                List<String> result = Arrays.asList(storeEvaluateBean.getDeliveryAppraise().getTagDeliveryAppraise().split(","));
                for (String ss : result) {
                    TextView textView = new TextView(getActivity());
                    textView.setText(ss);
                    textView.setTextSize(13);
                    textView.setTextColor(getActivity().getResources().getColor(R.color.tv_b1));
                    fl_delivery_service_tag.addView(textView);
                }
            } else {
                fl_delivery_service_tag.setVisibility(View.GONE);
            }
        } else {
            fl_delivery_service_tag.setVisibility(View.GONE);
            tv_delivery_eva.setVisibility(View.GONE);
        }
        iv_shop_service_eva_level.setImageResource((Integer) DataUtil.getEvaStar(getActivity(), storeEvaluateBean.getShopAppraise()));
        if (!XEmptyUtils.isEmpty(storeEvaluateBean.getContentShopAppraise())) {
            tv_shop_eva.setText(storeEvaluateBean.getContentShopAppraise());
        } else {
            tv_shop_eva.setVisibility(View.GONE);
        }

        if (!XEmptyUtils.isEmpty(storeEvaluateBean.getTagShopAppraise())) {
            List<String> shopAppraise = Arrays.asList(storeEvaluateBean.getTagShopAppraise().split(","));
            for (String ss : shopAppraise) {
                TextView textView = new TextView(getActivity());
                textView.setText(ss);
                textView.setTextSize(13);
                textView.setTextColor(getActivity().getResources().getColor(R.color.tv_b1));
                fl_seller_service_tag.addView(textView);
            }
        } else {
            fl_seller_service_tag.setVisibility(View.GONE);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_good_pic.setLayoutManager(manager);
        storeAppraiseImageAdapter = new StoreAppraiseImageAdapter(R.layout.item_image, storeEvaluateBean.getShopAppraiseImageList());
        storeAppraiseImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<String> list = new ArrayList<>();
                for (int j = 0; j < storeAppraiseImageAdapter.getData().size(); j++) {
                    list.add(Constants.IMAGE_HOST + storeAppraiseImageAdapter.getData().get(j) + Constants.IMG_EVA_BIG);
                }
                Intent i = new Intent(getActivity(), BigPhotoActivity.class);
                i.putStringArrayListExtra("urlList", (ArrayList<String>) list);
                i.putExtra("position", position);
                startActivity(i);
//                start(BigPhotoActivity.newInstance(list.toArray(new String[list.size()]),position));
            }
        });
        rv_good_pic.setAdapter(storeAppraiseImageAdapter);
    }

    private void getEveluationDetail() {
        OkGo.<HttpResponseData<StoreEvaluateBean>>get(Constants.API_GET_ORDER_EVELUATION + orderBean.getOrderId())
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .execute(new JsonCallback<HttpResponseData<StoreEvaluateBean>>() {
                    @Override
                    public void onSuccess(Response<HttpResponseData<StoreEvaluateBean>> response) {
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            showEvaluationDetail(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<StoreEvaluateBean>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    public void showEvaluationDetail(StoreEvaluateBean storeEvaluateBean) {
        mineOrderEvaluationAdapter = new MineOrderEvaluationAdapter(R.layout.item_goods_evaluation, storeEvaluateBean.getGoodsAppraiseList());
        rv_evaluation.setAdapter(mineOrderEvaluationAdapter);
        rv_evaluation.setLayoutManager(new LinearLayoutManager(getActivity()));
        addTop();
        addBottom(storeEvaluateBean);
    }
}
