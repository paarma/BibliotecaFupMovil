package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterEditorialAdmin extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 3;
	private String titles[] = new String[] { "Crear/Editar", "Listado Editorial", "Busqueda" };

	public ViewPagerAdapterEditorialAdmin(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open FmCrearLibroAdmin.java
		case 0:
			FmCrearEditorialAdmin fragmenttab1 = new FmCrearEditorialAdmin();
			return fragmenttab1;

			// Open FmListaLibrosAdmin.java
		case 1:
			FmListaEditorialAdmin fragmenttab2 = new FmListaEditorialAdmin();
			return fragmenttab2;

        case 2:
            FmBuscarEditorialAdmin fmBuscarEditorialAdmin = new FmBuscarEditorialAdmin();
            return fmBuscarEditorialAdmin;

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