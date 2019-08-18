package com.xinyuan.xyorder.util.BigPhoto;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class PhotoPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<String> urlList;
    ProgressBar pb_progress;

    public PhotoPagerAdapter(FragmentManager fm, ArrayList<String> urlList,ProgressBar pb_progress) {
        super(fm);
        this.urlList=urlList;
        this.pb_progress=pb_progress;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(urlList.get(position),pb_progress);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
