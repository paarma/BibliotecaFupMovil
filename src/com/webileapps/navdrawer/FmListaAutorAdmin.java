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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Autor;
import util.DatasourceAutores;
import util.TareasGenerales;
import util.VariablesGlobales;

/**
 * Created by Pablo on 8/07/15.
 */
public class FmListaAutorAdmin extends SherlockFragment {

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Autor> adapterAutor;
    private ListView autorListView;

    private List<Autor> listaAutores = new ArrayList<Autor>();
    private Autor autorSeleccionado;


    private DatasourceAutores datasourceAutores;
    private static final int PAGESIZE = 10;
    private TextView textViewDisplaying;
    View viewAux = null;
    View footerView;
    boolean loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("AUTOR_ADMIN", "************************************** INICIO LISTA_AUTORES_ADMIN");
        View view = inflater.inflate(R.layout.fm_lista_autor_admin, container, false);

        datasourceAutores = DatasourceAutores.getInstance();
        viewAux = view;

        footerView = inflater.inflate(R.layout.footer_load, null);

        inicializarComponentes(view);
        inicializarListaAutores();


        //Scroll del listView
        autorListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                try {

                    //boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && libroListView.getChildAt(visibleItemCount -1) != null && libroListView.getChildAt(visibleItemCount-1).getBottom() <= libroListView.getHeight();
                    boolean lastItem = (firstVisibleItem + visibleItemCount == totalItemCount);
                    boolean moreRows = adapterAutor.getCount() < datasourceAutores.getSize();

                    if (!loading &&  lastItem && moreRows)
                    {
                        loading = true;
                        autorListView.addFooterView(footerView);
                        (new LoadNextPage()).execute("");
                    }
                }catch (Exception e){
                    Log.e("AutoresAdmin","xxx Error scroll listaAutoresAdmin: "+e.getMessage());
                }

            }
        });

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        autorListView = (ListView) view.findViewById(R.id.listViewAutoresAdmin);
    }

    /**
     * Mensaje de cabezera indicando la cantidad de registros
     */
    private void updateDisplayingTextView()
    {
        textViewDisplaying = (TextView) viewAux.findViewById(R.id.displaying);
        String text = getString(R.string.display);
        text = String.format(text, adapterAutor.getCount(), datasourceAutores.getSize());
        textViewDisplaying.setText(text);
    }

    /**
     * Se carga el listado de autores provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaAutores(){

        TareaWsBuscarAutores tareaListarAutor = new TareaWsBuscarAutores();
        tareaListarAutor.execute();

        //Evento al seleccionar un elemento de la lista
        autorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                //autorSeleccionado = listaAutores.get(posicion);
                autorSeleccionado = (Autor) autorListView.getItemAtPosition(posicion);
                variablesGlobales.setAutorSeleccionadoAdmin(autorSeleccionado);

                //Se ocultan todos los detalles de autores que esten deplegados
//                try {
//                    for(int j = 0; j<autorListView.getCount(); j++){
//                        View containerAux = autorListView.getChildAt(j);
//                        if(containerAux != null) {
//                            autorListView.getChildAt(j).findViewById(R.id.contenedorDetalleAutorAdmin).setVisibility(View.GONE);
//                        }
//                    }
//                }catch (Exception e){
//                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
//                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleAutorAdmin).setVisibility(View.VISIBLE);
            }
        });

    }


    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar los autores de la biblioteca ya sea el listado general
     * o un listado segun parametros de un autor a buscar
     */
    private class TareaWsBuscarAutores extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;
        int cantidad = 0;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                //listaAutores = tareasGenerales.listarAutores(variablesGlobales.getAutorBuscar());
                cantidad = tareasGenerales.cantidadAutores(variablesGlobales.getAutorBuscar());
                Log.i("AutorAdmin",">>>>>>>>>>> Tamaño lista autores buscada: "+cantidad);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("AutorAdmin ", "xxx Error TareaWsBuscarAutores: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {

                    datasourceAutores.setSIZE(cantidad);

                    adapterAutor = new AutorListAdapterAdmin(getActivity(), datasourceAutores.getData(0, PAGESIZE));

                    autorListView.addFooterView(footerView);
                    autorListView.setAdapter(adapterAutor);
                    autorListView.removeFooterView(footerView);

                    updateDisplayingTextView();

                }catch (Exception e){
                    Log.e("AutorAdmin ", "xxx Error listando autores: " + e.getMessage());
                }
            }else{
                String msn = "Error listando autores";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Clase encargada de carga la siguente pagina del listView
     */
    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        private List<Autor> newData = null;
        @Override
        protected String doInBackground(String... arg0)
        {
            //para que de tiempo a ver el footer <span class="wp-smiley wp-emoji wp-emoji-wink" title=";)">;)</span>
            try
            {
                Thread.sleep(1000);
                newData = datasourceAutores.getData(adapterAutor.getCount(), PAGESIZE);
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

            for (Autor value : newData)
            {
                adapterAutor.add(value);
            }
            adapterAutor.notifyDataSetChanged();

            autorListView.removeFooterView(footerView);
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
        inicializarListaAutores();
    }
}