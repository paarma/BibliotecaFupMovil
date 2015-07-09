package com.webileapps.navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

import modelo.Autor;
import util.VariablesGlobales;

/**
 * Created by Pablo on 9/05/15.
 */
public class FmBuscarAutorAdmin extends SherlockFragment {

    private static EditText primerNombre, segundoNombre, primerApellido, segundoApellido;
    private static Spinner spinnerTipoAutor;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("BUSCAR", "************************************** INICIO BUSCAR AUTOR. (ADMIN)");
        View view = inflater.inflate(R.layout.fm_buscar_autor_admin, container, false);

        inicializarComponentes(view);
        return view;
    }

    public void inicializarComponentes(View view){

        primerNombre = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        segundoNombre = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        primerApellido = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        segundoApellido = (EditText) view.findViewById(R.id.editTextSegundoApellido);

        spinnerTipoAutor = (Spinner) view.findViewById(R.id.spinnerTipoAutor);

        //Se cargan algunos spinners con los datos del archivo arrays.xml
        ArrayAdapter adapterTipoAutor = ArrayAdapter.createFromResource(getActivity(), R.array.tipoAutor, R.layout.spinner_item);
        adapterTipoAutor.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipoAutor.setAdapter(adapterTipoAutor);
    }

    /**
     * Metodo encargado de crear el objeto de busqueda con los parametros indicados
     */
    public static void capturarObjetoBusqueda(){

        Autor autor = new Autor();

        if(primerNombre.getText().toString().trim().length() > 0){
            autor.setPrimerNombre(primerNombre.getText().toString());
        }

        if(segundoNombre.getText().toString().trim().length() > 0){
            autor.setSegundoNombre(segundoNombre.getText().toString());
        }

        if(primerApellido.getText().toString().trim().length() > 0){
            autor.setPrimerApellido(primerApellido.getText().toString());
        }

        if(segundoApellido.getText().toString().trim().length() > 0){
            autor.setSegundoApellido(segundoApellido.getText().toString());
        }

        if(!spinnerTipoAutor.getSelectedItem().toString().equals("Seleccione...")){
            autor.setTipoAutor(spinnerTipoAutor.getSelectedItem().toString());
        }

        variablesGlobales.setAutorBuscar(autor);
    }
}
