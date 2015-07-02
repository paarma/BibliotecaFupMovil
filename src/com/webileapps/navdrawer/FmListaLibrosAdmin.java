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

import modelo.Libro;
import util.TareasGenerales;
import util.VariablesGlobales;

public class FmListaLibrosAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Libro> adapterLibro;
    private ListView libroListView;

    private List<Libro> listaLibros = new ArrayList<Libro>();
    private Libro libroSeleccionado;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i("LIBROS_ADMIN", "************************************** INICIO LISTA_LIBROS_ADMIN");

		// Get the view from fm_lista_libros_admin.xmladmin.xml
		View view = inflater.inflate(R.layout.fm_lista_libros_admin, container, false);

        inicializarComponentes(view);
        inicializarListaLibros();

		return view;
	}

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewLibrosAdmin);
    }

    /**
     * Se carga el listado de libros provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaLibros(){

        TareaWsBuscarLibros tareaListarLibro = new TareaWsBuscarLibros();
        tareaListarLibro.execute();

        //Evento al seleccionar un elemento de la lista
        libroListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                libroSeleccionado = listaLibros.get(posicion);
                variablesGlobales.setLibroSeleccionadoAdmin(libroSeleccionado);

                //Se ocultan todos los detalles de libros que esten deplegados
                try {
                    for(int j = 0; j<libroListView.getCount(); j++){
                        View containerAux = libroListView.getChildAt(j);
                        if(containerAux != null) {
                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroAdmin).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleLibroAdmin).setVisibility(View.VISIBLE);
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
                listaLibros = tareasGenerales.buscarLibros(variablesGlobales.getLibroBuscar());
                Log.i("LibrosAdmin",">>>>>>>>>>> Tamaño lista libros buscada: "+listaLibros.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("ListaLibrosAdmin ", "xxx Error TareaWsBuscarLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                adapterLibro = new LibroListAdapterAdmin(getActivity(), listaLibros);
                libroListView.setAdapter(adapterLibro);
            }else{
                String msn = "Error listando libros";
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
        inicializarListaLibros();
    }
}
