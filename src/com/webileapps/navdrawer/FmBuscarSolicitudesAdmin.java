package com.webileapps.navdrawer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.Calendar;
import java.util.Date;

import modelo.Libro;
import modelo.Solicitud;
import modelo.Usuario;
import util.Utilidades;
import util.VariablesGlobales;

/**
 * Created by Pablo on 11/07/15.
 */
public class FmBuscarSolicitudesAdmin extends SherlockFragment {

    private Calendar calendario = Calendar.getInstance();

    private static EditText fechaSolicitud, titulo, isbn, codTopografico, codUsuario, cedulaUsuario;
    private static Spinner spinnerEstadoSoliciutd;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("BUSCAR", "************************************** INICIO BUSCAR SOLICITUDES. (ADMIN)");
        View view = inflater.inflate(R.layout.fm_buscar_solicitudes_admin, container, false);

        inicializarComponentes(view);

        /////////////////////////////Funcionalidades para DatepikerDialog
        final DatePickerDialog.OnDateSetListener dateFechaSolicitud = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFechaSolicitud();
            }
        };

        //Despliega el calendario al momento de seleccionar la caja de texto.
        fechaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateFechaSolicitud, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    public void inicializarComponentes(View view){

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);

        codUsuario = (EditText) view.findViewById(R.id.editTextCodigoUsuario);
        cedulaUsuario = (EditText) view.findViewById(R.id.editTextCedula);

        fechaSolicitud = (EditText) view.findViewById(R.id.editTextFechaSolicitud);
        spinnerEstadoSoliciutd = (Spinner) view.findViewById(R.id.spinnerEstadoSolicitud);

        //Se cargan algunos spinners con los datos del archivo arrays.xml
        ArrayAdapter adapterEstadoSolicitud = ArrayAdapter.createFromResource(getActivity(), R.array.estados_solicitud, R.layout.spinner_item);
        adapterEstadoSolicitud.setDropDownViewResource(R.layout.spinner_item);
        spinnerEstadoSoliciutd.setAdapter(adapterEstadoSolicitud);
    }

    private void updateLabelFechaSolicitud() {
        fechaSolicitud.setText(Utilidades.formatoFechaYYYYMMDD.format(calendario.getTime()));
    }

    /**
     * Metodo encargado de crear el objeto de busqueda con los parametros indicados
     */
    public static void capturarObjetoBusqueda(){

        Libro libro = new Libro();
        Usuario usuario = new Usuario();
        Solicitud solicitud = new Solicitud();

        if(titulo.getText().toString().trim().length() > 0){
            libro.setTitulo(titulo.getText().toString());
        }

        if(isbn.getText().toString().trim().length() > 0){
            libro.setIsbn(isbn.getText().toString());
        }

        if(codTopografico.getText().toString().trim().length() > 0){
            libro.setCodigoTopografico(codTopografico.getText().toString());
        }

        if(codUsuario.getText().toString().trim().length() > 0){
            usuario.setCodigo(codUsuario.getText().toString());
        }

        if(cedulaUsuario.getText().toString().trim().length() > 0){
            usuario.setCedula(Integer.parseInt(cedulaUsuario.getText().toString()));
        }

        if(fechaSolicitud.getText().toString().trim().length() > 0){
            try {
                Date fechaSol = Utilidades.formatoFechaYYYYMMDD.parse(fechaSolicitud.getText().toString());
                solicitud.setFechaSolicitud(fechaSol);
            }catch (Exception e){
                Log.e("BuscarSol","XXX Error capturarndo fechaSolicitud en buscarSolicitudes");
            }
        }

        if (!spinnerEstadoSoliciutd.getSelectedItem().toString().equals("Seleccione...")) {
            solicitud.setEstado(spinnerEstadoSoliciutd.getSelectedItem().toString());
        }

        solicitud.setLibro(libro);
        solicitud.setUsuario(usuario);

        variablesGlobales.setSolicitudBuscar(solicitud);
    }
}
