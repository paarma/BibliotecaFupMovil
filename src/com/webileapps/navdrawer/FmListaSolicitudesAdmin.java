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

import modelo.Solicitud;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Pablo on 9/05/15.
 */
public class FmListaSolicitudesAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Solicitud> adapterSolicitud;
    private ListView solicitudListView;

    private List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
    private Solicitud solicitudSeleccionada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("SOLICITUD", "************************************** INICIO LISTA_SOLICITUD_ADMIN");
        View view = inflater.inflate(R.layout.fm_lista_solicitudes_admin, container, false);

        inicializarComponentes(view);
        inicializarListaSolicitudes();

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        solicitudListView = (ListView) view.findViewById(R.id.listViewSolicitudesAdmin);
    }

    /**
     * Se carga el listado de solicitudes provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaSolicitudes(){

        TareaWsBuscarSolicitudes tareaListarSolicitud = new TareaWsBuscarSolicitudes();
        tareaListarSolicitud.execute();

        //Evento al seleccionar un elemento de la lista
        solicitudListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                solicitudSeleccionada = listaSolicitudes.get(posicion);

                //Se ocultan todos los detalles de libros que esten deplegados
                try {
                    for(int j = 0; j<solicitudListView.getCount(); j++){
                        View containerAux = solicitudListView.getChildAt(j);
                        if(containerAux != null) {
                            solicitudListView.getChildAt(j).findViewById(R.id.contenedorDetalleSolAdmin).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles de la solicitud: " + e.getMessage());
                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleSolAdmin).setVisibility(View.VISIBLE);
            }
        });
    }

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar las  solicitudes de la biblioteca ya sea el listado general
     * o un listado segun parametros de una solicitud a buscar
     */
    private class TareaWsBuscarSolicitudes extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaSolicitudes = tareasGenerales.buscarSolicitudes(variablesGlobales.getLibroBuscar(),
                        0, "");
                Log.i("SolAdmin",">>>>>>>>>>> Tamaño lista solicitud buscada: "+listaSolicitudes.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("SolAdmin ", "xxx Error TareaWsBuscarSolicitudes: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                adapterSolicitud = new SolicitudListAdapterAdmin(getActivity(), listaSolicitudes);
                solicitudListView.setAdapter(adapterSolicitud);
            }else{
                String msn = "Error listando solicitudes";
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
        inicializarListaSolicitudes();
    }
}