package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Editorial implements Serializable {

    private int idEditorial;
    private String descripcion;



    public Editorial() {

        this.idEditorial = 0;
        this.descripcion = "";


    }

    public Editorial(int idEditorial, String descripcion) {

        this.idEditorial = idEditorial;
        this.descripcion = descripcion;


    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Metodo sobreescrito toString para retornar el valor del campo descripcion.
     * Util para mostrar la descripcion en los spinner
     * @return
     */
    @Override
    public String toString()
    {
        return this.descripcion;
    }
}
