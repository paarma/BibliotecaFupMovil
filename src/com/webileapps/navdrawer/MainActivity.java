/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webileapps.navdrawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import modelo.Autor;
import modelo.Editorial;
import modelo.Libro;
import modelo.Solicitud;
import modelo.Usuario;
import util.Configuracion;
import util.VariablesGlobales;

public class MainActivity extends SherlockFragmentActivity {

    // Declare Variables
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    MenuListAdapter mMenuAdapter;
    String[] title;
    String[] subtitle;
    int[] icon;
    Fragment fmInicioAdmin = new FmInicioAdmin();
    Fragment fmLibrosAdmin = new FmLibrosAdmin();
    Fragment fmEditorialAdmin = new FmEditorialAdmin();
    Fragment fmAutorAdmin = new FmAutorAdmin();
    Fragment fmSolicitudesAdmin = new FmSolicitudesAdmin();
    Fragment fmUsuarioAdmin = new FmUsuarioAdmin();

    //Usuario
    Fragment fmInicioUsuario = new FmInicioUsuario();
    Fragment fmlibrosUsuario;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Usuario usuarioLogueado;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Se obtiene el usuario logueado
        usuarioLogueado = (Usuario) getIntent().getExtras().getSerializable("usuarioLogueado");

        //Se obtiene el valor de multa para su respectivo mudulo
        TareaWSValorMulta tareaValorMulta = new TareaWSValorMulta();
        tareaValorMulta.execute();

        // Get the view from drawer_main.xml
        setContentView(R.layout.drawer_main);

        // Get the Title
        mTitle = mDrawerTitle = getTitle();

        /**
         * Se genera el menu segun el rol del usuario logueado
         */
        if(usuarioLogueado != null){
            //Administrador
            if(usuarioLogueado.getRol().equalsIgnoreCase("ADMINISTRADOR")){

                // Generate title
                title = new String[] { "Inicio", "Libros", "Editorial","Autor","Solicitudes","Usuarios", "Salir"};

                // Generate subtitle
                subtitle = new String[] { "Inicio Admin", "Gestion Libros", "Gestion Editorial", "Gestión Autores", "Gestión Solicitudes", "Ver reportes","Acceder a App", "Cerrar sesión"};

                // Generate icon
                icon = new int[] { R.drawable.ic_home_white_48dp, R.drawable.ic_style_white_48dp, R.drawable.ic_attach_file_white_48dp,R.drawable.ic_group_white_48dp, R.drawable.ic_book_white_48dp, R.drawable.ic_person_add_white_48dp, R.drawable.ic_exit_to_app_white_48dp };
            }

            //Usuario
            if(usuarioLogueado.getRol().equalsIgnoreCase("USUARIO")){

                // Generate title
                title = new String[]{"Inicio", "Reservar", "Mis Libros", "Buscar", "Salir"};

                // Generate subtitle
                subtitle = new String[]{"Inicio Usuario", "Reservar Libros", "Mis libros", "Busacar Libros", "Cerrar sesión"};


                // Generate icon
                icon = new int[]{R.drawable.ic_home_white_48dp, R.drawable.ic_book_white_48dp, R.drawable.ic_style_white_48dp, R.drawable.action_search, R.drawable.ic_exit_to_app_white_48dp};
            }

        }


        // Locate DrawerLayout in drawer_main.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Locate ListView in drawer_main.xml
        mDrawerList = (ListView) findViewById(R.id.listview_drawer);

