package com.xinyuan.xyorder.util.BigPhoto;

import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinyuan.xyorder.R;
import com.xinyuan.xyorder.app.base.BaseActivity;
import com.xinyuan.xyorder.app.base.BasePresenter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/12/5
 */
public class BigPhotoActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPagerFix viewPager;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private ArrayList<String> urlList;
    int position;
    @BindView(R.id.pb_progress)
    ProgressBar pb_progress;

    @Override
    public void initView() {
        if (getIntent() != null) {
            urlList = getIntent().getStringArrayListExtra("urlList");
            position = getIntent().getIntExtra("position", 0);
        }
        PhotoPagerAdapter viewPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), urlList, pb_progress);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (urlList.size() > 1) {
                    tvNum.setText(String.valueOf(position + 1) + "/" + urlList.size());
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.big_photo;
    }
}
