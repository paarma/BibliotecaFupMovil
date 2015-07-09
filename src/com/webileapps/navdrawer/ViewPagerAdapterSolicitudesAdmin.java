package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo on 9/07/15.
 */
public class ViewPagerAdapterSolicitudesAdmin extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 2;
    private String titles[] = new String[] { "Listado Solicitudes", "Busqueda" };

    //Variables extra para recargar los fragment
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public ViewPagerAdapterSolicitudesAdmin(FragmentManager fm) {
        super(fm);
        mFragmentTags = new HashMap<Integer,String>();
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("POSITION", "************************** POSITION: " + position);
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

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    /**Metodos extra para actualizar los fragment
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    /**
     *  Metodos extra para actualizar los fragment
     */
    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null){
            return null;
        }

        return mFragmentManager.findFragmentByTag(tag);
    }

}

