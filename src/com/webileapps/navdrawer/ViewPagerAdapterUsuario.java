package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Damian on 11/05/2015.
 */
public class ViewPagerAdapterUsuario extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Reservar", "Mis Libros", "Buscar" };

    //Variables extra para recargar los fragment
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public ViewPagerAdapterUsuario(FragmentManager fm) {

        super(fm);
        mFragmentTags = new HashMap<Integer,String>();
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("POSITION", "************************** POSITION: "+position);
        switch (position) {

            // Open FmReservar.java
            case 0:
                FmReservarUsuario fmReservarUsuario = new FmReservarUsuario();
                return fmReservarUsuario;

            // Open FragmentTab2.java
            case 1:
                FmMisLibrosUsuario fmMisLibrosUsuario = new FmMisLibrosUsuario();
                return fmMisLibrosUsuario;

            case 2:
                FmBuscarLibroUsuario fmBuscarLibroUsuario = new FmBuscarLibroUsuario();
                return fmBuscarLibroUsuario;

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