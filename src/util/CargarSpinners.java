package util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.webileapps.navdrawer.R;

import java.util.ArrayList;
import java.util.List;

import modelo.Area;
import modelo.Ciudad;
import modelo.Editorial;
import modelo.Pais;
import modelo.Sede;

/**
 * Created by pablo on 26/06/15.
 * Clase encargada de cargar los diversos Spinners encontrados en el sistema
 */
public class CargarSpinners {

    static VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    public static void loadDatos(FragmentActivity fActivity,
                                 String nombreClase ,
                                 Spinner spinner, int idFiltro){
        TareaWsCargarDatosSpinner tareaCargarDatosSpinner = new TareaWsCargarDatosSpinner();

        tareaCargarDatosSpinner.setfActivity(fActivity);
        tareaCargarDatosSpinner.setNombreClase(nombreClase);
        tareaCargarDatosSpinner.setSpinner(spinner);
        tareaCargarDatosSpinner.setIdFiltro(idFiltro);

        tareaCargarDatosSpinner.execute();
    }

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    //////////////////////////////////////////////////// tareas
    //Tarea Asíncrona para llamar al WS de consulta en segundo plano

    /**
     * Tarea WS encargada de listar los datos para los spinners
     */
    private static class TareaWsCargarDatosSpinner extends AsyncTask<String,Integer,Boolean> {

        /**
         * FragmentActivity de la cual proceden los spinner
         */
        private FragmentActivity fActivity;

        /**
         * Nombre de cla clase de la cual se cargara el spinner
         */
        private String nombreClase;

        /**
         * El spinner al cual se le cargaran los datos
         */
        private Spinner spinner;

        /**
         * Parametro en el caso de filtrar por algun ID
         */
        private int idFiltro = 0;

        /**
         * Listado de datos que se cargaran al spinner
         */
        private List<Editorial> listaEditoriales = new ArrayList<Editorial>();
        private List<Area> listaAreas = new ArrayList<Area>();
        private List<Sede> listaSedes = new ArrayList<Sede>();
        private List<Pais> listaPaises = new ArrayList<Pais>();
        private List<Ciudad> listaCiudades = new ArrayList<Ciudad>();

        boolean resultadoTarea = true;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                //Se cargan los datos de Editorial
                if(nombreClase.equals(Editorial.class.getSimpleName())){
                    listaEditoriales = tareasGenerales.listarEditoriales(new Editorial());
                    Log.i("cargaSpinner", ">>>>>>>>>>> Tamaño lista editoriales: " + listaEditoriales.size());
                }

                //Se cargan los datos de Area
                if(nombreClase.equals(Area.class.getSimpleName())){
                    listaAreas = tareasGenerales.listarAreas();
                    Log.i("cargaSpinner", ">>>>>>>>>>> Tamaño lista Areas: " + listaAreas.size());
                }

                //Se cargan los datos de Sede
                if(nombreClase.equals(Sede.class.getSimpleName())){
                    listaSedes = tareasGenerales.listarSedes();
                    Log.i("cargaSpinner", ">>>>>>>>>>> Tamaño lista Sedes: " + listaSedes.size());
                }

                 //Se cargan los datos de Pais
                if(nombreClase.equals(Pais.class.getSimpleName())){
                    listaPaises = tareasGenerales.listarPaises();
                    Log.i("cargaSpinner", ">>>>>>>>>>> Tamaño lista Paises: " + listaPaises.size());
                }

                //Se cargan los datos de Ciudad
                if(nombreClase.equals(Ciudad.class.getSimpleName())){
                    listaCiudades = tareasGenerales.listarCiudades(idFiltro);
                    Log.i("cargaSpinner", ">>>>>>>>>>> Tamaño lista Ciudades: " + listaCiudades.size());
                }

            }catch (Exception e){
                resultadoTarea = false;
                Log.d("cargaSpinner ", "xxx Error TareaWsCargarDatosSpinner: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){

                //spinnerEditorial.setAdapter(adapter);
                //Se setea el adapter agregando el item "Seleccione..." (NothingSelectedSpinnerAdapter)

                try {

                    if (nombreClase.equals(Editorial.class.getSimpleName())) {
                        ArrayAdapter<Editorial> adapter = new ArrayAdapter<Editorial>(fActivity,
                                R.layout.spinner_item, listaEditoriales);

                        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                                adapter, R.layout.contact_spinner_nothing_selected,
                                fActivity));

                        //Se selecciona el valor por defecto en caso de cargar el spinner
                        //con un libro seleccionado previamente. (Editar Libro)
                        if (variablesGlobales.getLibroSeleccionadoAdmin() != null
                                && variablesGlobales.getLibroSeleccionadoAdmin().getEditorial() != null) {
                            spinner.setSelection(Utilidades.getIndexSpinner(spinner,
                                    variablesGlobales.getLibroSeleccionadoAdmin().getEditorial().getDescripcion()));
                        }

                    }

                    if (nombreClase.equals(Area.class.getSimpleName())) {
                        ArrayAdapter<Area> adapter = new ArrayAdapter<Area>(fActivity,
                                R.layout.spinner_item, listaAreas);

                        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                                adapter, R.layout.contact_spinner_nothing_selected,
                                fActivity));

