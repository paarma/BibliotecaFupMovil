package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import modelo.Solicitud;
import util.Utilidades;

/**
 * Created by pablo on 22/05/15.
 */
public class SolicitudListAdapterAdmin extends ArrayAdapter<Solicitud> {

    private Activity ctx;

    public SolicitudListAdapterAdmin(Activity context, List<Solicitud> listaSolicitud) {
        super(context, R.layout.list_view_item_solicitud_admin,listaSolicitud);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_solicitud_admin,parent,false);
        }

        Solicitud solicutudActual = this.getItem(position);
        inicializarCamposTexto(view, solicutudActual);
        return view;
    }

    private void inicializarCamposTexto(View view, Solicitud solicutudActual) {

        TextView textView = (TextView) view.findViewById(R.id.tbxTitulo);
        textView.setText(solicutudActual.getLibro().getTitulo());

        textView = (TextView) view.findViewById(R.id.tbxIsbn);
        textView.setText(solicutudActual.getLibro().getIsbn());

        //Detalles del libro (campos ocultos)
        EditText editText = (EditText) view.findViewById(R.id.editTextCodTopo);
        editText.setText(solicutudActual.getLibro().getCodigoTopografico());

        editText = (EditText) view.findViewById(R.id.editTextTemas);
        editText.setText(solicutudActual.getLibro().getTemas());

        editText = (EditText) view.findViewById(R.id.editTextPaginas);
        editText.setText(String.valueOf(solicutudActual.getLibro().getPaginas()));

        //Detalles de la reserva (campos ocultos)
        editText = (EditText) view.findViewById(R.id.editTextFechaReserva);
        editText.setText(Utilidades.formatoFechaYYYYMMDD.format(solicutudActual.getFechaReserva()));

        editText = (EditText) view.findViewById(R.id.editTextFechaDevolucion);
        editText.setText(Utilidades.formatoFechaYYYYMMDD.format(solicutudActual.getFechaDevolucion()));

        editText = (EditText) view.findViewById(R.id.editTextEstadoReserva);
        editText.setText(solicutudActual.getEstado());

    }
}
