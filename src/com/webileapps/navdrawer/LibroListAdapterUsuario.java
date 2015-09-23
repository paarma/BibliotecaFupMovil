package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import modelo.Libro;
import util.Utilidades;

/**
 * Created by pablo on 22/05/15.
 */
public class LibroListAdapterUsuario extends ArrayAdapter<Libro> {

    private Activity ctx;

    //Variables especificas para listar los autores
    ViewGroup parentAux;
    private LinearLayout linearListViewAutores;

    public LibroListAdapterUsuario(Activity context, List<Libro> libros) {
        super(context, R.layout.list_view_item_libro_usuario,libros);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_libro_usuario,parent,false);
        }

        //Variables especificas para listar los autores
        parentAux = parent;
        linearListViewAutores = (LinearLayout) view.findViewById(R.id.linear_listview_autores);

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
        view.findViewById(R.id.contenedorDetalleLibroReservar).setVisibility(View.GONE);

        EditText editText = (EditText) view.findViewById(R.id.editTextCodTopo);
        editText.setText(libroActual.getCodigoTopografico());

        editText = (EditText) view.findViewById(R.id.editTextTemas);
        editText.setText(libroActual.getTemas());

        editText = (EditText) view.findViewById(R.id.editTextPaginas);
        editText.setText(String.valueOf(libroActual.getPaginas()));


        editText = (EditText) view.findViewById(R.id.editTextDisponibilidad);

        //Se verifica la disponibilidad segun la cantidad de copias del libro
        if(libroActual.getCantidad() > Utilidades.cantidadMininaLibroPrestar){
            editText.setText("SI");
        }else{
            editText.setText("NO");
        }

        if(libroActual.getEditorial() != null) {
            editText = (EditText) view.findViewById(R.id.editTextEditorial);
            editText.setText(String.valueOf(libroActual.getEditorial().getDescripcion()));
        }

        if(libroActual.getArea() != null) {
            editText = (EditText) view.findViewById(R.id.editTextArea);
            editText.setText(String.valueOf(libroActual.getArea().getDescripcion()));
        }

    }
}
