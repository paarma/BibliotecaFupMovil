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
import modelo.Autor;

/**
 * Created by pablo on 22/05/15.
 */
public class AutorListAdapterAdmin extends ArrayAdapter<Autor> {

    private Activity ctx;

    public AutorListAdapterAdmin(Activity context, List<Autor> autores) {
        super(context, R.layout.list_view_item_autor_admin,autores);
        this.ctx = context;

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = null;

        if(view == null){

            holder = new Holder();

            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_autor_admin,parent,false);

            //Almacena en el holder la info que esta en pantalla
            ButterKnife.bind(holder, view);

            //cada "view" tendrá su único tag, ya que cada layout es compartido
            //fila que aparezca en le linearLauout, esto optimiza recursos eficientemente
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        Autor autorActual = this.getItem(position);
        inicializarCamposTexto(view,  holder, autorActual);
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
        @Bind(R.id.imgAutorAdmin)
        protected ImageView imgAutor;

        @Bind(R.id.tbxNombre)
        protected TextView nombre;

        @Bind(R.id.tbxTipo)
        protected TextView tbxTipo;

        @Bind(R.id.contenedorDetalleAutorAdmin)
        protected  LinearLayout contenedorOculto;

        @Bind(R.id.editTextPrimerNombre)
        protected EditText primerNombre;

        @Bind(R.id.editTextSegundoNombre)
        protected EditText segundoNombre;

        @Bind(R.id.editTextPrimerApellido)
        protected EditText primerApellido;

        @Bind(R.id.editTextSegundoApellido)
        protected EditText segundoApellido;

        @Bind(R.id.editTextTipoAutor)
        protected EditText editTextTipo;
    }

    private void inicializarCamposTexto(View view, Holder holder, Autor autorActual) {

        holder.nombre.setText(autorActual.getPrimerNombre()+" "+autorActual.getPrimerApellido());
        holder.tbxTipo.setText(autorActual.getTipoAutor());

        //Detalles del autor (campos ocultos)
        holder.contenedorOculto.setVisibility(View.GONE);

        holder.primerNombre.setText(autorActual.getPrimerNombre());
        holder.segundoNombre.setText(autorActual.getSegundoNombre());
        holder.primerApellido.setText(autorActual.getPrimerApellido());
        holder.segundoApellido.setText(autorActual.getSegundoApellido());
        holder.editTextTipo.setText(autorActual.getTipoAutor());
    }
}
