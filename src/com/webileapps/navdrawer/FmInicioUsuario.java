package com.webileapps.navdrawer;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

import util.VariablesGlobales;

public class FmInicioUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_inicio_usuario, container, false);

        Button btnReservarLibroUer = (Button) rootView.findViewById(R.id.buttonReservarLibroUser);
        Button btnMisLibrosUser = (Button) rootView.findViewById(R.id.buttonMisLibrosUser);


        btnReservarLibroUer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.e("FmInicioUsuario", "inicio - onclick boton");
                                            fragment = new FmLibrosUsuario();

                                            //Se setea la variable global para cargar la vista de Libros (Reservas)
                                            variablesGlobales.setOpcionMenu(0);

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.e("FmInicioUsuario", "XXX - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );

        btnMisLibrosUser.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                // update the main content by replacing fragments
                                                SherlockFragment fragment = null;
                                                Log.e("FmInicioUsuario", "inicio - onclick boton");
                                                fragment = new FmLibrosUsuario();

                                                //Se setea la variable global para cargar la vista de Mis Libros
                                                variablesGlobales.setOpcionMenu(1);

                                                if (fragment != null) {
                                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                    fragmentManager.beginTransaction()
                                                            .replace(R.id.content_frame, fragment).commit();
                                                } else {
                                                    // error in creating fragment
                                                    Log.e("FmInicioUsuario", "XXX - Error cuando se creo el fragment");
                                                }

                                            }
                                        }

        );

        return rootView;


    }


}

