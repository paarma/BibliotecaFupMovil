package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Editorial;
import modelo.Libro;
import util.NothingSelectedSpinnerAdapter;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Damian on 11/05/2015.
 */
public class FmBuscarLibroUsuario extends SherlockFragment {

    private static Spinner spinnerEditorial;
    private static EditText titulo, isbn, codTopografico, temas;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();
    private List<Editorial> listaEditoriales = new ArrayList<Editorial>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("BUSCAR","************************************** INICIO BUSCAR LIBRO. (USUARIO)");
        View view = inflater.inflate(R.layout.fm_buscar_libro_usuario, container, false);

        inicializarComponentes(view);

        return view;
    }

    public void inicializarComponentes(View view){

        spinnerEditorial = (Spinner) view.findViewById(R.id.spinnerEditorial);
        cargarSpinnerEditorial();

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);
        temas = (EditText) view.findViewById(R.id.editTextTemas);
    }

    /**
     * Metodo encagado de carar el spinner Editorial
     */
    private void cargarSpinnerEditorial(){

        TareaWsListarEditoriales tareaListarEditoriales = new TareaWsListarEditoriales();
        tareaListarEditoriales.execute();
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

        Editorial editorialSeleccionada = (Editorial) spinnerEditorial.getSelectedItem();
        if(editorialSeleccionada != null){
            libro.setIdEditorial(editorialSeleccionada.getIdEditorial());
            Log.i("Buscar",">>>>>>>> Editorial seleccionada buscarLibro: "+editorialSeleccionada.getDescripcion());
        }

        variablesGlobales.setLibroBuscar(libro);
    }


    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea WS encargada de listar las Editoriales y cargar su spinner
     */
    private class TareaWsListarEditoriales extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaEditoriales = tareasGenerales.listarEditoriales();
                Log.i("Buscar",">>>>>>>>>>> Tamaño lista editoriales: "+listaEditoriales.size());

            }catch (Exception e){
                resultadoTarea = false;
                Log.d("Buscar ", "xxx Error TareaWsListarEditoriales: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){

                ArrayAdapter<Editorial> adapter = new ArrayAdapter<Editorial>(getActivity(),
                        android.R.layout.simple_list_item_1,listaEditoriales);

                //Se modifica el seteo general del adapter...
                //spinnerEditorial.setAdapter(adapter);

                //Se setea el adapter agregando el item "Seleccione..." (NothingSelectedSpinnerAdapter)
                spinnerEditorial.setAdapter(new NothingSelectedSpinnerAdapter(
                        adapter,R.layout.contact_spinner_nothing_selected,
                        getActivity()));

            }else{
                //Se despliega mensaje de error si esta en la pantalla de "buscar"
                if(variablesGlobales.getOpcionMenu() == 2) {
                    String msn = "Error listando Editoriales";
                    Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
