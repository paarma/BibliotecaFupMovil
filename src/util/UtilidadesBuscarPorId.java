package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import modelo.Area;
import modelo.Ciudad;
import modelo.Editorial;
import modelo.Libro;
import modelo.Pais;
import modelo.Sede;
import modelo.Usuario;

/**
 * Created by pablo on 30/06/15.
 * Clase especifica para obtener objetos desde la BD segun su ID
 */
public class UtilidadesBuscarPorId {

    /**
     * Objeto de la clase configuracion la cual contiene atributos
     * generales y conf. para conexion al server
     */
    private Configuracion conf = new Configuracion();

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
                    Log.i("Generales.java", ">>>>>>>>>>>> buscarLibroPorId: " + lib.getIdLibro());
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
                    usuario = obtenerUsuarioSoap(user);
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

    /**
     * Metodo encargado de retornar una Ciudad segun su ID
     * @param idCiudad
     * @return Ciudad
     */
    public Ciudad buscarCiudadPorId(int idCiudad){

        final String SOAP_ACTION = conf.getUrl()+"/buscarCiudadPorId";
        final String METHOD_NAME = "buscarCiudadPorId";
        final String NAMESPACE = conf.getNamespace();
        final String URL = conf.getUrl();
        Ciudad ciudad = null;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("idCiudad",idCiudad);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;

        HttpTransportSE transporte = new HttpTransportSE(URL);
        try {
            transporte.call(SOAP_ACTION, envelope);
            java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

            if (rs != null)
            {
                for (SoapObject ciudadSoap : rs)
                {
                    ciudad = new Ciudad();
                    ciudad.setIdCiudad(Integer.parseInt(ciudadSoap.getProperty("ID_CIUDAD").toString()));
                    ciudad.setNombre(ciudadSoap.getProperty("NOM_CIUDAD").toString());

                    Pais pais = new Pais();
                    pais.setIdPais(Integer.parseInt(ciudadSoap.getProperty("ID_PAIS").toString()));
                    pais.setNombre(ciudadSoap.getProperty("NOM_PAIS").toString());

                    ciudad.setPais(pais);
                    Log.i("Generales.java",">>>>>>>>>>>> buscarCiudadPorId: "+ciudad.getIdCiudad());
                    break;
                }
            }
        }catch (Exception e){
            Log.d("Generales.java ", "xxx Error buscarCiudadPorId(): "+e.getMessage());
        }
        return ciudad;
    }

    /**
     * Metodo encardado de setear los valores desde la BD (Soap) a un Libro.
     * @param libroSoap Objeto Soap que contiene los datos de libro para ser setados.
     * @return
     */
    public Libro obtenerLibroSoap(SoapObject libroSoap){

        Libro lib = new Libro();
        lib.setIdLibro(Integer.parseInt(libroSoap.getProperty("ID_LIBRO").toString()));

        if(libroSoap.getProperty("TITULO") != null){
            lib.setTitulo(libroSoap.getProperty("TITULO").toString());
        }

        if(libroSoap.getProperty("ISBN") != null){
            lib.setIsbn(libroSoap.getProperty("ISBN").toString());
        }

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

        if(libroSoap.getProperty("FECHA_INGRESO") != null) {
            try{
                Date fechaIngreso;
                fechaIngreso = Utilidades.formatoFechaYYYYMMDD.parse(libroSoap.getProperty("FECHA_INGRESO").toString());
                lib.setFechaIngreso(fechaIngreso);
            }catch (Exception e){
                Log.e("Generales","XXX Error seteando fechaIngresoLibro: "+e.getMessage());
            }
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

        //Ciudad
        if(libroSoap.getProperty("ID_CIUDAD") != null) {
            lib.setCiudad(buscarCiudadPorId(Integer.parseInt(libroSoap.getProperty("ID_CIUDAD").toString())));
        }

        if(libroSoap.getProperty("ADQUISICION") != null) {
            lib.setAdquisicion(libroSoap.getProperty("ADQUISICION").toString());
        }

        if(libroSoap.getProperty("ESTADO") != null) {
            lib.setEstado(libroSoap.getProperty("ESTADO").toString());
        }

        if(libroSoap.getProperty("CANTIDAD") != null) {
            lib.setCantidad(Integer.parseInt(libroSoap.getProperty("CANTIDAD").toString()));
        }

        return lib;
    }

    /**
     * Metodo encardado de setear los valores desde la BD (Soap) a un Usuario.
     * @param user Objeto Soap que contiene los datos del usuario para ser setados.
     * @return
     */
    public static Usuario obtenerUsuarioSoap(SoapObject user){

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(Integer.parseInt(user.getProperty("ID_USUARIO").toString()));

        if(user.getProperty("CEDULA") != null){
            usuario.setCedula(Integer.parseInt(user.getProperty("CEDULA").toString()));
        }

        if(user.getProperty("PRIMER_NOMBRE") != null){
            usuario.setPrimerNombre(user.getProperty("PRIMER_NOMBRE").toString());
        }

        if(user.getProperty("SEGUNDO_NOMBRE") != null){
            usuario.setSegundoNombre(user.getProperty("SEGUNDO_NOMBRE").toString());
        }

        if(user.getProperty("PRIMER_APELLIDO") != null){
            usuario.setPrimerApellido(user.getProperty("PRIMER_APELLIDO").toString());
        }

        if(user.getProperty("SEGUNDO_APELLIDO") != null){
            usuario.setSegundoApellido(user.getProperty("SEGUNDO_APELLIDO").toString());
        }

        if(user.getProperty("TELEFONO") != null){
            usuario.setTelefono(Integer.parseInt(user.getProperty("TELEFONO").toString()));
        }

        if(user.getProperty("DIRECCION") != null){
            usuario.setDireccion(user.getProperty("DIRECCION").toString());
        }

        if(user.getProperty("EMAIL") != null) {
            usuario.setEmail(user.getProperty("EMAIL").toString());
        }

        if(user.getProperty("CODIGO") != null){
            usuario.setCodigo(user.getProperty("CODIGO").toString());
        }

        if(user.getProperty("CLAVE") != null){
            usuario.setClave(user.getProperty("CLAVE").toString());
        }

        if(user.getProperty("ROL") != null){
            usuario.setRol(user.getProperty("ROL").toString());
        }

        return usuario;
    }
}
