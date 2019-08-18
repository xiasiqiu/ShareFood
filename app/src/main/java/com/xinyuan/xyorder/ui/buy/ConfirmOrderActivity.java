package com.xinyuan.xyorder.ui.buy;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.constant.Constants;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/27
 */
public class ConfirmOrderActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initView() {
        //fragmentation框架里加载主Fragment
        String goodtype = getIntent().getStringExtra(Constants.ORDER_TYPE);
        String shopId = getIntent().getStringExtra(Constants.STORE_ID);

        if (goodtype.equals(Constants.ORDER_NORML)) {
            loadRootFragment(R.id.confirm_content, ConfirmOrderFragment.newInstance(Constants.ORDER_TAKEOUT, Long.parseLong(shopId)));
        } else if (goodtype.equals(Constants.ORDER_RESERVCE)) {
            loadRootFragment(R.id.confirm_content, ConfirmOrderFragment.newInstance(Constants.ORDER_RESERVE, Long.parseLong(shopId)));
        }
        setFragmentAnimator(new DefaultHorizontalAnimator());
    }

    @Override
    public void initData() {

    }

}
