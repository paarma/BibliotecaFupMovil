package com.webileapps.navdrawer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Area;
import modelo.Editorial;
import modelo.Libro;
import modelo.Sede;
import util.CargarSpinners;
import util.Configuracion;
import util.VariablesGlobales;

public class FmCrearLibroAdmin extends SherlockFragment {

    EditText titulo, isbn, codTopografico, temas, paginas, valor, adquisicion, radicado,anio,serie;
    Spinner estado, spinnerEditorial, spinnerArea, spinnerSede, ciudad;
    VariablesGlobales variablesGlobales = VariablesGlobales.getInstance();

    private final static String[] tipoEstado = { "Seleccione..", "Bueno", "Malo"};
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
        adquisicion.getText().clear();
        radicado.getText().clear();
    }

    public void inicializarComponentes(View view){

        titulo = (EditText) view.findViewById(R.id.editTextTitulo);
        isbn = (EditText) view.findViewById(R.id.editTextIsbn);
        codTopografico = (EditText) view.findViewById(R.id.editTextCodTopo);
        temas = (EditText) view.findViewById(R.id.editTextTemas);
        paginas = (EditText) view.findViewById(R.id.editTextPaginas);
        anio = (EditText) view.findViewById(R.id.editTextAnio);
        serie = (EditText) view.findViewById(R.id.editTextSerie);
        estado = (Spinner) view.findViewById(R.id.spinnerEstado);
        spinnerEditorial = (Spinner) view.findViewById(R.id.spinnerEditorial);
        spinnerArea = (Spinner) view.findViewById(R.id.spinnerArea);
        spinnerSede = (Spinner) view.findViewById(R.id.spinnerSede);
        ciudad = (Spinner) view.findViewById(R.id.spinnerCiudad);
        valor = (EditText) view.findViewById(R.id.editTextValor);
        adquisicion = (EditText) view.findViewById(R.id.editTextAdquisicion);
        radicado = (EditText) view.findViewById(R.id.editTextRadicado);

        cargarSpinners();

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, tipoEstado);
        Spinner spTipoEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        spTipoEstado.setAdapter(adapter);

        ArrayAdapter adapterCiudad = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, tipoCiudad);
        Spinner spCiudad = (Spinner) view.findViewById(R.id.spinnerCiudad);
        spCiudad.setAdapter(adapterCiudad);
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

            if(anio.getText().toString().trim().length() > 0){
                lib.setAnio(Integer.parseInt(anio.getText().toString()));
            }


            lib.setSerie(serie.getText().toString());
            lib.setIsbn(isbn.getText().toString());
            lib.setCodigoTopografico(codTopografico.getText().toString());
            lib.setAdquisicion(adquisicion.getText().toString());

            if (estado.getSelectedItem().toString().equals("Bueno")) {
                lib.setEstado("BUENO");
            }

            if (estado.getSelectedItem().toString().equals("Malo")) {
                lib.setEstado("MALO");
            }

            lib.setRadicado(radicado.getText().toString());
            lib.setFechaIngreso(new Date());
            //lib.setSerie("1");
            //lib.setAnio(1);
            lib.setTemas(temas.getText().toString());
            //lib.setDisponibilidad("SI");
            lib.setIdUsuario(variablesGlobales.getUsuarioLogueado().getIdUsuario());
            //lib.setIdCiudad(1);

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
                lib.setIdSede(sedeSeleccionada.getIdSede());
            }

            if (ciudad.getSelectedItem().toString().equals("Ciudad_1")) {
                lib.setIdCiudad(1);
            }

            if (ciudad.getSelectedItem().toString().equals("Ciudad_2")) {
                lib.setIdCiudad(2);
            }

            try {
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
        request.addProperty("idSede",libro.getIdSede());
        request.addProperty("idEditorial",libro.getEditorial().getIdEditorial());
        request.addProperty("idArea",libro.getArea().getIdArea());
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

}
