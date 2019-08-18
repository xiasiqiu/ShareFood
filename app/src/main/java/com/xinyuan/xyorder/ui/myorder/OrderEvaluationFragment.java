package com.xinyuan.xyorder.ui.myorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.adapter.ImagePickerAdapter;
import com.xinyuan.xyorder.adapter.storedetail.GoodsEvaluationAdapter;
import com.xinyuan.xyorder.app.MyApplication;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.common.bean.UploadFileBean;
import com.xinyuan.xyorder.common.bean.order.OrderBean;
import com.xinyuan.xyorder.common.bean.order.OrderGoods;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.DeliveryAppraiseTag;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.DeliveryEvaBean;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodEvaBean;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodsAppraiseTag;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.OrderAppraiseAllTag;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.ShopAppraiseTag;
import com.xinyuan.xyorder.common.bean.store.store.StoreEvaluateBean;
import com.xinyuan.xyorder.common.callback.DialogCallback;
import com.xinyuan.xyorder.common.GlideImageLoader;
import com.xinyuan.xyorder.common.constant.Constants;
import com.xinyuan.xyorder.common.even.OrderEvaEven;
import com.xinyuan.xyorder.common.even.OrderRefreshEven;
import com.xinyuan.xyorder.common.http.HttpResponseData;
import com.xinyuan.xyorder.common.http.HttpUtil;
import com.xinyuan.xyorder.mvp.contract.OrderEvaluationView;
import com.xinyuan.xyorder.mvp.presenter.OrderEvaluationPresenter;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.util.SystemBarHelper;
import com.xinyuan.xyorder.widget.EditTextWithDel;
import com.xinyuan.xyorder.widget.SelectDialog;
import com.xinyuan.xyorder.widget.StarBar;
import com.youth.xframe.utils.XBitmapUtils;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.widget.loadingview.XLoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/19
 */
public class OrderEvaluationFragment extends BaseFragment<OrderEvaluationPresenter> implements OrderEvaluationView, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.rv_evaluation)
    RecyclerView rv_evaluation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_header_center)
    TextView tv_header_center;
    @BindView(R.id.iv_header_left)
    ImageView iv_header_left;
    @BindView(R.id.lodingView)
    XLoadingView lodingView;
    RecyclerView rv_good_pic;
    CircleImageView customer_image;

    //    StarBar sb_shop_environment;
