package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelo.Usuario;
import util.Configuracion;
import util.TareasGenerales;
import util.Utilidades;
import util.VariablesGlobales;


/**
 * Created by Pablo on 9/05/15.
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

                validarGuardar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
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

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        clave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                clave.setError(null);
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
     * Metodo que carga los datos de un determinado usuario previamente seleccionado.
     */
    public void cargarDatosUsuarioSeleccionado() {
        if (variablesGlobales.getUsuarioSeleccionadoAdmin() != null) {

            try {
                cedula.setText(String.valueOf(variablesGlobales.getUsuarioSeleccionadoAdmin().getCedula()));
                primerNombre.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getPrimerNombre());
                segundoNombre.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getSegundoNombre());
                primerApellido.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getPrimerApellido());
                segundoApellido.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getSegundoApellido());
                telefono.setText(String.valueOf(variablesGlobales.getUsuarioSeleccionadoAdmin().getTelefono()));
                direccion.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getDireccion());
                email.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getEmail());
                codigo.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getCodigo());
                clave.setText(variablesGlobales.getUsuarioSeleccionadoAdmin().getClave());

                //Se cargan los spinners con su respectivo valor.
                spRol.setSelection(Utilidades.getIndexSpinner(spRol, variablesGlobales.getUsuarioSeleccionadoAdmin().getRol()));
            }catch (Exception e){
                Log.e("FmCrearUsuario ", "xxx Error cargarDatosUsuarioSeleccionado: " + e.getMessage());
            }
        }
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

            user.setCedula(Integer.parseInt(cedula.getText().toString()));
            user.setPrimerNombre(primerNombre.getText().toString().trim());
            user.setSegundoNombre(segundoNombre.getText().toString().trim());
            user.setPrimerApellido(primerApellido.getText().toString().trim());
            user.setSegundoApellido(segundoApellido.getText().toString().trim());

            if(telefono.getText().toString().trim().length() > 0){
                user.setTelefono(Integer.parseInt(telefono.getText().toString()));
            }
            user.setDireccion(direccion.getText().toString().trim());
            user.setEmail(email.getText().toString().trim());
            user.setCodigo(codigo.getText().toString().trim());
            user.setClave(clave.getText().toString().trim());

            if (!spRol.getSelectedItem().toString().equals("Seleccione...")) {
                user.setRol(spRol.getSelectedItem().toString());
            }

            try {

                /**
                 * Se setea el idUsuario en caso de ser edicion, de lo contrario este
                 * quedara con valor de 0.
                 * Funcionalidad especifica para validar si se guarda un nuevo usuario o se edita uno existente.
                 */
                if(variablesGlobales.getUsuarioSeleccionadoAdmin() != null){
                    user.setIdUsuario(variablesGlobales.getUsuarioSeleccionadoAdmin().getIdUsuario());
                }

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

    /**
     * Metodo encardado de limpiar los campos del formulario
     */
    public void limpiarCampos(){
        cedula.getText().clear();
        primerNombre.getText().clear();
        segundoNombre.getText().clear();
        primerApellido.getText().clear();
        segundoApellido.getText().clear();
        telefono.getText().clear();
        direccion.getText().clear();
        email.getText().clear();
        codigo.getText().clear();
        clave.getText().clear();
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
        request.addProperty("idUsuario", usuario.getIdUsuario());
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

    /**
     * Metodo encargado de validar los campos de texto y llamar al metodo para almacenear la info.
     */
    public void validarGuardar(){

        boolean resultado = true;

        if(cedula.getText().toString().trim().length() == 0){
            cedula.setError("Cedula requerida");
            resultado = false;
        }

        if(primerNombre.getText().toString().trim().length() == 0){
            primerNombre.setError("Primer nombre requerido");
            resultado = false;
        }

        if(primerApellido.getText().toString().trim().length() == 0){
            primerApellido.setError("Primer apellido requerido");
            resultado = false;
        }

        if(codigo.getText().toString().trim().length() == 0){
            codigo.setError("Codigo requerido");
            resultado = false;
        }

        if(clave.getText().toString().trim().length() == 0){
            clave.setError("Clave requerida");
            resultado = false;
        }

        //Validacion de rol
        if (spRol.getSelectedItem().toString().equals("Seleccione...")) {

            TextView errorText = (TextView)spRol.getSelectedView();
            errorText.setError("Rol requerido"); //anything here, just to add the icon
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Rol requerido");//changes the selected item text to this
        }

        final String emailValue = email.getText().toString().trim();
        if(!TextUtils.isEmpty(emailValue) && !isValidEmail(emailValue)){
            email.setError("Email inválido");
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

        private boolean cedulaRepetida = false;
        private boolean emailRepetido = false;
        private boolean codigoRepetido = false;
        private boolean pasaValidacionPrevia = false;

        @Override
        protected Boolean doInBackground(String... strings) {

            try{
                TareasGenerales tareasGenerales = new TareasGenerales();

                /**
                 * Si esta creando un nuevo usuario
                 */
                if(variablesGlobales.getUsuarioSeleccionadoAdmin() == null){

                    if(tareasGenerales.verficarDatoEnBd("USUARIO","CEDULA",cedula.getText().toString())){
                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> cedula ya registrada (crear)");
                        datoRepetido = true;
                        cedulaRepetida = true;
                    }

                    if(!TextUtils.isEmpty(email.getText().toString().trim()) &&
                            tareasGenerales.verficarDatoEnBd("USUARIO","EMAIL",email.getText().toString())){
                        Log.i("GuardandoUsuario", ">>>>>>>>>>>>>>>>>> email ya registrado (crear)");
                        datoRepetido = true;
                        emailRepetido = true;
                    }

                    if(tareasGenerales.verficarDatoEnBd("USUARIO","CODIGO",codigo.getText().toString())){
                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> codigo ya registrado (crear)");
                        datoRepetido = true;
                        codigoRepetido = true;
                    }

                }


                /**
                 * Si esta editando un usuario
                 */
                if(variablesGlobales.getUsuarioSeleccionadoAdmin() != null){

                    if(variablesGlobales.getUsuarioSeleccionadoAdmin().getCedula() != Integer.parseInt(cedula.getText().toString()) &&
                            tareasGenerales.verficarDatoEnBd("USUARIO","CEDULA",cedula.getText().toString())){
                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> cedula ya registrada (editar)");
                        datoRepetido = true;
                        cedulaRepetida = true;
                    }

                    if(!TextUtils.isEmpty(email.getText().toString().trim()) &&
                            !variablesGlobales.getUsuarioSeleccionadoAdmin().getEmail().equals(email.getText().toString())){
                        if(tareasGenerales.verficarDatoEnBd("USUARIO","EMAIL",email.getText().toString())) {
                        Log.i("GuardandoUsuario", ">>>>>>>>>>>>>>>>>> email ya registrado (editar)");
                        datoRepetido = true;
                        emailRepetido = true;
                        }
                    }

                    if(!variablesGlobales.getUsuarioSeleccionadoAdmin().getCodigo().equals(codigo.getText().toString()) &&
                            tareasGenerales.verficarDatoEnBd("USUARIO","CODIGO",codigo.getText().toString())){
                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> codigo ya registrado (editar)");
                        datoRepetido = true;
                        codigoRepetido = true;
                     }

                }

            }catch (Exception e){
                Log.e("Generales", "xxx Error TareaWsVerificarDatoEnBd: " + e.getMessage());
            }
            return datoRepetido;
        }

        public void onPostExecute(Boolean result){
            if(result){ //Indica que hay almenos un dato repetido en BD

                if(cedulaRepetida){
                    cedula.setError("Cedula ya registrada");
                }

                if(emailRepetido){
                    email.setError("Email ya registrado");
                }

                if(codigoRepetido){
                    codigo.setError("Código ya registrado");
                }

                Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();

            }else{ // Pasa validacion exitosa de campos repetidos

                if(pasaValidacionPrevia){
                    TareaWsGuardarUsuario tareaWsGuardarUsuario = new TareaWsGuardarUsuario();
                    tareaWsGuardarUsuario.execute();
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

    /**
     * Metodo encargado de validar si un formato de un email es correco
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

        //Si existe un usuario selecciondo previamente por el admin, se cargan los datos
        cargarDatosUsuarioSeleccionado();
    }

}

