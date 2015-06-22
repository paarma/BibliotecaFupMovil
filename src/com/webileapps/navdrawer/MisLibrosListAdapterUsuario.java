package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import modelo.Solicitud;

/**
 * Created by pablo on 22/05/15.
 */
public class MisLibrosListAdapterUsuario extends ArrayAdapter<Solicitud> {

    private Activity ctx;

    public MisLibrosListAdapterUsuario(Activity context, List<Solicitud> listaSolicitud) {
        super(context, R.layout.list_view_item_mis_libros,listaSolicitud);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_mis_libros,parent,false);
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

    }
}
