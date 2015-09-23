package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Area;
import modelo.Autor;
import modelo.Ciudad;
import modelo.Editorial;
import modelo.Libro;
import modelo.LibroAutor;
import modelo.Pais;
import modelo.Sede;
import util.CargarSpinners;
import util.Configuracion;
import util.TareasGenerales;
import util.Utilidades;
import util.VariablesGlobales;

public class FmCrearLibroAdmin extends SherlockFragment {

    EditText titulo, isbn, codTopografico, temas, paginas, valor, radicado, serie, cantidad;
    Spinner spinnerEstado, spinnerAdquisicion, spinnerEditorial, spinnerArea,
            spinnerSede, spinnerCiudad, spinnerPais, spinnerAutor;

    DatePicker dpickerAnioLibro;

    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private LinearLayout linearListViewAutores, linearCantidadLibro;
    private ArrayList<Autor> listaAutores;
    ImageButton btnAgregarAutor, btnEliminarAutor;
    LayoutInflater inflaterAux;
    int indexAutorSeleccionado = -1;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    private String idAutoresConcatenados;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fm_crear_libro_adminro_admin.xml
        View view = inflater.inflate(R.layout.fm_crear_libro_admin, container, false);
        inflaterAux = inflater;

        Log.i("CREAR_LIB", "************************************** INICIO CREAR LIBRO");

        //Dialog para agregar autores (Antes de cargar los spinners)
        crearDialogAutores();

        //Creamos un objeto de AlertDialog
        alertDialog = alertDialogBuilder.create();

        //Se inicializan componentes y se cargan los spinners
        inicializarComponentes(view);

        ImageButton btnCrearLibro = (ImageButton) view.findViewById(R.id.btnGuardarLibroAdmin);
        ImageButton btnCancelar = (ImageButton) view.findViewById(R.id.btnCancelarLibroAdmin);

        btnCrearLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CrearLibro", ">>>>>>>>>>>>>>>>>>>> pulsando boton crear libro");

