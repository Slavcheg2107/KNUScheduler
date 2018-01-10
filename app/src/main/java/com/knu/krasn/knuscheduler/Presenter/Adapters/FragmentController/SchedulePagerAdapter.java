package com.knu.krasn.knuscheduler.Presenter.Adapters.FragmentController;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.knu.krasn.knuscheduler.View.Fragments.Week1Fragment;
import com.knu.krasn.knuscheduler.View.Fragments.Week2Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krasn on 12/12/2017.
 */

public class SchedulePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener, ToolbarUpdaterInterface {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private ToolbarUpdater toolbarUpdater;

    public SchedulePagerAdapter(FragmentManager manager, ToolbarUpdater toolbarUpdater) {
        super(manager);
        this.toolbarUpdater = toolbarUpdater;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        checkFragment(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        checkFragment(position);
    }

    private void checkFragment(int position) {
        Week1Fragment fr1;
        Week2Fragment fr2;
        if (mFragmentList.get(position) instanceof Week1Fragment) {
            fr1 = (Week1Fragment) mFragmentList.get(position);
            toolbarUpdater.updateToolbar(fr1.getCurrentDay());
        } else if (mFragmentList.get(position) instanceof Week2Fragment) {
            fr2 = (Week2Fragment) mFragmentList.get(position);
            toolbarUpdater.updateToolbar(fr2.getCurrentDay());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void updateToolbar(int dayNumber) {

    }
}
