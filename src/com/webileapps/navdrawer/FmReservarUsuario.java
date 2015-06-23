
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the view from fm_crear_libro_adminro_admin.xml
        View view = inflater.inflate(R.layout.fm_lista_libros_reservar_usuario, container, false);

        btnReservar = (ImageButton) view.findViewById(R.id.btnReservarUser);

        inicializarComponentes(view);
        inicializarListaLibros();

        btnReservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("Reservar", ">>>>>>>>>>>>fecha reserva: " +
                        Utilidades.formatoFechaYYYYMMDD.format(Utilidades.getDateFromDatePicker(dpFechaReserva)));
                Log.i("Reservas", ">>>>>>>>>>>>>>> Fecha devolucion: "+
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
            }
        });

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewReservarUsuario);


        /**
         * Se inicializa el objeto libroBuscar
         * Funcionalidad necesaria para fijar los parametros
         * por defecto para la busqueda de libro
         */
        if(variablesGlobales.getLibroBuscar() == null){
            variablesGlobales.setLibroBuscar(new Libro());
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
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                libroSeleccionado = listaLibros.get(posicion);

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

                //Se obtiene la referencia del Datepiker
                dpFechaReserva = (DatePicker) vista.findViewById(R.id.datePickerFechaReserva);

                //Se inhabilita seleccion de fechas pasadas en el datepicker
/*                Calendar minCalendar = Calendar.getInstance();
                minCalendar.set(Calendar.MILLISECOND, minCalendar.MILLISECOND - 1000);
                dpFechaReserva.setMinDate(minCalendar.getTimeInMillis() - 1000);*/

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
                listaLibros = tareasGenerales.buscarLibros(variablesGlobales.getLibroBuscar());
                Log.i("Reservar",">>>>>>>>>>> Tamaño lista libros buscada: "+listaLibros.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.d("ReservarUsuario ", "xxx Error TareaWsBuscarLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            //Se inicializa el  objeto de busqueda Libro
            variablesGlobales.setLibroBuscar(new Libro());

            if(result){
                adapterLibro = new LibroListAdapterUsuario(getActivity(), listaLibros);
                libroListView.setAdapter(adapterLibro);
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
                TareasGenerales tareasGenerales = new TareasGenerales();
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
}
