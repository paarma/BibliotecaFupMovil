package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pablo on 22/05/15.
 */
public class Libro implements Serializable {

    private int idLibro;
    private String titulo;
    private float valor;
    private String adquisicion;
    private String estado;
    private String isbn;
    private String radicado;
    private Date fechaIngreso;
    private String codigoTopografico;
    private String serie;
    private int idSede;
    private int idEditorial;
    private int idArea;
    private int anio;
    private String temas;
    private int paginas;
    private String disponibilidad;
    private int idUsuario; //Usuario que registra el libro en la biblioteca
    private int idCiudad;

    public Libro() {

        this.idLibro = 0;
        this.titulo = "";
        this.valor = 0;
        this.adquisicion = "";
        this.estado = "";
        this.isbn = "";
        this.radicado = "";
        this.fechaIngreso = null;
        this.codigoTopografico = "";
        this.serie = "";
        this.idSede = 0;
        this.idEditorial = 0;
        this.idArea = 0;
        this.anio = 0;
        this.temas = "";
        this.paginas = 0;
        this.disponibilidad = "";
        this.idUsuario = 0;
        this.idCiudad = 0;
    }

    public Libro(int idLibro, String titulo, float valor, String adquisicion,
                 String estado, String isbn, String radicado, Date fechaIngreso,
                 String codigoTopografico, String serie, int idSede, int idEditorial,
                 int idArea, int anio, String temas, int paginas, String disponibilidad,
                 int idUsuario, int idCiudad) {

        this.idLibro = idLibro;
        this.titulo = titulo;
        this.valor = valor;
        this.adquisicion = adquisicion;
        this.estado = estado;
        this.isbn = isbn;
        this.radicado = radicado;
        this.fechaIngreso = fechaIngreso;
        this.codigoTopografico = codigoTopografico;
        this.serie = serie;
        this.idSede = idSede;
        this.idEditorial = idEditorial;
        this.idArea = idArea;
        this.anio = anio;
        this.temas = temas;
        this.paginas = paginas;
        this.disponibilidad = disponibilidad;
        this.idUsuario = idUsuario;
        this.idCiudad = idCiudad;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(String adquisicion) {
        this.adquisicion = adquisicion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getRadicado() {
        return radicado;
    }

    public void setRadicado(String radicado) {
        this.radicado = radicado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getCodigoTopografico() {
        return codigoTopografico;
    }

    public void setCodigoTopografico(String codigoTopografico) {
        this.codigoTopografico = codigoTopografico;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getTemas() {
        return temas;
    }

    public void setTemas(String temas) {
        this.temas = temas;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }
}
