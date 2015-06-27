package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import modelo.Libro;

/**
 * Created by pablo on 22/05/15.
 */
public class LibroListAdapterAdmin extends ArrayAdapter<Libro> {

    private Activity ctx;

    public LibroListAdapterAdmin(Activity context, List<Libro> libros) {
        super(context, R.layout.list_view_item_libro_admin,libros);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_libro_admin,parent,false);
        }

        Libro  libroActual = this.getItem(position);
        inicializarCamposTexto(view, libroActual);
        return view;
    }

    private void inicializarCamposTexto(View view, Libro libroActual) {

        TextView textView = (TextView) view.findViewById(R.id.tbxTitulo);
        textView.setText(libroActual.getTitulo());

        textView = (TextView) view.findViewById(R.id.tbxIsbn);
        textView.setText(libroActual.getIsbn());

        //Detalles del libro (campos ocultos)
        EditText editText = (EditText) view.findViewById(R.id.editTextCodTopo);
        editText.setText(libroActual.getCodigoTopografico());

        editText = (EditText) view.findViewById(R.id.editTextTemas);
        editText.setText(libroActual.getTemas());

        editText = (EditText) view.findViewById(R.id.editTextPaginas);
        editText.setText(String.valueOf(libroActual.getPaginas()));

        editText = (EditText) view.findViewById(R.id.editTextValor);
        editText.setText(String.valueOf(libroActual.getValor()));

        editText = (EditText) view.findViewById(R.id.editTextRadicado);
        editText.setText(libroActual.getRadicado());

        editText = (EditText) view.findViewById(R.id.editTextSerie);
        editText.setText(String.valueOf(libroActual.getSerie()));

        editText = (EditText) view.findViewById(R.id.editTextAnio);
        editText.setText(String.valueOf(libroActual.getAnio()));

    }
}
