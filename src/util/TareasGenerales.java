package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.Area;
import modelo.Editorial;
import modelo.Libro;
import modelo.Sede;
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

        if(libroBuscar.getEditorial() != null){
        request.addProperty("editorial",libroBuscar.getEditorial().getIdEditorial());
        }else{
            request.addProperty("editorial","");
        }


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
                    listaLibro.add(obtenerLibroSoap(libroSoap));
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error buscarLibros(): " + e.getMessage());
        }
        return listaLibro;
    }

    /**
     * Metodo encardado de setear los valores desde la BD (Soap) a un Libro.
     * @param libroSoap Objeto Soap que contiene los datos de libro para ser setados.
     * @return
     */
    public Libro obtenerLibroSoap(SoapObject libroSoap){

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

        if(libroSoap.getProperty("VALOR") != null) {
            lib.setValor(Integer.parseInt(libroSoap.getProperty("VALOR").toString()));
        }

        if(libroSoap.getProperty("RADICADO") != null) {
            lib.setRadicado(libroSoap.getProperty("RADICADO").toString());
        }

        if(libroSoap.getProperty("SERIE") != null) {
            lib.setSerie(libroSoap.getProperty("SERIE").toString());
        }

        if(libroSoap.getProperty("ANIO") != null) {
            lib.setAnio(Integer.parseInt(libroSoap.getProperty("ANIO").toString()));
        }

        //Editorial
        if(libroSoap.getProperty("ID_EDITORIAL") != null) {
            lib.setEditorial(buscarEditorialPorId(Integer.parseInt(libroSoap.getProperty("ID_EDITORIAL").toString())));
        }

        //Area
        if(libroSoap.getProperty("ID_AREA") != null) {
            lib.setArea(buscarAreaPorId(Integer.parseInt(libroSoap.getProperty("ID_AREA").toString())));
        }

        //Sede
        if(libroSoap.getProperty("ID_SEDE") != null) {
            lib.setSede(buscarSedePorId(Integer.parseInt(libroSoap.getProperty("ID_SEDE").toString())));
        }

        if(libroSoap.getProperty("ADQUISICION") != null) {
            lib.setAdquisicion(libroSoap.getProperty("ADQUISICION").toString());
        }

        if(libroSoap.getProperty("ESTADO") != null) {
            lib.setEstado(libroSoap.getProperty("ESTADO").toString());
        }

        return lib;
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

        //Busqueda por editorial
        if(libroBuscar.getEditorial() != null){
            request.addProperty("editorial",libroBuscar.getEditorial().getIdEditorial());
        }else{
            request.addProperty("editorial","");
        }

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
     * Metodo encargado de listar las Areas de la BD.
     * @return
     */
    public List<Area> listarAreas(){

        final String SOAP_ACTION = conf.getUrl()+"/listadoAreas";
        final String METHOD_NAME = "listadoAreas";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Area> listaAreas = new ArrayList<Area>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject editorialSoap : rs) {

                    Area area = new Area();
                    area.setIdArea(Integer.parseInt(editorialSoap.getProperty("ID_AREA").toString()));
                    area.setDescripcion(editorialSoap.getProperty("DESCRIPCION").toString());
                    listaAreas.add(area);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarAreas(): " + e.getMessage());
        }
        return listaAreas;
    }

    /**
     * Metodo encargado de listar las Sedes de la BD.
     * @return
     */
    public List<Sede> listarSedes(){

        final String SOAP_ACTION = conf.getUrl()+"/listadoSedes";
        final String METHOD_NAME = "listadoSedes";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Sede> listaDedes = new ArrayList<Sede>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject editorialSoap : rs) {

                    Sede sede = new Sede();
                    sede.setIdSede(Integer.parseInt(editorialSoap.getProperty("ID_SEDE").toString()));
                    sede.setDescripcion(editorialSoap.getProperty("DESCRIPCION").toString());
                    listaDedes.add(sede);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarSedes(): " + e.getMessage());
        }
        return listaDedes;
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
                    lib = obtenerLibroSoap(libroSoap);
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

    /**
     * Metodo encargado de retornar una Editorial segun su ID
     * @param idEditorial
     * @return Editorial
     */
    public Editorial buscarEditorialPorId(int idEditorial){

        final String SOAP_ACTION = conf.getUrl()+"/buscarEditorialPorId";
        final String METHOD_NAME = "buscarEditorialPorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Editorial editorial = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idEditorial",idEditorial);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject edit : rs)
                {
                    editorial = new Editorial();
                    editorial.setIdEditorial(Integer.parseInt(edit.getProperty("ID_EDITORIAL").toString()));
                    editorial.setDescripcion(edit.getProperty("DESCRIPCION").toString());

                    Log.i("Generales.java",">>>>>>>>>>>> buscarEditorialPorId: "+editorial.getIdEditorial());
                    break;
                }
            }
        }catch (Exception e){
            Log.d("Generales.java ", "xxx Error buscarEditorialPorId(): "+e.getMessage());
        }
        return editorial;
    }

    /**
     * Metodo encargado de retornar un Area segun su ID
     * @param idArea
     * @return Area
     */
    public Area buscarAreaPorId(int idArea){

        final String SOAP_ACTION = conf.getUrl()+"/buscarAreaPorId";
        final String METHOD_NAME = "buscarAreaPorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Area area = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idArea",idArea);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject areaSoap : rs)
                {
                    area = new Area();
                    area.setIdArea(Integer.parseInt(areaSoap.getProperty("ID_AREA").toString()));
                    area.setDescripcion(areaSoap.getProperty("DESCRIPCION").toString());

                    Log.i("Generales.java",">>>>>>>>>>>> buscarAreaPorId: "+area.getIdArea());
                    break;
                }
            }
        }catch (Exception e){
            Log.d("Generales.java ", "xxx Error buscarAreaPorId(): "+e.getMessage());
        }
        return area;
    }

    /**
     * Metodo encargado de retornar una Sede segun su ID
     * @param idSede
     * @return Sede
     */
    public Sede buscarSedePorId(int idSede){

        final String SOAP_ACTION = conf.getUrl()+"/buscarSedePorId";
        final String METHOD_NAME = "buscarSedePorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Sede sede = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idSede",idSede);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject sedeSoap : rs)
                {
                    sede = new Sede();
                    sede.setIdSede(Integer.parseInt(sedeSoap.getProperty("ID_SEDE").toString()));
                    sede.setDescripcion(sedeSoap.getProperty("DESCRIPCION").toString());

                    Log.i("Generales.java",">>>>>>>>>>>> buscarSedePorId: "+sede.getIdSede());
                    break;
                }
            }
        }catch (Exception e){
            Log.d("Generales.java ", "xxx Error buscarSedePorId(): "+e.getMessage());
        }
        return sede;
    }

}
