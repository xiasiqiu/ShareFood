package com.xinyuan.xyorder.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseFragment;
import com.xinyuan.xyorder.app.base.BasePresenter;
import com.xinyuan.xyorder.common.even.MainFragmentStartEvent;
import com.xinyuan.xyorder.common.even.TabSelectedEvent;
import com.xinyuan.xyorder.ui.home.AroundStoreFragment;
import com.xinyuan.xyorder.ui.home.HomeFragment;
import com.xinyuan.xyorder.ui.mine.MineFragment;
import com.xinyuan.xyorder.ui.myorder.OrderFragment;
import com.xinyuan.xyorder.widget.BottomBar;
import com.xinyuan.xyorder.widget.BottomBarTab;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.widget.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/9/14
 */
public class MainFragment extends BaseFragment<BasePresenter> {
    @BindView(R.id.bottomBar)
    BottomBar bottomBar; //底部菜单栏

    public static final int HOME = 0;   //首页位置
    public static final int CLASS = 1;  //附近
    public static final int CAR = 3;    //订单
    public static final int MINE = 4;   //我的
    public static final int MENU = 2;//menu暂时不用

    private SupportFragment[] mFragments = new SupportFragment[5];  //主页5页面

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View rootView) {
        bottomBar.addItem(new BottomBarTab(_mActivity, R.drawable.rb_menu_home, getString(R.string.menu_home)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.rb_menu_around, getString(R.string.menu_near)))
                .addItem(new BottomBarTab(_mActivity, 0, null))
                .addItem(new BottomBarTab(_mActivity, R.drawable.rb_menu_order, getString(R.string.menu_order)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.rb_menu_mine, getString(R.string.menu_mine)));
        bottomBar.setCurrentItem(0);    //设置首页为默认加载
        bottomBar.getItem(HOME).setUnreadCount(0);
        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (position == 2) {
                    return;
                }

                showHideFragment(mFragments[position], mFragments[prePosition]);
                BottomBarTab tab = bottomBar.getItem(HOME);
                EventBus.getDefault().post(new TabSelectedEvent(position));     //发送消息，当前选择的页面位置
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
            }
        });

    }

    @Override
    public void initData() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomeFragment homeFragment = findChildFragment(HomeFragment.class);
        //如果没有创建，则新建Fragment并加载
        if (homeFragment == null) {
            mFragments[HOME] = HomeFragment.newInstance();
            mFragments[CLASS] = AroundStoreFragment.newInstance(false,null);
            mFragments[CAR] = OrderFragment.newInstance();
            mFragments[MINE] = MineFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_tab_container, HOME,
                    mFragments[HOME],
                    mFragments[CLASS],
                    mFragments[CAR],
                    mFragments[MINE]);
        } else {
            mFragments[HOME] = homeFragment;
            mFragments[CLASS] = findChildFragment(HomeFragment.class);
            mFragments[CAR] = findChildFragment(OrderFragment.class);
            mFragments[MINE] = findChildFragment(MineFragment.class);
        }
    }

    /**
     * 收到启动通知，启动子Fragment
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void startBrother(MainFragmentStartEvent event) {
        if (event.getFlag().equals(MainFragmentStartEvent.GoFragment)) {
            start(event.getTargetFragment());
        } else if (event.getFlag().equals(MainFragmentStartEvent.GoHomeIndex)) {
            bottomBar.setCurrentItem(0);
        }

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_main;
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


    private long mExitTime;

    @Override
    public boolean onBackPressedSupport() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            XToast.info(getString(R.string.noitce_exit));
            mExitTime = System.currentTimeMillis();

        } else {
            XActivityStack.getInstance().appExit();
        }
        return true;
    }

}
