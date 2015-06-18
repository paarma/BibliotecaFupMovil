package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pablo on 27/05/15.
 */
public class Solicitud implements Serializable {

    private int idSolicitud;
    private Date fechaSolicitud; // Fecha en la  que se realiza la solicitud
    private Date fechaReserva; // Fecha para la cual se reserva el libro.

    // Fecha ideal en la cual el usuario deberia regresar el libro.
    // Dos dias mas apartir de la fecha de reserva.
    private Date fechaDevolucion;
    private Date fechaEntrega; // Fecha  en la cual el usuario entregó el libro.
    private Usuario usuario; // Usuario al cual se le presta el libro.
    private Libro libro;
    private String estado;


    public Solicitud(){
        this.idSolicitud = 0;
        this.fechaSolicitud = null;
        this.fechaReserva = null;
        this.fechaDevolucion = null;
        this.fechaEntrega = null;
        this.usuario = null;
        this.libro = null;
        this.estado = "";
    }

    public Solicitud(int idSolicitud, Date fechaSolicitud, Date fechaReserva,
                     Date fechaDevolucion, Date fechaEntrega, Usuario usuario,
                     Libro libro, String estado) {
        this.idSolicitud = idSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaEntrega = fechaEntrega;
        this.usuario = usuario;
        this.libro = libro;
        this.estado = estado;
    }

    //Getters and Setters

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }
}
