package com.xinyuan.xyorder.adapter.storedetail;


import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.buy.ChooesCarGoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodBean;
import com.xinyuan.xyorder.common.bean.store.good.GoodsSpecifications;
import com.xinyuan.xyorder.common.bean.store.good.SpecialsGoodBean;
import com.xinyuan.xyorder.ui.stores.StoreDetailActivity;
import com.xinyuan.xyorder.ui.stores.StoreGoodsFragment;
import com.xinyuan.xyorder.util.CommUtil;
import com.xinyuan.xyorder.util.DataUtil;
import com.xinyuan.xyorder.widget.AddWidget;
import com.xinyuan.xyorder.widget.ReduceButton;
import com.youth.xframe.utils.XEmptyUtils;
import com.youth.xframe.widget.XToast;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by f-x on 2017/10/1817:04
 */
public class CarAdapter extends BaseQuickAdapter<ChooesCarGoodBean, BaseViewHolder> {
    private AddWidget.OnAddClick onAddClick;

    public CarAdapter(@Nullable List<ChooesCarGoodBean> data, AddWidget.OnAddClick onAddClick) {
        super(R.layout.item_cart, data);
        this.onAddClick = onAddClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ChooesCarGoodBean item) {
        helper.setText(R.id.car_name, item.getGoodsName())
                .setText(R.id.car_name_hint, (XEmptyUtils.isEmpty(item.getSepcName()) ? "" : item.getSepcName()) + (XEmptyUtils.isEmpty(item.getChooesProertys()) ? "" : "/")
                + DataUtil.listToSpecString(item.getChooesProertys()));
        BigDecimal amount = new BigDecimal(0.0);
        BigDecimal originalAmount = new BigDecimal(0.0);
        originalAmount = originalAmount.add(item.getGoodPrice().multiply(BigDecimal.valueOf(item.getChooesNum())));

        if (XEmptyUtils.isEmpty(item.getGoodActivityBean())) {
            amount = amount.add(item.getGoodPrice().multiply(BigDecimal.valueOf(item.getChooesNum())));
            helper.setVisible(R.id.car_old_price, false);
            helper.setText(R.id.car_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(amount));
        } else {
            helper.setVisible(R.id.car_old_price, true);
            BigDecimal priceA = item.getGoodPrice().multiply(item.getGoodActivityBean().getDiscount()).divide(new BigDecimal(10));
            BigDecimal priceB = item.getGoodPrice();
            int couponCount = item.getGoodActivityBean().getLimitedQuantity();

            for (ChooesCarGoodBean chooesCarGoodBean : getData()) {
                if (chooesCarGoodBean.getGoodId() == item.getGoodId()) {
                    if (chooesCarGoodBean.getSpecId() == item.getSpecId() && chooesCarGoodBean.getChooesProertys().equals(item.getChooesProertys())) {
                        if (couponCount == 0) {
                            amount = amount.add(priceB.multiply(BigDecimal.valueOf(chooesCarGoodBean.getChooesNum())));
                        } else if (item.getChooesNum() > couponCount) {
                            amount = amount.add(priceA.multiply(BigDecimal.valueOf(couponCount)).add(priceB.multiply(BigDecimal.valueOf(item.getChooesNum() - couponCount))));
                            couponCount = 0;
                        } else {
                            amount = amount.add(priceA.multiply(BigDecimal.valueOf(chooesCarGoodBean.getChooesNum())));
                            couponCount -= chooesCarGoodBean.getChooesNum();
                            break;
                        }
                    } else {
                        if (chooesCarGoodBean.getChooesNum() > couponCount) {
                            couponCount = 0;
                        } else {
                            couponCount -= chooesCarGoodBean.getChooesNum();
                        }

                    }
                }
            }
            if (amount.compareTo(originalAmount) == 0) {
                helper.setVisible(R.id.car_old_price, false);
                helper.setText(R.id.car_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(amount));
            } else {
                ((TextView) (helper.getView(R.id.car_old_price))).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setText(R.id.car_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(amount));
                helper.setText(R.id.car_old_price, mContext.getResources().getString(R.string.money_rmb) + CommUtil.getPriceString(originalAmount));
            }

        }
        int selecCount = 0;
        for (ChooesCarGoodBean chooesCarGoodBean : getData()) {
            if (item.getGoodId() == chooesCarGoodBean.getGoodId()) {
                selecCount += chooesCarGoodBean.getChooesNum();
            }
        }
        item.setSelecCount(selecCount);
        AddWidget addWidget = helper.getView(R.id.car_addwidget);
        for (GoodBean goodBean : StoreGoodsFragment.getGoodDatas()) {
            if (goodBean.getGoodsId() == item.getGoodId()) {
                GoodBean indexGood = new GoodBean();
                indexGood.setGoodsId(goodBean.getGoodsId());
                indexGood.setShowSpecSelecter(goodBean.isShowSpecSelecter());
                indexGood.setShopId(goodBean.getShopId());
                indexGood.setGoodsName(goodBean.getGoodsName());
                indexGood.setShopName(goodBean.getShopName());
                indexGood.setPosition(goodBean.getPosition());
                indexGood.setHaveAnim(goodBean.isHaveAnim());
                indexGood.setGoodsSpecifications(goodBean.getGoodsSpecifications());
                indexGood.setInfiniteInventory(goodBean.isInfiniteInventory());
                indexGood.setGoodsPropertys(goodBean.getGoodsPropertys());
                for (GoodsSpecifications specialsGoodBean : goodBean.getGoodsSpecifications()) {
                    if (specialsGoodBean.getGoodsSpecificationId() == item.getSpecId()) {
                        specialsGoodBean.setCheckNum(item.getChooesNum());
                        indexGood.setChooesSpecID(specialsGoodBean);
                        indexGood.setChooesProerty(item.getChooesProertys());
                        indexGood.setSelectCount(selecCount);
                        indexGood.setChooesNum(item.getChooesNum());
                        break;
                    }
                }
                addWidget.setData(onAddClick, indexGood, true, true, false);
                addWidget.setListener();
                break;
            }
        }

    }
}
