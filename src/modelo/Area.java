package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Area implements Serializable {

    private int idArea;
    private String descripcion;


    public Area() {

        this.idArea = 0;
        this.descripcion = "";
    }

    public Area(int idArea, String descripcion) {

        this.idArea = idArea;
        this.descripcion = descripcion;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
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
