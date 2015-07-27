package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pablo on 22/05/15.
 */
public class Libro implements Serializable {

    private int idLibro;
    private String titulo;
    private int valor;
    private String adquisicion;
    private String estado;
    private String isbn;
    private String radicado;
    private Date fechaIngreso;
    private String codigoTopografico;
    private String serie;
    private Sede sede;
    private Editorial editorial;
    private Area area;
    private int anio;
    private String temas;
    private int paginas;
    private String disponibilidad;
    private int idUsuario; //Usuario que registra el libro en la biblioteca
    private Ciudad ciudad;
    private int cantidad;

    //Transient
    // Utilizado para la busqueda de libros por Autor (LIBRO_AUTOR)
    private int idAutor;

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
        this.sede = null;
        this.editorial = null;
        this.area = null;
        this.anio = 0;
        this.temas = "";
        this.paginas = 0;
        this.disponibilidad = "";
        this.idUsuario = 0;
        this.ciudad = null;
        this.cantidad = 0;

        //Transient
        this.idAutor = 0;
    }

    public Libro(int idLibro, String titulo, int valor, String adquisicion,
                 String estado, String isbn, String radicado, Date fechaIngreso,
                 String codigoTopografico, String serie, Sede sede, Editorial editorial,
                 Area area, int anio, String temas, int paginas, String disponibilidad,
                 int idUsuario, Ciudad ciudad, int cantidad) {

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
        this.sede = sede;
        this.editorial = editorial;
        this.area = area;
        this.anio = anio;
        this.temas = temas;
        this.paginas = paginas;
        this.disponibilidad = disponibilidad;
        this.idUsuario = idUsuario;
        this.ciudad = ciudad;
        this.cantidad = cantidad;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
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

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
}
