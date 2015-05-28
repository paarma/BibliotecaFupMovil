package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alex on 9/05/15.
 */
public class ViewPagerAdapterUsuarioAdmin extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Crear/Editar", "Listado Usuarios", "Busqueda" };

    public ViewPagerAdapterUsuarioAdmin(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FmCrearLibroAdmin.java
            case 0:
                FmCrearUsuarioAdmin fragmenttab1 = new FmCrearUsuarioAdmin();
                return fragmenttab1;

            // Open FmListaLibrosAdmin.java
            case 1:
                FmListaUsuarioAdmin fragmenttab2 = new FmListaUsuarioAdmin();
                return fragmenttab2;

            case 2:
                FmBuscarUsuarioAdmin fmBuscarUsuarioAdmin = new FmBuscarUsuarioAdmin();
                return fmBuscarUsuarioAdmin;


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

