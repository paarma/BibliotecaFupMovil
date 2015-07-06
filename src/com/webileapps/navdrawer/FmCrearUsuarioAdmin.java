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

import modelo.Usuario;
import util.Configuracion;
import util.VariablesGlobales;


/**
 * Created by alex on 9/05/15.
 */
public class FmCrearUsuarioAdmin extends SherlockFragment {

    EditText primerNombre, segundoNombre, primerApellido, segundoApellido, cedula, telefono, direccion, email, codigo, clave;
    Spinner spRol;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_Autor_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_usuario_admin, container, false);

        inicializarComponentes(view);

        ImageButton btnCrearUsuario = (ImageButton) view.findViewById(R.id.btnGuardarUsuarioAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarUsuarioAdmin);

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

        cedula = (EditText) view.findViewById(R.id.editTextCedula);
        primerNombre = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        segundoNombre = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        primerApellido = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        segundoApellido = (EditText) view.findViewById(R.id.editTextSegundoApellido);
        telefono = (EditText) view.findViewById(R.id.editTextTelefono);
        direccion = (EditText) view.findViewById(R.id.editTextDireccion);
        email = (EditText) view.findViewById(R.id.editTextEmail);
        codigo = (EditText) view.findViewById(R.id.editTextCodigo);
        clave = (EditText) view.findViewById(R.id.editTextClave);
        spRol = (Spinner) view.findViewById(R.id.spinnerRol);

        //Se carga el spinner rol con los datos del archivo arrays.xml
        ArrayAdapter adapterRol = ArrayAdapter.createFromResource(getActivity(), R.array.roles, R.layout.spinner_item);
        adapterRol.setDropDownViewResource(R.layout.spinner_item);
        spRol.setAdapter(adapterRol);
    }


    /**
     * Tarea encargada de guardar un Usuario
     */
    private class TareaWsGuardarUsuario extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            Usuario user = new Usuario();

            if(cedula.getText().toString().trim().length() > 0){
                user.setCedula(Integer.parseInt(cedula.getText().toString()));
            }
            user.setPrimerNombre(primerNombre.getText().toString());
            user.setSegundoNombre(segundoNombre.getText().toString());
            user.setPrimerApellido(primerApellido.getText().toString());
            user.setSegundoApellido(segundoApellido.getText().toString());

            if(telefono.getText().toString().trim().length() > 0){
                user.setTelefono(Integer.parseInt(telefono.getText().toString()));
            }
            user.setDireccion(direccion.getText().toString());
            user.setEmail(email.getText().toString());
            user.setCodigo(codigo.getText().toString());
            user.setClave(clave.getText().toString());

            if (!spRol.getSelectedItem().toString().equals("Seleccione...")) {
                user.setRol(spRol.getSelectedItem().toString());
            }

            try {
                resultadoTarea = guardarUsuario(user);

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
            primerNombre.getText().clear();
            segundoNombre.getText().clear();
        }


    /**
     * Metodo encargado de guardar un usuario  en la BD para acceder a la App
     * @param usuario Usuario el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarUsuario(Usuario usuario){

        Configuracion conf = new Configuracion();
        final String SOAP_ACTION = conf.getUrl()+"/guardarUsuario";
        final String METHOD_NAME = "guardarUsuario";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cedula", usuario.getCedula());
        request.addProperty("primerNombre", usuario.getPrimerNombre());
        request.addProperty("segundoNombre", usuario.getSegundoNombre());
        request.addProperty("primerApellido", usuario.getPrimerApellido());
        request.addProperty("segundoApellido", usuario.getSegundoApellido());
        request.addProperty("telefono", usuario.getTelefono());
        request.addProperty("direccion", usuario.getDireccion());
        request.addProperty("email", usuario.getEmail());
        request.addProperty("codigo", usuario.getCodigo());
        request.addProperty("clave", usuario.getClave());
        request.addProperty("rol",usuario.getRol());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("GuardandoUsuario","*********************** guardandoUsuario: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("GuardandoUsuario", "xxx Error guardarUsuario(): " + e.getMessage());
        }

        return resultado;
    }

}

