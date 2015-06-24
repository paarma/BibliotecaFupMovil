package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FmCrearEditorialAdmin extends SherlockFragment {

	EditText descripcion;

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
				Log.i("CrearAutor", ">>>>>>>>>>>>>>>>>>>> pulsando boton crear Autor");
				TareaWsGuardarEditorial tareaWsGuardarEditorial = new TareaWsGuardarEditorial();
				tareaWsGuardarEditorial.execute();

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
			}else{
				Toast.makeText(getActivity(), "Error almacenando editorial", Toast.LENGTH_LONG).show();
			}
		}

	}
	//Metodo encardado de limpiar los campos del formulario
	public void limpiarCampos(){
		descripcion.getText().clear();
	}
}
