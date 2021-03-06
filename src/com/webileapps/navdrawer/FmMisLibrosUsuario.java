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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Libro;
import modelo.Solicitud;
import util.DatasourceSolicitudes;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Pablo on 11/05/2015.
 * @author paarma80@gmail.com
 */
public class FmMisLibrosUsuario extends SherlockFragment {

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Solicitud> adapterSolicitud;
    private ListView libroListView;

    private List<Solicitud> listaSolicitud = new ArrayList<Solicitud>();
    private Libro libroSeleccionado;

    ImageButton btnRefrescar;


    private DatasourceSolicitudes datasourceSolicitudes;
    private static final int PAGESIZE = 10;
    private TextView textViewDisplaying;
    View viewAux = null;
    View footerView;
    boolean loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("MIS_LIBROS","************************************** INICIO MIS_LIBROS");

        View view = inflater.inflate(R.layout.fm_mis_libros_usuario, container, false);

        datasourceSolicitudes = DatasourceSolicitudes.getInstance();
        viewAux = view;

        footerView = inflater.inflate(R.layout.footer_load, null);


        inicializarComponentes(view);
        inicializarListaLibros();

        btnRefrescar = (ImageButton) view.findViewById(R.id.btnRefrescarMisLibros);

        btnRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarListaLibros();
            }
        });


        //Scroll del listView
        libroListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        libroListView.addFooterView(footerView);
                        (new LoadNextPage()).execute("");
                    }
                }catch (Exception e){
                    Log.e("MisLibros","xxx Error scroll listaMisLibros: "+e.getMessage());
                }

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
            Log.e("MisLibros","xxx Error desplegando label cantidad registros: "+e.getMessage());
        }
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

                try {
                    //libroSeleccionado = listaSolicitud.get(posicion).getLibro();
                    Solicitud solicitudSeleccionada = (Solicitud) libroListView.getItemAtPosition(posicion);
                    libroSeleccionado = solicitudSeleccionada.getLibro();
                }catch (Exception e){
                    e.printStackTrace();
                }


                //Se ocultan todos los detalles de libros que esten deplegados
//                try {
//                    for(int j = 0; j<libroListView.getCount(); j++){
//                        View containerAux = libroListView.getChildAt(j);
//                        if(containerAux != null) {
//                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroMisLibros).setVisibility(View.GONE);
//                        }
//                    }
//                }catch (Exception e){
//                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
//                }

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
        int cantidad = 0;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                //listaSolicitud = tareasGenerales.buscarSolicitudes(variablesGlobales.getSolicitudBuscar());
                cantidad = tareasGenerales.cantidadSolicitudes(variablesGlobales.getSolicitudBuscar());
                Log.i("MisLibros",">>>>>>>>>>> Tamaño lista Mislibros buscada: "+cantidad);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("MisLibros ", "xxx Error TareaWsBuscarMisLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    //adapterSolicitud = new MisLibrosListAdapterUsuario(getActivity(), listaSolicitud);
                    //libroListView.setAdapter(adapterSolicitud);

                    datasourceSolicitudes.setSIZE(cantidad);

                    adapterSolicitud = new MisLibrosListAdapterUsuario(getActivity(), datasourceSolicitudes.getData(0, PAGESIZE));

                    libroListView.addFooterView(footerView);
                    libroListView.setAdapter(adapterSolicitud);
                    libroListView.removeFooterView(footerView);

                    updateDisplayingTextView();

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

            libroListView.removeFooterView(footerView);
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
        inicializarListaLibros();
    }

}