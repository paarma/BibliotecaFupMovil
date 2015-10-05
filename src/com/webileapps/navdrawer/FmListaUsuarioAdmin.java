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

import modelo.Usuario;
import util.DatasourceUsuarios;
import util.TareasGenerales;
import util.UtilidadGenerarReportes;
import util.VariablesGlobales;

/**
 * Created by Pablo on 9/05/15.
 * paarma80@gmail.com
 */
public class FmListaUsuarioAdmin extends SherlockFragment {

    TareasGenerales tareasGenerales = new TareasGenerales();
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Usuario> adapterUsuario;
    private ListView usuarioListView;

    private List<Usuario> listaUsuarios = new ArrayList<Usuario>();
    private Usuario usuarioSeleccionado;

    private ImageButton btnReporte;


    private DatasourceUsuarios datasourceUsuarios;
    private static final int PAGESIZE = 10;
    private TextView textViewDisplaying;
    View viewAux = null;
    View footerView;
    boolean loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("USUARIOS_ADMIN", "************************************** INICIO LISTA_USUARIOS_ADMIN");

        View view = inflater.inflate(R.layout.fm_lista_usuario_admin, container, false);

        datasourceUsuarios = DatasourceUsuarios.getInstance();
        viewAux = view;

        footerView = inflater.inflate(R.layout.footer_load, null);

        btnReporte = (ImageButton) view.findViewById(R.id.btnReporteUserAdmin);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarReporte();
            }
        });

        inicializarComponentes(view);
        inicializarListaUsuarios();


        //Scroll del listView
        usuarioListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                try {

                    //boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && libroListView.getChildAt(visibleItemCount -1) != null && libroListView.getChildAt(visibleItemCount-1).getBottom() <= libroListView.getHeight();
                    boolean lastItem = (firstVisibleItem + visibleItemCount == totalItemCount);
                    boolean moreRows = adapterUsuario.getCount() < datasourceUsuarios.getSize();

                    if (!loading &&  lastItem && moreRows)
                    {
                        loading = true;
                        usuarioListView.addFooterView(footerView);
                        (new LoadNextPage()).execute("");
                    }
                }catch (Exception e){
                    Log.e("UsersAdmin","xxx Error scroll listaUsariosAdmin: "+e.getMessage());
                }

            }
        });


        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        usuarioListView = (ListView) view.findViewById(R.id.listViewUsuariosAdmin);
    }

    /**
     * Mensaje de cabezera indicando la cantidad de registros
     */
    private void updateDisplayingTextView()
    {
        textViewDisplaying = (TextView) viewAux.findViewById(R.id.displaying);
        String text = getString(R.string.display);
        text = String.format(text, adapterUsuario.getCount(), datasourceUsuarios.getSize());
        textViewDisplaying.setText(text);
    }

    /**
     * Se carga el listado de usuarios provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaUsuarios(){

        TareaWsBuscarUsuarios tareaListarUsuario = new TareaWsBuscarUsuarios();
        tareaListarUsuario.execute();

        //Evento al seleccionar un elemento de la lista
        usuarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                //usuarioSeleccionado = listaUsuarios.get(posicion);
                usuarioSeleccionado = (Usuario) usuarioListView.getItemAtPosition(posicion);
                variablesGlobales.setUsuarioSeleccionadoAdmin(usuarioSeleccionado);

                //Se ocultan todos los detalles de usurios que esten deplegados
//                try {
//                    for(int j = 0; j<usuarioListView.getCount(); j++){
//                        View containerAux = usuarioListView.getChildAt(j);
//                        if(containerAux != null) {
//                            usuarioListView.getChildAt(j).findViewById(R.id.contenedorDetalleUsuarioAdmin).setVisibility(View.GONE);
//                        }
//                    }
//                }catch (Exception e){
//                    Log.e("Error", "Error ocultando detalles del usuario: " + e.getMessage());
//                }

                //Se despliega el detalle del item seleccionado
                vista.findViewById(R.id.contenedorDetalleUsuarioAdmin).setVisibility(View.VISIBLE);
            }
        });
    }

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar los usuarios de la biblioteca ya sea el listado general
     * o un listado segun parametros de un usuario a buscar
     */
    private class TareaWsBuscarUsuarios extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;
        int cantidad = 0;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                //listaUsuarios = tareasGenerales.buscarUsuarios(variablesGlobales.getUsuarioBuscar());
                cantidad = tareasGenerales.cantidadUsuarios(variablesGlobales.getUsuarioBuscar());
                Log.i("UsuariosAdmin",">>>>>>>>>>> Tamaño lista usuarios buscada: "+cantidad);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("UsuariosAdmin ", "xxx Error TareaWsBuscarUsuarios: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    //adapterUsuario = new UsuarioListAdapterAdmin(getActivity(), listaUsuarios);
                    //usuarioListView.setAdapter(adapterUsuario);

                    datasourceUsuarios.setSIZE(cantidad);

                    adapterUsuario = new UsuarioListAdapterAdmin(getActivity(), datasourceUsuarios.getData(0, PAGESIZE));

                    usuarioListView.addFooterView(footerView);
                    usuarioListView.setAdapter(adapterUsuario);
                    usuarioListView.removeFooterView(footerView);

                    updateDisplayingTextView();

                }catch (Exception e){
                    Log.e("UsuariosAdmin ", "xxx Error listando usuarios: " + e.getMessage());
                }
            }else{
                String msn = "Error listando usuarios";
                Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Metodo encargado de generar un reporte .xls conteniendo el listado de usuarios.
     */
    public void generarReporte(){

        UtilidadGenerarReportes utilidadReporte = new UtilidadGenerarReportes();
        utilidadReporte.setTipoArchivo(3);
        utilidadReporte.saveExcelFile(getActivity(), "UsuariosFUP.xls");
    }


    /**
     * Clase encargada de carga la siguente pagina del listView
     */
    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        private List<Usuario> newData = null;
        @Override
        protected String doInBackground(String... arg0)
        {
            //para que de tiempo a ver el footer <span class="wp-smiley wp-emoji wp-emoji-wink" title=";)">;)</span>
            try
            {
                Thread.sleep(1000);
                newData = datasourceUsuarios.getData(adapterUsuario.getCount(), PAGESIZE);
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

            for (Usuario value : newData)
            {
                adapterUsuario.add(value);
            }
            adapterUsuario.notifyDataSetChanged();

            usuarioListView.removeFooterView(footerView);
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
        inicializarListaUsuarios();
    }
}