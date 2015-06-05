package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

import modelo.Libro;
import util.VariablesGlobales;

/**
 * Created by Damian on 11/05/2015.
 */
public class FmBuscarLibroUsuario extends SherlockFragment {

    private final static String[] tipoEditorial = { "Seleccione..", "Editorial_1", "Editorial_2"};

    private static Spinner editorial;
    private static EditText titulo, isbn, codTopografico, temas;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_lista_libros_admin.xmladmin.xml
        View view = inflater.inflate(R.layout.fm_buscar_libro_usuario, container, false);

        inicializarComponentes(view);

        return view;
    }

    public void inicializarComponentes(View view){

        editorial = (Spinner) view.findViewById(R.id.spinnerEditorial);
        ArrayAdapter adapterEditorial = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, tipoEditorial);
        Spinner spEditorial = (Spinner) view.findViewById(R.id.spinnerEditorial);
        spEditorial.setAdapter(adapterEditorial);

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);
        temas = (EditText) view.findViewById(R.id.editTextTemas);
    }

    /**
     * Metodo encargado de crear el objeto de busqueda con los parametros indicados
     */
    public static void capturarObjetoBusqueda(){

        Libro libro = new Libro();

        if(titulo.getText().toString().trim().length() > 0){
            libro.setTitulo(titulo.getText().toString());
        }

        if(isbn.getText().toString().trim().length() > 0){
            libro.setIsbn(isbn.getText().toString());
        }

        if(codTopografico.getText().toString().trim().length() > 0){
            libro.setCodigoTopografico(codTopografico.getText().toString());
        }

        if(temas.getText().toString().trim().length() > 0){
            libro.setTemas(temas.getText().toString());
        }

        //*********************************funcionalidad temporal para editorial
        if (editorial.getSelectedItem().toString().equals("Editorial_1")) {
            libro.setIdEditorial(1);
        }

        if (editorial.getSelectedItem().toString().equals("Editorial_2")) {
            libro.setIdEditorial(2);
        }
        //**********************************fin funcionalidad temporal editorial

        variablesGlobales.setLibroBuscar(libro);

    }
}
