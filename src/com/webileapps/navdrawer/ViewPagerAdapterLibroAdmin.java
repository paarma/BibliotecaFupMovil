package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterLibroAdmin extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 3;
	private String titles[] = new String[] { "Crear/Editar", "Listado Libros", "Busqueda" };

	public ViewPagerAdapterLibroAdmin(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open FmCrearLibroAdmin.java
		case 0:
			FmCrearLibroAdmin fragmenttab1 = new FmCrearLibroAdmin();
			return fragmenttab1;

			// Open FmListaLibrosAdmin.java
		case 1:
			FmListaLibrosAdmin fragmenttab2 = new FmListaLibrosAdmin();
			return fragmenttab2;

        case 2:
            FmBuscarLibrosAdmin fmBuscarLibrosAdmin = new FmBuscarLibrosAdmin();
            return fmBuscarLibrosAdmin;

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