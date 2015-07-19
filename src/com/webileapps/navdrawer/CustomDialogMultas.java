package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import util.Configuracion;
import util.VariablesGlobales;

/**
 * Created by pablo on 17/07/15.
 */
public class CustomDialogMultas extends DialogFragment {

    EditText valorSugerido, valorCancelado, nota;
    long diasMora = 0;
    int idSolicitud;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_multa, null);
        valorSugerido = (EditText) view.findViewById(R.id.editTextValorSugerido);
        valorCancelado = (EditText) view.findViewById(R.id.editTextValorCancelado);
        nota = (EditText) view.findViewById(R.id.editTextNota);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        //Se reciben los parametros enviados desde la pagina invocante
        idSolicitud = getArguments().getInt("idSolicitud");
        diasMora = getArguments().getLong("diasMora");

        valorSugerido.setText("$ "+(variablesGlobales.getValorMulta() * diasMora));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setPositiveButton(R.string.aceptar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                TareaWsGuardarMulta tareaGuardarMulta = new TareaWsGuardarMulta();
                                tareaGuardarMulta.execute();

                            }
                        })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_multa, container);
        editTextValorSugerido = (EditText) view.findViewById(R.id.editTextValorSugerido);
        editTextValorSugerido.setText("XXX");

        return view;
    }*/


    /**
     * Tarea encargada de guardar la multa
     */
    private class TareaWsGuardarMulta extends AsyncTask<String,Integer,Boolean> {

        Configuracion conf = new Configuracion();
        final String SOAP_ACTION = conf.getUrl()+"/guardarMulta";
        final String METHOD_NAME = "guardarMulta";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("idSolicitud", idSolicitud);
            request.addProperty("valorSugerido", (variablesGlobales.getValorMulta() * diasMora));
            request.addProperty("valorCancelado", valorCancelado.getText().toString());
            request.addProperty("diasMora", diasMora);
            request.addProperty("nota", nota.getText().toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

                Log.i("GuardandoMulta", "*********************** TareaWsGuardarMulta: " + resultadoGuardar);
                if (resultadoGuardar == 1)
                {
                    resultadoTarea = true;
                }
            } catch (Exception e) {
                resultadoTarea = false;
                Log.e("GuardandoMulta", "xxx Error TareaWsGuardarMulta: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(resultadoTarea){
                //Toast.makeText(getActivity(), "Datos almacenados exitosamente", Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(getActivity(), "Error almacenando datos", Toast.LENGTH_LONG).show();
            }
        }

    }


}