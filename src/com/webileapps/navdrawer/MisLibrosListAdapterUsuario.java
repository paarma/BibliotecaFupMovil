package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import modelo.Solicitud;
import util.Utilidades;

/**
 * Created by pablo on 22/05/15.
 */
public class MisLibrosListAdapterUsuario extends ArrayAdapter<Solicitud> {

    private Activity ctx;

    LinearLayout linearItemLibro;
    ImageView imagenReserva;

    public MisLibrosListAdapterUsuario(Activity context, List<Solicitud> listaSolicitud) {
        super(context, R.layout.list_view_item_mis_libros,listaSolicitud);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_mis_libros,parent,false);
        }

        linearItemLibro = (LinearLayout) view.findViewById(R.id.linearItemMisLibrosLibro);
        imagenReserva = (ImageView) view.findViewById(R.id.imgReservaUsuario);

        Solicitud solicutudActual = this.getItem(position);
        inicializarCamposTexto(view, solicutudActual);
        return view;
    }

    private void inicializarCamposTexto(View view, Solicitud solicutudActual) {

        /**
         * Estados de "Mis Libros"
         * BLANCO: Indica que se ha realizado una solicitud para ese libro.(EN PROCESO)
         * VERDE: Indica que se acepto la solicitud y el libro ya se le entrego al usuario. (PRESTADO)
         * ROJO: Indica que se acepto la solicitud y el libro ya se le entrego al usuario pero
         *          este no lo ha regresado en la fecha de devolucion (MORA)
         */
        if(solicutudActual.getEstado().equals(Utilidades.estadoEnProceso)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#D8D57F"));
            imagenReserva.setImageResource(R.drawable.ic_book_white_48dp);

        }else if(solicutudActual.getEstado().equals(Utilidades.estadoPrestado)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#5F9968"));
            imagenReserva.setImageResource(R.drawable.ic_book_green_48dp);

        }else if(solicutudActual.getEstado().equals(Utilidades.estadoEnMora)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#FA9393"));
            imagenReserva.setImageResource(R.drawable.ic_book_red_48dp);

        }

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
