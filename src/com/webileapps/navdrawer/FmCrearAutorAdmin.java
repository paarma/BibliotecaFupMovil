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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import util.Configuracion;
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
     * Tarea encargada de guardar un autor
     */
    private class TareaWsGuardarAutor extends AsyncTask<String,Integer,Boolean> {

        Configuracion conf = new Configuracion();
        final String SOAP_ACTION = conf.getUrl()+"/guardarAutor";
        final String METHOD_NAME = "guardarAutor";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("descripcion", descripcion.getText().toString());

            if (select.getSelectedItem().toString().equals("Personal")) {
                request.addProperty("tipo",1);
            }

            if (select.getSelectedItem().toString().equals("Institucional")) {
                request.addProperty("tipo",2);
            }
            if (select.getSelectedItem().toString().equals("Corporativo")) {
                request.addProperty("tipo",3);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {

                transporte.call(SOAP_ACTION, envelope);
                int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

                if(resultadoGuardar == 1) {
                    resultadoTarea = true;
                }

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