        // Set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Pass string arrays to MenuListAdapter
        mMenuAdapter = new MenuListAdapter(MainActivity.this, title, subtitle,
                icon);

        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);

        // Capture listview menu item click
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                // Set the title on the action when drawer open
                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // ListView click listener in the navigation drawer
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /**
         * Se genera el menu segun el rol del usuario logueado
         */
        if(usuarioLogueado != null) {
            //Administrador
            if (usuarioLogueado.getRol().equalsIgnoreCase("ADMINISTRADOR")) {

                //Se inicializa el objeto libroSeleccionadoAdmin
                variablesGlobales.setLibroSeleccionadoAdmin(null);

                //Se inicializa el objeto usuarioSeleccionadoAdmin
                variablesGlobales.setUsuarioSeleccionadoAdmin(null);

                //Se inicializa el objeto editorialSeleccionadaAdmin
                variablesGlobales.setEditorialSeleccionadaAdmin(null);

                //Se inicializa el objeto autorSeleccionadoAdmin
                variablesGlobales.setAutorSeleccionadoAdmin(null);

                //Se inicializa el objeto libroBuscar
                variablesGlobales.setLibroBuscar(new Libro());

                //Se inicializa el objeto usuarioBuscar
                variablesGlobales.setUsuarioBuscar(new Usuario());

                //Se inicializa el objeto editorialBuscar
                variablesGlobales.setEditorialBuscar(new Editorial());

                //Se inicializa el objeto autorBuscar
                variablesGlobales.setAutorBuscar(new Autor());

                //Se inicializa el objeto solicitudBuscar
                variablesGlobales.setSolicitudBuscar(new Solicitud());
                variablesGlobales.getSolicitudBuscar().setUsuario(new Usuario());
                variablesGlobales.getSolicitudBuscar().setLibro(new Libro());

                // Locate Position
                switch (position) {
                    case 0:
                        //Inicio Admin
                        ft.replace(R.id.content_frame, fmInicioAdmin);
                        break;
                    case 1:
                        //Libro Admin
                        ft.replace(R.id.content_frame, fmLibrosAdmin);
                        break;
                    case 2:
                        //Editorial Admin
                        ft.replace(R.id.content_frame, fmEditorialAdmin);
                        break;
                    case 3:
                        //Autor Admin
                        ft.replace(R.id.content_frame, fmAutorAdmin);
                        break;
                    case 4:
                        //Reservas Admin
                        ft.replace(R.id.content_frame, fmSolicitudesAdmin);
                        break;
                    case 5:
                        //Usuarios Admin
                        ft.replace(R.id.content_frame, fmUsuarioAdmin);
                        break;
                    case 6:
                        //Salir Admin
                        finish();
                        break;
                }

            }

            //USUARIO
            if (usuarioLogueado.getRol().equalsIgnoreCase("USUARIO")) {

                fmlibrosUsuario= new FmLibrosUsuario();
                //Se inicializa el objeto libroBuscar
                variablesGlobales.setLibroBuscar(new Libro());

                //Se inicializa el objeto solicitudBuscar
                variablesGlobales.setSolicitudBuscar(new Solicitud());

                //Se inicializa el objeto solicitudBuscar
                variablesGlobales.setSolicitudBuscar(new Solicitud());
                variablesGlobales.getSolicitudBuscar().setUsuario(new Usuario());
                variablesGlobales.getSolicitudBuscar().setLibro(new Libro());
                //Para listar MisLibros. para el caso de usuario.
                variablesGlobales.getSolicitudBuscar().getUsuario().setIdUsuario(
                        variablesGlobales.getUsuarioLogueado().getIdUsuario());

                // Locate Position
                switch (position) {
                    case 0:
                        ft.replace(R.id.content_frame, fmInicioUsuario);
                        break;
                    case 1:
                        variablesGlobales.setOpcionMenu(0); //Reservar
                        ft.replace(R.id.content_frame, fmlibrosUsuario);
                        break;
                    case 2:
                        variablesGlobales.setOpcionMenu(1); //Mis Libros
                        ft.replace(R.id.content_frame, fmlibrosUsuario);
                        break;
                    case 3:
                        variablesGlobales.setOpcionMenu(2); //Buscar Libros
                        //Se setea la variable "setBuscarLibroDesdeVista" para el caso de Buscar Liro
                        //libros desde la opcion del Menu. Esto con el fin de que el
                        //resultado de la busqueda lo dirija a la pantalla "Reservar"
                        variablesGlobales.setBuscarLibroDesdeVista("reservar");
                        ft.replace(R.id.content_frame, fmlibrosUsuario);
                        break;
                    case 4:
                        finish();
                        break;
                }

            }

        }

        ft.commit();
        mDrawerList.setItemChecked(position, true);
        // Get the title followed by the position
        setTitle(title[position]);
        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            super.onBackPressed();
        }
    }

    //Botones al pie de la pantalla (Usuario)
    /**
     * Boton Buscar Libro ubicado al pie de las pantallas reessrvar y misLibros del rol Usuario
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarLibroUser(View view){
        String vista = view.getTag().toString();
        Log.i("MainActivity.java", "************** Pulsando boton Buscar Libro desde: "+vista);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fmlibrosUsuario= new FmLibrosUsuario();

        variablesGlobales.setOpcionMenu(2); //Buscar Libros
        variablesGlobales.setBuscarLibroDesdeVista(vista);
        ft.replace(R.id.content_frame, fmlibrosUsuario);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar libros y direccionar a la pagina que invoco la busqueda
     * ya sea por Reservar o MisLibros del rol Usuario
     * @param view
     */
    public void buscarLibroUser(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fmlibrosUsuario= new FmLibrosUsuario();

        //Para el caso del llamado desde la vista Mis Libros
        if(variablesGlobales != null &&
                variablesGlobales.getBuscarLibroDesdeVista() != null &&
                variablesGlobales.getBuscarLibroDesdeVista().equalsIgnoreCase("misLibros")){
            variablesGlobales.setOpcionMenu(1); //Mis Libros

        }else{
            //En caso contrario se envia a la vista de Reservar
            variablesGlobales.setOpcionMenu(0); //Reservar
        }

        //Se capturan los parametros para la busqueda del libro
        FmBuscarLibroUsuario.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmlibrosUsuario);
        ft.commit();
    }

    //Botones al pie de la pantalla (Admin)
    /**
     * Boton Buscar Libro ubicado al pie de las pantallas listadoLibros del rol Admin
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarLibroAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //Se inicializa el objeto libroSeleccionadoAdmin
        variablesGlobales.setLibroSeleccionadoAdmin(null);

        variablesGlobales.setOpcionMenu(2); //Buscar Libros
        fmLibrosAdmin = new FmLibrosAdmin();

        ft.replace(R.id.content_frame, fmLibrosAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar libros y direccionar a la pagina que invoco la busqueda
     * ya sea por Reservar o MisLibros del rol Usuario
     * @param view
     */
    public void buscarLibroAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(1); //Listado Libros
        fmLibrosAdmin = new FmLibrosAdmin();

        //Se capturan los parametros para la busqueda del libro
        FmBuscarLibrosAdmin.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmLibrosAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de cargar los datos de un determinado libro para ser actualizado.
     * @param view
     */
    public void actualizarLibroAdmin(View view){

        if(variablesGlobales.getLibroSeleccionadoAdmin() != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            variablesGlobales.setOpcionMenu(0); //Crear-Editar Libro
            fmLibrosAdmin = new FmLibrosAdmin();

            ft.replace(R.id.content_frame, fmLibrosAdmin);
            ft.commit();
        }else{
            Toast.makeText(this, "Seleccione un libro", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Boton buscar Usuario, ubicado al pie de las pantallas listadoUsuarios Admin
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarUsuarioAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //Se inicializa el objeto usuarioSeleccionadoAdmin
        variablesGlobales.setUsuarioSeleccionadoAdmin(null);

        variablesGlobales.setOpcionMenu(2); //Buscar Libros
        fmUsuarioAdmin = new FmUsuarioAdmin();

        ft.replace(R.id.content_frame, fmUsuarioAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar usuario y direccionar a la pagina lista usuarios
     * @param view
     */
    public void buscarUsuarioAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(1); //Listado Usuarios
        fmUsuarioAdmin = new FmUsuarioAdmin();

        //Se capturan los parametros para la busqueda del usuario
        FmBuscarUsuarioAdmin.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmUsuarioAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de cargar los datos de un determinado usuario para ser actualizado.
     * @param view
     */
    public void actualizarUsuarioAdmin(View view){

        if(variablesGlobales.getUsuarioSeleccionadoAdmin() != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            variablesGlobales.setOpcionMenu(0); //Crear-Editar Usuario
            fmUsuarioAdmin = new FmUsuarioAdmin();

            ft.replace(R.id.content_frame, fmUsuarioAdmin);
            ft.commit();
        }else{
            Toast.makeText(this, "Seleccione un usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Boton buscar Editorial, ubicado al pie de las pantallas listadoEditoriales Admin
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarEditorialAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //Se inicializa el objeto editorialSeleccionadaAdmin
        variablesGlobales.setEditorialSeleccionadaAdmin(null);

        variablesGlobales.setOpcionMenu(2); //Buscar Editoriales
        fmEditorialAdmin = new FmEditorialAdmin();

        ft.replace(R.id.content_frame, fmEditorialAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar editoriales y direccionar a la pagina lista editoriales
     * @param view
     */
    public void buscarEditorialAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(1); //Listado Editoriales
        fmEditorialAdmin = new FmEditorialAdmin();

        //Se capturan los parametros para la busqueda de la editorial
        FmBuscarEditorialAdmin.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmEditorialAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de cargar los datos de una editorial para ser actualizada
     * @param view
     */
    public void actualizarEditorialAdmin(View view){

        if(variablesGlobales.getEditorialSeleccionadaAdmin() != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            variablesGlobales.setOpcionMenu(0); //Crear-Editar Editorial
            fmEditorialAdmin = new FmEditorialAdmin();

            ft.replace(R.id.content_frame, fmEditorialAdmin);
            ft.commit();
        }else{
            Toast.makeText(this, "Seleccione una editorial", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Boton buscar Autor, ubicado al pie de las pantallas listadoAutores Admin
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarAutorAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //Se inicializa el objeto autorSeleccionadoAdmin
        variablesGlobales.setAutorSeleccionadoAdmin(null);

        variablesGlobales.setOpcionMenu(2); //Buscar Autor
        fmAutorAdmin = new FmAutorAdmin();

        ft.replace(R.id.content_frame, fmAutorAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar autores y direccionar a la pagina lista autores
     * @param view
     */
    public void buscarAutorAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(1); //Listado Autores
        fmAutorAdmin = new FmAutorAdmin();

        //Se capturan los parametros para la busqueda del autor
        FmBuscarAutorAdmin.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmAutorAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de cargar los datos de un autor para ser actualizado
     * @param view
     */
    public void actualizarAutorAdmin(View view){

        if(variablesGlobales.getAutorSeleccionadoAdmin() != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            variablesGlobales.setOpcionMenu(0); //Crear-Editar Autor
            fmAutorAdmin = new FmAutorAdmin();

            ft.replace(R.id.content_frame, fmAutorAdmin);
            ft.commit();
        }else{
            Toast.makeText(this, "Seleccione un autor", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Boton buscar Solicitud, ubicado al pie de las pantallas listadoSolicitudes Admin
     * y permite dirigir a la pantalla de buscar
     * @param view
     */
    public void verBuscarSolicitudAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(1); //Buscar Solicitudes
        fmSolicitudesAdmin = new FmSolicitudesAdmin();

        ft.replace(R.id.content_frame, fmSolicitudesAdmin);
        ft.commit();
    }

    /**
     * Funcion encargada de buscar solicitudes y direccionar a la pagina lista autores
     * @param view
     */
    public void buscarSolicitudAdmin(View view){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        variablesGlobales.setOpcionMenu(0); //Listado Solicitudes
        fmSolicitudesAdmin = new FmSolicitudesAdmin();

        //Se capturan los parametros para la busqueda de la solicitud
        FmBuscarSolicitudesAdmin.capturarObjetoBusqueda();

        ft.replace(R.id.content_frame, fmSolicitudesAdmin);
        ft.commit();
    }

    /**
     * Tarea buscarValorMulta
     */
    private class TareaWSValorMulta extends AsyncTask<String,Integer,Boolean> {
        Configuracion conf = new Configuracion();

        private final String SOAP_ACTION = conf.getUrl()+"/buscarValorMulta";
        private final String METHOD_NAME = "buscarValorMulta";
        private final String NAMESPACE = conf.getNamespace();
        private final String URL = conf.getUrl();
        private int valorMulta = 0;
        boolean resultadoTarea = false;

        protected Boolean doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

                if (rs != null)
                {
                    for (SoapObject multaSoap : rs)
                    {
                        if(multaSoap.getProperty("VALOR").toString() != null) {
                            valorMulta = Integer.parseInt(multaSoap.getProperty("VALOR").toString());
                            resultadoTarea = true;
                        }
                        break;
                    }
                }
            }catch (Exception e){
                Log.e("MainAct ", "xxx Error TareaWSValorMulta: "+e.getMessage());
            }
            return resultadoTarea;
        }

        protected void onPostExecute(Boolean result) {
            if (resultadoTarea) {
                variablesGlobales.setValorMulta(valorMulta);
            }else{
              Log.d("MainAct","XXX Error obteniendo el valor de la multa ");
            }
        }
    }

    /**
     * Metodo especifico que captura el evento al pulsar botones fisicos del dispositivo.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        switch(keyCode){
            case KeyEvent.KEYCODE_BACK: //Botón "Atras".

                new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Está seguro que desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            finish();
                        }
                    }).show();

                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}