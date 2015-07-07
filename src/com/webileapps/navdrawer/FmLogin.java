package com.webileapps.navdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import modelo.Usuario;
import util.Configuracion;
import util.UtilidadesBuscarPorId;
import util.VariablesGlobales;

/**
 * Created by pablo on 29/04/15.
 */
public class FmLogin extends Activity {

    private EditText tbxUsuario, tbxClave;
    private Button btnLogin;

    private Usuario usuario;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_login);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        tbxUsuario = (EditText) findViewById(R.id.tbxUsuario);
        tbxClave = (EditText) findViewById(R.id.tbxClave);
    }

    public void login(View view){
        usuario = null;
        TareaWSLogin tareaLogin = new TareaWSLogin();
        tareaLogin.execute();
    }

    private void limpiar() {
        tbxUsuario.getText().clear();
        tbxClave.getText().clear();

        //Regresa el foco al campo Usuario
        tbxUsuario.requestFocus();
    }


    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano Login

    /**
     * Tarea login
     */
    private class TareaWSLogin extends AsyncTask<String,Integer,Boolean> {

        //Objeto de la clase configuracion la cual contiene atributos generales y conf. para conexion al server
        Configuracion conf = new Configuracion();

        private final String SOAP_ACTION = conf.getUrl()+"/login";
        private final String METHOD_NAME = "login";
        private final String NAMESPACE = conf.getNamespace();
        private final String URL = conf.getUrl();

        boolean resultadoTarea = true;

        protected Boolean doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("codigo", tbxUsuario.getText().toString());
            request.addProperty("clave",tbxClave.getText().toString());

            Log.i("FmLogin.java", ">>>>>>>>>>>> login " + tbxUsuario.getText().toString());
            Log.i("FmLogin.java",">>>>>>>>>>>> password "+tbxClave.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

                if (rs != null)
                {
                    for (SoapObject user : rs)
                    {
                        usuario = UtilidadesBuscarPorId.obtenerUsuarioSoap(user);
                        Log.i("FmLogin.java",">>>>>>>>>>>> idUsuario: "+usuario.getIdUsuario());
                        break;
                    }
                }
            }catch (Exception e){
                resultadoTarea = false;
                Log.d("FmLogin ", "xxx Error TareaWSLogin: "+e.getMessage());
            }
            return resultadoTarea;
        }

        protected void onPostExecute(Boolean result) {
            if (resultadoTarea && usuario != null) {
                limpiar();

                //Redireccion segun rol
                Intent goInicial = null;

                //Administrador
                /*if(usuario.getRol().equalsIgnoreCase("ADMIN")){
                    goInicial = new Intent(FmLogin.this, MainActivity.class);
                }*/
                variablesGlobales.setUsuarioLogueado(usuario);

                goInicial = new Intent(FmLogin.this, MainActivity.class);

                //Se envia el usuario logueado como parametro
                goInicial.putExtra("usuarioLogueado", usuario);
                startActivity(goInicial);

            }else{
                Toast.makeText(FmLogin.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
            }
        }

    }

}
