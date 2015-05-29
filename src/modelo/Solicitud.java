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
    private int idUsuario; // Usuario al cual se le presta el libro.
    private int idLibro;
    private String estado;


    public Solicitud(){
        this.idSolicitud = 0;
        this.fechaSolicitud = null;
        this.fechaReserva = null;
        this.fechaDevolucion = null;
        this.fechaEntrega = null;
        this.idUsuario = 0;
        this.idLibro = 0;
        this.estado = "";
    }

    public Solicitud(int idSolicitud, Date fechaSolicitud, Date fechaReserva,
                     Date fechaDevolucion, Date fechaEntrega, int idUsuario,
                     int idLibro, String estado) {
        this.idSolicitud = idSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaEntrega = fechaEntrega;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }
}
