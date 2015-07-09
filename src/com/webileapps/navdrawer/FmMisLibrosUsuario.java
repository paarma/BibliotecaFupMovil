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

import modelo.Libro;
import modelo.Solicitud;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Damian on 11/05/2015.
 */
public class FmMisLibrosUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Solicitud> adapterSolicitud;
    private ListView libroListView;

    private List<Solicitud> listaSolicitud = new ArrayList<Solicitud>();
    private Libro libroSeleccionado;

    ImageButton btnRefrescar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("MIS_LIBROS","************************************** INICIO MIS_LIBROS");

        View view = inflater.inflate(R.layout.fm_mis_libros_usuario, container, false);

        inicializarComponentes(view);
        inicializarListaLibros();

        btnRefrescar = (ImageButton) view.findViewById(R.id.btnRefrescarMisLibros);

        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarListaLibros();
            }
        });

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewMisLibrosUsuario);
    }

    /**
     * Se carga el listado de libros provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     * y el metodo onclickLong para desplegar los detalles del libro
     */
    private void inicializarListaLibros(){

        TareaWsBuscarMisLibros tareaListarMisLibros = new TareaWsBuscarMisLibros();
        tareaListarMisLibros.execute();

        //Evento al seleccionar un elemento de la lista
        libroListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                libroSeleccionado = listaSolicitud.get(posicion).getLibro();

                //Se ocultan todos los detalles de libros que esten deplegados
                try {
                    for(int j = 0; j<libroListView.getCount(); j++){
                        View containerAux = libroListView.getChildAt(j);
                        if(containerAux != null) {
                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroMisLibros).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleLibroMisLibros).setVisibility(View.VISIBLE);
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
    private class TareaWsBuscarMisLibros extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaSolicitud = tareasGenerales.buscarSolicitudes(variablesGlobales.getSolicitudBuscar());
                Log.i("MisLibros",">>>>>>>>>>> Tamaño lista Mislibros buscada: "+listaSolicitud.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("MisLibros ", "xxx Error TareaWsBuscarMisLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    adapterSolicitud = new MisLibrosListAdapterUsuario(getActivity(), listaSolicitud);
                    libroListView.setAdapter(adapterSolicitud);
                }catch (Exception e){
                    Log.e("MisLibros","XXX Error cargando MisLibros: "+e.getMessage());
                }

            }else{
                //Se despliega mensaje de error si esta en la pantalla de "misLibros"
                if(variablesGlobales.getOpcionMenu()==1) {
                    String msn = "Error listando mis libros";
                    Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
                }
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