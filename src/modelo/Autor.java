package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Autor implements Serializable {

    private int idAutor;
    private String descripcion;
    private int tipoAutor;



    public Autor() {

        this.idAutor = 0;
        this.descripcion = "";
        this.tipoAutor = 0;

    }

    public Autor(int idAutor, String descripcion, int tipoAutor) {

        this.idAutor = idAutor;
        this.descripcion = descripcion;
        this.tipoAutor = tipoAutor;

    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipoAutor() {
        return tipoAutor;
    }

    public void setTipoAutor(int tipoAutor) {
        this.tipoAutor = tipoAutor;
    }
}
