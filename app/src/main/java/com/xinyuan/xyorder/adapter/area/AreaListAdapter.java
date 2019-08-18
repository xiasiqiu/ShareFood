//package com.xinyuan.xyorder.adapter.area;
//
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.xinyuan.xyorder.R;
//import com.xinyuan.xyorder.common.bean.SearchSortItemBean;
//import com.xinyuan.xyorder.common.even.AroundStoreEven;
//import com.xinyuan.xyorder.common.even.SearchStoreEven;
////import com.xinyuan.xyorder.ui.home.SearchStoreFragment;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.List;
//
///**
// * Created by f-x on 2017/10/14.
// */
//
//public class AreaListAdapter extends BaseQuickAdapter<SearchSortItemBean, BaseViewHolder> {
//    private int sortType;
//    private int position; //1-附近 2-搜索
//
//    public AreaListAdapter(int layoutResId, int position, int type, @Nullable List<SearchSortItemBean> data) {
//        super(layoutResId, data);
//        this.sortType = type;
//        this.position = position;
//    }
//
//    @Override
//    protected void convert(final BaseViewHolder helper, final SearchSortItemBean item) {
//        final TextView tv_area = helper.getView(R.id.rb_SortDefault);
//        ImageView iv_check = helper.getView(R.id.iv_check);
//        tv_area.setText(item.getItemName());
//        if (item.isCheck()) {
//            iv_check.setVisibility(View.VISIBLE);
//            tv_area.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//        } else {
//            iv_check.setVisibility(View.GONE);
//            tv_area.setTextColor(mContext.getResources().getColor(R.color.tv_hint));
//        }
//        helper.setOnClickListener(R.id.rl_search_item, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.setCheck(true);
//                for (SearchSortItemBean bean : getData()) {
//                    if (bean.isCheck()) {
//                        if (!bean.getItemName().equals(item.getItemName())) {
//                            bean.setCheck(false);
//                            item.setCheck(true);
//                            getCheck(item);
//                        } else {
//                            dismiss();
//                        }
//                        notifyDataSetChanged();
//                    }
//                }
//            }
//        });
//    }
//
//    private void getCheck(SearchSortItemBean item) {
//        switch (sortType) {
//            case 1:
//                if(position==1){
//                    AroundStoreFragment.areaListWindow.dismiss();
//                    EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.AREAR_SEARCH, item));
//
//                }else {
////                    SearchStoreFragment.areaListWindow.dismiss();
//                    EventBus.getDefault().post(new SearchStoreEven(SearchStoreEven.AREAR_SEARCH, item));
//                }
//                break;
//            case 2:
//                if(position==1){
//                    AroundStoreFragment.storeCategoryWindow.dismiss();
//                    EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.CLASS_SEARCH, item));
//
//                }else {
////                    SearchStoreFragment.storeCategoryWindow.dismiss();
//                    EventBus.getDefault().post(new SearchStoreEven(SearchStoreEven.CLASS_SEARCH, item));
//                }
//                break;
//            case 3:
//                if(position==1){
//                    AroundStoreFragment.sortListWindow.dismiss();
//                    EventBus.getDefault().post(new AroundStoreEven(AroundStoreEven.ORDER_SEARCH, item));
//                }else {
////                    SearchStoreFragment.sortListWindow.dismiss();
//                    EventBus.getDefault().post(new SearchStoreEven(SearchStoreEven.ORDER_SEARCH, item));
//                }
//                break;
//        }
//
//    }
//    private void dismiss() {
//        switch (sortType) {
//            case 1:
//                if (position == 1) {
//                    AroundStoreFragment.areaListWindow.dismiss();
//
//                } else {
////                    SearchStoreFragment.areaListWindow.dismiss();
//
//                }
//                break;
//            case 2:
//                if (position == 1) {
//
//                    AroundStoreFragment.storeCategoryWindow.dismiss();
//
//                } else {
////                    SearchStoreFragment.storeCategoryWindow.dismiss();
//
//                }
//                break;
//            case 3:
//                if (position == 1) {
//                    AroundStoreFragment.sortListWindow.dismiss();
//
//                } else {
////                    SearchStoreFragment.sortListWindow.dismiss();
//
//                }
//                break;
//
//        }
//    }
//
//}
