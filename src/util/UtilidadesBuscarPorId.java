package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import modelo.Area;
import modelo.Editorial;
import modelo.Libro;
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
                    usuario = new Usuario();
                    usuario.setIdUsuario(Integer.parseInt(user.getProperty("ID_USUARIO").toString()));
                    usuario.setCedula(Integer.parseInt(user.getProperty("CEDULA").toString()));
                    usuario.setPrimerNombre(user.getProperty("PRIMER_NOMBRE").toString());
                    usuario.setSegundoNombre(user.getProperty("SEGUNDO_NOMBRE").toString());
                    usuario.setPrimerApellido(user.getProperty("PRIMER_APELLIDO").toString());
                    usuario.setSegundoApellido(user.getProperty("SEGUNDO_APELLIDO").toString());
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
}
