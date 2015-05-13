package com.webileapps.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import java.lang.reflect.Field;

/**
 * Created by Damian on 11/05/2015.
 */
public class FmMisLibrosUsuario extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_lista_libros_admin.xmladmin.xml
        View view = inflater.inflate(R.layout.fm_mis_libros_usuario, container, false);
        return view;
    }
}