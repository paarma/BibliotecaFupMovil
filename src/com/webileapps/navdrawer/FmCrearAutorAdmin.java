package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created by alex on 9/05/15.
 */
public class FmCrearAutorAdmin extends SherlockFragment {

    private final static String[] tipoAutor = { "Seleccione..", "Personal", "Institucional",
            "Corporativo"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_libro_adminro_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_autor_admin, container, false);
        inicializarComponentes(view);

        return view;


    }

    public void inicializarComponentes(View view){
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, tipoAutor);
        Spinner spTipoAutor = (Spinner) view.findViewById(R.id.spinnerTipoAutor);
        spTipoAutor.setAdapter(adapter);
    }

}