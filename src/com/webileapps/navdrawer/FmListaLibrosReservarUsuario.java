package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import modelo.Libro;
import util.TareasGenerales;
import util.VariablesGlobales;

public class FmListaLibrosReservarUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Libro> adapterLibro;
    private ListView libroListView;

    private List<Libro> listaLibros = new ArrayList<Libro>();
    private Libro libroSeleccionado, libroBuscar;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the view from fm_crear_libro_adminro_admin.xml
		View view = inflater.inflate(R.layout.fm_lista_libros_reservar_usuario, container, false);

        inicializarComponentes(view);
        inicializarListaLibros();

        return view;
	}

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewReservarUsuario);

        /**
         * Se inicializa el objeto libroBuscar si este no llega por el
         * buscadorAvanzado de libros. Funcionalidad necesaria para
         * fijar los parametros por defecto para la busqueda de libro
         */
        if(libroBuscar == null){
            libroBuscar = new Libro();
        }
    }

    /**
     * Se carga el listado de libros provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     * y el metodo onclickLong para desplegar los detalles del libro
     */
    private void inicializarListaLibros(){

        TareaWsBuscarLibros tareaListarLibro = new TareaWsBuscarLibros();
        tareaListarLibro.execute();

        //Evento al seleccionar un elemento de la lista
        libroListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                libroSeleccionado = listaLibros.get(posicion);

                //Se ocultan todos los detalles que esten deplegados
                try {
                    for(int j = 0; j<libroListView.getCount(); j++){
                        View containerAux = libroListView.getChildAt(j);
                        if(containerAux != null) {
                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroReservar).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleLibroReservar).setVisibility(View.VISIBLE);
            }
        });
    }

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar los libros de la biblioteca ya sea el listado general
     * o un listado segun parametros de un libro a buscar
     */
    private class TareaWsBuscarLibros extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaLibros = tareasGenerales.buscarLibros(libroBuscar);
                Log.i("Reservar",">>>>>>>>>>> Tamaño lista libros buscada: "+listaLibros.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.d("ReservarUsuario ", "xxx Error TareaWsListadoLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                adapterLibro = new LibroListAdapterUsuario(getActivity(), listaLibros);
                libroListView.setAdapter(adapterLibro);
            }else{
                String msn = "Error listando libros";
                Toast.makeText(null, msn, Toast.LENGTH_LONG).show();
            }
        }
    }
}
