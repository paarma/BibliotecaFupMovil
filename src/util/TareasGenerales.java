package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import modelo.Autor;
import modelo.Editorial;
import modelo.Libro;
import modelo.Solicitud;
import modelo.Usuario;

/**
 * Created by pablo on 22/05/15.
 * Clase encargada de contener tareas generales (consultas a la BD) para el sistema
 */
public class TareasGenerales {

    /**
     * Objeto de la clase configuracion la cual contiene atributos
     * generales y conf. para conexion al server
     */
    Configuracion conf = new Configuracion();

    /**
     * Metodo encargado de retornar el listado de libros segun busqueda
     * @param libroBuscar objeto de la clase Libro el cual contiene los parametros
     *                    de busqueda ya sean fijados o por defecto. En el caso
     *                    de tenerlos por defecto (new Libro()) se listaran todos los libros
     *                    precentes.
     * @return ListadoLibros
     */
    public List<Libro> buscarLibros(Libro libroBuscar){

        final String SOAP_ACTION = conf.getUrl()+"/listadoLibros";
        final String METHOD_NAME = "listadoLibros";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Libro> listaLibro = new ArrayList<Libro>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("titulo",libroBuscar.getTitulo());
        request.addProperty("isbn",libroBuscar.getIsbn());
        request.addProperty("codTopografico",libroBuscar.getCodigoTopografico());
        request.addProperty("temas",libroBuscar.getTemas());
        request.addProperty("editorial",libroBuscar.getIdEditorial());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject libroSoap : rs)
                {
                    Libro lib = new Libro();
                    lib.setIdLibro(Integer.parseInt(libroSoap.getProperty("ID_LIBRO").toString()));
                    lib.setTitulo(libroSoap.getProperty("TITULO").toString());
                    lib.setIsbn(libroSoap.getProperty("ISBN").toString());

                    if(libroSoap.getProperty("COD_TOPOGRAFICO") != null){
                        lib.setCodigoTopografico(libroSoap.getProperty("COD_TOPOGRAFICO").toString());
                    }

                    if(libroSoap.getProperty("TEMAS") != null){
                        lib.setTemas(libroSoap.getProperty("TEMAS").toString());
                    }
                    if(libroSoap.getProperty("PAGINAS") != null) {
                        lib.setPaginas(Integer.parseInt(libroSoap.getProperty("PAGINAS").toString()));
                    }

                    listaLibro.add(lib);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error buscarLibros(): " + e.getMessage());
        }
        return listaLibro;
    }

    /**
     * Metodo encargado de guardar un libro en la BD
     * @param libro Libro el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarLibro(Libro libro){

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
        request.addProperty("idEditorial",libro.getIdEditorial());
        request.addProperty("idArea",libro.getIdArea());
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

            Log.i("GuardandoLibro","*********************** guardandoLibro: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error guardarLibro(): " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Metodo encargado de guardar un Autor en la BD
     * @param autor Autor el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarAutor(Autor autor){

        final String SOAP_ACTION = conf.getUrl()+"/guardarAutor";
        final String METHOD_NAME = "guardarAutor";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("descripcion", autor.getDescripcion());
        request.addProperty("tipo",autor.getTipoAutor());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("GuardandoAutor","*********************** guardandoAutor: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error guardarAutor(): " + e.getMessage());
        }

        return resultado;
    }


    /**
     * Metodo encargado de guardar una editorial en la BD
     * @param editorial Editorial el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarEditorial(Editorial editorial){

        final String SOAP_ACTION = conf.getUrl()+"/guardarEditorial";
        final String METHOD_NAME = "guardarEditorial";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("descripcion", editorial.getDescripcion());




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("GuardandoEditorial","*********************** guardandoEditorial: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error guardarEditorial(): " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Metodo encargado de guardar un usuario  en la BD para acceder a la App
     * @param usuario Usuario el cual va  a ser guardado
     * @return resultado
     */
    public boolean guardarUsuario(Usuario usuario){

        final String SOAP_ACTION = conf.getUrl()+"/guardarUsuario";
        final String METHOD_NAME = "guardarUsuario";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cedula", usuario.getCedula());
        request.addProperty("nombre", usuario.getNombre());
        request.addProperty("apellido", usuario.getApellido());
        request.addProperty("telefono", usuario.getTelefono());
        request.addProperty("direccion", usuario.getDireccion());
        request.addProperty("email", usuario.getEmail());
        request.addProperty("codigo", usuario.getCodigo());
        request.addProperty("clave", usuario.getClave());

        request.addProperty("rol",usuario.getRol());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoGuardar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("GuardandoUsuario","*********************** guardandoUsuario: "+resultadoGuardar);
            if (resultadoGuardar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error guardarUsuario(): " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Metodo encargado de reservar un libro
     * @param solicitud
     * @return
     */
    public boolean reservar(Solicitud solicitud){

        final String SOAP_ACTION = conf.getUrl()+"/reservar";
        final String METHOD_NAME = "reservar";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("fechaSolicitud", Utilidades.formatoFechaYYYYMMDD.format(solicitud.getFechaSolicitud()));
        request.addProperty("fechaReserva", Utilidades.formatoFechaYYYYMMDD.format(solicitud.getFechaReserva()));

        //Dos dias más a partir de la fecha de reserva.
        request.addProperty("fechaDevolucion",
                Utilidades.formatoFechaYYYYMMDD.format(solicitud.getFechaDevolucion()));

        request.addProperty("idUsuario", solicitud.getIdUsuario());
        request.addProperty("idLibro", solicitud.getIdLibro());
        request.addProperty("estado", solicitud.getEstado());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoReserva = Integer.parseInt(envelope.getResponse().toString());

            Log.i("Reservando","*********************** resultadoReserva: "+resultadoReserva);
            if (resultadoReserva == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error reservar(): " + e.getMessage());
        }

        return resultado;
    }

}
