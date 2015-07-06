package com.webileapps.navdrawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import modelo.Usuario;

/**
 * Created by pablo on 22/05/15.
 */
public class UsuarioListAdapterAdmin extends ArrayAdapter<Usuario> {

    private Activity ctx;

    public UsuarioListAdapterAdmin(Activity context, List<Usuario> usuarios) {
        super(context, R.layout.list_view_item_usuario_admin, usuarios);
        this.ctx = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            view = ctx.getLayoutInflater().inflate(R.layout.list_view_item_usuario_admin,parent,false);
        }

        Usuario  usuarioActual = this.getItem(position);
        inicializarCamposTexto(view, usuarioActual);
        return view;
    }

    private void inicializarCamposTexto(View view, Usuario usuarioActual) {

        TextView textView = (TextView) view.findViewById(R.id.tbxNombre);
        textView.setText(usuarioActual.getPrimerNombre()+" "+usuarioActual.getPrimerApellido());

        textView = (TextView) view.findViewById(R.id.tbxCodigo);
        textView.setText(usuarioActual.getCodigo());

        //Detalles del usuario (campos ocultos)
        EditText editText = (EditText) view.findViewById(R.id.editTextCedula);
        editText.setText(String.valueOf(usuarioActual.getCedula()));

        editText = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        editText.setText(usuarioActual.getPrimerNombre());

        editText = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        editText.setText(usuarioActual.getSegundoNombre());

        editText = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        editText.setText(usuarioActual.getPrimerApellido());

        editText = (EditText) view.findViewById(R.id.editTextSegundoApellido);
        editText.setText(usuarioActual.getSegundoApellido());

        editText = (EditText) view.findViewById(R.id.editTextTelefono);
        editText.setText(String.valueOf(usuarioActual.getTelefono()));

        editText = (EditText) view.findViewById(R.id.editTextDireccion);
        editText.setText(usuarioActual.getDireccion());

        editText = (EditText) view.findViewById(R.id.editTextEmail);
        editText.setText(usuarioActual.getEmail());

        editText = (EditText) view.findViewById(R.id.editTextCodigo);
        editText.setText(usuarioActual.getCodigo());

        editText = (EditText) view.findViewById(R.id.editTextClave);
        editText.setText(usuarioActual.getClave());

        editText = (EditText) view.findViewById(R.id.editTextRol);
        editText.setText(usuarioActual.getRol());

    }
}
