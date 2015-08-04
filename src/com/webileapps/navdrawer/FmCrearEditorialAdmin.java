package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import util.Configuracion;
import util.TareasGenerales;
import util.VariablesGlobales;

public class FmCrearEditorialAdmin extends SherlockFragment {

	EditText descripcion;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fm_crear_libro_adminro_admin.xml
		View view = inflater.inflate(R.layout.fm_crear_editorial_admin, container, false);

		descripcion = (EditText) view.findViewById(R.id.editTextEditorial);

		ImageButton btnCrearEditorial = (ImageButton) view.findViewById(R.id.btnGuardarEditAdmin);
		ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarEditAdmin);


		btnCrearEditorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

                validarGuardar();
			}
		});

		btnCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				limpiarCampos();
                variablesGlobales.setEditorialSeleccionadaAdmin(null);
			}
		});


        //Limpia validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                descripcion.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        //Fin limpiar validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

		return view;
	}

    /**
     * Metodo que carga los datos de un determinado libro previamente seleccionado.
     */
    public void cargarDatosEditorialSeleccionada(){
        if(variablesGlobales.getEditorialSeleccionadaAdmin() != null) {

            try {
                descripcion.setText(variablesGlobales.getEditorialSeleccionadaAdmin().getDescripcion());
            }catch (Exception e){
                Log.e("Editorial","XXX Error cargando datos editorial: "+e.getMessage());
            }

        }
    }

	/**
	 * Tarea encargada de guardar una editorial
	 */
	private class TareaWsGuardarEditorial extends AsyncTask<String,Integer,Boolean> {

        Configuracion conf = new Configuracion();
        final String SOAP_ACTION = conf.getUrl()+"/guardarEditorial";
        final String METHOD_NAME = "guardarEditorial";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
		boolean resultadoTarea = false;

		@SuppressLint("LongLogTag")
		@Override
		protected Boolean doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            if(variablesGlobales.getEditorialSeleccionadaAdmin() != null){
                request.addProperty("idEditorial", variablesGlobales.getEditorialSeleccionadaAdmin().getIdEditorial());
            }else{
                request.addProperty("idEditorial", 0);
            }

            request.addProperty("descripcion", descripcion.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;

            HttpTransportSE transporte = new HttpTransportSE(URL);

			try {

                transporte.call(SOAP_ACTION, envelope);
                int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

                Log.i("GuardandoEditorial","*********************** guardandoEditorial: "+resultadoGuardar);
                if (resultadoGuardar == 1)
                {
                    resultadoTarea = true;
                }
			} catch (Exception e) {
				resultadoTarea = false;
				Log.e("FmCrearEditorial", "xxx Error TareaWsGuardarEditorial: " + e.getMessage());
			}
			return resultadoTarea;
		}

		public void onPostExecute(Boolean result){

			if(result){
				Toast.makeText(getActivity(), "Editorial almacenada con exito", Toast.LENGTH_LONG).show();
				limpiarCampos();
                variablesGlobales.setEditorialSeleccionadaAdmin(null);
			}else{
				Toast.makeText(getActivity(), "Error almacenando editorial", Toast.LENGTH_LONG).show();
			}
		}

	}
	//Metodo encardado de limpiar los campos del formulario
	public void limpiarCampos(){
		descripcion.getText().clear();
	}

    /**
     * Metodo encargado de validar los campos de texto y llamar al metodo para almacenear la info.
     */
    public void validarGuardar(){

        boolean resultado = true;

        if(descripcion.getText().toString().length() == 0 ||
                TextUtils.isEmpty(descripcion.getText().toString().trim())){
            descripcion.setError("Nombre requerido");
            resultado = false;
        }

        //Verificar campos ya registrados (repetidos) en la BD.
        TareaWsVerificarDatoEnBd tarea = new TareaWsVerificarDatoEnBd();
        tarea.setPasaValidacionPrevia(resultado);

        tarea.execute();
    }

    /**
     * Tarea encargada de verificar si existe un determinado dato repetido en la BD
     * en caso de pasar todas las validaciones de los campos de texto, llama a la
     * tarea encargada de guardar la info.
     */
    private class TareaWsVerificarDatoEnBd extends AsyncTask<String,Integer,Boolean> {

        private boolean datoRepetido = false;

        private boolean descripcionRepetida = false;
        private boolean pasaValidacionPrevia = false;

        @Override
        protected Boolean doInBackground(String... strings) {

            try{
                TareasGenerales tareasGenerales = new TareasGenerales();

                if(tareasGenerales.verficarDatoEnBd("EDITORIAL","DESCRIPCION",descripcion.getText().toString())){
                    Log.i("FmCrearEditorial",">>>>>>>>>>>>>>>>>> editorial ya registrada");
                    datoRepetido = true;
                    descripcionRepetida = true;
                }
            }catch (Exception e){
                Log.e("FmCrearEditorial", "xxx Error TareaWsVerificarDatoEnBd: " + e.getMessage());
            }
            return datoRepetido;
        }

        public void onPostExecute(Boolean result){
            if(result){ //Indica que hay almenos un dato repetido en BD

                if(descripcionRepetida){
                    descripcion.setError("Editorial ya registrada");
                }

                Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();

            }else{ // Pasa validacion exitosa de campos repetidos

                if(pasaValidacionPrevia){
                    TareaWsGuardarEditorial tareaWsGuardarEditorial = new TareaWsGuardarEditorial();
                    tareaWsGuardarEditorial.execute();
                }else{
                    Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();
                }

            }
        }

        //Setters
        public void setPasaValidacionPrevia(boolean pasaValidacionPrevia) {
            this.pasaValidacionPrevia = pasaValidacionPrevia;
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

        //Si existe una Editorial seleccionda previamente por el admin, se cargan los datos
        cargarDatosEditorialSeleccionada();
    }
}
