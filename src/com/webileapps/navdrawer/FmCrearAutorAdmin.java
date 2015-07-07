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

    EditText primerNombre, segundoNombre, primerApellido, segundoApellido;
    Spinner spinnerTipoAutor;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_Autor_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_autor_admin, container, false);

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

        primerNombre = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        segundoNombre = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        primerApellido = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        segundoApellido = (EditText) view.findViewById(R.id.editTextSegundoApellido);

        spinnerTipoAutor = (Spinner) view.findViewById(R.id.spinnerTipoAutor);

        //Se cargan algunos spinners con los datos del archivo arrays.xml
        ArrayAdapter adapterTipoAutor = ArrayAdapter.createFromResource(getActivity(), R.array.tipoAutor, R.layout.spinner_item);
        adapterTipoAutor.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipoAutor.setAdapter(adapterTipoAutor);

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

            request.addProperty("primerNombre", primerNombre.getText().toString());
            request.addProperty("segundoNombre", segundoNombre.getText().toString());
            request.addProperty("primerApellido", primerApellido.getText().toString());
            request.addProperty("segundoApellido", segundoApellido.getText().toString());

            if(!spinnerTipoAutor.getSelectedItem().toString().equals("Seleccione...")){
                request.addProperty("tipo", spinnerTipoAutor.getSelectedItem().toString());
            }else{
                request.addProperty("tipo", "");
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
            primerNombre.getText().clear();
            segundoNombre.getText().clear();
            primerApellido.getText().clear();
            segundoApellido.getText().clear();
        }

}

