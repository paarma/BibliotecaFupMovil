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

/**
 * Created by pablo on 22/05/15.
 */
public class LibroListAdapterAdmin extends ArrayAdapter<Libro> {

    private Activity ctx;

    //Variables especificas para listar los autores
    ViewGroup parentAux;
    private LinearLayout linearListViewAutores;

    public LibroListAdapterAdmin(Activity context, List<Libro> libros) {
        super(context, R.layout.list_view_item_libro_admin,libros);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_libro_admin,parent,false);
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

        //Se omite la cantidad
        //editText = (EditText) view.findViewById(R.id.editTextCantidad);
        //editText.setText(String.valueOf(libroActual.getCantidad()));

        editText = (EditText) view.findViewById(R.id.editTextAnio);
        editText.setText(String.valueOf(libroActual.getAnio()));

        if(libroActual.getEditorial() != null) {
            editText = (EditText) view.findViewById(R.id.editTextEditorial);
            editText.setText(String.valueOf(libroActual.getEditorial().getDescripcion()));
        }

        if(libroActual.getArea() != null) {
            editText = (EditText) view.findViewById(R.id.editTextArea);
            editText.setText(String.valueOf(libroActual.getArea().getDescripcion()));
        }

        if(libroActual.getSede() != null) {
            editText = (EditText) view.findViewById(R.id.editTextSede);
            editText.setText(libroActual.getSede().getDescripcion());
        }

        editText = (EditText) view.findViewById(R.id.editTextAdquisicion);
        editText.setText(libroActual.getAdquisicion());

        editText = (EditText) view.findViewById(R.id.editTextEstado);
        editText.setText(libroActual.getEstado());



        linearListViewAutores.removeAllViewsInLayout();
        //Se recaga la interfaz con datos vacios en caso de no cargar autores
        if(libroActual.getListaAutores() != null && libroActual.getListaAutores().size() == 0){
            linearListViewAutores.removeAllViewsInLayout();

            //View mLinearView = inflaterAux.inflate(R.layout.row_autores_libro, null);
            View mLinearView = ctx.getLayoutInflater().inflate(R.layout.row_autores_libro_gray,parentAux,false);
            linearListViewAutores.addView(mLinearView);
        }

        if(libroActual.getListaAutores() != null){
            for(int i = 0; i < libroActual.getListaAutores().size(); i++){
                /**
                 * inflate items/ add items in linear layout instead of listview
                 */
                //View mLinearView = inflaterAux.inflate(R.layout.row_autores_libro, null);
                View mLinearView = ctx.getLayoutInflater().inflate(R.layout.row_autores_libro_gray,parentAux,false);
                /**
                 * getting id of row.xml
                 */
                TextView mFirstName = (TextView) mLinearView
                        .findViewById(R.id.textViewName);
                TextView mLastName = (TextView) mLinearView
                        .findViewById(R.id.textViewLastName);

                /**
                 * set item into row
                 */
                final String fName = libroActual.getListaAutores().get(i).getPrimerNombre();
                final String lName = libroActual.getListaAutores().get(i).getPrimerApellido();
                mFirstName.setText(fName);
                mLastName.setText(lName);

                /**
                 * add view in top linear
                 */
                linearListViewAutores.addView(mLinearView);

            }
        }

    }
}
