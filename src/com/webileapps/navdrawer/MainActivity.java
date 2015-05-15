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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import modelo.Usuario;
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
    Fragment fmReportesAdmin = new FmReportesAdmin();
    Fragment fmSolicitudesAdmin = new FmSolicitudesAdmin();

    //Estudiante
    Fragment fmInicioUsuario = new FmInicioUsuario();
    Fragment fmlibrosUsuario= new FmLibrosUsuario();

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Usuario usuarioLogueado;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Se obtiene el usuario logueado
        usuarioLogueado = (Usuario) getIntent().getExtras().getSerializable("usuarioLogueado");


        // Get the view from drawer_main.xml
        setContentView(R.layout.drawer_main);

        // Get the Title
        mTitle = mDrawerTitle = getTitle();

        /**
         * Se genera el menu segun el rol del usuario logueado
         */
        if(usuarioLogueado != null){
            //Administrador
            if(usuarioLogueado.getRol().equalsIgnoreCase("ADMIN")){

                // Generate title
                title = new String[] { "Inicio", "Libros", "Editorial","Autor","Solicitudes","Reportes"};

                // Generate subtitle
                subtitle = new String[] { "Inicio Admin", "Gestion Libros", "Gestion Editorial", "Gestión Autores", "Gestión Solicitudes", "Ver reportes"};

                // Generate icon
                icon = new int[] { R.drawable.ic_home_white_48dp, R.drawable.ic_style_white_48dp, R.drawable.ic_attach_file_white_48dp,R.drawable.ic_group_white_48dp, R.drawable.ic_book_white_48dp, R.drawable.ic_assessment_white_48dp };
            }

            //Estudiante
            if(usuarioLogueado.getRol().equalsIgnoreCase("EST")){

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
            if (usuarioLogueado.getRol().equalsIgnoreCase("ADMIN")) {
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
                        //Autor Admin
                        ft.replace(R.id.content_frame, fmSolicitudesAdmin);
                        break;
                    case 5:
                        //Autor Admin
                        ft.replace(R.id.content_frame, fmReportesAdmin);
                        break;
                }

            }

            //ESTUDIANTE
            if (usuarioLogueado.getRol().equalsIgnoreCase("EST")) {
                fmlibrosUsuario= new FmLibrosUsuario();
                // Locate Position
                switch (position) {
                    case 0:
                        ft.replace(R.id.content_frame, fmInicioUsuario);
                        break;
                    case 1:
                        variablesGlobales.setOpcionMenu(0);
                        ft.replace(R.id.content_frame, fmlibrosUsuario);
                        break;
                    case 2:
                        variablesGlobales.setOpcionMenu(1);
                        ft.replace(R.id.content_frame, fmlibrosUsuario);
                        break;
                    case 3:
                        variablesGlobales.setOpcionMenu(2);
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

}