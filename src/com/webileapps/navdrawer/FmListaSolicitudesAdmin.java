package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Solicitud;
import util.TareasGenerales;
import util.Utilidades;
import util.VariablesGlobales;

/**
 * Created by Pablo on 9/05/15.
 */
public class FmListaSolicitudesAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Solicitud> adapterSolicitud;
    private ListView solicitudListView;

    private List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
    private static Solicitud solicitudSeleccionada;

    private ImageButton btnAccionLibroAdmin;

    private GridLayout gridLayoutBtnAccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("SOLICITUD", "************************************** INICIO LISTA_SOLICITUD_ADMIN");
        View view = inflater.inflate(R.layout.fm_lista_solicitudes_admin, container, false);

        inicializarComponentes(view);
        inicializarListaSolicitudes();

        btnAccionLibroAdmin = (ImageButton) view.findViewById(R.id.btnAccionLibroAdmin);
        btnAccionLibroAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gestionSolicitud();
            }
        });

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        solicitudListView = (ListView) view.findViewById(R.id.listViewSolicitudesAdmin);
        gridLayoutBtnAccion = (GridLayout) view.findViewById(R.id.gridLayoutBtnAccion);
    }

    /**
     * Metodo encargado de prestar o retornar un libro segun solicitud seleccionada
     */
    public void gestionSolicitud(){
        if(solicitudSeleccionada == null){
            Toast.makeText(getActivity(), "Seleccione una solicitud", Toast.LENGTH_LONG).show();
        }else{
            TareaWsGestionLibro tareaGestionLibro = new TareaWsGestionLibro();
            tareaGestionLibro.execute();
        }
    }

    /**
     * Se carga el listado de solicitudes provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaSolicitudes(){

        TareaWsBuscarSolicitudes tareaListarSolicitud = new TareaWsBuscarSolicitudes();
        tareaListarSolicitud.execute();

        gridLayoutBtnAccion.setVisibility(View.GONE);

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


                //Si la solicitud seleccionada tiene estado PRESTADO O FINALIZADO,
                // se oculta el boton "AccionSolicitud"
                if(solicitudSeleccionada != null){
                    if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoFinalizado)){
                        gridLayoutBtnAccion.setVisibility(View.GONE);
                    }else{
                        gridLayoutBtnAccion.setVisibility(View.VISIBLE);

                        if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoEnProceso)){
                            btnAccionLibroAdmin.setImageResource(R.drawable.ic_assignment_turned_in_white_48dp);

                        }else if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoPrestado) ||
                                solicitudSeleccionada.getEstado().equals(Utilidades.estadoEnMora)){
                            btnAccionLibroAdmin.setImageResource(R.drawable.ic_assignment_returned_white_48dp);
                        }
                    }
                }
                ////////////////////////////////////////////////////////////////////////////////////

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
                listaSolicitudes = tareasGenerales.buscarSolicitudes(variablesGlobales.getSolicitudBuscar());
                Log.i("SolAdmin",">>>>>>>>>>> Tamaño lista solicitud buscada: "+listaSolicitudes.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("SolAdmin ", "xxx Error TareaWsBuscarSolicitudes: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    adapterSolicitud = new SolicitudListAdapterAdmin(getActivity(), listaSolicitudes);
                    solicitudListView.setAdapter(adapterSolicitud);
                }catch (Exception e){
                    Log.e("SolAdmin ", "xxx Error TareaWsBuscarSolicitudes: " + e.getMessage());
                }

            }else{
                String msn = "Error listando solicitudes";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Tarea encargada de prestar o retornar un libro.
     * Cambia el estado de la solicitud por "PRESTADO" o "FINALIZADO"
     * El llamado al WS recibe los parametros:
     *  idSolicitud: En caso de ser = 0, se actualizaran todas las solicitudes, junto con el parametro "updateAll".
     *  estado: Estado al cual se modificara la solicitud.
     *  updateAll: Indica si se actualizan todas las solicitudes o no. (No aplica para este caso)
     *  fechaDevolucion: Indica la fecha en la cual se regresa el libro.
     */
    private class TareaWsGestionLibro extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                //Si la solicitud esta en estado "EN PROCESO", pasa a estado "PRESTADO"
                if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoEnProceso)){
                    solicitudSeleccionada.setEstado(Utilidades.estadoPrestado);
                }else{
                    //De lo contrario indica que se esta regresando un libro.
                    solicitudSeleccionada.setEstado(Utilidades.estadoFinalizado);
                    solicitudSeleccionada.setFechaEntrega(new Date());
                }

                resultadoTarea = tareasGenerales.actualizarSolicitudes(solicitudSeleccionada, false);
                Log.i("SolAdmin",">>>>>>>>>>> TareaWsGestionLibro: "+listaSolicitudes.size());

            }catch (Exception e){
                Log.e("SolAdmin ", "xxx Error TareaWsGestionLibro: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try{
                    String msn = "Registro exitoso";
                    Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
                    inicializarListaSolicitudes();

                    //Si la solicitud tenia estado es "EN  MORA", se gestiona la funcionalidad de multas.
                    if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoEnMora)) {
                        llamarMultas();
                    }

                }catch (Exception e){
                    Log.e("SolAdmin","XXX Error prestando o regresando TareaWsGestionLibro: "+e.getMessage());
                }

            }else{
                String msn = "Error registrando datos";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
            solicitudSeleccionada = null;
            inicializarListaSolicitudes();
        }
    }

    /**
     * Metodo encargado para gestionar la funcionalidad de multas
     */
    public void llamarMultas(){
        try {
            DialogFragment dialogMultas = new CustomDialogMultas();

            //Dias mora
            Long diasMora = Utilidades.diasDiferenciaEntreFechas(
                    solicitudSeleccionada.getFechaReserva(), new Date());

            //Enviamos parametros al dialog.
            Bundle args = new Bundle();
            args.putInt("idSolicitud",solicitudSeleccionada.getIdSolicitud());
            args.putLong("diasMora", diasMora);
            dialogMultas.setArguments(args);

            dialogMultas.show(getFragmentManager(), "dialogMultas");

        }catch (Exception e){
            Log.e("DialogMulta","XXX Error desplegando multas: "+e.getMessage());
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

    //Metodos Getters and Setetters
    public static Solicitud getSolicitudSeleccionada() {
        return solicitudSeleccionada;
    }

    public static void setSolicitudSeleccionada(Solicitud solicitudSeleccionada) {
        FmListaSolicitudesAdmin.solicitudSeleccionada = solicitudSeleccionada;
    }
}