package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class LibroAutor implements Serializable {

    private int idLibroAutor;
    private Libro libro;
    private Autor autor;


    public LibroAutor() {

        this.idLibroAutor = 0;
        this.libro = null;
        this.autor = null;

    }

    public LibroAutor(int idLibroAutor, Libro libro, Autor autor) {

        this.idLibroAutor = idLibroAutor;
        this.libro = libro;
        this.autor = autor;

    }

    public int getIdLibroAutor() {
        return idLibroAutor;
    }

    public void setIdLibroAutor(int idLibroAutor) {
        this.idLibroAutor = idLibroAutor;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
