package util;

import modelo.Autor;
import modelo.Editorial;
import modelo.Libro;
import modelo.Solicitud;
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
    private Libro libroBuscar = new Libro();

    /**
     * Variable que contiene un libro seleccionado por el admin.
     */
    private Libro libroSeleccionadoAdmin;

    /**
     * Variable qeu contiene un usuario seleccionado por el admin.
     */
    private Usuario usuarioSeleccionadoAdmin;

    /**
     * Variable que contiene un usuario con los parametros cargados para busqueda
     */
    private Usuario usuarioBuscar = new Usuario();

    /**
     * Variable que contiene una Editorial con los parametros cargados para busqueda
     */
    private Editorial editorialBuscar = new Editorial();

    /**
     * Variable qeu contiene una editorial seleccionada por el admin.
     */
    private Editorial editorialSeleccionadaAdmin;

    /**
     * Variable que contiene un autor con los parametros cargados para busqueda
     */
    private Autor autorBuscar = new Autor();

    /**
     * Variable que contiene un autor seleccionado por el admin.
     */
    private Autor autorSeleccionadoAdmin;

    /**
     * Variable que contiene una solicitud con los parametros cargados para busqueda.
     */
    private Solicitud solicitudBuscar = new Solicitud();

    private int valorMulta = 0;

    // Restrict the constructor from being instantiated
    private VariablesGlobales(){
        solicitudBuscar.setUsuario(new Usuario());
        solicitudBuscar.setLibro(new Libro());
    }

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

    public Libro getLibroSeleccionadoAdmin() {
        return libroSeleccionadoAdmin;
    }

    public void setLibroSeleccionadoAdmin(Libro libroSeleccionadoAdmin) {
        this.libroSeleccionadoAdmin = libroSeleccionadoAdmin;
    }

    public Usuario getUsuarioSeleccionadoAdmin() {
        return usuarioSeleccionadoAdmin;
    }

    public void setUsuarioSeleccionadoAdmin(Usuario usuarioSeleccionadoAdmin) {
        this.usuarioSeleccionadoAdmin = usuarioSeleccionadoAdmin;
    }

    public Usuario getUsuarioBuscar() {
        return usuarioBuscar;
    }

    public void setUsuarioBuscar(Usuario usuarioBuscar) {
        this.usuarioBuscar = usuarioBuscar;
    }

    public Editorial getEditorialBuscar() {
        return editorialBuscar;
    }

    public void setEditorialBuscar(Editorial editorialBuscar) {
        this.editorialBuscar = editorialBuscar;
    }


    public Editorial getEditorialSeleccionadaAdmin() {
        return editorialSeleccionadaAdmin;
    }

    public void setEditorialSeleccionadaAdmin(Editorial editorialSeleccionadaAdmin) {
        this.editorialSeleccionadaAdmin = editorialSeleccionadaAdmin;
    }

    public Autor getAutorBuscar() {
        return autorBuscar;
    }

    public void setAutorBuscar(Autor autorBuscar) {
        this.autorBuscar = autorBuscar;
    }

    public Autor getAutorSeleccionadoAdmin() {
        return autorSeleccionadoAdmin;
    }

    public void setAutorSeleccionadoAdmin(Autor autorSeleccionadoAdmin) {
        this.autorSeleccionadoAdmin = autorSeleccionadoAdmin;
    }

    public Solicitud getSolicitudBuscar() {
        return solicitudBuscar;
    }

    public void setSolicitudBuscar(Solicitud solicitudBuscar) {
        this.solicitudBuscar = solicitudBuscar;
    }

    public static synchronized VariablesGlobales getInstance(){
        if(instance==null){
            instance=new VariablesGlobales();
        }
        return instance;
    }

    public int getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(int valorMulta) {
        this.valorMulta = valorMulta;
    }
}
