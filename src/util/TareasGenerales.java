package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        request.addProperty("idUsuario", solicitud.getUsuario().getIdUsuario());
        request.addProperty("idLibro", solicitud.getLibro().getIdLibro());
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


    /**
     * Metodo encargado de retornar el listado de solicitudes (reservas)
     * @param libroBuscar objeto de la clase Libro el cual contiene los parametros
     *                    de busqueda ya sean fijados o por defecto. En el caso
     *                    de tenerlos por defecto (new Libro()) se listaran todas las reservas.
     * @param idUsuarioReserva ID del usuario que reservo los libros.
     *                         asignal el valor de 0 en caso de no filtrar por usuario.
     * @param  estadoReserva Estado de la reserva. En caso de null, no se toma en cuenta
     *                       este filtro.
     * @return ListadoSolictudes
     */
    public List<Solicitud> buscarSolicitudes(Libro libroBuscar, int idUsuarioReserva, String estadoReserva){

        final String SOAP_ACTION = conf.getUrl()+"/listadoReservas";
        final String METHOD_NAME = "listadoReservas";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("titulo",libroBuscar.getTitulo());
        request.addProperty("isbn",libroBuscar.getIsbn());
        request.addProperty("codTopografico",libroBuscar.getCodigoTopografico());
        request.addProperty("temas",libroBuscar.getTemas());
        request.addProperty("editorial",libroBuscar.getIdEditorial());
        request.addProperty("idUsuarioReserva",idUsuarioReserva);
        request.addProperty("estadoReserva",estadoReserva);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject solicitudSoap : rs)
                {
                    Libro libroBd = buscarLibroPorId(Integer.parseInt(solicitudSoap.getProperty("ID_LIBRO_SOL").toString()));
                    Usuario usuarioBd = buscarUsuarioPorId(Integer.parseInt(solicitudSoap.getProperty("ID_USUARIO_SOL").toString()));

                    Solicitud sol = new Solicitud();
                    sol.setIdSolicitud(Integer.parseInt(solicitudSoap.getProperty("ID_SOLICITUD").toString()));

                    sol.setUsuario(usuarioBd);
                    sol.setLibro(libroBd);
                    sol.setEstado(solicitudSoap.getProperty("ESTADO_SOL").toString());

                    //Fechas
                    Date fechas;

                    if(solicitudSoap.getProperty("FECHA_SOLICITUD") != null){
                        fechas = Utilidades.formatoFechaYYYYMMDD.parse(solicitudSoap.getProperty("FECHA_SOLICITUD").toString());
                        sol.setFechaSolicitud(fechas);
                    }

                    if(solicitudSoap.getProperty("FECHA_RESERVA") != null){
                        fechas = Utilidades.formatoFechaYYYYMMDD.parse(solicitudSoap.getProperty("FECHA_RESERVA").toString());
                        sol.setFechaReserva(fechas);
                    }

                    if(solicitudSoap.getProperty("FECHA_DEVOLUCION") != null){
                        fechas = Utilidades.formatoFechaYYYYMMDD.parse(solicitudSoap.getProperty("FECHA_DEVOLUCION").toString());
                        sol.setFechaDevolucion(fechas);
                    }

                    if(solicitudSoap.getProperty("FECHA_ENTREGA") != null){
                        fechas = Utilidades.formatoFechaYYYYMMDD.parse(solicitudSoap.getProperty("FECHA_ENTREGA").toString());
                        sol.setFechaEntrega(fechas);
                    }

                    Log.i("TareasGenerales", ">>>>>>>>>>>>>>>>>>>> fecha SOLICITUD: " + sol.getFechaSolicitud());
                    Log.i("TareasGenerales", ">>>>>>>>>>>>>>>>>>>> fecha RESERVA: " + sol.getFechaReserva());
                    Log.i("TareasGenerales", ">>>>>>>>>>>>>>>>>>>> fecha DEVOLUCION: " + sol.getFechaDevolucion());
                    Log.i("TareasGenerales", ">>>>>>>>>>>>>>>>>>>> fecha ENTREGA: " + sol.getFechaEntrega());

                    listaSolicitudes.add(sol);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error buscarSolicitudes(): " + e.getMessage());
        }
        return listaSolicitudes;
    }

    /**
     * Metodo encargado de listar las editoriales de la BD.
     * @return
     */
    public List<Editorial> listarEditoriales(){

        final String SOAP_ACTION = conf.getUrl()+"/listadoEditoriales";
        final String METHOD_NAME = "listadoEditoriales";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Editorial> listaEditoriales = new ArrayList<Editorial>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject editorialSoap : rs) {

                    Editorial editorial = new Editorial();
                    editorial.setIdEditorial(Integer.parseInt(editorialSoap.getProperty("ID_EDITORIAL").toString()));
                    editorial.setDescripcion(editorialSoap.getProperty("DESCRIPCION").toString());
                    listaEditoriales.add(editorial);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarEditoriales(): " + e.getMessage());
        }
        return listaEditoriales;
    }

    /**
     * Metodo encargado de retornar un libro segun su ID
     * @param idLibro
     * @return Libro
     */
    public Libro buscarLibroPorId(int idLibro){

        final String SOAP_ACTION = conf.getUrl()+"/buscarLibroPorId";
        final String METHOD_NAME = "buscarLibroPorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Libro lib = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idLibro",idLibro);

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
                    lib = new Libro();
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

                    Log.i("Generales.java",">>>>>>>>>>>> buscarLibroPorId: "+lib.getIdLibro());
                    break;
                }
            }
        }catch (Exception e){
            Log.e("Generales.java", "xxx Error buscarLibroPorId(): "+e.getMessage());
        }
        return lib;
    }

    /**
     * Metodo encargado de retornar un usuario segun su ID
     * @param idUsuario
     * @return Usuario
     */
    public Usuario buscarUsuarioPorId(int idUsuario){

        final String SOAP_ACTION = conf.getUrl()+"/buscarUsuarioPorId";
        final String METHOD_NAME = "buscarUsuarioPorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Usuario usuario = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idUsuario",idUsuario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject user : rs)
                {
                    usuario = new Usuario();
                    usuario.setIdUsuario(Integer.parseInt(user.getProperty("ID_USUARIO").toString()));
                    usuario.setCedula(Integer.parseInt(user.getProperty("CEDULA").toString()));
                    usuario.setNombre(user.getProperty("NOMBRE").toString());
                    usuario.setApellido(user.getProperty("APELLIDO").toString());
                    usuario.setTelefono(Integer.parseInt(user.getProperty("TELEFONO").toString()));
                    usuario.setDireccion(user.getProperty("DIRECCION").toString());
                    usuario.setEmail(user.getProperty("EMAIL").toString());
                    usuario.setCodigo(user.getProperty("CODIGO").toString());
                    usuario.setClave(user.getProperty("CLAVE").toString());
                    usuario.setRol(user.getProperty("ROL").toString());

                    Log.i("Generales.java",">>>>>>>>>>>> buscarUsuarioPorId: "+usuario.getIdUsuario());
                    break;
                }
            }
        }catch (Exception e){
            Log.d("Generales.java ", "xxx Error buscarUsuarioPorId(): "+e.getMessage());
        }
        return usuario;

    }

}
