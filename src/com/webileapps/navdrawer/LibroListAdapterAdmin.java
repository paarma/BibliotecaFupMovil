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

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = null;

        if(view == null){

            holder = new Holder();
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_libro_admin,parent,false);

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
        @Bind(R.id.imgLibroAdmin)
        protected ImageView imgLibro;

        @Bind(R.id.tbxTitulo)
        protected TextView titulo;

        @Bind(R.id.tbxIsbn)
        protected TextView isbn;

        @Bind(R.id.contenedorDetalleLibroAdmin)
        protected  LinearLayout contenedorOculto;

        @Bind(R.id.editTextCodTopo)
        protected EditText codTopografico;

        @Bind(R.id.editTextTemas)
        protected EditText temas;

        @Bind(R.id.editTextPaginas)
        protected EditText paginas;

        @Bind(R.id.editTextValor)
        protected EditText valor;

        @Bind(R.id.editTextRadicado)
        protected EditText radicado;

        @Bind(R.id.editTextSerie)
        protected EditText serie;

        @Bind(R.id.editTextAnio)
        protected EditText anio;

        @Bind(R.id.editTextEditorial)
        protected EditText editorial;

        @Bind(R.id.editTextArea)
        protected EditText area;

        @Bind(R.id.editTextSede)
        protected EditText sede;

        @Bind(R.id.editTextAdquisicion)
        protected EditText adquisicion;

        @Bind(R.id.editTextEstado)
        protected EditText estado;

    }

    private void inicializarCamposTexto(View view, Holder holder, Libro libroActual) {

        holder.titulo.setText(libroActual.getTitulo());
        holder.isbn.setText(libroActual.getIsbn());

        //Detalles del libro (campos ocultos)
        holder.contenedorOculto.setVisibility(View.GONE);

        holder.codTopografico.setText(libroActual.getCodigoTopografico());
        holder.temas.setText(libroActual.getTemas());
        holder.paginas.setText(String.valueOf(libroActual.getPaginas()));
        holder.valor.setText(String.valueOf(libroActual.getValor()));
        holder.radicado.setText(libroActual.getRadicado());
        holder.serie.setText(String.valueOf(libroActual.getSerie()));
        holder.anio.setText(String.valueOf(libroActual.getAnio()));

        if(libroActual.getEditorial() != null) {
            holder.editorial.setText(libroActual.getEditorial().getDescripcion());
        }

        if(libroActual.getArea() != null) {
            holder.area.setText(libroActual.getArea().getDescripcion());
        }

        if(libroActual.getSede() != null) {
            holder.sede.setText(libroActual.getSede().getDescripcion());
        }

        holder.adquisicion.setText(libroActual.getAdquisicion());
        holder.estado.setText(libroActual.getEstado());

        //Se omite la cantidad view.findViewById(R.id.editTextCantidad);

    }

}