                        //Se selecciona el valor por defecto en caso de cargar el spinner
                        //con un libro seleccionado previamente. (Editar Libro)
                        if (variablesGlobales.getLibroSeleccionadoAdmin() != null
                                && variablesGlobales.getLibroSeleccionadoAdmin().getArea() != null) {
                            spinner.setSelection(Utilidades.getIndexSpinner(spinner,
                                    variablesGlobales.getLibroSeleccionadoAdmin().getArea().getDescripcion()));
                        }
                    }

                    if (nombreClase.equals(Sede.class.getSimpleName())) {
                        ArrayAdapter<Sede> adapter = new ArrayAdapter<Sede>(fActivity,
                                R.layout.spinner_item, listaSedes);

                        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                                adapter, R.layout.contact_spinner_nothing_selected,
                                fActivity));

                        //Se selecciona el valor por defecto en caso de cargar el spinner
                        //con un libro seleccionado previamente. (Editar Libro)
                        if (variablesGlobales.getLibroSeleccionadoAdmin() != null
                                && variablesGlobales.getLibroSeleccionadoAdmin().getSede() != null) {
                            spinner.setSelection(Utilidades.getIndexSpinner(spinner,
                                    variablesGlobales.getLibroSeleccionadoAdmin().getSede().getDescripcion()));
                        }
                    }

                    if (nombreClase.equals(Pais.class.getSimpleName())) {
                        ArrayAdapter<Pais> adapter = new ArrayAdapter<Pais>(fActivity,
                                R.layout.spinner_item, listaPaises);

                        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                                adapter, R.layout.contact_spinner_nothing_selected,
                                fActivity));

                        //Se selecciona el valor por defecto en caso de cargar el spinner
                        //con un libro seleccionado previamente. (Editar Libro)
                        if (variablesGlobales.getLibroSeleccionadoAdmin() != null
                                && variablesGlobales.getLibroSeleccionadoAdmin().getCiudad() != null) {
                            spinner.setSelection(Utilidades.getIndexSpinner(spinner,
                                    variablesGlobales.getLibroSeleccionadoAdmin().getCiudad().getPais().getNombre()));
                        }
                    }

                    if (nombreClase.equals(Ciudad.class.getSimpleName())) {
                        ArrayAdapter<Ciudad> adapter = new ArrayAdapter<Ciudad>(fActivity,
                                R.layout.spinner_item, listaCiudades);

                        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                                adapter, R.layout.contact_spinner_nothing_selected,
                                fActivity));

                        //Se selecciona el valor por defecto en caso de cargar el spinner
                        //con un libro seleccionado previamente. (Editar Libro)
                        if (variablesGlobales.getLibroSeleccionadoAdmin() != null
                                && variablesGlobales.getLibroSeleccionadoAdmin().getCiudad() != null) {
                            spinner.setSelection(Utilidades.getIndexSpinner(spinner,
                                    variablesGlobales.getLibroSeleccionadoAdmin().getCiudad().getNombre()));
                        }
                    }

                }catch (Exception e){
                    Log.e("Spiners","XXX Error cargando spinners: "+e.getMessage());
                }

            }else{
                Log.e("Spinners","XXX Error cargando Spinner: "+nombreClase);
            }
        }

        //Getters and Setters
        public void setfActivity(FragmentActivity fActivity) {
            this.fActivity = fActivity;
        }

        public void setNombreClase(String nombreClase) {
            this.nombreClase = nombreClase;
        }

        public void setSpinner(Spinner spinner) {
            this.spinner = spinner;
        }

        public void setIdFiltro(int idFiltro) {
            this.idFiltro = idFiltro;
        }
    }

}
