package com.webileapps.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import java.lang.reflect.Field;

import util.VariablesGlobales;

/**
 * Created by Damian on 11/05/2015.
 */
public class FmLibrosUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main, container, false);
        // Locate the ViewPager in viewpager_main.xml
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        // Set the ViewPagerAdapter into ViewPager
        mViewPager.setAdapter(new ViewPagerAdapterUsuario(getChildFragmentManager()));

        //Despliega el TAB especifico seleccionado desde el MENU
        mViewPager.setCurrentItem(variablesGlobales.getOpcionMenu());
        Log.i("FmLibrosUsuario", ">>>>>>>>>>>>>>>> opcion menu seleccionado " + variablesGlobales.getOpcionMenu());

        //Evento al cambiar de TAB'S
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("pagina",">>>>>>>>>>>>>>>>>>>>>>>>>>> pagina seleccionada: "+position);

                Fragment fragment = ((ViewPagerAdapterUsuario)mViewPager.getAdapter()).getFragment(position);
                if(fragment != null) {
                    switch (position) {
                        case 0:
                            Log.i("pagina0", ">>>>>>>>>>>>>>>>> reservar");
                            break;
                        case 1:
                            Log.i("pagina1", ">>>>>>>>>>>>>>>>> misLibros");
                            fragment.onResume();
                            break;
                        case 2:
                            Log.i("pagina2", ">>>>>>>>>>>>>>>>> buscar");
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}