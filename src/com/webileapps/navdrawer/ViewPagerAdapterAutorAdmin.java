package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alex on 9/05/15.
 */
public class ViewPagerAdapterAutorAdmin extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Crear/Editar", "Listado Autor", "Busqueda" };

    public ViewPagerAdapterAutorAdmin(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FmCrearLibroAdmin.java
            case 0:
                FmCrearAutorAdmin fragmenttab1 = new FmCrearAutorAdmin();
                return fragmenttab1;

            // Open FmListaLibrosAdmin.java
            case 1:
                FmListaAutorAdmin fragmenttab2 = new FmListaAutorAdmin();
                return fragmenttab2;

            case 2:
                FmBuscarAutorAdmin fmBuscarAutorAdmin = new FmBuscarAutorAdmin();
                return fmBuscarAutorAdmin;


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

