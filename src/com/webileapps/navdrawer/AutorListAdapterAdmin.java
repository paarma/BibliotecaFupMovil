package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import modelo.Autor;

/**
 * Created by pablo on 22/05/15.
 */
public class AutorListAdapterAdmin extends ArrayAdapter<Autor> {

    private Activity ctx;

    public AutorListAdapterAdmin(Activity context, List<Autor> autores) {
        super(context, R.layout.list_view_item_autor_admin,autores);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_autor_admin,parent,false);
        }

        Autor autorActual = this.getItem(position);
        inicializarCamposTexto(view, autorActual);
        return view;
    }

    private void inicializarCamposTexto(View view, Autor autorActual) {

        TextView textView = (TextView) view.findViewById(R.id.tbxNombre);
        textView.setText(autorActual.getPrimerNombre()+" "+autorActual.getPrimerApellido());

        textView = (TextView) view.findViewById(R.id.tbxTipo);
        textView.setText(autorActual.getTipoAutor());

        //Detalles del autor (campos ocultos)
        EditText editText = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        editText.setText(autorActual.getPrimerNombre());

        editText = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        editText.setText(autorActual.getSegundoNombre());

        editText = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        editText.setText(autorActual.getPrimerApellido());

        editText = (EditText) view.findViewById(R.id.editTextSegundoApellido);
        editText.setText(autorActual.getSegundoApellido());

        editText = (EditText) view.findViewById(R.id.editTextTipoAutor);
        editText.setText(autorActual.getTipoAutor());

    }
}
