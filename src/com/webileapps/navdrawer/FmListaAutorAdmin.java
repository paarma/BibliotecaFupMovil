package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Autor;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Pablo on 8/07/15.
 */
public class FmListaAutorAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Autor> adapterAutor;
    private ListView autorListView;

    private List<Autor> listaAutores = new ArrayList<Autor>();
    private Autor autorSeleccionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("AUTOR_ADMIN", "************************************** INICIO LISTA_AUTORES_ADMIN");
        View view = inflater.inflate(R.layout.fm_lista_autor_admin, container, false);

        inicializarComponentes(view);
        inicializarListaAutores();

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        autorListView = (ListView) view.findViewById(R.id.listViewAutoresAdmin);
    }

    /**
     * Se carga el listado de autores provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaAutores(){

        TareaWsBuscarAutores tareaListarAutor = new TareaWsBuscarAutores();
        tareaListarAutor.execute();

        //Evento al seleccionar un elemento de la lista
        autorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                autorSeleccionado = listaAutores.get(posicion);
                variablesGlobales.setAutorSeleccionadoAdmin(autorSeleccionado);

                //Se ocultan todos los detalles de autores que esten deplegados
                try {
                    for(int j = 0; j<autorListView.getCount(); j++){
                        View containerAux = autorListView.getChildAt(j);
                        if(containerAux != null) {
                            autorListView.getChildAt(j).findViewById(R.id.contenedorDetalleAutorAdmin).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleAutorAdmin).setVisibility(View.VISIBLE);
            }
        });

    }


    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar los autores de la biblioteca ya sea el listado general
     * o un listado segun parametros de un autor a buscar
     */
    private class TareaWsBuscarAutores extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaAutores = tareasGenerales.listarAutores(variablesGlobales.getAutorBuscar());
                Log.i("AutorAdmin",">>>>>>>>>>> Tamaño lista autores buscada: "+listaAutores.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("AutorAdmin ", "xxx Error TareaWsBuscarAutores: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    if (listaAutores.size() > 0) {
                        adapterAutor = new AutorListAdapterAdmin(getActivity(), listaAutores);
                        autorListView.setAdapter(adapterAutor);
                    }
                }catch (Exception e){
                    Log.e("AutorAdmin ", "xxx Error listando autores: " + e.getMessage());
                }
            }else{
                String msn = "Error listando autores";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    /**
     * Sobreescritura del metodo onResume
     * (se agrega la funcionalidad para recargar los datos generales de la clase)
     */
    @Override
    public void onResume()
    {
        super.onResume();

        //Funcionalidad para recergar las variables del Fragment
        inicializarListaAutores();
    }
}