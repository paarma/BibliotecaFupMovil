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

        setNotifyOnChange(true);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = null;

        if(view == null){

            holder = new Holder();
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_libro_usuario,parent,false);

            //Almacena en el holder la info que esta en pantalla
            ButterKnife.bind(holder, view);

            //cada "view" tendrá su único tag, ya que cada layout es compartido
            //fila que aparezca en le linearLauout, esto optimiza recursos eficientemente
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        //Variables especificas para listar los autores
        parentAux = parent;
        linearListViewAutores = (LinearLayout) view.findViewById(R.id.linear_listview_autores);

        Libro  libroActual = this.getItem(position);
        inicializarCamposTexto(view, holder, libroActual);
        return view;
    }


    /**
     *Clase para contener una referencia al contenido del layout definido en el xml (libro)
     */
    public class Holder
    {
        /**
         * Notaciones de la libreria butterknife
         * para inyectar vistas
         */
        @Bind(R.id.imgReservaUsuario)
        protected ImageView imgLibro;

        @Bind(R.id.tbxTitulo)
        protected TextView titulo;

        @Bind(R.id.tbxIsbn)
        protected TextView isbn;

        @Bind(R.id.contenedorDetalleLibroReservar)
        protected  LinearLayout contenedorOculto;

        @Bind(R.id.editTextCodTopo)
        protected EditText codTopografico;

        @Bind(R.id.editTextTemas)
        protected EditText temas;

        @Bind(R.id.editTextPaginas)
        protected EditText paginas;

        @Bind(R.id.editTextDisponibilidad)
        protected EditText disponibilidad;

        @Bind(R.id.editTextEditorial)
        protected EditText editorial;

        @Bind(R.id.editTextArea)
        protected EditText area;
    }

    private void inicializarCamposTexto(View view, Holder holder, Libro libroActual) {

        holder.titulo.setText(libroActual.getTitulo());
        holder.isbn.setText(libroActual.getIsbn());

        //Detalles del libro (campos ocultos)
        holder.contenedorOculto.setVisibility(View.GONE);

        holder.codTopografico.setText(libroActual.getCodigoTopografico());
        holder.temas.setText(libroActual.getTemas());
        holder.paginas.setText(String.valueOf(libroActual.getPaginas()));

        //Se verifica la disponibilidad segun la cantidad  del libro y por la misma disponiblidad
        if(libroActual.getCantidad() > Utilidades.cantidadMininaLibroPrestar &&
                libroActual.getDisponibilidad().equals("SI")){
            holder.disponibilidad.setText("SI");
        }else{
            holder.disponibilidad.setText("NO");
        }


        if(libroActual.getEditorial() != null) {
            holder.editorial.setText(libroActual.getEditorial().getDescripcion());
        }

        if(libroActual.getArea() != null) {
            holder.area.setText(libroActual.getArea().getDescripcion());
        }


    }

}
