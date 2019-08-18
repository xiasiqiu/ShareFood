package com.xinyuan.xyorder.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.common.bean.home.ShopCategory;
import com.xinyuan.xyorder.common.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description：分类adapter
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/20
 */
public class MenuMoreAdapter extends BaseQuickAdapter<ShopCategory, BaseViewHolder> {
	private List<ShopCategory> menus = new ArrayList<>();

	public MenuMoreAdapter(@LayoutRes int layoutResId, @Nullable List<ShopCategory> data) {
		super(layoutResId, data);
		this.menus = data;
	}

	@Override
	protected void convert(BaseViewHolder helper, ShopCategory item) {
		helper.setText(R.id.home_menu_title, item.getShopCategoryName());
		GlideImageLoader.setUrlImg(mContext, item.getIcon(), (ImageView) helper.getView(R.id.home_menu_img));

	}
}
