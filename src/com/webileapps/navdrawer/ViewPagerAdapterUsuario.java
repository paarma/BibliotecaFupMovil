package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Damian on 11/05/2015.
 */
public class ViewPagerAdapterUsuario extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Reservar", "Mis Libros", "Buscar" };

    public ViewPagerAdapterUsuario(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FmReservar.java
            case 0:
                FmListaLibrosReservarUsuario fmListaLibrosUsuario = new FmListaLibrosReservarUsuario();
                return fmListaLibrosUsuario;

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

}