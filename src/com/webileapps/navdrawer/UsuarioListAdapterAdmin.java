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
import modelo.Usuario;

/**
 * Created by pablo on 22/05/15.
 */
public class UsuarioListAdapterAdmin extends ArrayAdapter<Usuario> {

    private Activity ctx;

    public UsuarioListAdapterAdmin(Activity context, List<Usuario> usuarios) {
        super(context, R.layout.list_view_item_usuario_admin, usuarios);
        this.ctx = context;

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Holder holder = null;

        if(view == null){

            holder = new Holder();

            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_usuario_admin,parent,false);

            //Almacena en el holder la info que esta en pantalla
            ButterKnife.bind(holder, view);

            //cada "view" tendrá su único tag, ya que cada layout es compartido
            //fila que aparezca en le linearLauout, esto optimiza recursos eficientemente
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }

        Usuario  usuarioActual = this.getItem(position);
        inicializarCamposTexto(view, holder, usuarioActual);
        return view;
    }

    /**
     *Clase para contener una referencia al contenido del layout definido en el xml (usuario)
     */
    public class Holder
    {
        /**
         * Notaciones de la libreria butterknife
         * para inyectar vistas
         */
        @Bind(R.id.imgUsuarioAdmin)
        protected ImageView imgAutor;

        @Bind(R.id.tbxNombre)
        protected TextView nombre;

        @Bind(R.id.tbxCodigo)
        protected TextView tbxCodigo;

        @Bind(R.id.contenedorDetalleUsuarioAdmin)
        protected LinearLayout contenedorOculto;

        @Bind(R.id.editTextCedula)
        protected EditText cedula;

        @Bind(R.id.editTextPrimerNombre)
        protected EditText primerNombre;

        @Bind(R.id.editTextSegundoNombre)
        protected EditText segundoNombre;

        @Bind(R.id.editTextPrimerApellido)
        protected EditText primerApellido;

        @Bind(R.id.editTextSegundoApellido)
        protected EditText segundoApellido;

        @Bind(R.id.editTextTelefono)
        protected EditText telefono;

        @Bind(R.id.editTextDireccion)
        protected EditText direccion;

        @Bind(R.id.editTextEmail)
        protected EditText email;

        @Bind(R.id.editTextCodigo)
        protected EditText editTextCodigo;

        @Bind(R.id.editTextClave)
        protected EditText clave;

        @Bind(R.id.editTextRol)
        protected EditText rol;

    }

    private void inicializarCamposTexto(View view, Holder holder, Usuario usuarioActual) {

        holder.nombre.setText(usuarioActual.getPrimerNombre()+" "+usuarioActual.getPrimerApellido());
        holder.tbxCodigo.setText(usuarioActual.getCodigo());

        //Detalles del usuario (campos ocultos)
        holder.contenedorOculto.setVisibility(View.GONE);

        holder.cedula.setText(String.valueOf(usuarioActual.getCedula()));
        holder.primerNombre.setText(usuarioActual.getPrimerNombre());
        holder.segundoNombre.setText(usuarioActual.getSegundoNombre());
        holder.primerApellido.setText(usuarioActual.getPrimerApellido());
        holder.segundoApellido.setText(usuarioActual.getSegundoApellido());
        holder.telefono.setText(String.valueOf(usuarioActual.getTelefono()));
        holder.direccion.setText(usuarioActual.getDireccion());
        holder.email.setText(usuarioActual.getEmail());
        holder.editTextCodigo.setText(usuarioActual.getCodigo());
        holder.clave.setText(usuarioActual.getClave());
        holder.rol.setText(usuarioActual.getRol());
    }
}
