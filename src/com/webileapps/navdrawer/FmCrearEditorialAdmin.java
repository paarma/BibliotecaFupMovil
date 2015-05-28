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

import modelo.Editorial;
import util.TareasGenerales;

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

		boolean resultadoTarea = false;

		@SuppressLint("LongLogTag")
		@Override
		protected Boolean doInBackground(String... params) {

			try {
				TareasGenerales tareasGenerales = new TareasGenerales();

				Editorial edit = new Editorial();
				edit.setDescripcion(descripcion.getText().toString());


				resultadoTarea = tareasGenerales.guardarEditorial(edit);
			} catch (Exception e) {
				resultadoTarea = false;
				Log.e("FmCrearAutor ", "xxx Error TareaWsGuardarEditorial: " + e.getMessage());
			}
			return resultadoTarea;
		}

		public void onPostExecute(Boolean result){

			if(result){
				Toast.makeText(getActivity(), "Editorial almacenado con exito", Toast.LENGTH_LONG).show();
				limpiarCampos();
			}else{
				Toast.makeText(getActivity(), "Error almacenando el Editorial", Toast.LENGTH_LONG).show();
			}
		}


	}
	//Metodo encardado de limpiar los campos del formulario
	public void limpiarCampos(){
		descripcion.getText().clear();



	}
}
