package com.webileapps.navdrawer;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

import util.VariablesGlobales;

public class FmInicioAdmin extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_inicio_admin, container, false);

        Button btnLibro = (Button) rootView.findViewById(R.id.buttonLibro);
        Button btnAutor = (Button) rootView.findViewById(R.id.buttonAutor);
        Button btnEditorial = (Button) rootView.findViewById(R.id.buttonEditorial);
        Button btnSolicitudes = (Button) rootView.findViewById(R.id.buttonSolicitudes);
        Button btnReportes = (Button) rootView.findViewById(R.id.buttonReportes);
        Button btnUsuarios = (Button) rootView.findViewById(R.id.buttonUsuarios);


        btnLibro.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.i("Alex", "inicio - onclick boton");

                                            //Se inicializa el objeto libroSeleccionadoAdmin
                                            variablesGlobales.setLibroSeleccionadoAdmin(null);

                                            fragment = new FmLibrosAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.i("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );




        btnAutor.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.i("Alex", "inicio - onclick boton");
                                            fragment = new FmAutorAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.i("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );


        btnEditorial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.i("Alex", "inicio - onclick boton");
                                            fragment = new FmEditorialAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.i("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );

        btnSolicitudes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.i("Alex", "inicio - onclick boton");
                                            fragment = new FmSolicitudesAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.i("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );

        btnReportes.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               // update the main content by replacing fragments
                                               SherlockFragment fragment = null;
                                               Log.i("Alex", "inicio - onclick boton");
                                               fragment = new FmReportesAdmin();

                                               if (fragment != null) {
                                                   android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                   fragmentManager.beginTransaction()
                                                           .replace(R.id.content_frame, fragment).commit();
                                               } else {
                                                   // error in creating fragment
                                                   Log.i("Alex", "MainActivity - Error cuando se creo el fragment");
                                               }

                                           }
                                       }

        );


        btnUsuarios.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;

                                            //Se inicializa el objeto libroSeleccionadoAdmin
                                            variablesGlobales.setUsuarioSeleccionadoAdmin(null);

                                            fragment = new FmUsuarioAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.d("InicioAdmin", "XXX InicioAdmin - Error cuando se creo el fragment usuarios");
                                            }

                                        }
                                    }

        );





        return rootView;

    }


}

