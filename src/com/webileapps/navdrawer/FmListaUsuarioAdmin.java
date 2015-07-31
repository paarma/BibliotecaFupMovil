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

import modelo.Usuario;
import util.TareasGenerales;
import util.UtilidadGenerarReportes;
import util.VariablesGlobales;

/**
 * Created by alex on 9/05/15.
 */
public class FmListaUsuarioAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<Usuario> adapterUsuario;
    private ListView usuarioListView;

    private List<Usuario> listaUsuarios = new ArrayList<Usuario>();
    private Usuario usuarioSeleccionado;

    private ImageButton btnReporte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("USUARIOS_ADMIN", "************************************** INICIO LISTA_USUARIOS_ADMIN");

        View view = inflater.inflate(R.layout.fm_lista_usuario_admin, container, false);

        btnReporte = (ImageButton) view.findViewById(R.id.btnReporteUserAdmin);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarReporte();
            }
        });

        inicializarComponentes(view);
        inicializarListaUsuarios();

        return view;
    }

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        usuarioListView = (ListView) view.findViewById(R.id.listViewUsuariosAdmin);
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

                usuarioSeleccionado = listaUsuarios.get(posicion);
                variablesGlobales.setUsuarioSeleccionadoAdmin(usuarioSeleccionado);

                //Se ocultan todos los detalles de usurios que esten deplegados
                try {
                    for(int j = 0; j<usuarioListView.getCount(); j++){
                        View containerAux = usuarioListView.getChildAt(j);
                        if(containerAux != null) {
                            usuarioListView.getChildAt(j).findViewById(R.id.contenedorDetalleUsuarioAdmin).setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando detalles del usuario: " + e.getMessage());
                }

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

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaUsuarios = tareasGenerales.buscarUsuarios(variablesGlobales.getUsuarioBuscar());
                Log.i("UsuariosAdmin",">>>>>>>>>>> Tamaño lista usuarios buscada: "+listaUsuarios.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("UsuariosAdmin ", "xxx Error TareaWsBuscarUsuarios: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    adapterUsuario = new UsuarioListAdapterAdmin(getActivity(), listaUsuarios);
                    usuarioListView.setAdapter(adapterUsuario);
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
        utilidadReporte.setListaUsuarios(listaUsuarios);
        utilidadReporte.setTipoArchivo(3);
        utilidadReporte.saveExcelFile(getActivity(), "UsuariosFUP.xls");
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