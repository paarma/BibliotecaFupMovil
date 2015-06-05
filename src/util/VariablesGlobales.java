package util;

import modelo.Libro;
import modelo.Usuario;

/**
 * Created by pablo on 15/05/15.
 * Clase utilizada para gestionar variables globales
 */
public class VariablesGlobales {

    private static VariablesGlobales instance;

    // Global variable
    /**
     * Variable que indica el item especifico seleccionado desde el menu
     */
    private int opcionMenu;

    /**
     * Variable que indica desde que vista se llama a la interfaz Buscar Libros
     * posibles valores: reservar, misLibros...
     */
    private String buscarLibroDesdeVista;

    /**
     *Variable que indica el usuario que inicio sesión en el sistema
     */
    private Usuario usuarioLogueado;

    /**
     * Variable que contiene un libro con los parametros cargados para busqueda
     */
    private Libro libroBuscar;

    // Restrict the constructor from being instantiated
    private VariablesGlobales(){}

    //Getters and Setters
    public void setOpcionMenu(int d){
        this.opcionMenu=d;
    }
    public int getOpcionMenu(){
        return this.opcionMenu;
    }
    public String getBuscarLibroDesdeVista() {
        return buscarLibroDesdeVista;
    }
    public void setBuscarLibroDesdeVista(String buscarLibroDesdeVista) {
        this.buscarLibroDesdeVista = buscarLibroDesdeVista;
    }
    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public Libro getLibroBuscar() {
        return libroBuscar;
    }

    public void setLibroBuscar(Libro libroBuscar) {
        this.libroBuscar = libroBuscar;
    }

    public static synchronized VariablesGlobales getInstance(){
        if(instance==null){
            instance=new VariablesGlobales();
        }
        return instance;
    }
}
