package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import modelo.Autor;
import util.TareasGenerales;
import util.VariablesGlobales;


/**
 * Created by alex on 9/05/15.
 */
public class FmCrearAutorAdmin extends SherlockFragment {
    EditText descripcion;
    Spinner select;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private final static String[] tipoAutor = { "Seleccione..", "Personal", "Institucional",
            "Corporativo"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_Autor_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_autor_admin, container, false);

        descripcion = (EditText) view.findViewById(R.id.editTextAutor);
        select = (Spinner) view.findViewById(R.id.spinnerTipoAutor);


        ImageButton btnCrearAutor = (ImageButton) view.findViewById(R.id.btnGuardarAutAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarAutAdmin);

        inicializarComponentes(view);

        btnCrearAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrearAutor", ">>>>>>>>>>>>>>>>>>>> pulsando boton crear Autor");
                TareaWsGuardarAutor tareaWsGuardarAutor = new TareaWsGuardarAutor();
                tareaWsGuardarAutor.execute();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });


        return view;


    }

    public void inicializarComponentes(View view){
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, tipoAutor);
        Spinner spTipoAutor = (Spinner) view.findViewById(R.id.spinnerTipoAutor);
        spTipoAutor.setAdapter(adapter);
    }


    /**
     * Tarea encargada de guardar un libro
     */
    private class TareaWsGuardarAutor extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                Autor aut = new Autor();
                aut.setDescripcion(descripcion.getText().toString());

                if (select.getSelectedItem().toString().equals("Personal")) {
                    aut.setTipoAutor(1);
                }

                if (select.getSelectedItem().toString().equals("Institucional")) {
                    aut.setTipoAutor(2);
                }
                if (select.getSelectedItem().toString().equals("Corporativo")) {
                    aut.setTipoAutor(3);
                }

                resultadoTarea = tareasGenerales.guardarAutor(aut);
            } catch (Exception e) {
                resultadoTarea = false;
                Log.e("FmCrearAutor ", "xxx Error TareaWsGuardarAutor: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                Toast.makeText(getActivity(), "Autor almacenado con exito", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(getActivity(), "Error almacenando el Autor", Toast.LENGTH_LONG).show();
            }
        }


    }
        //Metodo encardado de limpiar los campos del formulario
        public void limpiarCampos(){
            descripcion.getText().clear();



        }

}

