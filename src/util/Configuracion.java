package util;

import android.app.Application;

/**
 * Created by pablo on 20/03/15.
 * Clase Global en la cual se asignaran funciones y atributos generales
 */
public class Configuracion extends Application {

    /**
     * IP del servidor dond se encuntran los WebService
     * para el caso de tener los WS locales y ejecutar la
     * aplicacion APP con el emulador se utiliza la direccion
     * ip virtual 10.0.2.2
     */
   // private final static String IP_SERVER_WS = "10.0.2.2";
    private final static String IP_SERVER_WS = "192.168.0.24";
    /**
     * Puerto del server donde estan publicados los WebService
     * Ejemplo del valor = :8080
     */
    private final static String PORT_SERVER_WS = "";

    /**
     * Namespace del server donde estan publicados los WebService
     */
    private final static String NAMESPACE = "wsPhpBibliotecaFup";

    /**
     * Achivo especifico del server donde se encuentran los WS
     */
    private final static String PAGINA_SERVER = "server.php";

    //Metodos
    /**
     * Metodo encargado de retornar la url del server estructurada
     */
    public String getUrl(){
        return "http://"+IP_SERVER_WS+PORT_SERVER_WS+"/"+NAMESPACE+"/"+PAGINA_SERVER;
    }

    /**
     * Metodo encargado de retornar el namespace del server estructurado
     */
    public String getNamespace(){
        return "http://"+IP_SERVER_WS+PORT_SERVER_WS+"/"+NAMESPACE+"/";
    }

}
