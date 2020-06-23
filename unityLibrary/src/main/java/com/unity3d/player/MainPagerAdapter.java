package com.unity3d.player;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int pagecount  = 4;
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabFragment_Info tabFragment_info = new TabFragment_Info();
                return tabFragment_info;
            case 1:
                sa_TabFragment_Alarm sa_tabFragment_alarm = new sa_TabFragment_Alarm();
                return sa_tabFragment_alarm;
//            case 1:
//                TabFragment_Alarm tabFragment_alarm = new TabFragment_Alarm();
//                return tabFragment_alarm;
            case 2:
                sa_TabFragment_Statistic sa_tabFragment_statistic = new sa_TabFragment_Statistic();
                return sa_tabFragment_statistic;
            case 3:
                sa_TabFragment_Sysset sa_tabFragment_sysset = new sa_TabFragment_Sysset();
                return sa_tabFragment_sysset;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pagecount;
    }
}
