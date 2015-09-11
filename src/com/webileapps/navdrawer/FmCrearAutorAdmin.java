package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import util.Utilidades;
import util.VariablesGlobales;


/**
 * Created by Pablo on 9/05/15.
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

                if(validarGuardar()){
                    TareaWsGuardarAutor tareaWsGuardarAutor = new TareaWsGuardarAutor();
                    tareaWsGuardarAutor.execute();
                }else{
                    Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
                variablesGlobales.setAutorSeleccionadoAdmin(null);
            }
        });


        //Limpia validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        primerNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                primerNombre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        primerApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                primerApellido.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        //Fin limpiar validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

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
     * Metodo que carga los datos de un determinado autor previamente seleccionado.
     */
    public void cargarDatosUsuarioSeleccionado(){
        if(variablesGlobales.getAutorSeleccionadoAdmin() != null) {

            try {
                primerNombre.setText(variablesGlobales.getAutorSeleccionadoAdmin().getPrimerNombre());
                segundoNombre.setText(variablesGlobales.getAutorSeleccionadoAdmin().getSegundoNombre());
                primerApellido.setText(variablesGlobales.getAutorSeleccionadoAdmin().getPrimerApellido());
                segundoApellido.setText(variablesGlobales.getAutorSeleccionadoAdmin().getSegundoApellido());

                //Se cargan los spinners con su respectivo valor.
                spinnerTipoAutor.setSelection(Utilidades.getIndexSpinner(spinnerTipoAutor,
                        variablesGlobales.getAutorSeleccionadoAdmin().getTipoAutor()));
            }catch (Exception e){
                Log.e("FmCrearAutor ", "xxx Error cargarDatosUsuarioSeleccionado: " + e.getMessage());
            }
        }else{
            limpiarCampos();
        }
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

            if(variablesGlobales.getAutorSeleccionadoAdmin() != null){
                request.addProperty("idAutor", variablesGlobales.getAutorSeleccionadoAdmin().getIdAutor());
            }else{
                request.addProperty("idAutor", 0);
            }

            request.addProperty("primerNombre", primerNombre.getText().toString().trim());
            request.addProperty("segundoNombre", segundoNombre.getText().toString().trim());
            request.addProperty("primerApellido", primerApellido.getText().toString().trim());
            request.addProperty("segundoApellido", segundoApellido.getText().toString().trim());

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
                variablesGlobales.setAutorSeleccionadoAdmin(null);
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

    /**
     * Metodo encargado de validar los campos de texto y llamar al metodo para almacenear la info.
     */
    public boolean validarGuardar() {

        boolean resultado = true;

        if (primerNombre.getText().toString().trim().length() == 0) {
            primerNombre.setError("Primer nombre requerido");
            resultado = false;
        }

        if(primerApellido.getText().toString().trim().length() == 0){
            primerApellido.setError("Primer apellido requerido");
            resultado = false;
        }

        return resultado;
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

        //Si existe un libro selecciondo previamente por el admin, se cargan los datos
        cargarDatosUsuarioSeleccionado();
    }

}

