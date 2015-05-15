package com.webileapps.navdrawer;


import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class FmInicioAdmin extends SherlockFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_inicio_admin, container, false);

        Button btnLibro = (Button) rootView.findViewById(R.id.buttonLibro);
        Button btnAutor = (Button) rootView.findViewById(R.id.buttonAutor);
        Button btnEditorial = (Button) rootView.findViewById(R.id.buttonEditorial);
        Button btnSolicitudes = (Button) rootView.findViewById(R.id.buttonSolicitudes);
        Button btnReportes = (Button) rootView.findViewById(R.id.buttonReportes);
        Button btnSalir = (Button) rootView.findViewById(R.id.buttonSalir);


        btnLibro.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.e("Alex", "inicio - onclick boton");
                                            fragment = new FmLibrosAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.e("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );




        btnAutor.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.e("Alex", "inicio - onclick boton");
                                            fragment = new FmAutorAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.e("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );


        btnEditorial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.e("Alex", "inicio - onclick boton");
                                            fragment = new FmEditorialAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.e("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );

        btnSolicitudes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // update the main content by replacing fragments
                                            SherlockFragment fragment = null;
                                            Log.e("Alex", "inicio - onclick boton");
                                            fragment = new FmSolicitudesAdmin();

                                            if (fragment != null) {
                                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.content_frame, fragment).commit();
                                            } else {
                                                // error in creating fragment
                                                Log.e("Alex", "MainActivity - Error cuando se creo el fragment");
                                            }

                                        }
                                    }

        );

        btnReportes.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

                                               // update the main content by replacing fragments
                                               SherlockFragment fragment = null;
                                               Log.e("Alex", "inicio - onclick boton");
                                               fragment = new FmReportesAdmin();

                                               if (fragment != null) {
                                                   android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                   fragmentManager.beginTransaction()
                                                           .replace(R.id.content_frame, fragment).commit();
                                               } else {
                                                   // error in creating fragment
                                                   Log.e("Alex", "MainActivity - Error cuando se creo el fragment");
                                               }

                                           }
                                       }

        );


        btnSalir.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            try {
                                                ActivityCompat.finishAffinity(new FmLogin());
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        }
                                    }

        );





        return rootView;


    }


}