                validarGuardar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
                variablesGlobales.setLibroSeleccionadoAdmin(null);
            }
        });

        //Evento al seleccionar el spinner Pais
        //Se cargan las ciudades segun el pais seleccionado
        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerPais.getSelectedItem() != null){

                    try{
                        Pais pais = (Pais) spinnerPais.getSelectedItem();
                        CargarSpinners.loadDatos(getActivity(), Ciudad.class.getSimpleName(), spinnerCiudad,
                                pais.getIdPais());
                    }catch (Exception e){
                        Log.e("CrearLibro","XXX Error cargando ciudades: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //////////////////////Autores
        listaAutores = new ArrayList<Autor>();

        btnAgregarAutor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("CrearLibro",">>>>>>>>>>> btnAgregarAutor");
                // Lanzamos el dialog
                alertDialog.show();
            }
        });

        btnEliminarAutor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("CrearLibro", ">>>>>>>>>>> btnEliminarAutor: "+indexAutorSeleccionado);
                if(indexAutorSeleccionado != -1){
                    listaAutores.remove(indexAutorSeleccionado);
                    recargarLinearLayoutAutores();
                }
            }
        });
        //////////////////////////Fin Autores


        //Limpia validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                titulo.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        isbn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                isbn.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        codTopografico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                codTopografico.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        //Fin limpiar validaciones de campos requeridos
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////

        return view;
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
        spinnerPais = (Spinner) view.findViewById(R.id.spinnerPais);
        spinnerCiudad = (Spinner) view.findViewById(R.id.spinnerCiudad);
        valor = (EditText) view.findViewById(R.id.editTextValor);
        radicado = (EditText) view.findViewById(R.id.editTextRadicado);
        cantidad = (EditText) view.findViewById(R.id.editTextCantidad);

        //Por defecto se carga la cantidad en 1
        cantidad.setText("1");

        cargarSpinners();

        //Se cargan algunos spinners con los datos del archivo arrays.xml
        ArrayAdapter adapterEstado = ArrayAdapter.createFromResource(getActivity(), R.array.estados_crear_libro, R.layout.spinner_item);
        adapterEstado.setDropDownViewResource(R.layout.spinner_item);
        spinnerEstado.setAdapter(adapterEstado);

        ArrayAdapter adapterAdquisicion = ArrayAdapter.createFromResource(getActivity(), R.array.adquisicion_crear_libro, R.layout.spinner_item);
        adapterAdquisicion.setDropDownViewResource(R.layout.spinner_item);
        spinnerAdquisicion.setAdapter(adapterAdquisicion);


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

        //Autores
        linearListViewAutores = (LinearLayout) view.findViewById(R.id.linear_listview_autores);
        btnAgregarAutor = (ImageButton) view.findViewById(R.id.btnAgregarAutor);
        btnEliminarAutor = (ImageButton) view.findViewById(R.id.btnEliminarAutor);

        linearCantidadLibro = (LinearLayout) view.findViewById(R.id.linearCantidad);
        linearCantidadLibro.setVisibility(View.VISIBLE);
    }

    /**
     * Carga los spinners contenidos en la creacion del libro.
     */
    public void cargarSpinners(){

        CargarSpinners.loadDatos(getActivity(), Editorial.class.getSimpleName(), spinnerEditorial, 0);
        CargarSpinners.loadDatos(getActivity(), Area.class.getSimpleName(), spinnerArea, 0);
        CargarSpinners.loadDatos(getActivity(), Sede.class.getSimpleName(), spinnerSede, 0);
        CargarSpinners.loadDatos(getActivity(), Pais.class.getSimpleName(), spinnerPais, 0);
        CargarSpinners.loadDatos(getActivity(), Autor.class.getSimpleName(), spinnerAutor, 0);
    }

    /**
     * Metodo que carga los datos de un determinado libro previamente seleccionado.
     */
    public void cargarDatosLibroSeleccionado(){
        if(variablesGlobales.getLibroSeleccionadoAdmin() != null) {

            //Si esta editando, se oculta el campo cantidad
            linearCantidadLibro.setVisibility(View.GONE);

            try {
                titulo.setText(variablesGlobales.getLibroSeleccionadoAdmin().getTitulo());
                isbn.setText(variablesGlobales.getLibroSeleccionadoAdmin().getIsbn());
                codTopografico.setText(variablesGlobales.getLibroSeleccionadoAdmin().getCodigoTopografico());
                temas.setText(variablesGlobales.getLibroSeleccionadoAdmin().getTemas());
                paginas.setText(String.valueOf(variablesGlobales.getLibroSeleccionadoAdmin().getPaginas()));
                serie.setText(variablesGlobales.getLibroSeleccionadoAdmin().getSerie());
                valor.setText(String.valueOf(variablesGlobales.getLibroSeleccionadoAdmin().getValor()));
                radicado.setText(variablesGlobales.getLibroSeleccionadoAdmin().getRadicado());
                cantidad.setText(String.valueOf(variablesGlobales.getLibroSeleccionadoAdmin().getCantidad()));


                //Se cargan los spinners con su respectivo valor.
                spinnerEstado.setSelection(Utilidades.getIndexSpinner(spinnerEstado, variablesGlobales.getLibroSeleccionadoAdmin().getEstado()));
                spinnerAdquisicion.setSelection(Utilidades.getIndexSpinner(spinnerAdquisicion, variablesGlobales.getLibroSeleccionadoAdmin().getAdquisicion()));

                //Para el caso de la editorial, area y sede...(Spinners con objetos) se valida y cargan en la funcion cargarSpinners().
                //spinnerEditorial.setSelection(...);

                //Pendiente cargar spinner ciudad
                //Referncia a spinnerCiudad ciudad = (Spinner) view.findViewById(R.id.spinnerCiudad);

                //Se carga el año del libro.
                // El dia y el mes se cargan por defecto con valores de 1 ya que solo nos interesa el año.
                if (variablesGlobales.getLibroSeleccionadoAdmin().getAnio() != 0) {
                    dpickerAnioLibro.updateDate(variablesGlobales.getLibroSeleccionadoAdmin().getAnio(), 1, 1);
                }

                //Si existe ciudad, cargamos el spinner de ciudades agrupadas por su respectivo pais
                if(variablesGlobales.getLibroSeleccionadoAdmin().getCiudad() != null){
                    CargarSpinners.loadDatos(getActivity(), Ciudad.class.getSimpleName(), spinnerCiudad,
                            variablesGlobales.getLibroSeleccionadoAdmin().getCiudad().getPais().getIdPais());
                }

                //Se cargan los autores asociados al libro en caso de tenerlos
                TareaWsListarLibroAutor tareaListarLibroAutor = new TareaWsListarLibroAutor();
                tareaListarLibroAutor.execute();

            }catch (Exception e){
                Log.e("CrearLibro","XXX Error cargando datos de libros seleccionado: "+e.getMessage());
            }
        }else{
            limpiarCampos();
        }
    }

    /**
     * Metodo encargado de crear el dialog para autores
     */
    public void crearDialogAutores(){

        // Rescatamos el layout creado para el prompt
        LayoutInflater li = LayoutInflater.from(getActivity());
        View prompt = li.inflate(R.layout.prompt_sppiner_autores, null);

        // Creamos un constructor de Alert Dialog y le añadimos nuestro layout al cuadro de dialogo
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(prompt);

        //Se referencia el spinnerAutores
        spinnerAutor = (Spinner) prompt.findViewById(R.id.spinnerAutores);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!spinnerAutor.getSelectedItem().toString().equals("Seleccione...")) {

                            Autor autor = (Autor) spinnerAutor.getSelectedItem();
                            if (autor != null) {
                                listaAutores.add(autor);
                                recargarLinearLayoutAutores();
                            }
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancelamos el cuadro de dialogo
                        dialog.cancel();
                    }
                });
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
        serie.getText().clear();
        cantidad.setText("1");

        valor.getText().clear();
        radicado.getText().clear();

        listaAutores.clear();
        recargarLinearLayoutAutores();
    }

    /**
     * Metodo encargado de recargar el linear_layout de autores
     */
    public void recargarLinearLayoutAutores(){

        indexAutorSeleccionado = -1;
        linearListViewAutores.removeAllViewsInLayout();

        //Se recaga la interfaz con datos vacios en caso de no cargar autores
        if(listaAutores.size() == 0){
            linearListViewAutores.removeAllViewsInLayout();

            View mLinearView = inflaterAux.inflate(R.layout.row_autores_libro, null);
            linearListViewAutores.addView(mLinearView);
        }

        for(int i = 0; i < listaAutores.size(); i++){
            /**
             * inflate items/ add items in linear layout instead of listview
             */
            View mLinearView = inflaterAux.inflate(R.layout.row_autores_libro, null);
            /**
             * getting id of row.xml
             */
            TextView mFirstName = (TextView) mLinearView
                    .findViewById(R.id.textViewName);
            TextView mLastName = (TextView) mLinearView
                    .findViewById(R.id.textViewLastName);

            /**
             * set item into row
             */
            final int index = i;
            final String fName = listaAutores.get(i).getPrimerNombre();
            final String lName = listaAutores.get(i).getPrimerApellido();
            mFirstName.setText(fName);
            mLastName.setText(lName);

            /**
             * add view in top linear
             */

            linearListViewAutores.addView(mLinearView);

            /**
             * get item row on click
             *
             */
            mLinearView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity(), "Seleccionó: " + fName+ " "+lName,
                            Toast.LENGTH_SHORT).show();
                    indexAutorSeleccionado = index;
                }
            });
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
            lib.setTitulo(titulo.getText().toString().trim());

            if(valor.getText().toString().trim().length() > 0){
                lib.setValor(Integer.parseInt(valor.getText().toString()));
            }

            if(paginas.getText().toString().trim().length() > 0){
                lib.setPaginas(Integer.parseInt(paginas.getText().toString()));
            }

            if(cantidad.getText().toString().trim().length() > 0){
                lib.setCantidad(Integer.parseInt(cantidad.getText().toString()));
            }

            lib.setAnio(dpickerAnioLibro.getYear());
            lib.setSerie(serie.getText().toString().trim());
            lib.setIsbn(isbn.getText().toString().trim());
            lib.setCodigoTopografico(codTopografico.getText().toString().trim());

            if (!spinnerEstado.getSelectedItem().toString().equals("Seleccione...")) {
                lib.setEstado(spinnerEstado.getSelectedItem().toString());
            }

            if (!spinnerAdquisicion.getSelectedItem().toString().equals("Seleccione...")) {
                lib.setAdquisicion(spinnerAdquisicion.getSelectedItem().toString());
            }

            lib.setRadicado(radicado.getText().toString().trim());
            lib.setFechaIngreso(new Date());
            lib.setTemas(temas.getText().toString().trim());
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

            Ciudad ciudadSeleccionada = (Ciudad) spinnerCiudad.getSelectedItem();
            if(ciudadSeleccionada != null){
                lib.setCiudad(ciudadSeleccionada);
            }

            try {

                /**
                 * Se setea el idLibro en caso de ser edicion, de lo contrario este
                 * quedara con valor de 0.
                 * Funcionalidad especifica para validar si se guarda un nuevo libro o se edita uno existente.
                 */
            if(variablesGlobales.getLibroSeleccionadoAdmin() != null){
                lib.setIdLibro(variablesGlobales.getLibroSeleccionadoAdmin().getIdLibro());
            }

            //Se gestionan los autores
            if(listaAutores.size() > 0){
                try{
                    StringBuilder cadena = new StringBuilder();
                    for(Autor item: listaAutores){
                        cadena.append(item.getIdAutor()+",");
                    }

                    //Se elimina la ultima coma
                    idAutoresConcatenados = cadena.substring(0, cadena.length()-1);
                }catch (Exception e){
                    idAutoresConcatenados = "";
                    Log.e("CadenaAuto","XXX Error armando cadena de autores: "+e.getMessage());
                }
            }else{
                idAutoresConcatenados = "";
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

        //Metodo anterior para el guardado de libros
        //final String SOAP_ACTION = conf.getUrl()+"/guardarLibro";
        //final String METHOD_NAME = "guardarLibro";

        //Metodo actual para el guardado de libros (almacena cada libro de manera individual segun la canatidad)
        final String SOAP_ACTION = conf.getUrl()+"/guardarLibroNew";
        final String METHOD_NAME = "guardarLibroNew";


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

        if(libro.getCiudad() != null) {
            request.addProperty("idCiudad", libro.getCiudad().getIdCiudad());
        }else{
            request.addProperty("idCiudad", "");
        }

        request.addProperty("cantidad",libro.getCantidad());
        request.addProperty("idAutoresConcatenados",idAutoresConcatenados);

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


    /**
     * Tarea encargada de listar los datos de la tabla LIBRO_AUTOR
     */
    private class TareaWsListarLibroAutor extends AsyncTask<String,Integer,Boolean> {

        List<LibroAutor> listaLibroAutor = new ArrayList<LibroAutor>();
        boolean resultadoTarea = false;

        @SuppressLint("LongLogTag")
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();
                listaLibroAutor = tareasGenerales.listarLibroAutor(variablesGlobales.getLibroSeleccionadoAdmin().getIdLibro());
                resultadoTarea = true;
                Log.i("LibrosAdmin",">>>>>>>>>>> Tamaño lista librosAutor buscada: "+listaLibroAutor.size());
            }catch (Exception e){
                resultadoTarea = false;
                Log.e("ListaLibrosAdmin ", "xxx Error TareaWsListarLibroAutor: " + e.getMessage());
            }
            return resultadoTarea;
        }

        public void onPostExecute(Boolean result){

            if(result){
                try {
                    if(listaLibroAutor.size() > 0){
                        //Se cargan los autores a su respectiva lista
                        listaAutores.clear();
                        for(LibroAutor item: listaLibroAutor){
                            listaAutores.add(item.getAutor());
                        }

                        recargarLinearLayoutAutores();
                    }
                }catch (Exception e){
                    Log.e("ListaLibrosAdmin","XXX Error listando librosAutor: "+e.getMessage());
                }
            }else{
                //String msn = "Error listando librosAutor";
                //Toast.makeText(getActivity(), msn, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo encargado de validar los campos de texto y llamar al metodo para almacenear la info.
     */
    public void validarGuardar() {

        boolean resultado = true;

        if (titulo.getText().toString().trim().length() == 0) {
            titulo.setError("Título requerido");
            resultado = false;
        }

        if (isbn.getText().toString().trim().length() == 0) {
            isbn.setError("ISBN requerido");
            resultado = false;
        }

        if (codTopografico.getText().toString().trim().length() == 0) {
            codTopografico.setError("Código requerido");
            resultado = false;
        }

        //Verificar campos ya registrados (repetidos) en la BD.
        TareaWsVerificarDatoEnBd tarea = new TareaWsVerificarDatoEnBd();
        tarea.setPasaValidacionPrevia(resultado);

        tarea.execute();

    }

    /**
     * Tarea encargada de verificar si existe un determinado dato repetido en la BD
     * en caso de pasar todas las validaciones de los campos de texto, llama a la
     * tarea encargada de guardar la info.
     */
    private class TareaWsVerificarDatoEnBd extends AsyncTask<String,Integer,Boolean> {

        private boolean datoRepetido = false;

//        private boolean isbnRepetido = false;
        private boolean codTopograficoRepetido = false;
        private boolean pasaValidacionPrevia = false;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                TareasGenerales tareasGenerales = new TareasGenerales();

                /**
                 * Si esta creando un nuevo libro
                 */
                if(variablesGlobales.getLibroSeleccionadoAdmin() == null) {

                    //Se omite validacion de ISBN repetido
//                    if(tareasGenerales.verficarDatoEnBd("LIBRO", "ISBN", isbn.getText().toString().trim())){
//                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> ISBN ya registrado (crear)");
//                        datoRepetido = true;
//                        isbnRepetido = true;
//                    }

                    if(tareasGenerales.verficarDatoEnBd("LIBRO","COD_TOPOGRAFICO",codTopografico.getText().toString().trim())){
                        Log.i("GuardandoUsuario",">>>>>>>>>>>>>>>>>> codTopografico ya registrado (crear)");
                        datoRepetido = true;
                        codTopograficoRepetido = true;
                    }
                }

                /**
                 * Si esta editando un libro
                 */
                if(variablesGlobales.getLibroSeleccionadoAdmin() != null) {

                    //Se omite validacion de ISBN repetido
//                    if(!variablesGlobales.getLibroSeleccionadoAdmin().getIsbn().equals(isbn.getText().toString().trim()) &&
//                            tareasGenerales.verficarDatoEnBd("LIBRO","ISBN",isbn.getText().toString().trim())){
//                        datoRepetido = true;
//                        isbnRepetido = true;
//                    }

                    if(!variablesGlobales.getLibroSeleccionadoAdmin().getCodigoTopografico().equals(codTopografico.getText().toString().trim()) &&
                            tareasGenerales.verficarDatoEnBd("LIBRO","COD_TOPOGRAFICO",codTopografico.getText().toString().trim())){
                        datoRepetido = true;
                        codTopograficoRepetido = true;
                    }
                }

            } catch (Exception e) {
                Log.e("Generales", "xxx Error TareaWsVerificarDatoEnBd: " + e.getMessage());
            }
            return datoRepetido;
        }

        public void onPostExecute(Boolean result) {
            if (result) { //Indica que hay almenos un dato repetido en BD

                //Se omite validacion de ISBN repetido
//                if(isbnRepetido){
//                    isbn.setError("ISBN ya registrado");
//                }

                if(codTopograficoRepetido){
                    codTopografico.setError("Código ya registrado");
                }

                Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();

            } else { // Pasa validacion exitosa de campos repetidos

                if (pasaValidacionPrevia) {
                    TareaWsGuardarLibro tareaGuardarLibro = new TareaWsGuardarLibro();
                    tareaGuardarLibro.execute();
                } else {
                    Toast.makeText(getActivity(), "Verificar campos requeridos", Toast.LENGTH_LONG).show();
                }

            }
        }

        //Setters
        public void setPasaValidacionPrevia(boolean pasaValidacionPrevia) {
            this.pasaValidacionPrevia = pasaValidacionPrevia;
        }

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
