package com.webileapps.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapterLibroAdmin extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 3;
	private String titles[] = new String[] { "Crear/Editar", "Listado Libros", "Busqueda" };

    //Variables extra para recargar los fragment
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

	public ViewPagerAdapterLibroAdmin(FragmentManager fm) {
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