//    StarBar sb_shop_taste;
    StarBar sb_shop_service;
    StarBar sb_delivery_service;
    TextView tv_submit_evaluation;
    EditTextWithDel et_shop_eva;
    EditTextWithDel et_delivery_eva;

    TextView tv_store_name;
    GoodsEvaluationAdapter goodsEvaluationAdapter;
    List<GoodEvaBean> goodsAppraiseBeanList = new ArrayList<>();
    private int store_start;
    private int delivery_start;
    private OrderBean orderItemBean;
    private long orderId;
    private long shopId;
    private String storeName;
    private HashMap<Long, List<String>> orderEvaTags;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter imagePickerAdapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;
    ArrayList<ImageItem> images = null;
    private FlowLayout fl_seller_service_tag;   //商家评论标签
    private FlowLayout fl_delivery_service_tag; //配送评论标签
    private List<DeliveryAppraiseTag> deliveryAppraiseTagList;  //配送评论标签数据
    private List<ShopAppraiseTag> shopAppraiseTagList;          //店铺评价标签数据
    private List<GoodsAppraiseTag> goodsAppraiseTags;           //商品评价标签数据

    public static OrderEvaluationFragment newInstance(OrderBean mineOrderItemBean) {
        OrderEvaluationFragment fragment = new OrderEvaluationFragment();
        fragment.orderItemBean = mineOrderItemBean;
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        if (orderItemBean.isFromOrderList()) {
            SystemBarHelper.immersiveStatusBar(getActivity(), 0); //设置状态栏透明
            SystemBarHelper.setHeightAndPadding(getActivity(), toolbar);
        }
        tv_header_center.setText("评价");
        orderEvaTags = new HashMap<Long, List<String>>();
    }

    private void showView() {
        if (!XEmptyUtils.isEmpty(orderItemBean)) {
            store_start = 5;
            delivery_start = 5;
            storeName = orderItemBean.getShopName();
            orderId = orderItemBean.getOrderId();
            shopId = orderItemBean.getShopId();
            if (!XEmptyUtils.isEmpty(orderItemBean.getOrderGoods()) && orderItemBean.getOrderGoods().size() > 0) {
                for (OrderGoods orderGoods : orderItemBean.getOrderGoods()) {
                    orderEvaTags.put(orderGoods.getGoodsId(), new ArrayList<String>());
                    GoodEvaBean goodEvaBean = new GoodEvaBean();
                    goodEvaBean.setAppraiseLevel(5);
                    goodEvaBean.setGoodsName(orderGoods.getGoodsName());
                    goodEvaBean.setGoodsId(orderGoods.getGoodsId());

                    List<GoodsAppraiseTag> goodTag = new ArrayList<GoodsAppraiseTag>();
                    for (GoodsAppraiseTag goodsAppraiseTag : goodsAppraiseTags) {
                        GoodsAppraiseTag goodsAppraiseTag1 = new GoodsAppraiseTag();
                        goodsAppraiseTag1.setChecked(false);
                        goodsAppraiseTag1.setGoodsAppraiseTagId(goodsAppraiseTag.getGoodsAppraiseTagId());
                        goodsAppraiseTag1.setGoodsAppraiseTagName(goodsAppraiseTag.getGoodsAppraiseTagName());
                        goodTag.add(goodsAppraiseTag1);
                    }

                    goodEvaBean.setGoodsAppraiseTags(goodTag);
                    goodsAppraiseBeanList.add(goodEvaBean);
                }
            }
        }
        goodsEvaluationAdapter = new GoodsEvaluationAdapter(R.layout.item_evaluation, goodsAppraiseBeanList);
        rv_evaluation.setAdapter(goodsEvaluationAdapter);
        rv_evaluation.setLayoutManager(new LinearLayoutManager(getActivity()));
        addTop();
        addBottom();
        lodingView.showContent();
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        mPresenter.getOrderAppraiseTag();

    }


    @Override
    public void initListener() {
        iv_header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.onBackPressed();
            }

        });
    }

    @Override
    protected OrderEvaluationPresenter createPresenter() {
        return new OrderEvaluationPresenter(getActivity(), this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_order_evaluation;
    }

    private void addTop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_evaluation_top, (ViewGroup) rv_evaluation.getParent(), false);
        goodsEvaluationAdapter.addHeaderView(view);
        tv_store_name = view.findViewById(R.id.tv_store_name);
        customer_image = view.findViewById(R.id.customer_image);
        tv_store_name.setText(storeName);
        GlideImageLoader.setCircleImageView(getActivity(), Constants.IMAGE_HOST + "/shopLogo/" + shopId + ".png" + Constants.IMG_STORE, customer_image);
    }


    private void addBottom() {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_evaluation_bottom, (ViewGroup) rv_evaluation.getParent(), false);
        goodsEvaluationAdapter.addFooterView(view);
        fl_delivery_service_tag = view.findViewById(R.id.fl_delivery_service_tag);
        fl_seller_service_tag = view.findViewById(R.id.fl_seller_service_tag);
        sb_shop_service = view.findViewById(R.id.sb_shop_service);
        sb_delivery_service = view.findViewById(R.id.sb_delivery_service);
        tv_submit_evaluation = view.findViewById(R.id.tv_submit_evaluation);
        et_shop_eva = view.findViewById(R.id.et_shop_eva);
        et_delivery_eva = view.findViewById(R.id.et_delivery_eva);
        sb_shop_service.setIntegerMark(true);
        sb_shop_service.setStarMark(5);
        sb_shop_service.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                store_start = (int) mark;
            }
        });
        sb_delivery_service.setIntegerMark(true);
        sb_delivery_service.setStarMark(5);
        sb_delivery_service.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                delivery_start = (int) mark;
            }
        });
        rv_good_pic = view.findViewById(R.id.rv_good_pic);
        selImageList = new ArrayList<>();
        imagePickerAdapter = new ImagePickerAdapter(getActivity(), selImageList, maxImgCount);
        imagePickerAdapter.setOnItemClickListener(this);
        rv_good_pic.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_good_pic.setHasFixedSize(true);
        rv_good_pic.setAdapter(imagePickerAdapter);
        tv_submit_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEvaluation();
            }
        });
        fl_delivery_service_tag.removeAllViews();
        if (!XEmptyUtils.isEmpty(deliveryAppraiseTagList)) {
            for (final DeliveryAppraiseTag deliveryAppraiseTag : deliveryAppraiseTagList) {
                final TextView textView = new TextView(getActivity());
                textView.setText(deliveryAppraiseTag.getDeliveryAppraiseTagName());
                textView.setBackground(getActivity().getResources().getDrawable(R.drawable.button_check_selector));
                textView.setTextColor(getActivity().getResources().getColor(R.color.gray_a0));
                //设置padding
                int pxlr = view.getResources().getDimensionPixelOffset(R.dimen.padding_left);
                int pxlb = view.getResources().getDimensionPixelOffset(R.dimen.padding_top);
                textView.setPadding(pxlr, pxlb, pxlr, pxlb);
                textView.setActivated(false);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textView.isActivated()) {
                            textView.setActivated(false);
                            textView.setTextColor(getActivity().getResources().getColorStateList(R.color.gray_a0));
                            deliveryAppraiseTag.setCheck(false);
                        } else {
                            textView.setTextColor(getActivity().getResources().getColorStateList(R.color.bg_yellow));
                            textView.setActivated(true);
                            deliveryAppraiseTag.setCheck(true);

                        }
                    }
                });
                fl_delivery_service_tag.addView(textView);
            }
            fl_seller_service_tag.removeAllViews();
            for (final ShopAppraiseTag shopAppraiseTag : shopAppraiseTagList) {
                final TextView textView = new TextView(getActivity());
                textView.setText(shopAppraiseTag.getShopAppraiseTagName());
                textView.setBackground(getActivity().getResources().getDrawable(R.drawable.button_check_selector));
                textView.setTextColor(getActivity().getResources().getColor(R.color.gray_a0));
                //设置padding
                int pxlr = view.getResources().getDimensionPixelOffset(R.dimen.padding_left);
                int pxlb = view.getResources().getDimensionPixelOffset(R.dimen.padding_top);
                textView.setPadding(pxlr, pxlb, pxlr, pxlb);
                textView.setActivated(false);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textView.isActivated()) {
                            textView.setActivated(false);
                            textView.setTextColor(getActivity().getResources().getColorStateList(R.color.gray_a0));
                            shopAppraiseTag.setChecked(false);
                        } else {
                            textView.setTextColor(getActivity().getResources().getColorStateList(R.color.bg_yellow));
                            textView.setActivated(true);
                            shopAppraiseTag.setChecked(true);

                        }
                    }
                });
                fl_seller_service_tag.addView(textView);
            }
        }
    }


    /**
     * 获取店铺评价信息
     */
    public void getEvaluation() {
        ArrayList<File> files = new ArrayList<>();
        if (selImageList != null && selImageList.size() > 0) {
            for (int i = 0; i < selImageList.size(); i++) {
                files.add(new File(selImageList.get(i).path));
                XBitmapUtils.compress(XBitmapUtils.getBitmapFromFile(selImageList.get(i).path, 800, 800), 500);
            }
            upImages(files);
        } else {
            submitEvaluation();
        }


    }

    private List<UploadFileBean> uploadImageList;

    /**
     * 上传评论图片
     *
     * @param imgs
     */
    private void upImages(ArrayList<File> imgs) {
        OkGo.<HttpResponseData<List<UploadFileBean>>>post(Constants.API_UPLOAD_IMG + "/evaluation")
                .tag(this)
                .addFileParams("fileList", imgs)
                .execute(new DialogCallback<HttpResponseData<List<UploadFileBean>>>(getActivity(), "提交中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<List<UploadFileBean>>> response) {
                        XLog.v(response.body().toString());
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            uploadImageList = response.body().getData();
                            submitEvaluation();
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<List<UploadFileBean>>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });
    }

    /**
     * 提交评价
     *
     * @param
     */

    public void submitEvaluation() {
        StoreEvaluateBean storeEvaluationBean = new StoreEvaluateBean();
        DeliveryEvaBean deliveryEvaBean = new DeliveryEvaBean();
        deliveryEvaBean.setAppraiseContent(et_delivery_eva.getText().toString());
        deliveryEvaBean.setAppraiseLevel(delivery_start);
        String deliverTag = "";
        String storeTag = "";
        for (DeliveryAppraiseTag deliveryAppraiseTag : deliveryAppraiseTagList) {
            if (deliveryAppraiseTag.isCheck()) {
                deliverTag = deliverTag + deliveryAppraiseTag.getDeliveryAppraiseTagName() + ",";
            }
        }
        if (!XEmptyUtils.isSpace(deliverTag)) {
            deliverTag = deliverTag.substring(0, deliverTag.length() - 1);
        }
        for (ShopAppraiseTag shopAppraiseTag : shopAppraiseTagList) {
            if (shopAppraiseTag.isChecked()) {
                storeTag = storeTag + shopAppraiseTag.getShopAppraiseTagName() + ",";
            }
        }
        if (!XEmptyUtils.isSpace(storeTag)) {
            storeTag = storeTag.substring(0, storeTag.length() - 1);

        }


        deliveryEvaBean.setTagDeliveryAppraise(deliverTag);
        storeEvaluationBean.setTagShopAppraise(storeTag);
        storeEvaluationBean.setDeliveryAppraise(deliveryEvaBean);
        storeEvaluationBean.setShopAppraise(store_start);
        storeEvaluationBean.setContentShopAppraise(et_shop_eva.getText().toString());
        for (GoodEvaBean goodEvaBean : goodsAppraiseBeanList) {
            if (orderEvaTags.containsKey(goodEvaBean.getGoodsId())) {
                List newList = new ArrayList(new HashSet(orderEvaTags.get(goodEvaBean.getGoodsId())));
                goodEvaBean.setTagGoodsAppraise(newList.toString());
                goodEvaBean.setTagGoodsAppraise(goodEvaBean.getTagGoodsAppraise().substring(1, goodEvaBean.getTagGoodsAppraise().length() - 1));
            }
            goodEvaBean.setGoodsAppraiseTags(null);
        }
        storeEvaluationBean.setGoodsAppraiseList(goodsAppraiseBeanList);
        storeEvaluationBean.setOrderId(orderId);
        if (!XEmptyUtils.isEmpty(uploadImageList)) {
            List<String> goodEvaImageBeanList = new ArrayList<>();
            for (UploadFileBean bean : uploadImageList) {
                goodEvaImageBeanList.add(bean.getOriginalUrl());
            }
            storeEvaluationBean.setShopAppraiseImageList(goodEvaImageBeanList);
        }
        String json = "";
        json = new Gson().toJson(storeEvaluationBean);
        OkGo.<HttpResponseData<Void>>
                put(Constants.API_SHOPAPPRAISE)
                .tag(this)
                .headers("TOKEN", DataUtil.getToken(getActivity()))
                .upJson(json)
                .execute(new DialogCallback<HttpResponseData<Void>>(getActivity(), "提交中") {
                    @Override
                    public void onSuccess(Response<HttpResponseData<Void>> response) {
                        XLog.v(response.body().toString());
                        if (HttpUtil.handleResponse(getActivity(), response.body())) {
                            EventBus.getDefault().post(new OrderRefreshEven(true));
                            startWithPop(OrderSuccessFragment.newInstance(orderItemBean));
                        }
                    }

                    @Override
                    public void onError(Response<HttpResponseData<Void>> response) {
                        super.onError(response);
                        HttpUtil.handleError(getActivity(), response);
                    }
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(OrderEvaEven orderEvaEven) {
        if (orderEvaEven.getFlag().equals(OrderEvaEven.CHOOESTAG)) {
            List<String> list = orderEvaTags.get(orderEvaEven.getId());
            list.add((String) orderEvaEven.getObj());
            orderEvaTags.put(orderEvaEven.getId(), list);
            goodsEvaluationAdapter.notifyDataSetChanged();
        } else if (orderEvaEven.getFlag().equals(OrderEvaEven.REMOVETAG)) {

            List<String> list = orderEvaTags.get(orderEvaEven.getId());
            for (String s : list) {
                if (orderEvaEven.getObj().equals(s)) {
                    list.remove(s);
                }
            }
            orderEvaTags.put(orderEvaEven.getId(), list);
            goodsEvaluationAdapter.notifyDataSetChanged();


        }
    }


    /**
     * 显示评论TAG
     *
     * @param allInfo
     */
    @Override
    public void showAppraiseTag(OrderAppraiseAllTag allInfo) {
        goodsAppraiseTags = allInfo.getGoodsAppraiseTagList();
        deliveryAppraiseTagList = allInfo.getDeliveryAppraiseTagList();
        shopAppraiseTagList = allInfo.getShopAppraiseTagList();
        showView();
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(getActivity(), ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) imagePickerAdapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    /**
     * 显示选择dialog
     *
     * @param listener
     * @param names
     * @return
     */
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(getActivity(), R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!getActivity().isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 选择后返回图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    imagePickerAdapter.setImages(selImageList);

                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    imagePickerAdapter.setImages(selImageList);
                }
            }
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
}
