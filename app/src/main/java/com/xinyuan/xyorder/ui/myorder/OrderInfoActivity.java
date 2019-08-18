package com.xinyuan.xyorder.ui.myorder;


import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.main.MainFragment;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/20
 */
public class OrderInfoActivity extends BaseActivity {
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.order_content, OrderDetailFragment.newInstance(getIntent().getLongExtra("orderId", 0)));
        }
        setFragmentAnimator(new DefaultHorizontalAnimator());
    }

    @Override
    public void initData() {
    }

}
