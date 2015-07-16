package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
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

import modelo.Editorial;
import util.TareasGenerales;
import util.VariablesGlobales;

public class FmListaEditorialAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private ArrayAdapter<String> adapterEditorial;
    private ListView editorialListView;

    private List<Editorial> listaEditoriales = new ArrayList<Editorial>();
    private Editorial editorialSeleccionada;

    private String[] nombresEditoriales;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        Log.i("EDITORIAL", "************************************** INICIO LISTA_EDITORIALES_ADMIN");
		View view = inflater.inflate(R.layout.fm_lista_editorial_admin, container, false);

        inicializarComponentes(view);
        inicializarListaEditoriales();

        return view;
	}

    /**
     * Se inicializan los componentes visuales
     */
    private void inicializarComponentes(View view) {

        editorialListView = (ListView) view.findViewById(R.id.listViewEditorialesAdmin);
    }

    /**
     * Se carga el listado de editoriales provenientes de la BD,
     * ademas contiene el evento onclick del item para capturar el mismo
     */
    private void inicializarListaEditoriales(){

        TareaWsBuscarEditoriales tareaListarEditorial = new TareaWsBuscarEditoriales();
        tareaListarEditorial.execute();

        //Evento al seleccionar un elemento de la lista
        editorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> padre, View vista, int posicion, long id) {

                editorialSeleccionada = listaEditoriales.get(posicion);
                variablesGlobales.setEditorialSeleccionadaAdmin(editorialSeleccionada);

                //Se quita el color del BackgroundColor a los items previamente seleccionados
                try {
                    for(int j = 0; j<editorialListView.getCount(); j++){
                        View containerAux = editorialListView.getChildAt(j);
                        if(containerAux != null) {
                            containerAux.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }catch (Exception e){
                    Log.e("Error", "Error ocultando color BackgroundColor: " + e.getMessage());
                }

                //Se agrega BackgroundColor al item seleccionado
                vista.setBackgroundColor(Color.parseColor("#01579B"));
            }
        });
    }


    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea encargada de listar las editoriales de la biblioteca ya sea el listado general
     * o un listado segun parametros de una editorial a buscar
     */
    private class TareaWsBuscarEditoriales extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaEditoriales = tareasGenerales.listarEditoriales(variablesGlobales.getEditorialBuscar());
                Log.i("EditorialAdmin",">>>>>>>>>>> Tamaño lista editorial buscada: "+listaEditoriales.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("EditorialAdmin ", "xxx Error TareaWsBuscarEditoriales: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    if (listaEditoriales.size() > 0) {

                        nombresEditoriales = new String[listaEditoriales.size()];

                        for (int i = 0; i < listaEditoriales.size(); i++) {
                            nombresEditoriales[i] = listaEditoriales.get(i).getDescripcion();
                        }

                        adapterEditorial = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, nombresEditoriales);
                        editorialListView.setAdapter(adapterEditorial);
                    }
                }catch (Exception e){
                    Log.e("Editorial","XXX Error cargando datos lista editorial: "+e.getMessage());
                }
            }else{
                String msn = "Error listando editoriales";
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
        inicializarListaEditoriales();
    }
}
