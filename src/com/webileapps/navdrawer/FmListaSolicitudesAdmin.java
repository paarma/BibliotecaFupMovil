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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Solicitud;
import util.DatasourceSolicitudes;
import util.TareasGenerales;
import util.UtilidadGenerarReportes;
import util.Utilidades;
import util.VariablesGlobales;

/**
 * Created by Pablo on 9/05/15.
 * @author paarma80@gmail.com
 */
public class FmListaSolicitudesAdmin extends SherlockFragment {

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Solicitud> adapterSolicitud;
    private ListView solicitudListView;
    private ImageButton btnReporte;

    private List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
    private static Solicitud solicitudSeleccionada;
    private boolean solicitudEnMora;

    private ImageButton btnAccionLibroAdmin, btnRefrescar;

    private GridLayout gridLayoutBtnAccion;


    private DatasourceSolicitudes datasourceSolicitudes;
    private static final int PAGESIZE = 10;
    private TextView textViewDisplaying;
    View viewAux = null;
    View footerView;
    boolean loading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("SOLICITUD", "************************************** INICIO LISTA_SOLICITUD_ADMIN");
        View view = inflater.inflate(R.layout.fm_lista_solicitudes_admin, container, false);

        datasourceSolicitudes = DatasourceSolicitudes.getInstance();
        viewAux = view;

        footerView = inflater.inflate(R.layout.footer_load, null);


        btnReporte = (ImageButton) view.findViewById(R.id.btnReporteSolicitudAdmin);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarReporte();
            }
        });

        inicializarComponentes(view);
        inicializarListaSolicitudes();

        btnAccionLibroAdmin = (ImageButton) view.findViewById(R.id.btnAccionLibroAdmin);
        btnAccionLibroAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gestionSolicitud();
            }
        });

        btnRefrescar = (ImageButton) view.findViewById(R.id.btnRefrescarSolicitudAdmin);
        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarListaSolicitudes();
            }
        });


        //Scroll del listView
        solicitudListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                try {

                    //boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && libroListView.getChildAt(visibleItemCount -1) != null && libroListView.getChildAt(visibleItemCount-1).getBottom() <= libroListView.getHeight();
                    boolean lastItem = (firstVisibleItem + visibleItemCount == totalItemCount);
                    boolean moreRows = adapterSolicitud.getCount() < datasourceSolicitudes.getSize();

                    if (!loading &&  lastItem && moreRows)
                    {
                        loading = true;
                        solicitudListView.addFooterView(footerView);
                        (new LoadNextPage()).execute("");
                    }
                }catch (Exception e){
                    Log.e("SolicitudAdmin","xxx Error scroll listaSolicitudAdmin: "+e.getMessage());
                }

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
     * Mensaje de cabezera indicando la cantidad de registros
     */
    private void updateDisplayingTextView()
    {
        try {
            textViewDisplaying = (TextView) viewAux.findViewById(R.id.displaying);
            String text = getString(R.string.display);
            text = String.format(text, adapterSolicitud.getCount(), datasourceSolicitudes.getSize());
            textViewDisplaying.setText(text);
        }catch (Exception e){
            Log.e("Solicitudes","xxx Error desplegando label cantidad registros: "+e.getMessage());
        }
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

                //solicitudSeleccionada = listaSolicitudes.get(posicion);
                solicitudSeleccionada = (Solicitud) solicitudListView.getItemAtPosition(posicion);

                //Se ocultan todos los detalles de libros que esten deplegados
//                try {
//                    for(int j = 0; j<solicitudListView.getCount(); j++){
//                        View containerAux = solicitudListView.getChildAt(j);
//                        if(containerAux != null) {
//                            solicitudListView.getChildAt(j).findViewById(R.id.contenedorDetalleSolAdmin).setVisibility(View.GONE);
//                        }
//                    }
//                }catch (Exception e){
//                    Log.e("Error", "Error ocultando detalles de la solicitud: " + e.getMessage());
//                }

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
        int cantidad = 0;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                //listaSolicitudes = tareasGenerales.buscarSolicitudes(variablesGlobales.getSolicitudBuscar());
                cantidad = tareasGenerales.cantidadSolicitudes(variablesGlobales.getSolicitudBuscar());
                Log.i("SolAdmin",">>>>>>>>>>> Tamaño lista solicitud buscada: "+cantidad);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("SolAdmin ", "xxx Error TareaWsBuscarSolicitudes: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    //adapterSolicitud = new SolicitudListAdapterAdmin(getActivity(), listaSolicitudes);
                    //solicitudListView.setAdapter(adapterSolicitud);

                    datasourceSolicitudes.setSIZE(cantidad);

                    adapterSolicitud = new SolicitudListAdapterAdmin(getActivity(), datasourceSolicitudes.getData(0, PAGESIZE));

                    solicitudListView.addFooterView(footerView);
                    solicitudListView.setAdapter(adapterSolicitud);
                    solicitudListView.removeFooterView(footerView);

                    updateDisplayingTextView();

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

                //////////////Funcionalidad para multas
                if(solicitudSeleccionada.getEstado().equals(Utilidades.estadoEnMora)) {
                    solicitudEnMora = true;
                }else{
                    solicitudEnMora = false;
                }

                //////////////Fin funcionalidad para multas

                /////////////////////////Se fija el estado en el cual quedara la solicitud.
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
                    if(solicitudEnMora) {
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
            //inicializarListaSolicitudes();
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


    /**
     * Metodo encargado de generar un reporte .xls conteniendo el listado de solicitudes.
     */
    public void generarReporte(){

        UtilidadGenerarReportes utilidadReporte = new UtilidadGenerarReportes();
        utilidadReporte.setTipoArchivo(2);
        utilidadReporte.saveExcelFile(getActivity(), "reservasFUP.xls");
    }


    /**
     * Clase encargada de carga la siguente pagina del listView
     */
    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        private List<Solicitud> newData = null;
        @Override
        protected String doInBackground(String... arg0)
        {
            //para que de tiempo a ver el footer <span class="wp-smiley wp-emoji wp-emoji-wink" title=";)">;)</span>
            try
            {
                Thread.sleep(1000);
                newData = datasourceSolicitudes.getData(adapterSolicitud.getCount(), PAGESIZE);
            }
            catch (InterruptedException e){
                Log.e("LoadNextPage","xxx Error cargando siguiente pagina InterruptedException: "+e.getMessage());
            }
            catch (Exception e)
            {
                Log.e("LoadNextPage","xxx Error cargando siguiente pagina Exception: "+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {

            for (Solicitud value : newData)
            {
                adapterSolicitud.add(value);
            }
            adapterSolicitud.notifyDataSetChanged();

            solicitudListView.removeFooterView(footerView);
            updateDisplayingTextView();
            loading = false;
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