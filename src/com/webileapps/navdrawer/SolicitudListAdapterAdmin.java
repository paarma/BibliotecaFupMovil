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

import butterknife.Bind;
import butterknife.ButterKnife;
import modelo.Solicitud;
import util.Utilidades;

/**
 * Created by pablo on 22/05/15.
 */
public class SolicitudListAdapterAdmin extends ArrayAdapter<Solicitud> {

    private Activity ctx;

    LinearLayout linearItemLibro;
    ImageView imagenReserva;

    public SolicitudListAdapterAdmin(Activity context, List<Solicitud> listaSolicitud) {
        super(context, R.layout.list_view_item_solicitud_admin,listaSolicitud);
        this.ctx = context;

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = null;

        if(view == null){

            holder = new Holder();

            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_solicitud_admin,parent,false);
            //Almacena en el holder la info que esta en pantalla
            ButterKnife.bind(holder, view);

            //cada "view" tendrá su único tag, ya que cada layout es compartido
            //fila que aparezca en le linearLauout, esto optimiza recursos eficientemente
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        linearItemLibro = (LinearLayout) view.findViewById(R.id.linearItemSolicitudesAdmin);
        imagenReserva = (ImageView) view.findViewById(R.id.imgLibroSolicitudAdmin);

        Solicitud solicutudActual = this.getItem(position);
        inicializarCamposTexto(view, holder, solicutudActual);
        return view;
    }


    /**
     *Clase para contener una referencia al contenido del layout definido en el xml (autor)
     */
    public class Holder
    {
        /**
         * Notaciones de la libreria butterknife
         * para inyectar vistas
         */
        @Bind(R.id.imgLibroSolicitudAdmin)
        protected ImageView imagenReserva;

        @Bind(R.id.tbxTitulo)
        protected TextView titulo;

        @Bind(R.id.tbxIsbn)
        protected TextView isbn;

        @Bind(R.id.contenedorDetalleSolAdmin)
        protected  LinearLayout contenedorOculto;

        @Bind(R.id.editTextCodTopo)
        protected EditText codTopografico;

        @Bind(R.id.editTextTemas)
        protected EditText temas;

        @Bind(R.id.editTextPaginas)
        protected EditText paginas;

        @Bind(R.id.editTextCodigoUsuario)
        protected EditText codUsuario;

        @Bind(R.id.editTextNombresUsuario)
        protected EditText nombresUsuario;

        @Bind(R.id.editTextApellidosUsuario)
        protected EditText apellidosUsuario;

        @Bind(R.id.editTextFechaReserva)
        protected EditText fechaReserva;

        @Bind(R.id.editTextFechaDevolucion)
        protected EditText fechaDevolucion;

        @Bind(R.id.editTextEstadoReserva)
        protected EditText estadoReserva;

    }


    private void inicializarCamposTexto(View view, Holder holder, Solicitud solicutudActual) {

        /**
         * Estados de "Solicitud"
         * BLANCO: Indica que se ha realizado una solicitud para ese libro.(EN PROCESO)
         * VERDE: Indica que se acepto la solicitud y el libro ya se le entrego al usuario. (PRESTADO)
         * ROJO: Indica que se acepto la solicitud y el libro ya se le entrego al usuario pero
         *          este no lo ha regresado en la fecha de devolucion (MORA)
         * GRIS: Indica que el libro ya se regreso (FINALIZADO)
         */
        if(solicutudActual.getEstado().equals(Utilidades.estadoEnProceso)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#D8D57F"));
            holder.imagenReserva.setImageResource(R.drawable.ic_book_white_48dp);

        }else if(solicutudActual.getEstado().equals(Utilidades.estadoPrestado)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#5F9968"));
            holder.imagenReserva.setImageResource(R.drawable.ic_book_green_48dp);

        }else if(solicutudActual.getEstado().equals(Utilidades.estadoEnMora)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#FA9393"));
            holder.imagenReserva.setImageResource(R.drawable.ic_book_red_48dp);

        }else if(solicutudActual.getEstado().equals(Utilidades.estadoFinalizado)){
            //linearItemLibro.setBackgroundColor(Color.parseColor("#FA9393"));
            holder.imagenReserva.setImageResource(R.drawable.ic_book_grey600_48dp);

        }

        holder.titulo.setText(solicutudActual.getLibro().getTitulo());
        holder.isbn.setText(solicutudActual.getLibro().getIsbn());

        //Detalles del libro (campos ocultos)
        holder.contenedorOculto.setVisibility(View.GONE);

        holder.codTopografico.setText(solicutudActual.getLibro().getCodigoTopografico());
        holder.temas.setText(solicutudActual.getLibro().getTemas());
        holder.paginas.setText(String.valueOf(solicutudActual.getLibro().getPaginas()));

        //Detalles del usuario (campos ocultos)
        if(solicutudActual.getUsuario() != null &&
                solicutudActual.getUsuario().getIdUsuario() != 0){

            holder.codUsuario.setText(solicutudActual.getUsuario().getCodigo());
            holder.nombresUsuario.setText(solicutudActual.getUsuario().getPrimerNombre()+" "+
                    solicutudActual.getUsuario().getSegundoNombre());
            holder.apellidosUsuario.setText(solicutudActual.getUsuario().getPrimerApellido()+" "+
                    solicutudActual.getUsuario().getSegundoApellido());
        }

        //Detalles de la reserva (campos ocultos)
        holder.fechaReserva.setText(Utilidades.formatoFechaYYYYMMDD.format(solicutudActual.getFechaReserva()));
        holder.fechaDevolucion.setText(Utilidades.formatoFechaYYYYMMDD.format(solicutudActual.getFechaDevolucion()));
        holder.estadoReserva.setText(solicutudActual.getEstado());
    }
}
