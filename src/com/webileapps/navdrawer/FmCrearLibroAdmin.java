package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Area;
import modelo.Editorial;
import modelo.Libro;
import modelo.Sede;
import util.CargarSpinners;
import util.Configuracion;
import util.Utilidades;
import util.VariablesGlobales;

public class FmCrearLibroAdmin extends SherlockFragment {

    EditText titulo, isbn, codTopografico, temas, paginas, valor, radicado, serie;
    Spinner spinnerEstado, spinnerAdquisicion, spinnerEditorial, spinnerArea, spinnerSede, ciudad;

    DatePicker dpickerAnioLibro;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private final static String[] tipoCiudad = { "Seleccione..", "Ciudad_1", "Ciudad_2"};

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fm_crear_libro_adminro_admin.xml
		View view = inflater.inflate(R.layout.fm_crear_libro_admin, container, false);

        inicializarComponentes(view);

        ImageButton btnCrearLibro = (ImageButton) view.findViewById(R.id.btnGuardarLibroAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarLibroAdmin);

        btnCrearLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrearLibro", ">>>>>>>>>>>>>>>>>>>> pulsando boton crear libro");
                TareaWsGuardarLibro tareaGuardarLibro = new TareaWsGuardarLibro();
                tareaGuardarLibro.execute();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });

		return view;
	}

    /**
     * Metodo encardado de limpiar los campos del formulario
     */
    public void limpiarCampos(){

        titulo.getText().clear();
        isbn.getText().clear();
        codTopografico.getText().clear();
        temas.getText().clear();
        paginas.getText().clear();

        valor.getText().clear();
        radicado.getText().clear();
    }

    public void inicializarComponentes(View view){

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);
        temas = (EditText) view.findViewById(R.id.editTextTemas);
        paginas = (EditText) view.findViewById(R.id.editTextPaginas);
        serie = (EditText) view.findViewById(R.id.editTextSerie);
        spinnerEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        spinnerAdquisicion = (Spinner) view.findViewById(R.id.spinnerAdquisicion);
        spinnerEditorial = (Spinner) view.findViewById(R.id.spinnerEditorial);
        spinnerArea = (Spinner) view.findViewById(R.id.spinnerArea);
        spinnerSede = (Spinner) view.findViewById(R.id.spinnerSede);
        ciudad = (Spinner) view.findViewById(R.id.spinnerCiudad);
        valor = (EditText) view.findViewById(R.id.editTextValor);
        radicado = (EditText) view.findViewById(R.id.editTextRadicado);

        cargarSpinners();

        //Se cargan algunos spinners con los datos del archivo arrays.xml
        ArrayAdapter adapterEstado = ArrayAdapter.createFromResource(getActivity(), R.array.estados_crear_libro, R.layout.spinner_item);
        adapterEstado.setDropDownViewResource(R.layout.spinner_item);
        spinnerEstado.setAdapter(adapterEstado);

        ArrayAdapter adapterAdquisicion = ArrayAdapter.createFromResource(getActivity(), R.array.adquisicion_crear_libro, R.layout.spinner_item);
        adapterAdquisicion.setDropDownViewResource(R.layout.spinner_item);
        spinnerAdquisicion.setAdapter(adapterAdquisicion);

        ArrayAdapter adapterCiudad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, tipoCiudad);
        Spinner spCiudad = (Spinner) view.findViewById(R.id.spinnerCiudad);
        spCiudad.setAdapter(adapterCiudad);

        ////////////////Se referencia el datepicker (Año Libro)
        //y se ocultan los campos dia y mes para solo mostrar el año.
        dpickerAnioLibro = (DatePicker) view.findViewById(R.id.datePickerAnioLibro);
        try {
            Field f[] = dpickerAnioLibro.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner") ||
                    field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner") ) {
                    field.setAccessible(true);
                    Object yearPicker = new Object();
                    yearPicker = field.get(dpickerAnioLibro);
                    ((View) yearPicker).setVisibility(View.GONE);
                }
            }
        }
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
        /////////////////Fin carga datepicker

    }

    /**
     * Carga los spinners contenidos en la creacion del libro.
     */
    public void cargarSpinners(){

        CargarSpinners.loadDatos(getActivity(), Editorial.class.getSimpleName(), spinnerEditorial);
        CargarSpinners.loadDatos(getActivity(), Area.class.getSimpleName(), spinnerArea);
        CargarSpinners.loadDatos(getActivity(), Sede.class.getSimpleName(), spinnerSede);
    }

    /**
     * Metodo que carga los datos de un determinado libro previamente seleccionado.
     */
    public void cargarDatosLibroSeleccionado(){
        if(variablesGlobales.getLibroSeleccionadoAdmin() != null) {

            titulo.setText(variablesGlobales.getLibroSeleccionadoAdmin().getTitulo());
            isbn.setText(variablesGlobales.getLibroSeleccionadoAdmin().getIsbn());
            codTopografico.setText(variablesGlobales.getLibroSeleccionadoAdmin().getCodigoTopografico());
            temas.setText(variablesGlobales.getLibroSeleccionadoAdmin().getTemas());
            paginas.setText(String.valueOf(variablesGlobales.getLibroSeleccionadoAdmin().getPaginas()));
            serie.setText(variablesGlobales.getLibroSeleccionadoAdmin().getSerie());
            valor.setText(String.valueOf(variablesGlobales.getLibroSeleccionadoAdmin().getValor()));
            radicado.setText(variablesGlobales.getLibroSeleccionadoAdmin().getRadicado());


            //Se cargan los spinners con su respectivo valor.
            spinnerEstado.setSelection(Utilidades.getIndexSpinner(spinnerEstado, variablesGlobales.getLibroSeleccionadoAdmin().getEstado()));
            spinnerAdquisicion.setSelection(Utilidades.getIndexSpinner(spinnerAdquisicion, variablesGlobales.getLibroSeleccionadoAdmin().getAdquisicion()));

            //Para el caso de la editorial, area y sede...(Spinners con objetos) se valida y cargan en la funcion cargarSpinners().
            //spinnerEditorial.setSelection(...);

            //Pendiente cargar spinner ciudad
            //Referncia a spinnerCiudad ciudad = (Spinner) view.findViewById(R.id.spinnerCiudad);

            //Se carga el año del libro.
            // El dia y el mes se cargan por defecto con valores de 1 ya que solo nos interesa el año.
            if(variablesGlobales.getLibroSeleccionadoAdmin().getAnio() != 0){
                dpickerAnioLibro.updateDate(variablesGlobales.getLibroSeleccionadoAdmin().getAnio(),1,1);
            }
        }
    }

    /**
     * Tarea encargada de guardar un libro
     */
    private class TareaWsGuardarLibro extends AsyncTask<String,Integer,Boolean> {

        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            Libro lib = new Libro();
            lib.setTitulo(titulo.getText().toString());

            if(valor.getText().toString().trim().length() > 0){
                lib.setValor(Integer.parseInt(valor.getText().toString()));
            }

            if(paginas.getText().toString().trim().length() > 0){
                lib.setPaginas(Integer.parseInt(paginas.getText().toString()));
            }

            lib.setAnio(dpickerAnioLibro.getYear());
            lib.setSerie(serie.getText().toString());
            lib.setIsbn(isbn.getText().toString());
            lib.setCodigoTopografico(codTopografico.getText().toString());

            if (!spinnerEstado.getSelectedItem().toString().equals("Seleccione...")) {
                lib.setEstado(spinnerEstado.getSelectedItem().toString());
            }

            if (!spinnerAdquisicion.getSelectedItem().toString().equals("Seleccione...")) {
                lib.setAdquisicion(spinnerAdquisicion.getSelectedItem().toString());
            }

            lib.setRadicado(radicado.getText().toString());
            lib.setFechaIngreso(new Date());
            lib.setTemas(temas.getText().toString());
            //lib.setDisponibilidad("SI");
            lib.setIdUsuario(variablesGlobales.getUsuarioLogueado().getIdUsuario());

            Editorial editorialSeleccionada = (Editorial) spinnerEditorial.getSelectedItem();
            if(editorialSeleccionada != null){
                lib.setEditorial(editorialSeleccionada);
            }

            Area arealSeleccionada = (Area) spinnerArea.getSelectedItem();
            if(arealSeleccionada != null){
                lib.setArea(arealSeleccionada);
            }

            Sede sedeSeleccionada = (Sede) spinnerSede.getSelectedItem();
            if(sedeSeleccionada != null){
                lib.setSede(sedeSeleccionada);
            }

            if (ciudad.getSelectedItem().toString().equals("Ciudad_1")) {
                lib.setIdCiudad(1);
            }

            if (ciudad.getSelectedItem().toString().equals("Ciudad_2")) {
                lib.setIdCiudad(2);
            }

            try {

                /**
                 * Se setea el idLibro en caso de ser edicion, de lo contrario este
                 * quedara con valor de 0.
                 * Funcionalidad especifica validar si se guarda un nuevo libro o se edita uno existente.
                 */
            if(variablesGlobales.getLibroSeleccionadoAdmin() != null){
                lib.setIdLibro(variablesGlobales.getLibroSeleccionadoAdmin().getIdLibro());
            }

            resultadoTarea = guardarLibro(lib);

            }catch (Exception e){
                resultadoTarea = false;
                Log.e("FmCrearLibro ", "xxx Error TareaWsGuardarLibro: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                Toast.makeText(getActivity(), "Libro almacenado con exito", Toast.LENGTH_LONG).show();
                limpiarCampos();
                variablesGlobales.setLibroSeleccionadoAdmin(null);
            }else{
                Toast.makeText(getActivity(), "Error almacenando el libro", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo encargado de guardar un libro en la BD
     * @param libro Libro el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarLibro(Libro libro){

        Configuracion conf = new Configuracion();
        final String SOAP_ACTION = conf.getUrl()+"/guardarLibro";
        final String METHOD_NAME = "guardarLibro";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idLibro",libro.getIdLibro());
        request.addProperty("titulo",libro.getTitulo());
        request.addProperty("valor",libro.getValor());
        request.addProperty("adquisicion",libro.getAdquisicion());
        request.addProperty("estado",libro.getEstado());
        request.addProperty("isbn",libro.getIsbn());
        request.addProperty("radicado",libro.getRadicado());

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        request.addProperty("fechaIngreso", formatoFecha.format(libro.getFechaIngreso()));

        request.addProperty("codTopografico",libro.getCodigoTopografico());
        request.addProperty("serie",libro.getSerie());

        if(libro.getSede() != null){
            request.addProperty("idSede",libro.getSede().getIdSede());
        }else{
            request.addProperty("idSede","");
        }

        if(libro.getEditorial() != null){
            request.addProperty("idEditorial",libro.getEditorial().getIdEditorial());
        }else{
            request.addProperty("idEditorial","");
        }

        if(libro.getArea() != null) {
            request.addProperty("idArea", libro.getArea().getIdArea());
        }else{
            request.addProperty("idArea","");
        }

        request.addProperty("anio",libro.getAnio());
        request.addProperty("temas",libro.getTemas());
        request.addProperty("paginas",libro.getPaginas());
        request.addProperty("disponibilidad",libro.getDisponibilidad());
        request.addProperty("idUsuario",libro.getIdUsuario()); //Usuario logueado
        request.addProperty("idCiudad",libro.getIdCiudad());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("GuardandoLibro","*********************** resultado guardarLibro: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("GuardandoLibro", "xxx Error guardarLibro(): " + e.getMessage());
        }

        return resultado;
    }

    /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////
    /**
     * Sobreescritura del metodo onResume
     * (se agrega la funcionalidad para recargar los datos generales de la clase)
     */
    @Override
    public void onResume()
    {
        super.onResume();

        //Si existe un libro selecciondo previamente por el admin, se cargan los datos
        cargarDatosLibroSeleccionado();
    }

}
