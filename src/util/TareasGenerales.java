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
import modelo.Autor;
import modelo.Ciudad;
import modelo.Editorial;
import modelo.Libro;
import modelo.LibroAutor;
import modelo.Pais;
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
     * Objeto de la clase UtilidadesBuscarPorId el cual contiene metodos para obtener objetos
     * segun su el ID del objeto.
     */
    UtilidadesBuscarPorId utilidadesBuscarPorId = new UtilidadesBuscarPorId();

    /**
     * Metodo encargado de retornar el listado de libros segun busqueda
     * @param libroBuscar objeto de la clase Libro el cual contiene los parametros
     *                    de busqueda ya sean fijados o por defecto. En el caso
     *                    de tenerlos por defecto (new Libro()) se listaran todos los libros
     *                    presentes.
     * @return ListadoLibros
     */
    public List<Libro> buscarLibros(Libro libroBuscar){

        //Anterior metodo para el listado de libros
        //final String SOAP_ACTION = conf.getUrl()+"/listadoLibros";
        //final String METHOD_NAME = "listadoLibros";

        //Metodo actual para el listado de libros (con los datos de objetos relacionales)
        final String SOAP_ACTION = conf.getUrl()+"/listadoLibrosNew";
        final String METHOD_NAME = "listadoLibrosNew";


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

        //Autor (LIBRO_AUTOR)
        request.addProperty("autor",libroBuscar.getIdAutor());

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

                    //Anterior metodo para el listado de libros
                    //listaLibro.add(utilidadesBuscarPorId.obtenerLibroSoap(libroSoap));

                    //Metodo actual para el listado de libros (con los datos de objetos relacionales)
                    listaLibro.add(utilidadesBuscarPorId.obtenerLibroSoapNew(libroSoap));
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
     * @param solicitudBuscar objeto de la clase Solicitud el cual contiene los parametros
     *                    de busqueda ya sean fijados o por defecto. En el caso
     *                    de tenerlos por defecto (new Solicitud()) se listaran todas las reservas.
     * @return ListadoSolictudes
     */
    public List<Solicitud> buscarSolicitudes(Solicitud solicitudBuscar){

        final String SOAP_ACTION = conf.getUrl()+"/listadoReservas";
        final String METHOD_NAME = "listadoReservas";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("titulo",solicitudBuscar.getLibro().getTitulo());
        request.addProperty("isbn",solicitudBuscar.getLibro().getIsbn());
        request.addProperty("codTopografico",solicitudBuscar.getLibro().getCodigoTopografico());
        request.addProperty("temas",solicitudBuscar.getLibro().getTemas());

        //Busqueda por editorial
        if(solicitudBuscar.getLibro().getEditorial() != null){
            request.addProperty("editorial",solicitudBuscar.getLibro().getEditorial().getIdEditorial());
        }else{
            request.addProperty("editorial","");
        }

        request.addProperty("idUsuarioReserva",solicitudBuscar.getUsuario().getIdUsuario());
        request.addProperty("estadoReserva",solicitudBuscar.getEstado());

        request.addProperty("codUsuario",solicitudBuscar.getUsuario().getCodigo());
        request.addProperty("cedulaUsuario",solicitudBuscar.getUsuario().getCedula());

        if(solicitudBuscar.getFechaSolicitud() != null) {
            request.addProperty("fechaSolicitud", Utilidades.formatoFechaYYYYMMDD.format(solicitudBuscar.getFechaSolicitud()));
        }else{
            request.addProperty("fechaSolicitud","");
        }

        //Autor (LIBRO_AUTOR)
        request.addProperty("autor",solicitudBuscar.getLibro().getIdAutor());

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
                    Libro libroBd = utilidadesBuscarPorId.buscarLibroPorId(Integer.parseInt(solicitudSoap.getProperty("ID_LIBRO_SOL").toString()));
                    Usuario usuarioBd = utilidadesBuscarPorId.buscarUsuarioPorId(Integer.parseInt(solicitudSoap.getProperty("ID_USUARIO_SOL").toString()));

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
     * @param editorialBuscar Objeto que contiene los parametros de busqueda
     *                        en caso de ser (new Editorial) se listaran todas las editoriales.
     * @return
     */
    public List<Editorial> listarEditoriales(Editorial editorialBuscar){

        final String SOAP_ACTION = conf.getUrl()+"/listadoEditoriales";
        final String METHOD_NAME = "listadoEditoriales";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Editorial> listaEditoriales = new ArrayList<Editorial>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("descripcion",editorialBuscar.getDescripcion());

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
     * Metodo encargado de listar los autores de la BD.
     * @param autorBuscar Objeto que contiene los parametros de busqueda
     *                        en caso de ser (new Autor) se listaran todos los auatores.
     * @return
     */
    public List<Autor> listarAutores(Autor autorBuscar){

        final String SOAP_ACTION = conf.getUrl()+"/listadoAutores";
        final String METHOD_NAME = "listadoAutores";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Autor> listaAutores = new ArrayList<Autor>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("primerNombre",autorBuscar.getPrimerNombre());
        request.addProperty("segundoNombre",autorBuscar.getSegundoNombre());
        request.addProperty("primerApellido",autorBuscar.getPrimerApellido());
        request.addProperty("segundoApellido",autorBuscar.getSegundoApellido());
        request.addProperty("tipo",autorBuscar.getTipoAutor());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject autorSoap : rs) {

                    Autor autor = new Autor();
                    autor.setIdAutor(Integer.parseInt(autorSoap.getProperty("ID_AUTOR").toString()));

                    if(autorSoap.getProperty("PRIMER_NOMBRE") != null) {
                        autor.setPrimerNombre(autorSoap.getProperty("PRIMER_NOMBRE").toString());
                    }

                    if(autorSoap.getProperty("SEGUNDO_NOMBRE") != null) {
                        autor.setSegundoNombre(autorSoap.getProperty("SEGUNDO_NOMBRE").toString());
                    }

                    if(autorSoap.getProperty("PRIMER_APELLIDO") != null) {
                        autor.setPrimerApellido(autorSoap.getProperty("PRIMER_APELLIDO").toString());
                    }

                    if(autorSoap.getProperty("SEGUNDO_APELLIDO") != null) {
                        autor.setSegundoApellido(autorSoap.getProperty("SEGUNDO_APELLIDO").toString());
                    }

                    if(autorSoap.getProperty("TIPO_AUTOR") != null) {
                        autor.setTipoAutor(autorSoap.getProperty("TIPO_AUTOR").toString());
                    }

                    listaAutores.add(autor);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarAutores(): " + e.getMessage());
        }
        return listaAutores;
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
     * Metodo encargado de listar los Paises de la BD.
     * @return
     */
    public List<Pais> listarPaises(){

        final String SOAP_ACTION = conf.getUrl()+"/listadoPaises";
        final String METHOD_NAME = "listadoPaises";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Pais> listaPaises = new ArrayList<Pais>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject paisSoap : rs) {

                    Pais pais = new Pais();
                    pais.setIdPais(Integer.parseInt(paisSoap.getProperty("ID_PAIS").toString()));
                    pais.setNombre(paisSoap.getProperty("NOMBRE").toString());
                    listaPaises.add(pais);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarPaises(): " + e.getMessage());
        }
        return listaPaises;
    }

    /**
     * Metodo encargado de listar las ciudades de la BD.
     * @param idPais Para el caso de filtrar por pais.
     *               En caso de 0, no tendrá en cuenta este filtro.
     * @return
     */
    public List<Ciudad> listarCiudades(int idPais){

        final String SOAP_ACTION = conf.getUrl()+"/listadoCiudades";
        final String METHOD_NAME = "listadoCiudades";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Ciudad> listaCiudades = new ArrayList<Ciudad>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idPais", idPais);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {

            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null) {
                for (SoapObject ciudadSoap : rs) {

                    Ciudad ciudad = new Ciudad();
                    ciudad.setIdCiudad(Integer.parseInt(ciudadSoap.getProperty("ID_CIUDAD").toString()));
                    ciudad.setNombre(ciudadSoap.getProperty("NOM_CIUDAD").toString());

                    Pais pais = new Pais();
                    pais.setIdPais(Integer.parseInt(ciudadSoap.getProperty("ID_PAIS").toString()));
                    pais.setNombre(ciudadSoap.getProperty("NOM_PAIS").toString());

                    ciudad.setPais(pais);

                    listaCiudades.add(ciudad);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarCiudades(): " + e.getMessage());
        }
        return listaCiudades;
    }

    /**
     * Metodo encargado de retornar el listado de usuarios segun busqueda
     * @param usuarioBuscar objeto de la clase Usuario el cual contiene los parametros
     *                    de busqueda ya sean fijados o por defecto. En el caso
     *                    de tenerlos por defecto (new Usuario()) se listaran todos los libros
     *                    presentes.
     * @return ListadoLibros
     */
    public List<Usuario> buscarUsuarios(Usuario usuarioBuscar){

        final String SOAP_ACTION = conf.getUrl()+"/listadoUsuarios";
        final String METHOD_NAME = "listadoUsuarios";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<Usuario> listaUsuario = new ArrayList<Usuario>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cedula", usuarioBuscar.getCedula());
        request.addProperty("primerNombre", usuarioBuscar.getPrimerNombre());
        request.addProperty("segundoNombre", usuarioBuscar.getSegundoNombre());
        request.addProperty("primerApellido", usuarioBuscar.getPrimerApellido());
        request.addProperty("segundoApellido", usuarioBuscar.getSegundoApellido());
        request.addProperty("codigo", usuarioBuscar.getCodigo());
        request.addProperty("rol", usuarioBuscar.getRol());


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
                    Usuario usuario = UtilidadesBuscarPorId.obtenerUsuarioSoap(user);
                    listaUsuario.add(usuario);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error buscarUsuarios(): " + e.getMessage());
        }
        return listaUsuario;
    }

    /**
     * Tarea encargada de actualizar las solicitudes.
     * @param solicitud
     *  El llamado al WS recibe los parametros:
     *  idSolicitud: En caso de ser = 0, se actualizaran todas las solicitudes, junto con el parametro "updateAll".
     *  estado: Estado al cual se modificara la solicitud.
     *  fechaDevolucion: Indica la fecha en la cual se regresa el libro.
     *  updateAll: Indica si se actualizan todas las solicitudes o no.
     */
    public boolean actualizarSolicitudes(Solicitud solicitud, boolean updateAll){

        final String SOAP_ACTION = conf.getUrl()+"/actualizarSolicitud";
        final String METHOD_NAME = "actualizarSolicitud";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        boolean resultado = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("idSolicitud", solicitud.getIdSolicitud());
        request.addProperty("estado", solicitud.getEstado());

        if(solicitud.getFechaEntrega() != null){
            request.addProperty("fechaEntrega", Utilidades.formatoFechaYYYYMMDD.format(solicitud.getFechaEntrega()));
        }else{
            request.addProperty("fechaEntrega", "");
        }

        if(updateAll){
            request.addProperty("updateAll", "true");
        }else{
            request.addProperty("updateAll", "false");
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultadoActualizar = Integer.parseInt(envelope.getResponse().toString());

            Log.i("TareasGenerales.java","*********************** resultado actualizarSolicitudes: "+resultadoActualizar);
            if (resultadoActualizar == 1)
            {
                resultado = true;
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error actualizarSolicitudes(): " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Metodo encargado de retornar el listado de autores de la tabla LIBRO_AUTOR
     * @param idLibro filtro por libro, en caso de ser 0 listara todos los datos
     *                de la tabla LIBRO_AUTOR
     */
    public List<LibroAutor> listarLibroAutor(int idLibro){

        final String SOAP_ACTION = conf.getUrl()+"/listadoLibroAutor";
        final String METHOD_NAME = "listadoLibroAutor";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        List<LibroAutor> listaLibroAutor = new ArrayList<LibroAutor>();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idLibro", idLibro);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject libroAutorSoap : rs)
                {
                    Libro libro = utilidadesBuscarPorId.buscarLibroPorId(Integer.parseInt(libroAutorSoap.getProperty("ID_LIBRO").toString()));

                    Autor autor = new Autor();
                    autor.setIdAutor(Integer.parseInt(libroAutorSoap.getProperty("ID_AUTOR").toString()));
                    autor.setPrimerNombre(libroAutorSoap.getProperty("PRIMER_NOMBRE").toString());
                    autor.setSegundoNombre(libroAutorSoap.getProperty("SEGUNDO_NOMBRE").toString());
                    autor.setPrimerApellido(libroAutorSoap.getProperty("PRIMER_APELLIDO").toString());
                    autor.setSegundoApellido(libroAutorSoap.getProperty("SEGUNDO_APELLIDO").toString());
                    autor.setTipoAutor(libroAutorSoap.getProperty("TIPO_AUTOR").toString());

                    LibroAutor libAutor =  new LibroAutor();
                    libAutor.setLibro(libro);
                    libAutor.setAutor(autor);

                    listaLibroAutor.add(libAutor);
                }
            }
        }catch (Exception e){
            Log.e("TareasGenerales.java ", "xxx Error listarLibroAutor(): " + e.getMessage());
        }
        return listaLibroAutor;
    }


    /**
     * Metodo que verifica si ya se encuentra registrado un determinado dato en la BD
     * @param tablaVerificar
     * @param campoVerificar
     * @param valorVerificar
     * @return
     */
    public boolean verficarDatoEnBd(String tablaVerificar, String campoVerificar, String valorVerificar){

        final String SOAP_ACTION = conf.getUrl()+"/verficarDatoEnBd";
        final String METHOD_NAME = "verficarDatoEnBd";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();

        boolean respuesta = false;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("tabla", tablaVerificar);
        request.addProperty("campo", campoVerificar);
        request.addProperty("valor", valorVerificar);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            int resultado = Integer.parseInt(envelope.getResponse().toString());

            Log.i("Generales","*********************** resultado verificar: "+resultado);
            if (resultado == 1)
            {
                respuesta = true;
            }
        } catch (Exception e) {
            respuesta = false;
            Log.e("Generales", "xxx Error TareaWsVerificarDatoEnBd: " + e.getMessage());
            e.printStackTrace();
        }

        return respuesta;

    }

}
