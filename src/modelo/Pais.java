package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Pais implements Serializable {

    private int idPais;
    private String nombre;


    public Pais() {

        this.idPais = 0;
        this.nombre = "";
    }

    public Pais(int idPais, String nombre) {

        this.idPais = idPais;
        this.nombre = nombre;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        return this.nombre;
    }
}
