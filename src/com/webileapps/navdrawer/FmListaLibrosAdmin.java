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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Autor;
import modelo.Libro;
import util.TareasGenerales;
import util.UtilidadGenerarReportes;
import util.VariablesGlobales;

public class FmListaLibrosAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Libro> adapterLibro;
    private ListView libroListView;

    private List<Libro> listaLibros = new ArrayList<Libro>();
    private Libro libroSeleccionado;

    private ImageButton btnReporte;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i("LIBROS_ADMIN", "************************************** INICIO LISTA_LIBROS_ADMIN");

		// Get the view from fm_lista_libros_admin.xmladmin.xml
		View view = inflater.inflate(R.layout.fm_lista_libros_admin, container, false);

        btnReporte = (ImageButton) view.findViewById(R.id.btnReporteLibroAdmin);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarReporte();
            }
        });

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


                for(int i = 0; i < listaLibros.size(); i++){
                    List<Autor> listaAutores = tareasGenerales.listarLibroAutorOnlyAutor(listaLibros.get(i).getIdLibro());
                    listaLibros.get(i).setListaAutores(listaAutores);
                }

            }catch (Exception e){
                resultadoTarea = false;
                Log.e("ListaLibrosAdmin ", "xxx Error TareaWsBuscarLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    adapterLibro = new LibroListAdapterAdmin(getActivity(), listaLibros);
                    libroListView.setAdapter(adapterLibro);
                }catch (Exception e){
                    Log.e("ListaLibrosAdmin","XXX Error listando libros: "+e.getMessage());
                }
            }else{
                String msn = "Error listando libros";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo encargado de generar un reporte .xls conteniendo el listado de libros.
     */
    public void generarReporte(){

        UtilidadGenerarReportes utilidadReporte = new UtilidadGenerarReportes();
        utilidadReporte.setListaLibros(listaLibros);
        utilidadReporte.setTipoArchivo(1);
        utilidadReporte.saveExcelFile(getActivity(), "LibrosFUP.xls");
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
