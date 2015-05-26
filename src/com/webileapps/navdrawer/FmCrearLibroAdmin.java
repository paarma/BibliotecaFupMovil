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

import java.util.Date;

import modelo.Libro;
import util.TareasGenerales;
import util.VariablesGlobales;

public class FmCrearLibroAdmin extends SherlockFragment {

    EditText titulo, isbn, codTopografico, temas, paginas, estado, valor, adquisicion, radicado;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fm_crear_libro_adminro_admin.xml
		View view = inflater.inflate(R.layout.fm_crear_libro_admin, container, false);

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);
        temas = (EditText) view.findViewById(R.id.editTextTemas);
        paginas = (EditText) view.findViewById(R.id.editTextPaginas);
        estado = (EditText) view.findViewById(R.id.editTextEstado);
        valor = (EditText) view.findViewById(R.id.editTextValor);
        adquisicion = (EditText) view.findViewById(R.id.editTextAdquisicion);
        radicado = (EditText) view.findViewById(R.id.editTextRadicado);

        ImageButton btnCrearLibro = (ImageButton) view.findViewById(R.id.btnGuardarLibroAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarLibroAdmin);

        btnCrearLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrearLibro",">>>>>>>>>>>>>>>>>>>> pulsando boton crear libro");
                TareaWsGuardarLibro tareaGuardarLibro = new TareaWsGuardarLibro();
                tareaGuardarLibro.execute();

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

    //Metodo encardado de limpiar los campos del formulario
    public void limpiarCampos(){
        titulo.getText().clear();
        isbn.getText().clear();
        codTopografico.getText().clear();
        temas.getText().clear();
        paginas.getText().clear();
        estado.getText().clear();
        valor.getText().clear();
        adquisicion.getText().clear();
        radicado.getText().clear();
    }

    /**
     * Tarea encargada de guardar un libro
     */
    private class TareaWsGuardarLibro extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                Libro lib = new Libro();
                lib.setTitulo(titulo.getText().toString());

                if(valor.getText().toString().trim().length() > 0){
                    lib.setValor(Integer.parseInt(valor.getText().toString()));
                }

                if(paginas.getText().toString().trim().length() > 0){
                    lib.setPaginas(Integer.parseInt(paginas.getText().toString()));
                }


                lib.setIsbn(isbn.getText().toString());
                lib.setCodigoTopografico(codTopografico.getText().toString());
                lib.setAdquisicion(adquisicion.getText().toString());
                lib.setEstado(estado.getText().toString());
                lib.setRadicado(radicado.getText().toString());
                lib.setFechaIngreso(new Date());
                //lib.setSerie("1");
                //lib.setIdSede(1);
                //lib.setIdEditorial(1);
                //lib.setIdArea(1);
                //lib.setAnio(1);
                lib.setTemas(temas.getText().toString());
                //lib.setDisponibilidad("SI");
                lib.setIdUsuario(variablesGlobales.getUsuarioLogueado().getIdUsuario());
                //lib.setIdCiudad(1);

                resultadoTarea = tareasGenerales.guardarLibro(lib);
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("FmCrearLibro ", "xxx Error TareaWsGuardarLibro: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                Toast.makeText(getActivity(), "Libro almacenado con exito", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(getActivity(), "Error almacenando el libro", Toast.LENGTH_LONG).show();
            }
        }
    }

}
