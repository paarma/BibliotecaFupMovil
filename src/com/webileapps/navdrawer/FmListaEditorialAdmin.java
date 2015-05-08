package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class FmListaEditorialAdmin extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fm_lista_libros_admin.xmladmin.xml
		View view = inflater.inflate(R.layout.fm_lista_editorial_admin, container, false);
		return view;
	}
}
