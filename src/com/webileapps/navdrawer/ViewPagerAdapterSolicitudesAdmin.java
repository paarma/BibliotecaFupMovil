package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alex on 9/05/15.
 */
public class ViewPagerAdapterSolicitudesAdmin extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 2;
    private String titles[] = new String[] { "Listado Solicitudes", "Busqueda" };

    public ViewPagerAdapterSolicitudesAdmin(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FmCrearLibroAdmin.java
            case 0:
                FmListaSolicitudesAdmin fragmenttab1 = new FmListaSolicitudesAdmin();
                return fragmenttab1;

            case 1:
                FmBuscarSolicitudesAdmin fragmenttab2 = new FmBuscarSolicitudesAdmin();
                return fragmenttab2;


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

