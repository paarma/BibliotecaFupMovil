package com.webileapps.navdrawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import util.VariablesGlobales;

public class FmListaLibrosReservarUsuario extends SherlockFragment {

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fm_crear_libro_adminro_admin.xml
		View view = inflater.inflate(R.layout.fm_lista_libros_reservar_usuario, container, false);

/*       ImageButton btnBuscarLibroReserva = (ImageButton) view.findViewById(R.id.btnBuscarLibroReservaUser);

        //Boton buscar desde la pantalla de reservar
        btnBuscarLibroReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Reservar", "************** Pulsando boton Buscar desde reservar");

                SherlockFragment fragment = null;
                fragment = new FmLibrosUsuario();

                //Se setea la variable global para cargar la vista de Libros (Buscar)
                variablesGlobales.setOpcionMenu(2);

                if (fragment != null) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment).commit();
                } else {
                    // error in creating fragment
                    Log.e("Reservar", "XXX - Error cuando se creo el fragment buscarLibros");
                }

            }
        });*/

		return view;
	}
}
