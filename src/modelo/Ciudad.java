package modelo;

import java.io.Serializable;

/**
 * Created by pablo on 22/05/15.
 */
public class Ciudad implements Serializable {

    private int idCiudad;
    private String nombre;
    private Pais pais;


    public Ciudad() {

        this.idCiudad = 0;
        this.nombre = "";
        this.pais = null;
    }

    public Ciudad(int idCiudad, String nombre, Pais pais) {

        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.pais = pais;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
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
