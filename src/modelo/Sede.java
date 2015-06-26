package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Sede implements Serializable {

    private int idSede;
    private String descripcion;


    public Sede() {

        this.idSede = 0;
        this.descripcion = "";
    }

    public Sede(int idSede, String descripcion) {

        this.idSede = idSede;
        this.descripcion = descripcion;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
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
