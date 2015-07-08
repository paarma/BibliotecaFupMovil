package com.webileapps.navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;

import modelo.Editorial;
import util.VariablesGlobales;

public class FmBuscarEditorialAdmin extends SherlockFragment {

    private static EditText descripcionEditorial;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i("BUSCAR", "************************************** INICIO BUSCAR EDITORIAL. (ADMIN)");
		View view = inflater.inflate(R.layout.fm_buscar_editorial_admin, container, false);

        inicializarComponentes(view);
        return view;
	}

    public void inicializarComponentes(View view){

        descripcionEditorial = (EditText) view.findViewById(R.id.editTextEditorial);
    }

    /**
     * Metodo encargado de crear el objeto de busqueda con los parametros indicados
     */
    public static void capturarObjetoBusqueda(){

        Editorial editorial = new Editorial();

        if(descripcionEditorial.getText().toString().trim().length() > 0){
            editorial.setDescripcion(descripcionEditorial.getText().toString());
        }

        variablesGlobales.setEditorialBuscar(editorial);
    }
}
