package com.xinyuan.xyorder.adapter.storedetail;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nex3z.flowlayout.FlowLayout;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.AddViewHolder;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodEvaBean;
import com.xinyuan.xyorder.common.bean.order.orderAppraise.GoodsAppraiseTag;
import com.xinyuan.xyorder.common.even.OrderEvaEven;
import com.xinyuan.xyorder.widget.StarBar;
import com.youth.xframe.utils.log.XLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * <p>
 * Description：商品评价adapter
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/19
 */
public class GoodsEvaluationAdapter extends BaseQuickAdapter<GoodEvaBean, BaseViewHolder> {

    private List<GoodEvaBean> datas;
    private int index = -1;

    public GoodsEvaluationAdapter(int layoutResId, @Nullable List<GoodEvaBean> data) {
        super(layoutResId, data);
        this.datas = data;
    }

    public void notifyData(List<GoodEvaBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodEvaBean item) {
        helper.setText(R.id.tv_goods_name, item.getGoodsName());
        final StarBar sb_goods_eva = helper.getView(R.id.sb_goods_evaluation);
        final EditText et_food = helper.getView(R.id.et_food);
        sb_goods_eva.setIntegerMark(true);
        sb_goods_eva.setStarMark(5);
        FlowLayout fbl_tag = helper.getView(R.id.fl_tag);

        fbl_tag.removeAllViews();

        if (item.getGoodsAppraiseTags() != null && item.getGoodsAppraiseTags().size() > 0) {

            for (int i = 0; i < item.getGoodsAppraiseTags().size(); i++) {
                AddViewHolder addViewHolder = new AddViewHolder(mContext, R.layout.view_eva_tag);
                final TextView tv_eva_tag = addViewHolder.getView(R.id.tv_eva_tag);
                tv_eva_tag.setText(item.getGoodsAppraiseTags().get(i).getGoodsAppraiseTagName());

                if (item.getGoodsAppraiseTags().get(i).isChecked()) {
                    tv_eva_tag.setActivated(true);
                    tv_eva_tag.setTextColor(mContext.getResources().getColorStateList(R.color.bg_yellow));
                } else {
                    tv_eva_tag.setActivated(false);
                    tv_eva_tag.setTextColor(mContext.getResources().getColorStateList(R.color.gray_a0));
                }

                final int finalI = i;
                tv_eva_tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv_eva_tag.isActivated()) {
                            tv_eva_tag.setActivated(false);
                            item.getGoodsAppraiseTags().get(finalI).setChecked(false);
                            EventBus.getDefault().postSticky(new OrderEvaEven(OrderEvaEven.REMOVETAG, item.getGoodsId(), item.getGoodsAppraiseTags().get(finalI).getGoodsAppraiseTagName()));
                        } else {
                            tv_eva_tag.setActivated(true);
                            item.getGoodsAppraiseTags().get(finalI).setChecked(true);
                            EventBus.getDefault().postSticky(new OrderEvaEven(OrderEvaEven.CHOOESTAG, item.getGoodsId(), item.getGoodsAppraiseTags().get(finalI).getGoodsAppraiseTagName()));
                        }
                    }

                });
                fbl_tag.addView(addViewHolder.getCustomView());
            }


            sb_goods_eva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setAppraiseLevel((int) sb_goods_eva.getStarMark());
                    item.setAppraiseContent(et_food.getText().toString());
                    datas.set(helper.getLayoutPosition() - 1, item);
                    EventBus.getDefault().post(datas);
                }
            });

            et_food.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = helper.getLayoutPosition() - 1;
                    }
                    return false;
                }
            });
            et_food.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // 输入的内容变化的监听
                    item.setAppraiseLevel((int) sb_goods_eva.getStarMark());
                    item.setAppraiseContent(et_food.getText().toString());
                    datas.set(helper.getLayoutPosition() - 1, item);
                    EventBus.getDefault().post(datas);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // 输入前的监听

                }
            });
            et_food.clearFocus();
            if (index != -1 && index == helper.getLayoutPosition() - 1)

            {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                et_food.requestFocus();
            }
        }

    }
}