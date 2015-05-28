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

import modelo.Usuario;
import util.TareasGenerales;
import util.VariablesGlobales;


/**
 * Created by alex on 9/05/15.
 */
public class FmCrearUsuarioAdmin extends SherlockFragment {
    EditText nombre,apellido,cedula,telefono,direccion,email,codigo,clave;
    Spinner spRol;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();


    private final static String[] rol = { "Seleccione..", "Administrador", "Usuario"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_Autor_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_usuario_admin, container, false);

        cedula = (EditText) view.findViewById(R.id.editTextCedula);
        nombre = (EditText) view.findViewById(R.id.editTexNombre);
        apellido = (EditText) view.findViewById(R.id.editTextApellido);
        telefono = (EditText) view.findViewById(R.id.editTextTelefono);
        direccion = (EditText) view.findViewById(R.id.editTextDireccion);
        email = (EditText) view.findViewById(R.id.editTextEmail);
        codigo = (EditText) view.findViewById(R.id.editTextCodigo);
        clave = (EditText) view.findViewById(R.id.editTextClave);
        spRol = (Spinner) view.findViewById(R.id.spinnerRol);


        ImageButton btnCrearUsuario = (ImageButton) view.findViewById(R.id.btnGuardarUsuarioAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarUsuarioAdmin);

        inicializarComponentes(view);

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrearAutor", ">>>>>>>>>>>>>>>>>>>> pulsando boton crear Autor");
                TareaWsGuardarUsuario tareaWsGuardarUsuario = new TareaWsGuardarUsuario();
                tareaWsGuardarUsuario.execute();

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
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, rol);
        Spinner spRol = (Spinner) view.findViewById(R.id.spinnerRol);
        spRol.setAdapter(adapter);
    }


    /**
     * Tarea encargada de guardar un Usuario
     */
    private class TareaWsGuardarUsuario extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                Usuario user = new Usuario();

                if(cedula.getText().toString().trim().length() > 0){
                    user.setCedula(Integer.parseInt(cedula.getText().toString()));
                }
                user.setNombre(nombre.getText().toString());
                user.setApellido(apellido.getText().toString());
                if(telefono.getText().toString().trim().length() > 0){
                    user.setTelefono(Integer.parseInt(telefono.getText().toString()));
                }
                user.setDireccion(direccion.getText().toString());
                user.setEmail(email.getText().toString());
                user.setCodigo(codigo.getText().toString());
                user.setClave(clave.getText().toString());


                if (spRol.getSelectedItem().toString().equals("Administrador")) {
                    user.setRol("ADMIN");
                }

                if (spRol.getSelectedItem().toString().equals("Usuario")) {
                    user.setRol("ADMIN");
                }


                resultadoTarea = tareasGenerales.guardarUsuario(user);
            } catch (Exception e) {
                resultadoTarea = false;
                Log.e("FmCrearUsuario ", "xxx Error TareaWsGuardarUsuario: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                Toast.makeText(getActivity(), "Usuario almacenado con exito", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(getActivity(), "Error almacenando el Usuario", Toast.LENGTH_LONG).show();
            }
        }


    }
        //Metodo encardado de limpiar los campos del formulario
        public void limpiarCampos(){
            nombre.getText().clear();



        }

}

