package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Autor implements Serializable {

    private int idAutor;
    private String descripcion;
    private String tipoAutor;



    public Autor() {

        this.idAutor = 0;
        this.descripcion = "";
        this.tipoAutor = "";

    }

    public Autor(int idAutor, String descripcion, String tipoAutor) {

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

    public String getTipoAutor() {
        return tipoAutor;
    }

    public void setTipoAutor(String tipoAutor) {
        this.tipoAutor = tipoAutor;
    }
}
