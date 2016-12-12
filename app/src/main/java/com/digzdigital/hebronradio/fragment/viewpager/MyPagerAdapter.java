package com.digzdigital.hebronradio.fragment.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Digz on 12/12/2016.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    Context ctxt=null;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();


    public MyPagerAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(7);
    }

    @Override
    public Fragment getItem(int position) {
        return(fragmentList.get(position));
    }

    @Override
    public String getPageTitle(int position) {
        return(fragmentTitleList.get(position));
    }
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

}
