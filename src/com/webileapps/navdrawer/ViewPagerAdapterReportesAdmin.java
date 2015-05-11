package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alex on 9/05/15.
 */
public class ViewPagerAdapterReportesAdmin extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 1;
    private String titles[] = new String[] { "Listado Reportes" };

    public ViewPagerAdapterReportesAdmin(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FmCrearLibroAdmin.java
            case 0:
                FmListaReportesAdmin fragmenttab1 = new FmListaReportesAdmin();
                return fragmenttab1;



        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}

