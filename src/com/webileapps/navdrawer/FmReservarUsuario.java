
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
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Autor;
import modelo.Libro;
import modelo.Solicitud;
import util.TareasGenerales;
import util.Utilidades;
import util.VariablesGlobales;

public class FmReservarUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Libro> adapterLibro;
    private ListView libroListView;

    private List<Libro> listaLibros = new ArrayList<Libro>();
    private Libro libroSeleccionado;
    private Solicitud solicitud;

    //Datepiker fecha reserva
    DatePicker dpFechaReserva;
    ImageButton btnReservar;

    GridLayout gridLayoutBtnReservar;

    TareasGenerales tareasGenerales = new TareasGenerales();
    private LinearLayout linearListViewAutores;
    ViewGroup parentAux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("RESERVAR","************************************** INICIO RESERVAR");

        parentAux = container;

        // Get the view from fm_crear_libro_adminro_admin.xml
        View view = inflater.inflate(R.layout.fm_lista_libros_reservar_usuario, container, false);

        btnReservar = (ImageButton) view.findViewById(R.id.btnReservarUser);

        inicializarComponentes(view);
        inicializarListaLibros();

        btnReservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(libroSeleccionado != null) {
                    Log.i("Reservar", ">>>>>>>>>>>>fecha reserva: " +
                            Utilidades.formatoFechaYYYYMMDD.format(Utilidades.getDateFromDatePicker(dpFechaReserva)));
                    Log.i("Reservas", ">>>>>>>>>>>>>>> Fecha devolucion: " +
                            Utilidades.sumarRestarDiasAFecha(
                                    Utilidades.getDateFromDatePicker(dpFechaReserva), Utilidades.diasTotalesPrestamo));


                    solicitud = new Solicitud();
                    solicitud.setFechaSolicitud(new Date());
                    solicitud.setFechaReserva(Utilidades.getDateFromDatePicker(dpFechaReserva));
                    solicitud.setFechaDevolucion(Utilidades.sumarRestarDiasAFecha(solicitud.getFechaReserva(), Utilidades.diasTotalesPrestamo));
                    solicitud.setUsuario(variablesGlobales.getUsuarioLogueado());
                    solicitud.setLibro(libroSeleccionado);
                    solicitud.setEstado(Utilidades.estadoEnProceso);

                    TareaWsReservar tareaReservar = new TareaWsReservar();
                    tareaReservar.execute();
                }else{
                    Toast.makeText(getActivity(), "Seleccione un libro", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewReservarUsuario);
        gridLayoutBtnReservar = (GridLayout) view.findViewById(R.id.gridLayoutBtnReservar);
    }

    /**
     * Se carga el listado de libros provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     * y el metodo onclickLong para desplegar los detalles del libro
     */
    private void inicializarListaLibros(){

        TareaWsBuscarLibros tareaListarLibro = new TareaWsBuscarLibros();
        tareaListarLibro.execute();
        libroSeleccionado = null;

        //Evento al seleccionar un elemento de la lista
        libroListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                libroSeleccionado = listaLibros.get(posicion);

                cargarAutoresAsociados(vista);

                //Se ocultan todos los detalles de libros que esten deplegados
                /*try {
                    for(int j = 0; j<libroListView.getCount(); j++){
                        View containerAux = libroListView.getChildAt(j);
                        if(containerAux != null) {
                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroReservar).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
                }*/

                //Se obtiene la referencia del Datepiker
                dpFechaReserva = (DatePicker) vista.findViewById(R.id.datePickerFechaReserva);

                //Se inhabilita seleccion de fechas pasadas en el datepicker
/*                Calendar minCalendar = Calendar.getInstance();
                minCalendar.set(Calendar.MILLISECOND, minCalendar.MILLISECOND - 1000);
                dpFechaReserva.setMinDate(minCalendar.getTimeInMillis() - 1000);*/

                /////////////////////////////////////////////////////////////////////
                //Se oculta el año del datepicker para solo mostrar el dia y la fecha.
                try {
                    Field f[] = dpFechaReserva.getClass().getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) {
                            field.setAccessible(true);
                            Object yearPicker = new Object();
                            yearPicker = field.get(dpFechaReserva);
                            ((View) yearPicker).setVisibility(View.GONE);
                        }
                    }
                }
                catch (SecurityException e) {
                    Log.d("ERROR", e.getMessage());
                }
                catch (IllegalArgumentException e) {
                    Log.d("ERROR", e.getMessage());
                }
                catch (IllegalAccessException e) {
                    Log.d("ERROR", e.getMessage());
                }
                ////////////////////////////////////////////////////////////////////
                /////////////////Fin ocultar campos del datepicker

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleLibroReservar).setVisibility(View.VISIBLE);


                //Se verifica la cantidad del libro para verificar si es posible reservarlo o no
                gridLayoutBtnReservar.setVisibility(View.GONE);
                if(libroSeleccionado != null &&
                        libroSeleccionado.getCantidad() > Utilidades.cantidadMininaLibroPrestar){
                    gridLayoutBtnReservar.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /**
     * Carga los autores asociados a un libro
     * @param vista
     */
    public void cargarAutoresAsociados(View vista){

        linearListViewAutores = (LinearLayout) vista.findViewById(R.id.linear_listview_autores);
        linearListViewAutores.removeAllViewsInLayout();

        //Se agrega una fila vacia en el layout de autores en el caso de no tener autores asociados.
        View mLinearViewAux = getActivity().getLayoutInflater().inflate(R.layout.row_autores_libro_gray,parentAux,false);
        linearListViewAutores.addView(mLinearViewAux);

        TareaWsAutoresAsociados tareaWsAutoresAsociados = new TareaWsAutoresAsociados();
        tareaWsAutoresAsociados.execute();
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
                listaLibros = tareasGenerales.buscarLibros(variablesGlobales.getLibroBuscar());
                Log.i("Reservar",">>>>>>>>>>> Tamaño lista libros buscada: "+listaLibros.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.d("ReservarUsuario ", "xxx Error TareaWsBuscarLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    adapterLibro = new LibroListAdapterUsuario(getActivity(), listaLibros);
                    libroListView.setAdapter(adapterLibro);
                }catch (Exception e){
                    Log.e("Reservar","XXX Error cargando pantalla reservar: "+e.getMessage());
                }
            }else{
                //Se despliega mensaje de error si esta en la pantalla de "reservar"
                if(variablesGlobales.getOpcionMenu() == 0) {
                    String msn = "Error listando libros";
                    Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Tarea encargada de reservar un libro
     */
    private class TareaWsReservar extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                resultadoTarea = tareasGenerales.reservar(solicitud);
            }catch (Exception e){
                resultadoTarea = false;
                Log.d("ReservarUsuario ", "xxx Error TareaWsReservar: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                Toast.makeText(getActivity(), "Reserva exitosa", Toast.LENGTH_LONG).show();

                //Se ocultan todos los detalles de libros que esten deplegados
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

            }else{
                Toast.makeText(getActivity(), "Error reservando libro", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Tarea encargada de listar los autores asociados a un determinado libro
     */
    private class TareaWsAutoresAsociados extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;
        private List<Autor> listaAutores;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                listaAutores = tareasGenerales.listarLibroAutorOnlyAutor(libroSeleccionado.getIdLibro());
                Log.i("Reservar",">>>>>>>>>>> Tamaño lista autoresAsocuados: "+listaAutores.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("Reservar ", "xxx Error TareaWsAutoresAsociados: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){

                if(listaAutores != null && listaAutores.size() > 0){

                    linearListViewAutores.removeAllViewsInLayout();

                    for(int i = 0; i < listaAutores.size(); i++){
                        /**
                         * inflate items/ add items in linear layout instead of listview
                         */
                        //View mLinearView = inflaterAux.inflate(R.layout.row_autores_libro, null);
                        View mLinearView = getActivity().getLayoutInflater().inflate(R.layout.row_autores_libro_gray,parentAux,false);
                        /**
                         * getting id of row.xml
                         */
                        TextView mFirstName = (TextView) mLinearView
                                .findViewById(R.id.textViewName);
                        TextView mLastName = (TextView) mLinearView
                                .findViewById(R.id.textViewLastName);

                        /**
                         * set item into row
                         */
                        final String fName = listaAutores.get(i).getPrimerNombre();
                        final String lName = listaAutores.get(i).getPrimerApellido();
                        mFirstName.setText(fName);
                        mLastName.setText(lName);

                        /**
                         * add view in top linear
                         */
                        linearListViewAutores.addView(mLinearView);
                    }
                }

            }else{
                Log.e("listaAutores"," XXX Error listando autoresAsocuadis");
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
        //inicializarListaLibros();
    }
}
