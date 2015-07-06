package com.webileapps.navdrawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

import modelo.Usuario;
import util.VariablesGlobales;

/**
 * Created by alex on 9/05/15.
 */
public class FmBuscarUsuarioAdmin extends SherlockFragment {

    private static EditText cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, codigo;
    private static Spinner spinnerRol;

    private static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("BUSCAR", "************************************** INICIO BUSCAR USUARIO. (ADMIN)");
        View view = inflater.inflate(R.layout.fm_buscar_usuario_admin, container, false);

        inicializarComponentes(view);
        return view;
    }

    public void inicializarComponentes(View view){

        cedula = (EditText) view.findViewById(R.id.editTextCedula);
        primerNombre = (EditText) view.findViewById(R.id.editTextPrimerNombre);
        segundoNombre = (EditText) view.findViewById(R.id.editTextSegundoNombre);
        primerApellido = (EditText) view.findViewById(R.id.editTextPrimerApellido);
        segundoApellido = (EditText) view.findViewById(R.id.editTextSegundoApellido);
        codigo = (EditText) view.findViewById(R.id.editTextCodigo);
        spinnerRol = (Spinner) view.findViewById(R.id.spinnerRol);

        //Se carga el spinner rol con los datos del archivo arrays.xml
        ArrayAdapter adapterRol = ArrayAdapter.createFromResource(getActivity(), R.array.roles, R.layout.spinner_item);
        adapterRol.setDropDownViewResource(R.layout.spinner_item);
        spinnerRol.setAdapter(adapterRol);

    }

    /**
     * Metodo encargado de crear el objeto de busqueda con los parametros indicados
     */
    public static void capturarObjetoBusqueda(){

        Usuario usuario = new Usuario();

        if(cedula.getText().toString().trim().length() > 0){
            usuario.setCedula(Integer.parseInt(cedula.getText().toString()));
        }

        if(primerNombre.getText().toString().trim().length() > 0){
            usuario.setPrimerNombre(primerNombre.getText().toString());
        }

        if(segundoNombre.getText().toString().trim().length() > 0){
            usuario.setSegundoNombre(segundoNombre.getText().toString());
        }

        if(primerApellido.getText().toString().trim().length() > 0){
            usuario.setPrimerApellido(primerApellido.getText().toString());
        }

        if(segundoApellido.getText().toString().trim().length() > 0){
            usuario.setSegundoApellido(segundoApellido.getText().toString());
        }

        if(codigo.getText().toString().trim().length() > 0){
            usuario.setCodigo(codigo.getText().toString());
        }

        if (!spinnerRol.getSelectedItem().toString().equals("Seleccione...")) {
            usuario.setRol(spinnerRol.getSelectedItem().toString());
        }

        variablesGlobales.setUsuarioBuscar(usuario);
    }

}
