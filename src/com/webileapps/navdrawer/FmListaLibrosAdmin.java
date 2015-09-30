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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import java.util.List;

import modelo.Autor;
import modelo.Libro;
import util.DatasourceLibros;
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

    TareasGenerales tareasGenerales = new TareasGenerales();
    private LinearLayout linearListViewAutores;
    ViewGroup parentAux;


    private DatasourceLibros datasourceLibros;
    private static final int PAGESIZE = 10;
    private TextView textViewDisplaying;
    View viewAux = null;
    View footerView;
    boolean loading;
    LayoutInflater inflaterAux;


	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i("LIBROS_ADMIN", "************************************** INICIO LISTA_LIBROS_ADMIN");

        parentAux = container;
        inflaterAux = inflater;

		// Get the view from fm_lista_libros_admin.xmladmin.xml
		View view = inflater.inflate(R.layout.fm_lista_libros_admin, container, false);

        datasourceLibros = DatasourceLibros.getInstance();
        viewAux = view;

        footerView = inflater.inflate(R.layout.footer_load, null);

        btnReporte = (ImageButton) view.findViewById(R.id.btnReporteLibroAdmin);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarReporte();
            }
        });

        inicializarComponentes(view);
        inicializarListaLibros();

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
                    boolean moreRows = adapterLibro.getCount() < datasourceLibros.getSize();

                    if (!loading &&  lastItem && moreRows)
                    {
                        loading = true;
                        libroListView.addFooterView(footerView);
                        (new LoadNextPage()).execute("");
                    }
                }catch (Exception e){
                    Log.e("LibrosAdmin","xxx Error scroll listaLibrosAmin: "+e.getMessage());
                }

            }
        });


		return view;
	}

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        libroListView = (ListView) view.findViewById(R.id.listViewLibrosAdmin);
    }

    /**
     * Mensaje de cabezera indicando la cantidad de registros
     */
    private void updateDisplayingTextView()
    {
        textViewDisplaying = (TextView) viewAux.findViewById(R.id.displaying);
        String text = getString(R.string.display);
        text = String.format(text, adapterLibro.getCount(), datasourceLibros.getSize());
        textViewDisplaying.setText(text);
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

                //libroSeleccionado = listaLibros.get(posicion);
                libroSeleccionado = (Libro) libroListView.getItemAtPosition(posicion);
                variablesGlobales.setLibroSeleccionadoAdmin(libroSeleccionado);

                cargarAutoresAsociados(vista);

                //Se ocultan todos los detalles de libros que esten deplegados
//                try {
//                    for(int j = 0; j<libroListView.getCount(); j++){
//                        View containerAux = libroListView.getChildAt(j);
//                        if(containerAux != null) {
//                            libroListView.getChildAt(j).findViewById(R.id.contenedorDetalleLibroAdmin).setVisibility(View.GONE);
//                        }
//                    }
//                }catch (Exception e){
//                    Log.e("Error", "Error ocultando detalles del libro: " + e.getMessage());
//                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleLibroAdmin).setVisibility(View.VISIBLE);
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
        int cantidad = 0;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                //listaLibros = tareasGenerales.buscarLibros(variablesGlobales.getLibroBuscar());
                cantidad = tareasGenerales.cantidadLibros(variablesGlobales.getLibroBuscar());
                Log.i("LibrosAdmin",">>>>>>>>>>> Tamaño lista libros buscadaAdmin: "+cantidad);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("ListaLibrosAdmin ", "xxx Error TareaWsBuscarLibros: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {

                    datasourceLibros.setSIZE(cantidad);
                    //datasourceLibros.setData(listaLibros);

                    //adapterLibro = new LibroListAdapterAdmin(getActivity(), listaLibros);
                    adapterLibro = new LibroListAdapterAdmin(getActivity(), datasourceLibros.getData(0, PAGESIZE));

                    libroListView.addFooterView(footerView);
                    libroListView.setAdapter(adapterLibro);
                    libroListView.removeFooterView(footerView);

                    updateDisplayingTextView();

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
                Log.i("LibrosAdmin",">>>>>>>>>>> Tamaño lista autoresAsocuados: "+listaAutores.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("ListaLibrosAdmin ", "xxx Error TareaWsAutoresAsociados: " + e.getMessage());
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


    /**
     * Metodo encargado de generar un reporte .xls conteniendo el listado de libros.
     */
    public void generarReporte(){

        UtilidadGenerarReportes utilidadReporte = new UtilidadGenerarReportes();
        utilidadReporte.setTipoArchivo(1);
        utilidadReporte.saveExcelFile(getActivity(), "LibrosFUP.xls");
    }


    /**
     * Clase encargada de carga la siguente pagina del listView
     */
    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        private List<Libro> newData = null;
        @Override
        protected String doInBackground(String... arg0)
        {
            //para que de tiempo a ver el footer <span class="wp-smiley wp-emoji wp-emoji-wink" title=";)">;)</span>
            try
            {
                Thread.sleep(1000);
                newData = datasourceLibros.getData(adapterLibro.getCount(), PAGESIZE);
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

            for (Libro value : newData)
            {
                adapterLibro.add(value);
            }
            adapterLibro.notifyDataSetChanged();

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
