package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	private String titles[] = new String[] { "Crear/Editar", "Listado Libros" };

	public ViewPagerAdapter(FragmentManager fm) {
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